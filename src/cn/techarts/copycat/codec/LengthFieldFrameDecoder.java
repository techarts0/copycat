package cn.techarts.copycat.codec;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.Utility;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;

public class LengthFieldFrameDecoder<T extends  Frame> extends Decoder<T> {
	
	private int offset = 0, length = 2; 
	
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
			int len = len(data.borrow(pos, length));
			var size = prefix + len;
			if(data.length() < size) break;
			var fbs = data.consume(size);
			result.add(Utility.frame(frameClass, fbs));
		}
		return result.isEmpty() ? null : result.toArray(Utility.array(frameClass, 0));
	}
	
	private static int len(byte[] bytes) {
		if(bytes == null) return 0;
		int len = bytes.length;
		if(len == 0) return 0;
		if(len == 1) return bytes[0];
		if(len == 2) {
			return Utility.toShort(bytes);
		}else {
			return Utility.toInt(bytes);
		}
	}

}
