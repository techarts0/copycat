package cn.techarts.copycat.decoder;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;

public class MqttPacketDecoder<T extends Frame> extends Decoder<T> {

	@Override
	public T[] decode(ByteBuf data) {
		// TODO Auto-generated method stub
		return null;
	}

}
