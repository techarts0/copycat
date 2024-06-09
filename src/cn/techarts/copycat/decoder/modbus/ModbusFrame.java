package cn.techarts.copycat.decoder.modbus;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

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
		var tmp = new byte[] {data[2], data[3]};
		if(Utility.toShort(tmp) != 0) {
			throw new CopycatException("Unsupported protocol.");
		}
		this.mbap = new MBAP();
		tmp = new byte[] {data[0], data[1]};
		mbap.setTid(Utility.toShort(tmp))
			.setIdentifier(this.data[6]);
		
		this.setFuncode(this.data[7]);
		if(isExceptionOccurred()) return;
		if(this.funcode < 0x05) {
			this.parseFunction1to4();
		}else {
			this.parseFunctionGt4();
		}
	}
	
	protected boolean isExceptionOccurred() {
		if(funcode <= 0X80) return false;
		this.setError(this.data[8]);
		return true; //An exception is occurred
	}
	
	//Function Code(01, 02, 03, 04)
	private void parseFunction1to4() {
		var len = data[8];
		this.payload = new byte[len];
		System.arraycopy(data, 9, payload, 0, len);
	}
			
	//Function Code(05, 06, 15, 16)
	private void parseFunctionGt4() {
		var tmp = new byte[] {data[8], data[9]};
		this.setRegister(Utility.toShort(tmp));
		payload = new byte[] {data[10], data[11]};
	}
	
	
	private int getFrameLength(int len) {
		return (funcode < 0X0F) ? 12 : (13 + len);
	}
	
	
	@Override
	public byte[] serialize() {
		int tmp = this.payload.length;
		int len = getFrameLength(tmp);
		this.data = new byte[len];
		//this.data[0] = 
		//this.data[1] =
		this.data[2] = 0x00;
		this.data[3] = 0x00;
		this.data[4] = 0x00;
		this.data[5] = 0x00;
		//this.data[6] = slave;
		this.data[7] = funcode;
		
		
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