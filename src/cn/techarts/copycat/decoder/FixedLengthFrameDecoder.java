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

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

/**
 * @author rocwon@gmail.com
 */
public class FixedLengthFrameDecoder<T extends Frame> extends Decoder<T>{
	
	private int length;
	
	public FixedLengthFrameDecoder(int length) {
		this.length = length;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		if(data.remaining() < length) return null;
		int fs = data.remaining() / length;
		T[] result = Utility.array(frameClass, fs);
		for(int i = 0; i < fs; i++) {
			var fbs = data.steal(length);
			var frame = Utility.frame(frameClass, fbs);
			if(frame != null) result[i] = frame;
		}
		return result;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}