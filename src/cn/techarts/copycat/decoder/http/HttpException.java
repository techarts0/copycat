package cn.techarts.copycat.decoder.http;

public class HttpException extends RuntimeException {
	
	private static final long serialVersionUID = -5299405781235035512L;

	public HttpException(String cause) {
		super(cause);
	}
}