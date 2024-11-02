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
 * @author rocwon@gmail.com
 */
public class HBFrame extends MoteFrame {
	
	public static final byte TYPE = 0X04;
	
	public HBFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public HBFrame(String sn) {
		this.setSn(sn, NUL);
		this.setType(TYPE);
	}
	
	@Override
	protected void decode() {
		super.decode();
		var idx = payload.length - 1;
		setSn(BitHelper.slice(payload, 0, idx));
	}

	@Override
	public ByteBuffer encode() {
		return this.serialize0(this.sn, TYPE);
	}

}
