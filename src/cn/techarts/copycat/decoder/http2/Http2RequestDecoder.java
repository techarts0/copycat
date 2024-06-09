package cn.techarts.copycat.decoder.http2;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;

/**
 *An implementation of HTTP/2.0
 *Please refer to RFC 7540 
 */
public class Http2RequestDecoder<T extends Frame> extends Decoder<T> {

	@Override
	public T[] decode(ByteBuf data) {
		// TODO Auto-generated method stub
		return null;
	}

}
