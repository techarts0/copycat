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

package cn.techarts.copycat.std.modbus;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

/**
 * MODBUS TCP Decoder.
 * |transaction id | protocol | remaining length | identifier|  DATA   |  
 * | 2 bytes       |  2 bytes |    2 bytes       |   1 byte  | N bytes |
 * 
 * @author rocwon@gmail.com
 */
public class MBTcpFrameDecoder extends LengthFieldFrameDecoder<MBTcpFrame> {

	public MBTcpFrameDecoder() {
		super(4, 2);
	}

	@Override
	public MBTcpFrame[] decode(ByteBuf data) {
		return super.decode(data);
	}
}