package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitUtil;

public class HBFrame extends MoteFrame {
	
	public static final byte TYPE = 0X03;
	
	@Override
	protected void parse() {
		super.parse();
		var idx = payload.length - 1;
		setSn(BitUtil.slice(payload, 0, idx));
	}

	@Override
	public byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}

}
