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

package cn.techarts.copycat.std.mqtt;

import java.nio.ByteBuffer;

import cn.techarts.copycat.core.Frame;

/**
 * @author rocwon@gmail.com
 */
public class MqttPacket extends Frame {
	private byte flag; //packet flag: 4 bits;
	private byte type; //packet type: 4 bits;
	private int prefix = 0; //Skip fix head and remaining length bytes
	
	public MqttPacket(byte[] raw) {
		super(raw);
		parseFixedHead(); //flag, type
		prefix = prefixLength(); //Content from...
	}

	@Override
	public void decode() {
		
		
	}

	@Override
	public ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int prefixLength() {
		int prefix = 1;
		while(true) {
			var b = rawdata[prefix++];
			if((b & 128) == 0) break; 
		}
		return prefix;
	}
	
	private void parseFixedHead() {
		this.setFlag((byte)(rawdata[0] & 0X0F));
		this.setType((byte)(rawdata[0] & 0XF0));
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
