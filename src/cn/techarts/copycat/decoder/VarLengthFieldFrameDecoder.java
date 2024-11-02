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

package cn.techarts.copycat.decoder;

import java.util.ArrayList;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.util.Utility;
import cn.techarts.copycat.Panic;

/**
 * The protocol is similar to MQTT. If the current byte is great than 
 * 127(The Most Significant Bit is 1) means what having next byte.
 * 
 * | Fixed Header | First Length Byte | Optional Length Byte | Optional Length Byte | Optional Length Byte  |
 * |     offset   |       1 byte      |        1 byte        |    1   byte          | 1 byte(MSB is 0)      |
 * 
 * @author rocwon@gmail.com
 */
public class VarLengthFieldFrameDecoder<T extends  Frame> extends Decoder<T> {
	protected int offset = 0, maxLength = 1;
	
	public VarLengthFieldFrameDecoder(int offset, int maxLength) {
		this.offset = offset;
		this.maxLength = maxLength;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		var result = new ArrayList<T>();
		int maxHeadLength = offset + maxLength;
		while(data.test(offset + 1)){
			int f = -7, remaining = 0, prefix = offset;
			var pos = offset + data.current();
			do {
				if(++prefix > maxHeadLength) {
					throw new Panic("Illegal remaining length.");
				}
				var b = data.lend(pos);
				remaining += ((b & 127) << (f += 7));
				if((b & 128) == 0) break; //End
			}while(data.test(++pos));
			
			var size = prefix + remaining;
			if(data.remaining() < size) break;
			var fbs = data.steal(size);
			result.add(Utility.frame(frameClass, fbs));
		}
		if(result.isEmpty()) return null;
		return result.toArray(Utility.array(frameClass, 0));
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}