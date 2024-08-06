package cn.techarts.copycat.std.modbus;

import java.util.concurrent.atomic.AtomicInteger;

import cn.techarts.copycat.Panic;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.util.BitHelper;

public class MBAP {
	private short tid; 			//Transaction ID
	private short protocol;		//Default is MODBUS(0)
	private short length;		//Remaining Length
	private byte address;		//Slave(Device) Address
	
	private static AtomicInteger generator;
	
	/**Construct a blank MBAP*/
	public MBAP(byte slave) {
		this.address = slave;
	}
	
	/**
	 * Decode the received bytes to a MBAP object.
	 */
	public MBAP(byte[] data) {
		var tmp = new byte[] {data[2], data[3]};
		if(BitHelper.toShort(tmp) != 0) {
			throw new Panic("Unsupported protocol.");
		}
		tmp = new byte[] {data[0], data[1]};
		this.tid = BitHelper.toShort(tmp);
		this.setAddress(data[6]);
	}
	
	public void encode(ByteBuf buffer) {
		buffer.appendShort(setTid());
		buffer.appendShort(protocol);
		length = (short)(buffer.capacity() - 6);
		buffer.appendShort(length);
		buffer.appendByte(address);
	}
	
	public short getTid() {
		return tid;
	}
	public short setTid() {
		if(generator == null) {
			generator = new AtomicInteger(0);
		}
		var result = generator.getAndIncrement();
		if(result > Short.MAX_VALUE) {
			generator = new AtomicInteger(0);
			result = generator.getAndIncrement();
		}
		this.tid = (short)result;
		return this.tid;
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
	public byte getAddress() {
		return address;
	}
	public MBAP setAddress(byte identifier) {
		this.address = identifier;
		return this;
	}
}
