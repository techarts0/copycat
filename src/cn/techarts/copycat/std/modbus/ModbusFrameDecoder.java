package cn.techarts.copycat.std.modbus;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

/**
 * MODBUS TCP Decoder.
 * |transaction id | protocol | remaining length | identifier|  DATA   |  
 * | 2 bytes       |  2 bytes |    2 bytes       |   1 byte  | N bytes |
 */
public class ModbusFrameDecoder extends LengthFieldFrameDecoder<ModbusFrame> {

	public ModbusFrameDecoder() {
		super(4, 2);
	}

	@Override
	public ModbusFrame[] decode(ByteBuf data) {
		return super.decode(data);
	}
}