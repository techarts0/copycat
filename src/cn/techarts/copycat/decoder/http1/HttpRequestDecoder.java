package cn.techarts.copycat.decoder.http1;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.util.Utility;
import cn.techarts.copycat.decoder.http.HttpHeader;
import cn.techarts.copycat.decoder.http.HttpRequest;

public class HttpRequestDecoder extends Decoder<HttpRequest> {
	
	@Override
	public HttpRequest[] decode(ByteBuf data) {
		var headers = new ArrayList<String>();
		var frames = new ArrayList<HttpRequest>();
		while(true) {
			var body = detectPacket(data, headers);
			if(body != null) break;
			frames.add(new HttpRequest(headers, body));
		}
		if(frames.isEmpty()) return null;
		return frames.toArray(new HttpRequest[0]);
	}
	
	//Returns the bytes of HTTP body.
	public byte[] detectPacket(ByteBuf data, List<String> headers) {
		int contentLength = 0, headerLength = 0;
		int index = data.current(), latest = index;
		
		while((index = nextHeader(data, index)) > 0) {
			var line = data.borrow(latest, index - 1);
			latest = index + 1; //Move the start point to next
			headerLength = line.length + 2;
			var strLine = Utility.toAsciiString(line);
			strLine = strLine.toLowerCase();
			headers.add(strLine);
			if(strLine.startsWith("content-length")) {
				contentLength = new HttpHeader(strLine).getIntValue();
			}
		}
		if(index == -1) return null;
		if(index == 0) headerLength += 2;
		data.current(data.current() + headerLength);
		if(!data.test(contentLength)) return null;
		return data.consume(contentLength);
	}
	
	/**
	 * @return > 1,  The real position of a header line<br>
	 *         = 0,  The end of the header fields(body followed)<br>
	 *         = -1, The bytes of headers are not completed.
	 */
	private int nextHeader(ByteBuf data, int from) {
		for(int i = from; i < data.length(); i++) {
			if(data.borrow(i) == 0X0D && 
			   data.borrow(i + 1) == 0X0A) {
				return from == i ? 0 : i + 1;
			}
		}
		return -1; //Moved to the end of HTTP head fields
	}
}