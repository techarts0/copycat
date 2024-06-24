package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitUtil;

/**
 * The frame is sent from server to DTU/RTU.
 * 
 */
public class TimingFrame extends MoteFrame {
	
	public static final byte TYPE = 0X21;
	
	private long timestamp;	//UTC Time-Stamp
	
	
	public TimingFrame(byte[] raw) {
		super(raw);
	}
	
	@Override
	protected void parse() {
		super.parse();
		this.timestamp = BitUtil.toInt(payload);
	}

	@Override
	public byte[] encode() {
		this.timestamp = this.seconds();
		return serialize0(BitUtil.toBytes(timestamp), TYPE);
	}
	
	
	public long getTimestamp() {
		return timestamp;
	}
}