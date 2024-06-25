package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.util.BitUtil;

/**
 * Upstream. Layout of data field:
 * |   SN   |   NUL  |  TS(Optional)  | SENSORS |
 * |N bytes | 1 byte |  8 bytes       |   TT    |
 * 
 * If the TS-Type equals 0, the TS field is ignored.
 */

public class DataFrame extends MoteFrame {
	
	public static final byte TYPE = 0X02;
	
	private byte[] timestamp; 		//UTC time-stamp in second
	
	public DataFrame(byte[] raw) {
		super(raw);
	}
	
	public DataFrame(String sn, byte[] data, Precision p) {
		this.setPayload(data);
		this.setSn(sn, p.getPrecision());
		timestamp = generateTimeStamp(p);
	}
	
	private byte[] generateTimeStamp(Precision p) {
		if(p == Precision.NUL) return null;
		if(p == Precision.SEC) {
			return BitUtil.toBytes(seconds());
		}else {
			return BitUtil.toBytes(milliseconds());
		}
	}
	
	protected void parse() {
		super.parse();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		this.setSn(BitUtil.slice(payload, 0, idx));
		
		byte[] tsBytes = null; // Next n bytes of TIMESTAMP
		byte tsLength = payload[idx]; //Time-Stamp-Length
		if(tsLength == 0) {
			
		}else if(tsLength == 4) {
			tsBytes = new byte[] {	payload[idx + 1], payload[idx + 2], 
		              				payload[idx + 3], payload[idx + 4]};
		}else if(tsLength == 8) {
			tsBytes = new byte[] {	payload[idx + 1], payload[idx + 2], 
		              				payload[idx + 3], payload[idx + 4],
		              				payload[idx + 5], payload[idx + 6],
		              				payload[idx + 7], payload[idx + 8]};
		}else {
			throw MoteException.invalidPrecision(tsLength);
		}
		this.setTimestamp(tsBytes); //0, 4, or 8 bytes
		
		int offset = tsLength + 1, len = payload.length - idx - offset;
		
		this.payload = BitUtil.slice(this.payload, idx + offset, len);
	}

	@Override
	public byte[] encode() {
		var len = timestamp == null ? 0 : timestamp.length;
		var start = sn.length + len;
		var data = new byte[start + payload.length];
		System.arraycopy(sn, 0, data, 0, sn.length);
		if(len > 0) {
			System.arraycopy(timestamp, 0, data, sn.length, len);
		}
		System.arraycopy(payload, 0, data, start, payload.length);
		return this.serialize0(data, TYPE);
	}
	
	public long getTimestamp() {
		if(timestamp == null) return 0;
		if(timestamp.length == 0) return 0;
		if(timestamp.length == 4) {
			return BitUtil.toInt(timestamp);
		}else {
			return BitUtil.toLong(timestamp);
		}
	}

	public void setTimestamp(byte[] timestamp) {
		this.timestamp = timestamp;
	}
}