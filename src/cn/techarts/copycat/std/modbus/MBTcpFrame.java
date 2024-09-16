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

/**
 * | Transaction | Protocol | Length | Slave Address | PDU |
 * |     2       |    2     |   2    |       1       |  n  |
 * 
 * @author rocwon@gmail.com
 */
public class MBTcpFrame extends ModbusFrame {
	private MBAP mbap;			//The TCP fixed header
	
	public MBTcpFrame() {}
	
	public MBTcpFrame(byte slave) {
		this.mbap = new MBAP(slave);
	}
	
	public MBTcpFrame(byte[] rawdata) {
		super(rawdata);
	}
	
	/**
	 * Encode a function code 0x04 MODBUS frame to bytes
	 */
	public ByteBuffer readInputRegisters(short address, short quantity) {
		this.funcode = 0x04;
		this.address = address;
		this.quantity = quantity;
		return this.encode();
	}
	
	/**
	 * Encode a function code 0x03 MODBUS frame to bytes
	 */
	public ByteBuffer readHoldingRegisters(short address, short quantity) {
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
		this.mbap = new MBAP(rawdata);
		this.setFuncode(this.rawdata[7]);
		if(isExceptionOccurred(8)) return;
		if(this.funcode <= 0x02) {
			this.decodeFunctionCode1And2();
		}else if(this.funcode <= 0x04){
			this.decodeFunctionCode3And4();
		}else {
			this.decodeFunctionCode56F10();
		}
	}
	
	protected int getRequestFrameLength() {
		return (funcode < 0X0F) ? 12 : (13 + payload.length);
	}
	
	/**To construct a ModBus Frame, the following fields are required:
	 * <ul>
	 * <li>function code</li>
	 * <li>address</li>
	 * <li>quantity</li>
	 * <li>data(Payload)</li>
	 * <li>slave address(default is 1)</li>
	 * </ul>
	 */
	@Override
	public ByteBuffer encode() {
		int len = getRequestFrameLength();
		var buffer = new ByteBuf(len);
		this.mbap.encode(buffer);
		if(this.funcode <= 6) {
			encodeFunctionCode1To6(buffer);
		}else if(this.funcode == 0X0F) {
			encodeFunctionCode0F(buffer);
		}else if(this.funcode == 0X010) {
			encodeFunctionCode10(buffer);
		}else {
			throw new Panic("Unsupported Function Code");
		}
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
}