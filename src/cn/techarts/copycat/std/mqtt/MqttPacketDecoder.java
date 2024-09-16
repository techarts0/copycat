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

package cn.techarts.copycat.std.mqtt;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.util.Utility;

/**
 * An implementation of <a href="https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html">MQTT/5.0</a>
 * @author rocwon@gmail.com
 */
public class MqttPacketDecoder extends Decoder<MqttPacket> {
	
	@Override
	public MqttPacket[] decode(ByteBuf data) {
		List<MqttPacket> result = new ArrayList<>();
		while(data.test(2)){ //At least 2 bytes
			int prefix = 1, f = -7, remaining = 0;
			while(data.test(++prefix)) {
				if(prefix > 5) {
					throw new MqttException("Illegal remaining length.");
				}
				f += 7; //Shift to left 7 bits
				var b = data.lend(prefix - 1);
				remaining += ((b & 127) << f);
				if((b & 128) == 0) break; //Remaining Length Bytes End
			}
			
			var size = prefix + remaining;
			if(data.remaining() < size) break;
			result.add(new MqttPacket(data.steal(size)));
		}
		return result.isEmpty() ? null : result.toArray(Utility.array(frameClass, 0));
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
