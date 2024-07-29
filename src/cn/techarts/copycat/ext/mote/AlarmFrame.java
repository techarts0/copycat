package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

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
	protected void decode() {
		super.decode();
		var idx = indexOfNul(payload, 0);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		var len = payload.length - idx - 1;
		this.setSn(BitHelper.slice(payload, 0, idx));
		payload = BitHelper.slice(payload, idx + 1, len);
	}
	
	@Override
	public ByteBuffer encode() {
		var length = sn.length + payload.length;
		var buffer = serialize0(TYPE, length);
		buffer.append(this.sn);
		buffer.append( this.payload);
		return buffer.toByteBuffer();
	}
}