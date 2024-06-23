package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitUtil;

public class HBFrame extends MoteFrame {
	
	public static final byte TYPE = 0X03;
	
	public HBFrame(byte[] raw) {
		super(raw);
	}
	
	public HBFrame(String sn) {
		this.setSn(sn, NUL);
		this.setType(TYPE);
	}
	
	@Override
	protected void parse() {
		super.parse();
		var idx = payload.length - 1;
		setSn(BitUtil.slice(payload, 0, idx));
	}

	@Override
	public byte[] encode() {
		return this.serialize0(getSn(), TYPE);
	}

}
