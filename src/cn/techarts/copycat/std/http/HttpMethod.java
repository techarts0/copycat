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
