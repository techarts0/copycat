package cn.techarts.copycat.std.modbus;

import java.nio.ByteBuffer;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitHelper;

/**
 * An implementation of
 * <a href="https://modbus.org/docs/Modbus_Application_Protocol_V1_1b.pdf">MODBUS</a>
 * <B><I>Transparent Transmission:</I></B> Framework does not care the content bytes.<br>
 * Please translate or set the {@value payload} according to your business.
 */
public class ModbusFrame extends Frame {
	private MBAP mbap;			//The TCP fixed header
	private byte funcode; 		//Function code;
	private byte error;			//Error Code
	private byte[] payload;		//The real data
	private short register;		//The register(or first register) address
	private short numbers;		//The register numbers read or write
	
	@Override
	protected void parse() {
		var tmp = new byte[] {rawdata[2], rawdata[3]};
		if(BitHelper.toShort(tmp) != 0) {
			throw new CopycatException("Unsupported protocol.");
		}
		this.mbap = new MBAP();
		tmp = new byte[] {rawdata[0], rawdata[1]};
		mbap.setTid(BitHelper.toShort(tmp))
			.setIdentifier(this.rawdata[6]);
		
		this.setFuncode(this.rawdata[7]);
		if(isExceptionOccurred()) return;
		if(this.funcode < 0x05) {
			this.parseFunction1to4();
		}else {
			this.parseFunctionGt4();
		}
	}
	
	protected boolean isExceptionOccurred() {
		if(funcode <= 0X80) return false;
		this.setError(this.rawdata[8]);
		return true; //An exception is occurred
	}
	
	//Function Code(01, 02, 03, 04)
	private void parseFunction1to4() {
		var len = rawdata[8];
		this.payload = new byte[len];
		System.arraycopy(rawdata, 9, payload, 0, len);
	}
			
	//Function Code(05, 06, 15, 16)
	private void parseFunctionGt4() {
		var tmp = new byte[] {rawdata[8], rawdata[9]};
		this.setRegister(BitHelper.toShort(tmp));
		payload = new byte[] {rawdata[10], rawdata[11]};
	}
	
	
	private int getFrameLength(int len) {
		return (funcode < 0X0F) ? 12 : (13 + len);
	}
	
	
	@Override
	public ByteBuffer encode() {
		int tmp = this.payload.length;
		int len = getFrameLength(tmp);
		this.rawdata = new byte[len];
		//this.data[0] = 
		//this.data[1] =
		this.rawdata[2] = 0x00;
		this.rawdata[3] = 0x00;
		this.rawdata[4] = 0x00;
		this.rawdata[5] = 0x00;
		//this.data[6] = slave;
		this.rawdata[7] = funcode;
		
		
		if(funcode < 0X0F) {
			
		}else { //15, 16
			
		}
		return null;
	}
	
	public byte getFuncode() {
		return funcode;
	}

	public void setFuncode(byte funcode) {
		this.funcode = funcode;
	}

	public byte getError() {
		return error;
	}
	
	public boolean isAnErrorFrame() {
		return error > 0;
	}

	public void setError(byte error) {
		this.error = error;
	}

	public short getRegister() {
		return register;
	}

	public void setRegister(short register) {
		this.register = register;
	}

	public short getNumbers() {
		return numbers;
	}

	public void setNumbers(short numbers) {
		this.numbers = numbers;
	}
}