package cn.techarts.copycat.decoder;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.Utility;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;

public class DelimiterFrameDecoder<T extends Frame> extends Decoder<T> {
	private byte[] delimiters = null;
	
	public DelimiterFrameDecoder(byte[] delimiters) {
		this.delimiters = delimiters;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		List<T> result = new ArrayList<>();
		int len = delimiters.length, index = 0;
		for(int i = 0; i < data.length(); i++) {
			if(data.borrow(i) != delimiters[0]) continue;
			for(int j = 1; j < len; j++) {
				i += j;
				if(data.borrow(i) != delimiters[j]) break;
				if(j == len - 1) { //Completed
					var size = i + 1 - index - len;
					index += (i + 1); //Move to start of next
					var fbs = data.consume(size, len);
					result.add(Utility.frame(frameClass, fbs));
				}
			}
		}
		return result.isEmpty() ? null : result.toArray(Utility.array(frameClass, 0));
	}
}