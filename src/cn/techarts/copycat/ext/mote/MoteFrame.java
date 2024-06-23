package cn.techarts.copycat.ext.mote;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitUtil;
import cn.techarts.copycat.util.StrUtil;

/**
 * A customized protocol for general IoT application.<br>
 * If you don't want to design your own protocol, mars is your best choice.<br> 
 * Trust me, it full fills your requirement in most scenarios.
 * <p>Frame Structure:</p>
 * 
 * |  Prefix   | Version | Type  |  Length  | Data |
 * | 0X27 0X66 | 1 byte  | 1 byte|  2 bytes | <-N  |
 * 
 *  Generally, the device SN is stuffed in the head of data field and ends with 
 *  a specific ASCII char NUL or ESC. All upstream packets contain a device SN 
 *  and down-stream packets without it.
 * 
 */
public class MoteFrame extends Frame {
	public static final char NUL = 0X00;
	public static final char ESC = 0X1B;
	public static final byte VERSION = 0x01;	//Protocol Version
	
	private byte type = 0;						//Frame Type(Refer to MarsType)
	protected byte[] sn;						//Device Serial Number
	protected short length;						//The Remaining Bytes Length
	protected byte[] payload;					//REAL PAYLOAD
	
	public MoteFrame() {}
	
	public MoteFrame(byte[] raw) {
		super(raw);
	}
	
	@Override
	protected void parse() {
		if(rawdata[2] != VERSION) {
			throw new RuntimeException("Illegal protocol version.");
		}
		this.setType(rawdata[3]);
		var tmp = new byte[] {rawdata[4], rawdata[5]};
		this.length = BitUtil.toShort(tmp);
		this.payload = BitUtil.slice(rawdata, 6, length);
	}
	
	@Override
	public byte[] encode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Head + Data
	 */
	protected byte[] serialize0(byte[] data, byte type) {
		length = (short)(data == null ? 0 : data.length);
		var result = new byte[6 + length];
		result[0] = 0x27;
		result[1] = 0x66;
		result[2] = VERSION;
		result[3] = type;
		var tmp = BitUtil.toBytes(length);
		result[4] = tmp[0];
		result[5] = tmp[1];
		System.arraycopy(data, 0, result, 6, length);
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
			if(bytes[i] == 0X1B) return i;
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

	public byte[] getSn() {
		return sn;
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
	 * Returns the device SN with a string encoded as ASCII char.
	 */
	public String getDsn() {
		if(sn == null) return null;
		if(sn.length == 0) return null;
		return StrUtil.toAsciiString(sn);
	}
	
	/**
	 * A time-zone insensitive UTC time-stamp
	 */
	public long now() {
		return Instant.now().getEpochSecond();
	}
}