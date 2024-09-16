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

package cn.techarts.copycat.std.modbus;

import java.nio.ByteBuffer;
import cn.techarts.copycat.Panic;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.util.BitHelper;
import cn.techarts.copycat.util.Utility;

/**MODBUS RTU over TCP.<p>
 * With 2 bytes CRC checksum and without MBAP(first 6 bytes).
 * 
 * @author rocwon@gmail.com *  
 */
public class MBRtuFrame extends ModbusFrame {

	private byte slave;			//Slave address
	
	public MBRtuFrame() {}
	
	public MBRtuFrame(byte slave) {
		this.slave = slave;
	}
	
	public MBRtuFrame(byte[] rawdata) {
		super(rawdata);
	}
	
	public MBRtuFrame(byte slave, byte functionCode) {
		this.slave = slave;
		this.funcode = functionCode;
	}
	
	/**
	 * Encode a function code 0x04 MODBUS frame to bytes
	 */
	public ByteBuffer readInputRegister(short address, short quantity) {
		this.funcode = 0x04;
		this.address = address;
		this.quantity = quantity;
		return this.encode();
	}
	
	/**
	 * Encode a function code 0x03 MODBUS frame to bytes
	 */
	public ByteBuffer readHoldingRegister(short address, short quantity) {
		this.funcode = 0x03;
		this.address = address;
		this.quantity = quantity;
		return this.encode();
	}
	
	/**
	 * Encode a function code 0x06 MODBUS frame to bytes
	 */
	public ByteBuffer writeRegister(short address, short value) {
		this.funcode = 0x06;
		this.address = address;
		this.quantity = value;
		return this.encode();
	}
	
	/**
	 * Encode a function code 0x10 MODBUS frame to bytes
	 */
	public ByteBuffer writeRegisters(short address, short quantity, byte[] value) {
		this.funcode = 0x10;
		this.address = address;
		this.quantity = quantity;
		this.payload = value;
		return this.encode();
	}
	
	@Override
	protected void decode() {
		this.slave = this.rawdata[0];
		this.setFuncode(this.rawdata[1]);
		if(isExceptionOccurred(2)) return;
		if(this.funcode <= 0x02) {
			this.decodeFunctionCode1And2();
		}else if(this.funcode <= 0x04){
			this.decodeFunctionCode3And4();
		}else {
			this.decodeFunctionCode56F10();
		}
		if(!validChecksum()) {
			new Panic("The modbus frame is illegal.");
		}
	}
	
	private boolean validChecksum() {
		var len = rawdata.length - 2;
		var crc = Utility.CRC16(BitHelper.slice(rawdata, 0, len));
		if(crc == null || crc.length != 2) return false;
		return (crc[0] == rawdata[len] && crc[1] == rawdata[len + 1]);
	}
	
	//Function Code, Starting Address, Quantity, Data, CRC16
	@Override
	protected int getRequestFrameLength() {
		return (funcode < 0X0F) ? 7 : (8 + payload.length);
	}
	
	/**To construct a ModBus Frame, the following fields are required:
	 * <ul>
	 * <li>function code</li>
	 * <li>address</li>
	 * <li>quantity</li>
	 * <li>data(Payload)</li>
	 * <li>slave address(default is 1)</li>
	 * </ul>
	 * 
	 */
	@Override
	public ByteBuffer encode() {
		int len = getRequestFrameLength();
		var buffer = new ByteBuf(len);
		buffer.appendByte(slave); //index 0
		if(this.funcode <= 6) {
			encodeFunctionCode1To6(buffer);
		}else if(this.funcode == 0X0F) {
			encodeFunctionCode0F(buffer);
		}else if(this.funcode == 0X010) {
			encodeFunctionCode10(buffer);
		}else {
			throw new Panic("Unsupported Function Code");
		}
		var tmp = buffer.lend(0, len);
		buffer.append(Utility.CRC16(tmp));
		return buffer.toByteBuffer();
	}
	
	public byte getFuncode() {
		return funcode;
	}

	public void setFuncode(byte funcode) {
		this.funcode = funcode;
	}

	public byte getError() {
		return funcode <= 0X80 ? 0 : funcode;
	}
	
	public boolean isAnErrorFrame() {
		return this.funcode > 0X80;
	}	

	/**Quantity or value in request*/
	public short getQuantity() {
		return quantity;
	}
	
	public short getValue() {
		return quantity;
	}

	/**Quantity or value in request*/
	public void setQuantity(short numbers) {
		this.quantity = numbers;
	}
	
	public void setValue(short value) {
		this.quantity = value;
	}

	public byte getException() {
		return exception;
	}

	public void setException(byte exception) {
		this.exception = exception;
	}

	public short getAddress() {
		return address;
	}

	public void setAddress(short address) {
		this.address = address;
	}

	public byte getSlave() {
		return slave;
	}

	public void setSlave(byte slave) {
		this.slave = slave;
	}	
}