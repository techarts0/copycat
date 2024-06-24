package cn.techarts.copycat.ext.mote;

/**
 * Downstream frame: The instructions send to device from the server.<br>
 * For example, you want to set the temperature, valve position, etc.
 */
public class ControlFrame extends MoteFrame {
	
	public ControlFrame(byte[] raw) {
		super(raw);
	}
	
	public ControlFrame(byte[] control, byte type) {
		this.setType(type);
		this.setPayload(control);
	}
	
	@Override
	protected void parse() {
		super.parse();
	}

	@Override
	public byte[] encode() {
		return serialize0(payload, getType());
	}
	
	public byte[] getControl() {
		return this.payload;
	}
}