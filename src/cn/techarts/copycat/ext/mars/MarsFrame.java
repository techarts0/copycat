package cn.techarts.copycat.ext.mars;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitUtil;

/**
 * A customized protocol for general IoT application.<br>
 * If you don't want to design your own protocol, mars is your best choice.<br> 
 * Trust me, it full fills your requirement in most scenarios.
 * <p>Frame Structure:</p>
 * 
 * | Prefix  | Version | Type  |  Length  | Data |
 * | 2 bytes | 1 byte  | 1 byte|  2 bytes | <-N  |
 * 
 *  Generally, the device SN is stuffed in the head of data field and ends with a specific ASCII char '\0'.
 * 
 */
public class MarsFrame extends Frame {
	private short prefix = 10086;	//Fixed 2 bytes[0x27, 0x66]
	private byte version;			//Protocol Version
	private byte type = 0;			//Frame Type(Refer to MarsType)
	protected byte[] sn;			//Device Serial Number
	protected short length;			//The Remaining Bytes Length
	protected byte[] payload;		//REAL PAYLOAD
	
	@Override
	protected void parse() {
		this.setVersion(data[2]);
		this.setType(data[3]);
		var tmp = new byte[] {data[4], data[5]};
		this.length = BitUtil.toShort(tmp);
		this.payload = BitUtil.slice(data, 6, length);
	}

	@Override
	public byte[] serialize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected byte[] serialize0(byte[] data) {
		this.length = (short)(data == null ? 0 : data.length);
		var result = new byte[6 + length];
		result[0] = 0x27;
		result[1] = 0x66;
		result[2] = version;
		result[3] = type;
		var tmp = BitUtil.toBytes(length);
		result[4] = tmp[0];
		result[5] = tmp[1];
		System.arraycopy(data, 0, result, 6, length);
		return result;
	}

	public short getPrefix() {
		return prefix;
	}

	public void setPrefix(short prefix) {
		this.prefix = prefix;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
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
	
	/**0X00: NUL*/
	protected int indexOfFirst0(byte[] bytes) {
		if(bytes == null) return -1;
		int length = bytes.length;
		if(length == 0) return -1;
		
		for(int i = 0; i < length; i++) {
			if(bytes[i] == 0X00) return i;
		}
		return -1;
	}

	public byte[] getSn() {
		return sn;
	}

	public void setSn(byte[] sn) {
		this.sn = sn;
	}
}