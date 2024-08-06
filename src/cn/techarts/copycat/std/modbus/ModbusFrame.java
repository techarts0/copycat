package cn.techarts.copycat.std.modbus;

import java.nio.ByteBuffer;

import cn.techarts.copycat.Panic;
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
 */
public class ModbusFrame extends Frame {
	private MBAP mbap;			//The TCP fixed header
	private byte funcode; 		//Function code
	private byte exception;		//Exception Code
	
	private byte[] payload;		//The real data
	
	private short address;		//The first register address for reading or writing
	private short quantity;		//The register numbers read or write
	
	
	public ModbusFrame() {}
	
	public ModbusFrame(byte slave) {
		this.mbap = new MBAP(slave);
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
		this.mbap = new MBAP(rawdata);
		this.setFuncode(this.rawdata[7]);
		if(isExceptionOccurred()) return;
		if(this.funcode <= 0x02) {
			this.decodeFunctionCode1And2();
		}else if(this.funcode <= 0x04){
			this.decodeFunctionCode3And4();
		}else {
			this.decodeFunctionCode56F10();
		}
	}
	
	protected boolean isExceptionOccurred() {
		if(funcode <= 0X80) return false;
		this.exception = this.rawdata[8];
		return true; //Error has only 2 bytes
	}
	
	//Function Code 1, 2
	//*N = Quantity of Outputs / 8, if the remainder is different of 0 ⇒ N = N+1
	private void decodeFunctionCode1And2() {
		this.payload = new byte[rawdata.length - 9];
		System.arraycopy(rawdata, 9, payload, 0, payload.length);
	}
	
	//Function Code 3, 4
	//N* x 2 Bytes (*N = Quantity of Registers)
	private void decodeFunctionCode3And4() {
		this.payload = new byte[this.rawdata[8]];
		System.arraycopy(rawdata, 9, payload, 0, payload.length);
	}
			
	//Function Code(05, 06, 15, 16)
	private void decodeFunctionCode56F10() {
		var tmp = new byte[] {rawdata[8], rawdata[9]};
		this.setAddress(BitHelper.toShort(tmp));
		payload = new byte[] {rawdata[10], rawdata[11]};
	}
	
	
	private int getRquestFrameLength() {
		return (funcode < 0X0F) ? 12 : (13 + payload.length);
	}
		
	private void encodeFunctionCode1To6(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
	}
	
	private void encodeFunctionCode0F(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
		var len = this.payload.length;
		if(quantity % 8 == 0) len -= 1;
		buffer.appendByte((byte)len);
		buffer.append(this.payload);
	}
	
	private void encodeFunctionCode10(ByteBuf buffer) {
		buffer.appendByte(funcode);
		buffer.appendShort(address);
		buffer.appendShort(quantity);
		buffer.appendByte((byte)(quantity << 1));
		buffer.append(this.payload);
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
		int len = getRquestFrameLength();
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