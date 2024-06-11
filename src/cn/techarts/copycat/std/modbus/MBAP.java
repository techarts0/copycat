package cn.techarts.copycat.std.modbus;

public class MBAP {
	private short tid; 			//Transaction ID
	private short protocol;		//Default is MODBUS
	private short length;		// Remaining Length
	private byte identifier;	//Server Identifier
	
	public MBAP() {
		
	}
	
	public short getTid() {
		return tid;
	}
	public MBAP setTid(short tid) {
		this.tid = tid;
		return this;
	}
	public short getProtocol() {
		return protocol;
	}
	public MBAP setProtocol(short protocol) {
		this.protocol = protocol;
		return this;
	}
	public short getLength() {
		return length;
	}
	public MBAP setLength(short length) {
		this.length = length;
		return this;
	}
	public byte getIdentifier() {
		return identifier;
	}
	public MBAP setIdentifier(byte identifier) {
		this.identifier = identifier;
		return this;
	}
}
