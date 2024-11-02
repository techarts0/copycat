package cn.techarts.copycat.std.http;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;

public class HttpResponseDecoder extends Decoder<HttpResponse> {

	@Override
	public boolean isSingleton() {
		return true;
	}

	@Override
	public HttpResponse[] decode(ByteBuf data) {
		// TODO Auto-generated method stub
		return null;
	}

}
