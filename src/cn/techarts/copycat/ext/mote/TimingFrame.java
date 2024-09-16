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
 * The frame is sent from server to DTU/RTU.
 * @author rocwon@gmail.com
 * 
 */
public class TimingFrame extends MoteFrame {
	
	public static final byte TYPE = 0X21;
	
	private long timestamp;	//UTC Time-Stamp
	
	
	public TimingFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	@Override
	protected void decode() {
		super.decode();
		this.timestamp = BitHelper.toInt(payload);
	}

	@Override
	public ByteBuffer encode() {
		this.timestamp = this.seconds();
		var buffer = this.serialize0(TYPE, 8);
		return buffer.appendLong(timestamp).toByteBuffer();
	}
	
	public long getTimestamp() {
		return timestamp;
	}
}