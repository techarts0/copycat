package cn.techarts.copycat.std.mqtt;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.util.Utility;

/**
 * An implementation of <a href="https://docs.oasis-open.org/mqtt/mqtt/v5.0/mqtt-v5.0.html">MQTT/5.0</a>
 * 
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
