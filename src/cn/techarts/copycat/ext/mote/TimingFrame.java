package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitUtil;

/**
 * The frame is sent from server to DTU/RTU.
 * 
 */
public class TimingFrame extends MoteFrame {
	
	public static final byte TYPE = 0X21;
	
	private int timestamp;	//UTC Time-Stamp
	
	@Override
	protected void parse() {
		super.parse();
		var tmp = new byte[] {payload[0], payload[1], payload[2], payload[3]};
		setTimestamp(BitUtil.toInt(tmp));
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
}
