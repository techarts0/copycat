package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

/**
 * Downstream frame: The instruction send to device from the server.<br>
 * For example, you want to set the temperature, valve position, etc.
 */
public class ControlFrame extends MoteFrame {
	
	public ControlFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public ControlFrame(byte[] control, byte type) {
		this.setType(type);
		this.setPayload(control);
	}
	
	@Override
	protected void decode() {
		super.decode();
	}

	@Override
	public ByteBuffer encode() {
		return serialize0(payload, getType());
	}
	
	public byte[] getControl() {
		return this.payload;
	}
}