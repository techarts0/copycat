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
import cn.techarts.copycat.util.Utility;

/**
 * @author rocwon@gmail.com
 */
public class DelimiterFrameDecoder<T extends Frame> extends Decoder<T> {
	private byte[] delimiters = null;
	
	public DelimiterFrameDecoder(byte[] delimiters) {
		this.delimiters = delimiters;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		List<T> result = new ArrayList<>();
		int len = delimiters.length, index = 0;
		for(int i = 0; i < data.remaining(); i++) {
			if(data.lend(i) != delimiters[0]) continue;
			for(int j = 1; j < len; j++) {
				i += j;
				if(data.lend(i) != delimiters[j]) break;
				if(j == len - 1) { //Completed
					var size = i + 1 - index - len;
					index += (i + 1); //Move to start of next
					var fbs = data.steal(size, len);
					result.add(Utility.frame(frameClass, fbs));
				}
			}
		}
		return result.isEmpty() ? null : result.toArray(Utility.array(frameClass, 0));
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}