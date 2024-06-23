package cn.techarts.copycat.demo.chat;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitUtil;
import cn.techarts.copycat.util.StrUtil;

/**
 * |0x4d 0x4d |  sender | receiver | length  | message |
 * | 2 bytes  | 4 bytes |  4 bytes | 2 bytes | N bytes |
 * 
 * If receiver is 0 and remaining length equals 0, it's a login frame.
 * 
 */
public class ChatFrame extends Frame {
	private int sender;
	private int receiver;
	private String message;
	private boolean loginFrame;
	
	public ChatFrame(byte[] raw) {
		super(raw);
	}
	
	public ChatFrame(int sender, int receiver, String msg) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = msg;
		if(receiver == 0 && message == null) {
			this.loginFrame = true;
		}
	}

	@Override
	protected void parse() {
		this.sender = BitUtil.toInt(new byte[] {rawdata[2], rawdata[3], rawdata[4], rawdata[5]});
		this.receiver = BitUtil.toInt(new byte[] {rawdata[6], rawdata[7], rawdata[8], rawdata[9]});
		
		if(receiver == 0 && rawdata.length == 12) {
			this.loginFrame = true;
			return; //Login frame has not message body
		}
		
		byte[] msg = new byte[rawdata.length - 12];
		System.arraycopy(rawdata, 12, msg, 0, msg.length);
		this.message = StrUtil.toGBKString(msg);
	}

	@Override
	public byte[] encode() {
		var msgLen = message == null ? 0 : message.length();
		this.rawdata = new byte[12 + msgLen];
		rawdata[0] = 0x4d;
		rawdata[1] = 0x4d;
		var sender = BitUtil.toBytes(this.sender);
		rawdata[2] = sender[0];
		rawdata[3] = sender[1];
		rawdata[4] = sender[2];
		rawdata[5] = sender[3];
		
		var recver = BitUtil.toBytes(this.receiver);
		rawdata[6] = recver[0];
		rawdata[7] = recver[1];
		rawdata[8] = recver[2];
		rawdata[9] = recver[3];
				
		if(receiver == 0 && msgLen == 0) return rawdata;
		
		var msg = message.getBytes();
		var lenBytes = BitUtil.toBytes((short)msgLen);
		rawdata[10] = lenBytes[0];
		rawdata[11] = lenBytes[1];
		System.arraycopy(msg, 0, rawdata, 12, msgLen);
		return this.rawdata;
	}

	public int getSender() {
		return sender;
	}

	public void setSender(int sender) {
		this.sender = sender;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isLoginFrame() {
		return loginFrame;
	}

	public void setLoginFrame(boolean loginFrame) {
		this.loginFrame = loginFrame;
	}

}
