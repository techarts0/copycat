package cn.techarts.copycat.demo.chat;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.Utility;

/**
 * |0x4d 0x4d |  sender | receiver | length  | message |
 * | 2 bytes  | 4 bytes |  4 bytes | 2 bytes | N bytes |
 * 
 */
public class ChatFrame extends Frame {
	private int sender;
	private int receiver;
	private String message;
	
	public ChatFrame(byte[] raw) {
		super(raw);
	}
	
	public ChatFrame(int sender, int receiver, String msg) {
		this.sender = sender;
		this.receiver = receiver;
		this.message = msg;
	}

	@Override
	protected void parse() {
		this.sender = Utility.toInt(new byte[] {data[2], data[3], data[4], data[5]});
		this.receiver = Utility.toInt(new byte[] {data[6], data[7], data[8], data[9]});
		byte[] msg = new byte[data.length - 12];
		System.arraycopy(data, 12, msg, 0, msg.length);
		this.message = Utility.toUTF8String(msg);
	}

	@Override
	public byte[] serialize() {
		if(message == null) return null;
		if(message.length() == 0) return null;
		var msg = message.getBytes();
		this.data = new byte[12 + msg.length];
		data[0] = 0x4d;
		data[1] = 0x4d;
		//Ignored the receiver and sender id.
		var lenBytes = Utility.toBytes((short)msg.length);
		data[10] = lenBytes[0];
		data[11] = lenBytes[1];
		System.arraycopy(msg, 0, data, 12, msg.length);
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

}
