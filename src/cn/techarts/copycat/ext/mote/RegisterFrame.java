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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import cn.techarts.copycat.util.BitHelper;

/**
 * The layout of data field:<p>
 * |  SN     | Delimiter |  token   | Delimiter  |  TT Protocol |
 * |  any    |  1(NUL)   |   any    |  1(NUL)    |  1 byte      |
 * 
 * @author rocwon@gmail.com
 */

public class RegisterFrame extends MoteFrame {
	
	public static final byte TYPE = 0X01;
	
	private byte[] token;	//Device Token
	private byte protocol;	//TT Protocol type
	
	public RegisterFrame(byte[] data, int remaining) {
		super(data, remaining);
	}
	
	/**
	 * Protocol: MODBUS
	 * TS-Type: 1, DTU Time-Stamp
	 */
	public RegisterFrame(String sn, String token) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol((byte)0); //MODBUS
	}	
	
	public RegisterFrame(String sn, String token, byte protocol) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol(protocol);
	}
	
	public RegisterFrame(String sn, byte[] token, byte protocol) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol(protocol);
	}
	
	@Override
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		setSn(BitHelper.slice(payload, 0, idx));
		var idx2 = this.indexOfNul(payload, idx + 1);
		var len = idx2 - idx - 1;
		setToken(BitHelper.slice(payload, idx + 1, len));
		setProtocol(payload[idx + 1]); // 1 byte only
	}
	
	public ByteBuffer encode() {
		var vlen = sn.length + token.length;
		var buffer = this.serialize0(TYPE, vlen + 1);
		buffer.append(sn).append(token);
		buffer.appendByte(protocol);
		return buffer.toByteBuffer();
	}
	
	public byte[] getToken() {
		return this.token;
	}
	
	public String getTokenString() {
		if(this.token == null) return null;
		if(this.token.length == 0) return null;
		return new String(token, StandardCharsets.US_ASCII);
	}

	public void setToken(String token) {
		if(token == null || token.isEmpty()) return;
		var tmp = new StringBuilder(token).append(NUL);
		this.token = tmp.toString().getBytes(StandardCharsets.US_ASCII);
	}	
	
	public void setToken(byte[] token) {
		this.token = token;
	}	

	public byte getProtocol() {
		return protocol;
	}

	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

	public void setSn(byte[] sn) {
		this.sn = sn;
	}
}