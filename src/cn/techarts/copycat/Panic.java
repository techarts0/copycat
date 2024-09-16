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

package cn.techarts.copycat;

/**
 * @author rocwon@gmail.com
 */
public class Panic extends RuntimeException{
	private static final long serialVersionUID = -4200614776037388222L;
	
	public Panic() {
		super();
	}
	
	public Panic(Exception e) {
		super(e);
	}
	
	public Panic(String cause) {
		super(cause);
	}
	
	public Panic(Exception e, String cause) {
		super(cause, e);
	}
	
	public Panic(Throwable e, String cause) {
		super(cause, e);
	}
	
	public static Panic nullBuffer() {
		return new Panic("Buffer is null.");
	}
	
	public static Panic nullAction() {
		return new Panic("The CLI action is required.");
	}
}
