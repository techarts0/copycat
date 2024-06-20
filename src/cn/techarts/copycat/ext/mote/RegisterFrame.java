package cn.techarts.copycat.ext.mote;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.util.BitUtil;

public class RegisterFrame extends MoteFrame {
	
	public static final byte TYPE = 0X01;
	
	private int token;		//Device Token
	private byte tst;		//Time-Stamp Type
	private byte protocol;	//TT Protocol type
	
	@Override
	protected void parse() {
		super.parse();
		var idx = super.indexOfFirst0(payload);
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
		return null; //TODO
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