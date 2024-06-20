package cn.techarts.copycat.ext.mars;

import java.time.Instant;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.util.BitUtil;

/**
 * Layout of data field:
 * |   SN   |   NUL  |  TS     | SENSORS|
 * |N bytes | 1 byte | 4 bytes |   TT   |
 *  
 */

public class SensorFrame extends MarsFrame {
	
	public static final byte TYPE = 0X02;
	
	private long timestamp; 		//UTC time-stamp in second
	
	protected void parse() {
		super.parse();
		var idx = super.indexOfFirst0(payload);
		if(idx == -1) {
			throw new CopycatException("Illegal device SN.");
		}
		setSn(BitUtil.slice(payload, 0, idx));
		var tmp = new byte[] {payload[idx + 1], payload[idx + 2], payload[idx + 3], payload[idx + 4]};
		setTimestamp(BitUtil.toInt(tmp));
		this.payload = BitUtil.slice(payload, idx + 5, payload.length - idx - 5);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		if(timestamp > 0) { //DTU TS
			this.timestamp = timestamp;
		}else {	//Server Time-Stamp
			this.timestamp = Instant.now().getEpochSecond();
		}
	}
}