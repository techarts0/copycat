package cn.techarts.copycat.ext.mote;

import java.time.Instant;
import cn.techarts.copycat.util.BitUtil;

/**
 * Layout of data field:
 * |   SN   |   NUL  |  TS(Optional)  | SENSORS |
 * |N bytes | 1 byte |  8 bytes       |   TT    |
 * 
 * If the TS-Type equals 0, the TS field is ignored.
 */

public class DataFrame extends MoteFrame {
	
	public static final byte TYPE = 0X02;
	
	private long timestamp; 		//UTC time-stamp in second
	
	public DataFrame(byte[] raw) {
		super(raw);
	}
	
	public DataFrame(String sn, byte[] data, boolean dtuts) {
		this.setPayload(data);
		this.setSn(sn, dtuts ? NUL : ESC);
		this.timestamp = dtuts ? now() : 0;
	}
	
	protected void parse() {
		super.parse();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		this.setSn(BitUtil.slice(payload, 0, idx));
		
		byte[] tsbs = null; // Next 8 bytes of TIMESTAMP
		var dtuts = (payload[idx] == 0X1B); //Using DTU Time-Stamp?
		if(dtuts) {
			tsbs = new byte[] {payload[idx + 1], payload[idx + 2], 
					              payload[idx + 3], payload[idx + 4],
					              payload[idx + 5], payload[idx + 6],
					              payload[idx + 7], payload[idx + 8]};
		}
		this.setTimestamp(dtuts ? BitUtil.toLong(tsbs) : this.now());
		
		int offset = dtuts ? 9 : 1, len = payload.length - idx - offset;
		
		this.payload = BitUtil.slice(this.payload, idx + offset, len);
	}

	@Override
	public byte[] serialize() {
		var dtuts = this.timestamp == 0;
		var start = sn.length + (dtuts ? 8 : 0);
		var data = new byte[start + payload.length];
		System.arraycopy(sn, 0, data, 0, sn.length);
		if(dtuts) {
			var tsbs = BitUtil.toBytes(timestamp);
			System.arraycopy(tsbs, 0, data, sn.length, 8);
		}
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