package cn.techarts.copycat.std.mqtt;

import java.nio.ByteBuffer;

import cn.techarts.copycat.core.Frame;

public class MqttPacket extends Frame {
	private byte flag; //packet flag: 4 bits;
	private byte type; //packet type: 4 bits;
	private int prefix = 0; //Skip fix head and remaining length bytes
	
	public MqttPacket(byte[] raw) {
		super(raw);
		parseFixedHead(); //flag, type
		prefix = prefixLength(); //Content from...
	}

	@Override
	public void decode() {
		
		
	}

	@Override
	public ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private int prefixLength() {
		int prefix = 1;
		while(true) {
			var b = rawdata[prefix++];
			if((b & 128) == 0) break; 
		}
		return prefix;
	}
	
	private void parseFixedHead() {
		this.setFlag((byte)(rawdata[0] & 0X0F));
		this.setType((byte)(rawdata[0] & 0XF0));
	}

	public byte getFlag() {
		return flag;
	}

	public void setFlag(byte flag) {
		this.flag = flag;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

}
