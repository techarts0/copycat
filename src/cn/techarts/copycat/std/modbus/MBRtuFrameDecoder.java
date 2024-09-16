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

import java.util.ArrayList;
import cn.techarts.copycat.Panic;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
/**
 *   |  Slave  |  Function Code | Byte Count | Data |  CRC |  <p>
 *        1    |         1      |      1     |  N   |  2   |  <p> 
 *   |  Slave  |  Function Code | Address | Value |  CRC   |  <p>   
 *   |    1    |        1       |    2    |   2   |   2    |  <p>  
 *   
 * @author rocwon@gmail.com
 */
public class MBRtuFrameDecoder extends Decoder<MBRtuFrame> {

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public MBRtuFrame[] decode(ByteBuf data) {
		MBRtuFrame frame = null;
		var result = new ArrayList<MBRtuFrame>();
		while(data.test(3)) {
			var pos = data.current() + 1;
			var funcode = data.lend(pos);
			if(funcode > 0X80) { //ERROR
				result.add(decode0(data, pos));
			}else {
				switch(funcode) {
					case 0X1:
					case 0X2:
					case 0X3:
					case 0X4:
						frame = decode1(data, pos);
						if(frame != null) {
							result.add(frame);
						}
						break;
					case 0X5:
					case 0X6:
					case 0X0B:
					case 0X0F:
					case 0X10:
						frame = decode2(data);
						if(frame != null) {
							result.add(frame);
						}
						break;
					default:
						throw new Panic("Unsupported function code.");
				}
			}
		}
		return result.toArray(new MBRtuFrame[0]); 
	}
	
	//Error
	private MBRtuFrame decode0(ByteBuf data, int pos) {
		if(data.remaining() < 3) return null;
		return new MBRtuFrame(data.steal(3));
	}
	
	//Function Code 1-4, 0X17: index 2 is length field
	private MBRtuFrame decode1(ByteBuf data, int pos) {
		var size = data.lend(pos) + 5;
		if(data.remaining() < size) return null;
		return new MBRtuFrame(data.steal(size));
	}
	
	//Function-Code 5, 6, 0X0B, 0X0F,  0X10: Fixed 1 + 5 + 2 bytes
	private MBRtuFrame decode2(ByteBuf data) {
		if(data.remaining() < 8) return null;
		return new MBRtuFrame(data.steal(8));
	}
}