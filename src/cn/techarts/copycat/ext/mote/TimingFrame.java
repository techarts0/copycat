package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

import cn.techarts.copycat.util.BitHelper;

/**
 * The frame is sent from server to DTU/RTU.
 * 
 */
public class TimingFrame extends MoteFrame {
	
	public static final byte TYPE = 0X21;
	
	private long timestamp;	//UTC Time-Stamp
	private int precision;
	
	public TimingFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public TimingFrame(int precision) {
		this.precision = precision;
	}
	
	@Override
	protected void decode() {
		super.decode();
		if(payload.length == 4) {
			this.timestamp = BitHelper.toInt(payload);
		}else {
			this.timestamp = BitHelper.toLong(payload);
		}
	}

	@Override
	public ByteBuffer encode() {
		var buffer = this.serialize0(TYPE, precision);
		if(precision == 8) {
			this.timestamp = this.milliseconds();
			return buffer.appendLong(timestamp).toByteBuffer();
		}else {
			this.timestamp = this.seconds();
			return buffer.appendInt((int)timestamp).toByteBuffer();	
		}
	}
	
	/**
	 * Milli-Seconds (8 bytes)
	 */
	public long getMilliseconds() {
		return timestamp;
	}
	
	/**
	 * Seconds(4 bytes)
	 */
	public int getSeconds() {
		return (int)timestamp;
	}
}