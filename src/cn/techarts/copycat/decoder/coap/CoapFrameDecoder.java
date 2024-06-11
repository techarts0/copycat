package cn.techarts.copycat.decoder.coap;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;

/**
 * We implemented the COAP on TCP (because COPYCAT does not support UDP now)
 */
public class CoapFrameDecoder extends Decoder<CoapFrame> {

	@Override
	public CoapFrame[] decode(ByteBuf data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
