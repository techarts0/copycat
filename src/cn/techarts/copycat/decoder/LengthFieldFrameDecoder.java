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
import java.util.List;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitHelper;
import cn.techarts.copycat.util.Utility;

/**
 * @author rocwon@gmail.com
 */
public class LengthFieldFrameDecoder<T extends  Frame> extends Decoder<T> {
	
	protected int offset = 0, length = 2; 
	
	public LengthFieldFrameDecoder(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}
		
	@Override
	public T[] decode(ByteBuf data) {
		var prefix = offset + length;
		List<T> result = new ArrayList<>();
		while(data.test(prefix)){
			var pos = data.current() + offset;
			int len = len(data.lend(pos, length));
			var size = prefix + len;
			if(data.remaining() < size) break;
			var fbs = data.steal(size); //A frame
			result.add(Utility.frame(frameClass, fbs));
		}
		if(result.isEmpty()) return null; //Need more
		return result.toArray(Utility.array(frameClass));
	}
	
	protected static int len(byte[] bytes) {
		if(bytes == null) return 0;
		int len = bytes.length;
		if(len == 0) return 0;
		if(len == 1) return bytes[0];
		if(len == 2) {
			return BitHelper.toShort(bytes);
		}else {
			return BitHelper.toInt(bytes);
		}
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}