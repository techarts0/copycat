package cn.techarts.copycat.decoder.modbus;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class ModbusFrameDecoder extends LengthFieldFrameDecoder<ModbusFrame> {

	public ModbusFrameDecoder() {
		super(4, 2);
	}

	@Override
	public ModbusFrame[] decode(ByteBuf data) {
		return super.decode(data);
	}

}
