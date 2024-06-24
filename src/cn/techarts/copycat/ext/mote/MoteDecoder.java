package cn.techarts.copycat.ext.mote;

import java.util.ArrayList;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.util.Utility;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class MoteDecoder extends LengthFieldFrameDecoder<MoteFrame> {

	public MoteDecoder() {
		super(4, 2);
	}
	
	public MoteFrame[] decode(ByteBuf data) {
		var prefix = offset + length;
		var result = new ArrayList<MoteFrame>();
		while(data.test(prefix)){
			
			var f = data.lend(data.current(), 2);
			if(f[0] != 0X27 || f[1] != 0X66) {
				throw MoteException.itIsNotMote();
			}
			
			var pos = data.current() + offset;
			var type = data.lend(pos - 1);// **
			int len = len(data.lend(pos, length));
			var size = prefix + len;
			if(data.remaining() < size) break;
			var fbs = data.steal(size);
			
			var clazz = toFrameClass(type); // **
			result.add(Utility.frame(clazz, fbs));
		}
		if(result.isEmpty()) return null;
		return result.toArray(Utility.array(frameClass, 0));
	}
	
	private Class<? extends MoteFrame> toFrameClass(byte type){
		switch(type) {
		case HBFrame.TYPE:
			return HBFrame.class;
		case DataFrame.TYPE:
			return DataFrame.class;
		case RegisterFrame.TYPE:
			return RegisterFrame.class;
		case StatusFrame.TYPE:
			return StatusFrame.class;
		case TimingFrame.TYPE:
			return TimingFrame.class;
		default:
			if(type > 33 && type < 128) {
				return ControlFrame.class;
			}else {
				throw MoteException.invalidType(type);
			}
		}
	}
}
