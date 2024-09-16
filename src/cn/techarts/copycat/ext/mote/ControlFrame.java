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

/**
 * Downstream frame: The instruction send to device from the server.<br>
 * For example, you want to set the temperature, valve position, etc.
 * @author rocwon@gmail.com
 */
public class ControlFrame extends MoteFrame {
	
	public ControlFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public ControlFrame(byte[] control, byte type) {
		this.setType(type);
		this.setPayload(control);
	}
	
	@Override
	protected void decode() {
		super.decode();
	}

	@Override
	public ByteBuffer encode() {
		return serialize0(payload, getType());
	}
	
	public byte[] getControl() {
		return this.payload;
	}
}