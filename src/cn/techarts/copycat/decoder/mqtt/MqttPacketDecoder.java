package cn.techarts.copycat.decoder.mqtt;

import java.util.ArrayList;
import java.util.List;

import cn.techarts.copycat.Utility;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;

public class MqttPacketDecoder<T extends Frame> extends Decoder<T> {

	private int offset = 0, length = 2; 
	@Override
	public T[] decode(ByteBuf data) {
		var prefix = offset + length;
		List<T> result = new ArrayList<>();
		if(data.test(5)) {
			int len = 1;
			var fs = data.borrow(1, 4);
			for(int i = 1; i < 4; i++) {
				if(hasNext(fs[i])) len += 1;
			}
			fs = data.borrow(1, len);
			Utility.toInt(fs);
		}
		return null;
	}
		
	private boolean hasNext(byte f) {
		return (f & (byte)128) == 1;
	}
	
//	public static int decodeRemainingLength(byte[] buffer) {
//		  int multiplier = 1;
//		  int remainingLength = 0;
//		  int byteIndex = 0;
//
//		  do {
//		    byte currentByte = buffer[byteIndex];
//		    remainingLength += (currentByte & 0x7F) * multiplier;
//		    multiplier *= 128;
//		    byteIndex++;
//		  } while ((currentByte & 0x80) != 0 && byteIndex < 4); // Limit to 4 bytes as per MQTT spec
//
//		  if (byteIndex == 4 && (currentByte & 0x80) != 0) {
//		    throw new IllegalArgumentException("Remaining length exceeds 4 bytes");
//		  }
//
//		  return remainingLength;
//		}
//
//	}

}
