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

import cn.techarts.copycat.util.BitHelper;

/**
 * Response
 * @author rocwon@gmail.com
 */
public class StatusFrame extends MoteFrame {
	
	public static final byte TYPE = 0X20;
	
	private byte status;
	
	public StatusFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public StatusFrame(String sn, byte status) {
		this.setSn(sn, NUL);
		this.status = status;
	}
	
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx != -1) {
			setSn(BitHelper.slice(payload, 0, idx));
		}
		setStatus(payload[idx + 1]);
	}
	
	public ByteBuffer encode() {
		var buffer = this.serialize0(TYPE, sn.length + 1);
		buffer.append(sn).appendByte(status);
		return buffer.toByteBuffer();
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}	
}