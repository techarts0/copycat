package cn.techarts.copycat.std.http;

public enum HttpMethod {
	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	HEAD("HEAD"),
	TRACE("TRACE"),
	DELETE("DELETE");
	
	
	private String method;
	
	HttpMethod(String m) {
		this.setMethod(m);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method.toLowerCase();
	}
	
	public static HttpMethod to(String m) {
		if("GET".equals(m)) return GET;
		if("PUT".equals(m)) return PUT;
		if("POST".equals(m)) return POST; 
		if("HEAD".equals(m)) return HEAD;
		throw new HttpException("Unsupport method: " + m);
	}
}
