package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitHelper;

/**
 * The frame is sent from server to DTU/RTU.
 * 
 */
public class TimingFrame extends MoteFrame {
	
	public static final byte TYPE = 0X21;
	
	private long timestamp;	//UTC Time-Stamp
	
	
	public TimingFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	@Override
	protected void parse() {
		super.parse();
		this.timestamp = BitHelper.toInt(payload);
	}

	@Override
	public byte[] encode() {
		this.timestamp = this.seconds();
		return serialize0(BitHelper.toBytes(timestamp), TYPE);
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
}