package cn.techarts.copycat.ext.mote;


import java.util.ArrayList;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.util.Utility;
import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.decoder.VarLengthFieldFrameDecoder;

public class MoteDecoder extends VarLengthFieldFrameDecoder<MoteFrame> {

	public MoteDecoder() {
		super(2, 4);
	}
	
	@Override
	public MoteFrame[] decode(ByteBuf data) {
		int maxHeadLength = offset + maxLength;
		var result = new ArrayList<MoteFrame>();
		while(data.test(offset + 1)){
			int f = -7, remaining = 0, prefix = offset;
			var pos = offset + data.current();
			var type = data.lend(pos - 1);
			do {
				if(++prefix > maxHeadLength) {
					throw MoteException.invalidRemaining();
				}
				var b = data.lend(pos);
				remaining += ((b & 127) << (f += 7));
				if((b & 128) == 0) break; //End
			}while(data.test(++pos));
			
			var size = prefix + remaining;
			if(data.remaining() < size) break;
			var fbs = data.steal(size);
			var clazz = toFrameClass(type); // **
			result.add(frame(clazz, fbs, remaining));
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
	
	/**
	 *@param data If it's null, the default constructor will be called. Please ensure
	 *there is a default constructor in the frame class before calling the method. 
	 */
	private static<T> T frame(Class<T> type, byte[] data, int attachment) {
		try {
			if(data == null) { //Default constructor
				var constructor = type.getDeclaredConstructor();
				return constructor != null ? constructor.newInstance() : null;
			}else { //Specific Constructor
				var constructor = type.getDeclaredConstructor(byte[].class, int.class);
				return constructor != null ? constructor.newInstance(data, attachment) : null;
			}
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to create frame object.");
		}
	}
}