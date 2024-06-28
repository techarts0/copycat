package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitHelper;

public class AlarmFrame extends MoteFrame {
	public static final byte TYPE = 0X03;
	
	public AlarmFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public AlarmFrame(String sn, byte[] alarm) {
		this.setSn(sn, NUL);
		this.setPayload(alarm);
	}
	
	@Override
	protected void parse() {
		super.parse();
		var idx = indexOfNul(payload, 0);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		var len = payload.length - idx - 1;
		this.setSn(BitHelper.slice(payload, 0, idx));
		payload = BitHelper.slice(payload, idx + 1, len);
	}
	
	@Override
	public byte[] encode() {
		var data = new byte[sn.length + payload.length];
		System.arraycopy(sn, 0, data, 0, sn.length);
		System.arraycopy(payload, 0, data, sn.length, payload.length);
		return this.serialize0(data, TYPE);
	}
}