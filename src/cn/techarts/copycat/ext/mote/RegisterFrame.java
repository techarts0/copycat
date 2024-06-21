package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.util.BitUtil;

public class RegisterFrame extends MoteFrame {
	
	public static final byte TYPE = 0X01;
	
	private int token;		//Device Token
	private byte tst;		//Time-Stamp Type
	private byte protocol;	//TT Protocol type
	
	public RegisterFrame(byte[] data) {
		super(data);
	}
	
	/**
	 * Protocol: MODBUS
	 * TS-Type: 1, DTU Time-Stamp
	 */
	public RegisterFrame(String sn, int token) {
		this.setSn(sn);
		this.setTst((byte)1);
		this.setToken(token);
		this.setProtocol((byte)0);
	}	
	
	public RegisterFrame(String sn, int token, byte tst, byte protocol) {
		this.setSn(sn);
		this.setTst(tst);
		this.setToken(token);
		this.setProtocol(protocol);
	}
	
	@Override
	protected void parse() {
		super.parse();
		var idx = indexOfFirst0(payload);
		if(idx == -1) {
			throw new CopycatException("Illegal device SN.");
		}
		setSn(BitUtil.slice(payload, 0, idx));
		var tmp = BitUtil.slice(payload, idx + 1, 4);
		setToken(BitUtil.toInt(tmp));
		setTst(payload[idx + 5]);
		setProtocol(payload[idx + 6]);
	}
	
	public byte[] serialize() {
		var len = sn.length + 6;
		var data = new byte[len];
		System.arraycopy(sn, 0, data, 0, sn.length);
		var tkn = BitUtil.toBytes(token);
		System.arraycopy(tkn, 0, data, sn.length, 4);
		data[len - 2] = tst;
		data[len - 1] = protocol;
		return this.serialize0(data, TYPE);
	}
	
	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public byte getTst() {
		return tst;
	}

	public void setTst(byte tst) {
		this.tst = tst;
	}

	public byte getProtocol() {
		return protocol;
	}

	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

	public byte[] getSn() {
		return sn;
	}

	public void setSn(byte[] sn) {
		this.sn = sn;
	}
}