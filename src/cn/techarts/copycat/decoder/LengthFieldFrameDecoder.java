package cn.techarts.copycat.decoder;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitUtil;
import cn.techarts.copycat.util.Utility;

public class LengthFieldFrameDecoder<T extends  Frame> extends Decoder<T> {
	
	protected int offset = 0, length = 2; 
	
	public LengthFieldFrameDecoder(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}
		
	@Override
	public T[] decode(ByteBuf data) {
		var prefix = offset + length;
		List<T> result = new ArrayList<>();
		while(data.test(prefix)){
			var pos = data.current() + offset;
			int len = len(data.lend(pos, length));
			var size = prefix + len;
			if(data.remaining() < size) break;
			var fbs = data.steal(size); //A frame
			result.add(Utility.frame(frameClass, fbs));
		}
		if(result.isEmpty()) return null; //Need more
		return result.toArray(Utility.array(frameClass));
	}
	
	protected static int len(byte[] bytes) {
		if(bytes == null) return 0;
		int len = bytes.length;
		if(len == 0) return 0;
		if(len == 1) return bytes[0];
		if(len == 2) {
			return BitUtil.toShort(bytes);
		}else {
			return BitUtil.toInt(bytes);
		}
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}