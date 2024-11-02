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

package cn.techarts.copycat.std.http;

import cn.techarts.copycat.core.ByteBuf;

public class HttpHelper {
	public static int nextHeader(ByteBuf data, int from) {
		for(int i = from; i < data.remaining(); i++) {
			if(data.lend(i) == 0X0D && 
			   data.lend(i + 1) == 0X0A) return i + 1;
		}
		return -1; //Moved to the end of HTTP head fields
	}
}
