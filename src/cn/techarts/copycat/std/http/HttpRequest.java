package cn.techarts.copycat.std.http;

import java.util.List;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import cn.techarts.copycat.core.Frame;

public class HttpRequest extends Frame {
	private String url;
	private String version;
	private HttpMethod method;
	private List<HttpHeader> headers;
	
	public HttpRequest() {
		super();
		headers = new ArrayList<>();
	}
	
	public HttpRequest(byte[] raw) {
		super(raw);
	}
	
	public HttpRequest(List<String> headers, byte[] body) {
		this.headers = new ArrayList<>();
		parseRequestLine(headers.get(0)); //GET /index.html HTTP/1.1
		for(int i = 1; i < headers.size(); i++) {
			var hdr = new HttpHeader(headers.get(i));
			this.headers.add(hdr);
			
				
			
		}
		//Parse Body
	}

	@Override
	protected void parse() {
		// TODO Auto-generated method stub

	}

	@Override
	public ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}

	private void parseRequestLine(String line) {
		var tokens = line.split(" ");
		if(tokens == null || tokens.length != 3) {
			throw new HttpException("Illegal request line");
		}
		this.setUrl(tokens[1]);
		this.setVersion(tokens[2]);
		this.setMethod(HttpMethod.to(tokens[0]));
	}

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}


	public HttpMethod getMethod() {
		return method;
	}


	public void setMethod(HttpMethod method) {
		this.method = method;
	}


	public List<HttpHeader> getHeaders() {
		return headers;
	}


	public void setHeaders(List<HttpHeader> headers) {
		this.headers = headers;
	}
	
	public void addHeader(String line) {
		this.headers.add(new HttpHeader(line));
	}
}