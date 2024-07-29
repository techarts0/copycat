package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

import cn.techarts.copycat.util.BitHelper;

/**
 * Response
 */
public class StatusFrame extends MoteFrame {
	
	public static final byte TYPE = 0X20;
	
	private byte status;
	
	public StatusFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public StatusFrame(String sn, byte status) {
		this.setSn(sn, NUL);
		this.status = status;
	}
	
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx != -1) {
			setSn(BitHelper.slice(payload, 0, idx));
		}
		setStatus(payload[idx + 1]);
	}
	
	public ByteBuffer encode() {
		var buffer = this.serialize0(TYPE, sn.length + 1);
		buffer.append(sn).appendByte(status);
		return buffer.toByteBuffer();
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}	
}