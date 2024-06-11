package cn.techarts.copycat.decoder;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

/**
 * The protocol is similar to MQTT. If the current byte is great than 
 * 127(The Most Significant Bit is 1) means what having next byte.
 * 
 * | Fixed Header | First Length Byte | Optional Length Byte | Optional Length Byte | ....
 * |     offset   |       1 byte      |        1 byte        |    1   byte          |
 * 
 */
public class VarLengthFieldFrameDecoder<T extends  Frame> extends Decoder<T> {
	private int offset = 0, maxLength = 1;
	
	public VarLengthFieldFrameDecoder(int offset, int maxLength) {
		this.offset = offset;
		this.maxLength = maxLength;
	}
	
	@Override
	public T[] decode(ByteBuf data) {
		var minHeadLength = offset + 1;
		var maxHeadLength = offset + maxLength;
		
		List<T> result = new ArrayList<>();
		while(data.test(minHeadLength)){ //At least n bytes
			int prefix = offset, f = -7, remaining = 0;
			while(data.test(++prefix)) {
				if(prefix > maxHeadLength) {
					throw new CopycatException("Illegal remaining length.");
				}
				f += 7; //Shift to left 7 bits
				var b = data.borrow(prefix - 1);
				remaining += ((b & 127) << f);
				if((b & 128) == 0) break; //Remaining Length Bytes End
			}
			
			var size = prefix + remaining;
			if(data.length() < size) break;
			var fbs = data.consume(size);
			result.add(Utility.frame(frameClass, fbs));
		}
		if(result.isEmpty()) return null;
		return result.toArray(Utility.array(frameClass, 0));
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
