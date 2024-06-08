package cn.techarts.copycat.decoder.http;

public class HttpHeader {
	private String name;
	private String value;
	
	public HttpHeader(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public HttpHeader(String header) {
		if(header == null) return;
		var hkv = header.split(":");
		this.name = hkv[0];
		this.value = hkv[1];
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
}
