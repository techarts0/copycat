/*
 * Copyright (C) 2024 techarts.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;

import cn.techarts.copycat.util.BitHelper;

/**
 * Upstream. Layout of data field:
 * |   SN   |   NUL  |  TS(Optional)            | SENSORS |
 * |N bytes | 1 byte |  0 OR 4 OR 8 bytes       |   TT    |
 * 
 * If the TS-Type equals 0, the TS field is ignored.
 * 
 * @author rocwon@gmail.com
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
			return BitHelper.toBytes((int)seconds());
		}else {
			return BitHelper.toBytes(milliseconds());
		}
	}
	
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) return; //Without SN and TS
		
		sn = BitHelper.slice(payload, 0, idx);
		
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
		
		int offset = idx + tsLength + 1, len = payload.length - offset;
		
		this.payload = BitHelper.slice(this.payload,  offset, len);
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