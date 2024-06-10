package cn.techarts.copycat.demo.chat;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

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
		this.sender = Utility.toInt(new byte[] {data[2], data[3], data[4], data[5]});
		this.receiver = Utility.toInt(new byte[] {data[6], data[7], data[8], data[9]});
		
		if(receiver == 0 && data.length == 12) {
			this.loginFrame = true;
			return; //Login frame has not message body
		}
		
		byte[] msg = new byte[data.length - 12];
		System.arraycopy(data, 12, msg, 0, msg.length);
		this.message = Utility.toUTF8String(msg);
	}

	@Override
	public byte[] serialize() {
		var msgLen = message == null ? 0 : message.length();
		this.data = new byte[12 + msgLen];
		data[0] = 0x4d;
		data[1] = 0x4d;
		var sender = Utility.toBytes(this.sender);
		data[2] = sender[0];
		data[3] = sender[1];
		data[4] = sender[2];
		data[5] = sender[3];
		
		var recver = Utility.toBytes(this.receiver);
		data[6] = recver[0];
		data[7] = recver[1];
		data[8] = recver[2];
		data[9] = recver[3];
				
		if(receiver == 0 && msgLen == 0) return data;
		
		var msg = message.getBytes();
		var lenBytes = Utility.toBytes((short)msgLen);
		data[10] = lenBytes[0];
		data[11] = lenBytes[1];
		System.arraycopy(msg, 0, data, 12, msgLen);
		return this.data;
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
