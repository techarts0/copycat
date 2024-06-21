package cn.techarts.copycat.ext.mote;

import java.time.Instant;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.util.BitUtil;

/**
 * Layout of data field:
 * |   SN   |   NUL  |  TS     | SENSORS |
 * |N bytes | 1 byte | 8 bytes |   TT    |
 *  
 */

public class DataFrame extends MoteFrame {
	
	public static final byte TYPE = 0X02;
	
	private long timestamp; 		//UTC time-stamp in second
	
	public DataFrame(byte[] raw) {
		super(raw);
	}
	
	public DataFrame(String sn, byte[] data) {
		this.setSn(sn);
		this.setPayload(data);
		this.timestamp = now();
	}
	
	protected void parse() {
		super.parse();
		var idx = super.indexOfFirst0(payload);
		if(idx == -1) {
			throw new CopycatException("Illegal device SN.");
		}
		setSn(BitUtil.slice(payload, 0, idx));
		var tmp = new byte[] {payload[idx + 1], payload[idx + 2], 
				              payload[idx + 3], payload[idx + 4],
				              payload[idx + 5], payload[idx + 6],
				              payload[idx + 7], payload[idx + 8]};
		setTimestamp(BitUtil.toLong(tmp));
		this.payload = BitUtil.slice(payload, idx + 9, payload.length - idx - 9);
	}

	@Override
	public byte[] serialize() {
		var start = sn.length + 8;
		var data = new byte[start + payload.length];
		System.arraycopy(sn, 0, data, 0, sn.length);
		var tkn = BitUtil.toBytes(timestamp);
		System.arraycopy(tkn, 0, data, sn.length, 8);
		System.arraycopy(payload, 0, data, start, payload.length);
		return this.serialize0(data, TYPE);
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