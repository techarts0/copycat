/*
 * Copyright (C) 2024 techarts.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
