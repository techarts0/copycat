package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitHelper;
import cn.techarts.copycat.util.StrHelper;

/**
 * A customized protocol for general IoT application.<br>
 * If you don't want to design your own protocol, mote is your best choice.<br> 
 * Trust me, it fullfills your requirement in most scenarios.
 * <p>Frame Structure:</p>
 * 
 * |   Flag  | Type   |  Remaining Length  | Data  |
 * | 1 byte  | 1 byte |     1 - 4 bytes    | <- N  |
 *
 * <p>Flag:</p>
 * | 1 | 1 | 1 | 1 | 1 | 0 | 0 | 1 |
 * |  Protocol Prefix  |  Version  |   
 * <p>
 *  Generally, the device SN is stuffed in the head of data field and ended with 
 *  a specific ASCII char NUL(or 0X04, 0X08). All upstream packets contain a device SN 
 *  and down-stream packets without it.
 * 
 */
public class MoteFrame extends Frame {
	public static final char NUL = 0X00;	// NUL
	public static final char TS4 = 0X04; 	// End Of Transmit
	public static final char TS8 = 0X08; 	// Back-Space
	
	public static final byte FLAG = (byte)0XF9;	//Protocol Version
	
	private byte type = 0;						//Frame Type(Refer to MarsType)
	protected byte[] sn;						//Device Serial Number
	protected int length;						//The Remaining Bytes Length
	protected byte[] payload;					//REAL PAYLOAD
	
	public MoteFrame() {}
	
	public MoteFrame(byte[] raw, int remaining) {
		this.rawdata = raw;
		this.length = remaining;
		this.decode();
	}
	
	@Override
	protected void decode() {
		if(rawdata[0] != FLAG) {
			throw MoteException.itIsNotMote();
		}
		this.setType(rawdata[1]);
		var idx = rawdata.length - length;
		this.payload = BitHelper.slice(rawdata, idx, length);
	}
	
	@Override
	public ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Head + Data
	 */
	protected ByteBuffer serialize0(byte[] data, byte type) {
		length = data == null ? 0 : data.length;
		var tmp = BitHelper.toRemainingBytes(length);
		var buffer = new ByteBuf(2 + tmp.length + length);
		buffer.appendByte(FLAG);
		buffer.appendByte(type);
		buffer.append(tmp);
		buffer.append(data);
		return buffer.toByteBuffer();
	}
	
	protected ByteBuf serialize0(byte type, int length) {
		var tmp = BitHelper.toRemainingBytes(length);
		var result = new ByteBuf(2 + tmp.length + length);
		result.appendByte(FLAG);
		result.appendByte(type);
		result.append(tmp);
		return result;
	}
	
	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte[] getPayload() {
		return payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
		if(payload != null) {
			this.length = (short)payload.length;
		}
	}
	
	/**0X00: NUL or 0X1B: ESC
	 * 
	 * NUL: There is a time-stamp(8 bytes) followed.<br>
	 * ESC: There is not a time-stamp(8 bytes) followed.<br>
	 * The convention is only effective for DataFrame.
	 */
	protected int indexOfDelimiter(byte[] bytes) {
		if(bytes == null) return -1;
		int length = bytes.length;
		if(length == 0) return -1;
		
		for(int i = 0; i < length; i++) {
			if(bytes[i] == 0X00) return i;
			if(bytes[i] == 0X04) return i;
			if(bytes[i] == 0X08) return i;
		}
		return -1;
	}
	
	protected int indexOfNul(byte[] bytes, int start) {
		if(bytes == null) return -1;
		int length = bytes.length;
		if(length == 0) return -1;
		if(start >= length) return -1;
		
		for(int i = start; i < length; i++) {
			if(bytes[i] == 0X00) return i;
			if(bytes[i] == 0X1B) return i;
		}
		return -1;
	}

	public void setSn(byte[] sn) {
		this.sn = sn;
	}
	
	/**
	 * With the end delimiter char '0'
	 * */
	public void setSn(String sn, char endChar) {
		if(sn == null || sn.isEmpty()) return;
		var tmp = new StringBuilder(sn).append(endChar);
		this.sn = tmp.toString().getBytes(StandardCharsets.US_ASCII);
	}
	
	/**
	 * Returns the Device-SN with a string encoded as ASCII char.
	 */
	public String getSn() {
		if(sn == null) return null;
		if(sn.length == 0) return null;
		return StrHelper.toAsciiString(sn);
	}
	
	/**
	 * A time-zone insensitive UTC time-stamp
	 */
	public long milliseconds() {
		return System.currentTimeMillis();
	}
	
	/**
	 * A time-zone insensitive UTC time-stamp
	 */
	public long seconds() {
		return Instant.now().getEpochSecond();
	}
}