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

package cn.techarts.copycat.ext.mote;

/**
 * @author rocwon@gmail.com
 */
public class MoteException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MoteException(String cause) {
		super(cause);
	}
	
	public static MoteException invalidSN() {
		return new MoteException("Illegal frame without device SN.");
	}
	
	public static MoteException itIsNotMote() {
		return new MoteException("Unrecognized protocol or version.");
	}
	
	public static MoteException invalidType(byte type) {
		return new MoteException("Unsupported frame type: " + type);
	}
	
	public static MoteException invalidPrecision(byte p) {
		return new MoteException("Unsupported time precision: " + p);
	}
	
	public static MoteException invalidRemaining() {
		return new MoteException("The remaining length is invalid.");
	}
}
