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
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitHelper;

/**
 * An implementation of
 * <a href="https://modbus.org/docs/Modbus_Application_Protocol_V1_1b.pdf">MODBUS</a>
 * <B><I>Transparent Transmission:</I></B> Framework does not care the content bytes.<br>
 * Please translate or set the {@value payload} according to your business.<p>
 * Now it just support function code 1, 2, 3, 4, 5, 0X0F, 0X10.<p>
 *  
 * | Transaction | Protocol | Length | Slave Address | PDU |
 * |     2       |    2     |   2    |       1       |  n  |
 * 
 * @author rocwon@gmail.com
 */
public abstract class ModbusFrame extends Frame {
	protected byte funcode; 		//Function code
	protected byte exception;		//Exception Code
	
	protected byte[] payload;		//The real data
	
	protected short address;		//The first register address for reading or writing
	protected short quantity;		//The register numbers read or write
	
	
	public ModbusFrame() {}
	
	public ModbusFrame(byte[] rawdata) {
		this.rawdata = rawdata;
	}
		
	/**
	 * Encode a request with function code 0x04 MODBUS frame to bytes
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
	
	
	protected boolean isExceptionOccurred(int index) {
		if(funcode <= 0X80) return false;
		this.exception = this.rawdata[index];
		return true; //Error has only 2 bytes
	}
	
	//Function Code 1, 2
	//*N = Quantity of Outputs / 8, if the remainder is different of 0 ⇒ N = N+1
	protected void decodeFunctionCode1And2() {
		this.payload = new byte[rawdata.length - 9];
		System.arraycopy(rawdata, 9, payload, 0, payload.length);
	}
	
	//Function Code 3, 4
	//N* x 2 Bytes (*N = Quantity of Registers)
	protected void decodeFunctionCode3And4() {
		this.payload = new byte[this.rawdata[8]];
		System.arraycopy(rawdata, 9, payload, 0, payload.length);
	}
			
	//Function Code(05, 06, 15, 16)
	protected void decodeFunctionCode56F10() {
		var tmp = new byte[] {rawdata[8], rawdata[9]};
		this.setAddress(BitHelper.toShort(tmp));
		payload = new byte[] {rawdata[10], rawdata[11]};
	}
	
	
	protected abstract int getRequestFrameLength();
		
	protected void encodeFunctionCode1To6(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
	}
	
	protected void encodeFunctionCode0F(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
		var len = this.payload.length;
		buffer.appendByte((byte)len);
		buffer.append(this.payload);
	}
	
	protected void encodeFunctionCode10(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
		buffer.appendByte((byte)(quantity << 1));
		buffer.append(this.payload);
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