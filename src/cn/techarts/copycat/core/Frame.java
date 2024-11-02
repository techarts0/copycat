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

package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
import java.util.Arrays;

import cn.techarts.copycat.Panic;

/**
 * The protocol frame structure
 * @author rocwon@gmail.com
 */
public abstract class Frame {
	/**The raw byte data*/
	protected byte[] rawdata;
	
	//Default constructor
	public Frame() {}
	
	public Frame(byte[] raw) {
		this.rawdata = raw;
		if(raw == null || raw.length == 0) {
			throw new Panic("Raw data is null.");
		}
		this.decode();
	}
	
	public int length() {
		if(rawdata == null) return 0;
		return this.rawdata.length;
	}
	
	/**
	 * @return The raw data received from peer.
	 */
	public byte[] getRawData() {
		return this.rawdata;
	}
	
	/**
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 * <p>
	 * Please note it's not same to the {@link Decoder.decode}.
	 */
	protected abstract void decode();
	
	/**
	 * Serialize the properties (as a byte array) to send to peer.
	 */
	public abstract ByteBuffer encode();
	
	@Override
	public String toString() {
		return Arrays.toString(rawdata);
	}
	
	protected byte[] slice(int pos, int length) {
		if(pos < 0 || length <= 0) return null;
		int len = length; 
		int remaining = rawdata.length - pos; 
		if(len > remaining) len = remaining;
		var result = new byte[len];
		System.arraycopy(rawdata, pos, result, 0, len);
		return result;
	}
}