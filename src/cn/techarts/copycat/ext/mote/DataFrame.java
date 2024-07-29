package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

import cn.techarts.copycat.util.BitHelper;

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
	
	public DataFrame(byte[] raw, int remaining) {
		super(raw, remaining);
	}
	
	public DataFrame(String sn, byte[] data, Precision p) {
		this.setPayload(data);
		this.setSn(sn, p.getPrecision());
		timestamp = generateTimeStamp(p);
	}
	
	private byte[] generateTimeStamp(Precision p) {
		if(p == Precision.NUL) return null;
		if(p == Precision.SEC) {
			return BitHelper.toBytes(seconds());
		}else {
			return BitHelper.toBytes(milliseconds());
		}
	}
	
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		this.setSn(BitHelper.slice(payload, 0, idx));
		
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
		
		this.payload = BitHelper.slice(this.payload, idx + offset, len);
	}

	@Override
	public ByteBuffer encode() {
		var len = timestamp == null ? 0 : timestamp.length;
		var buffer = serialize0(TYPE, sn.length + len + payload.length);
		buffer.append(sn);
		buffer.appendOn(timestamp, len > 0);
		buffer.append(payload);
		return buffer.toByteBuffer();
	}
	
	public long getTimestamp() {
		if(timestamp == null) return 0;
		if(timestamp.length == 0) return 0;
		if(timestamp.length == 4) {
			return BitHelper.toInt(timestamp);
		}else {
			return BitHelper.toLong(timestamp);
		}
	}

	public void setTimestamp(byte[] timestamp) {
		this.timestamp = timestamp;
	}
}