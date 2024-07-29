package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

import cn.techarts.copycat.util.BitHelper;

public class HBFrame extends MoteFrame {
	
	public static final byte TYPE = 0X04;
	
	public HBFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public HBFrame(String sn) {
		this.setSn(sn, NUL);
		this.setType(TYPE);
	}
	
	@Override
	protected void decode() {
		super.decode();
		var idx = payload.length - 1;
		setSn(BitHelper.slice(payload, 0, idx));
	}

	@Override
	public ByteBuffer encode() {
		return this.serialize0(this.sn, TYPE);
	}

}
