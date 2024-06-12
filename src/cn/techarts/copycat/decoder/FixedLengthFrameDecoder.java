package cn.techarts.copycat.decoder;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

public class FixedLengthFrameDecoder<T extends Frame> extends Decoder<T>{
	
	private int length;
	
	public FixedLengthFrameDecoder(int length) {
		this.length = length;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		if(data.remaining() < length) return null;
		int fs = data.remaining() / length;
		T[] result = Utility.array(frameClass, fs);
		for(int i = 0; i < fs; i++) {
			var fbs = data.steal(length);
			var frame = Utility.frame(frameClass, fbs);
			if(frame != null) result[i] = frame;
		}
		return result;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}