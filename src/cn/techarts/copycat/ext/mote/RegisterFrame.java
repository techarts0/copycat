package cn.techarts.copycat.ext.mote;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import cn.techarts.copycat.util.BitHelper;

/**
 * The layout of data field:<p>
 * |  SN     | Delimiter |  token   | Delimiter  |  TT Protocol |    TS     |
 * |  any    |  1(NUL)   |   any    |  1(NUL)    |  1 byte      |  1 byte   |
 */

public class RegisterFrame extends MoteFrame {
	
	public static final byte TYPE = 0X01;
	
	private byte[] token;	//Device Token
	private byte protocol;	//TT Protocol type
	private byte precision; // Time-stamp Precision
	
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
		this.setPrecision((byte)0);
	}	
	
	/**
	 * Protocol: MODBUS
	 * TS-Type: 1, DTU Time-Stamp
	 */
	public RegisterFrame(String sn, String token, Precision precision) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol((byte)0); //MODBUS
		this.setPrecision(precision.toByte());
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
	
	public RegisterFrame(String sn, byte[] token, byte protocol, Precision precision) {
		this.setSn(sn, NUL);
		this.setToken(token);
		this.setProtocol(protocol);
		this.setPrecision(precision.toByte());
	}
	
	@Override
	protected void decode() {
		super.decode();
		var idx = indexOfDelimiter(payload);
		if(idx == -1) {
			throw MoteException.invalidSN();
		}
		setSn(BitHelper.slice(payload, 0, idx));
		var idx2 = this.indexOfNul(payload, idx + 1);
		var len = idx2 - idx - 1;
		setToken(BitHelper.slice(payload, idx + 1, len));
		setProtocol(payload[idx2 + 1]); // 1 byte only
		setPrecision(payload[idx2 + 2]); // 1 byte only
	}
	
	public ByteBuffer encode() {
		var vlen = sn.length + token.length;
		var buffer = serialize0(TYPE, vlen + 2);
		buffer.append(sn).append(token);
		buffer.appendByte(protocol);
		buffer.appendByte(precision);
		return buffer.toByteBuffer();
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

	public byte getPrecision() {
		return precision;
	}

	public void setPrecision(byte precision) {
		this.precision = precision;
	}
}