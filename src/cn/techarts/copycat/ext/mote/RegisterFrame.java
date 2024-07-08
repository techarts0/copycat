package cn.techarts.copycat.ext.mote;

import java.nio.charset.StandardCharsets;

import cn.techarts.copycat.util.BitHelper;

/**
 * The layout of data field:<p>
 * |  SN     | Delimiter |  token   | Delimiter  |  TT Protocol |
 * |  any    |  1(NUL)   |   any    |  1(NUL)    |  1 byte      |
 */

public class RegisterFrame extends MoteFrame {
	
	public static final byte TYPE = 0X01;
	
	private byte[] token;	//Device Token
	private byte protocol;	//TT Protocol type
	
	public RegisterFrame(byte[] data, int remaining) {
		super(data, remaining);
	}
	
	/**
	 * Protocol: MODBUS
	 * TS-Type: 1, DTU Time-Stamp
	 */
	public RegisterFrame(String sn, String token) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol((byte)0); //MODBUS
	}	
	
	public RegisterFrame(String sn, String token, byte protocol) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol(protocol);
	}
	
	public RegisterFrame(String sn, byte[] token, byte protocol) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol(protocol);
	}
	
	@Override
	protected void parse() {
		super.parse();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		setSn(BitHelper.slice(payload, 0, idx));
		var idx2 = this.indexOfNul(payload, idx + 1);
		var len = idx2 - idx - 1;
		setToken(BitHelper.slice(payload, idx + 1, len));
		setProtocol(payload[idx + 1]); // 1 byte only
	}
	
	public byte[] encode() {
		var vlen = sn.length + token.length;
		var data = new byte[vlen + 1]; //
		System.arraycopy(sn, 0, data, 0, sn.length);
		System.arraycopy(token, 0, data, sn.length, token.length);
		data[vlen] = protocol;
		return this.serialize0(data, TYPE);
	}
	
	public byte[] getToken() {
		return this.token;
	}
	
	public String getTokenString() {
		if(this.token == null) return null;
		if(this.token.length == 0) return null;
		return new String(token, StandardCharsets.US_ASCII);
	}

	public void setToken(String token) {
		if(token == null || token.isEmpty()) return;
		var tmp = new StringBuilder(token).append(NUL);
		this.token = tmp.toString().getBytes(StandardCharsets.US_ASCII);
	}	
	
	public void setToken(byte[] token) {
		this.token = token;
	}	

	public byte getProtocol() {
		return protocol;
	}

	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

	public void setSn(byte[] sn) {
		this.sn = sn;
	}
}