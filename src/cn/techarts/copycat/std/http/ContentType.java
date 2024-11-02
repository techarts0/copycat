package cn.techarts.copycat.std.http;

public enum ContentType {
	TEXT_HTML("text/html"),
	TEXT_PLAIN("text/plain"),
	APP_XML("application/xml"),
	APP_JSON("application/json"),
	IMG_JPEG("image/jpeg"),
	IMG_PNG("image/png"),
	FORM_DATA("multipart/form-data"),
	WWW_FORM("application/x-www-form-urlencoded");
	
	
	private String type;
	
	ContentType(String type){
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
