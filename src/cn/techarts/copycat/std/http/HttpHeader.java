package cn.techarts.copycat.std.http;

public final class HttpHeader {
	private String name;
	private String value;
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public HttpHeader(String header) {
		if(header == null) return;
		var pair = header.split(":");
		this.name = pair[0].trim();
		this.value = pair[1].trim();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getIntValue() {
		try {
			return Integer.parseInt(value);
		}catch(Exception e) {
			return 0;
		}
	}
	
	public boolean isContentType() {
		if(name == null) return false;
		return "content-type".equals(name.trim().toLowerCase());
	}
}
