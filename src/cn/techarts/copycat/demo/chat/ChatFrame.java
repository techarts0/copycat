package cn.techarts.copycat.demo.chat;

import java.nio.ByteBuffer;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.util.BitHelper;
import cn.techarts.copycat.util.StrHelper;

/**
 * |0x4d 0x4d |  sender | receiver |  TYPE  |  length  | message |
 * | 2 bytes  | 4 bytes |  4 bytes | 1 byte |  2 bytes | N bytes |
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
	protected void decode() {
		this.sender = BitHelper.toInt(new byte[] {rawdata[2], rawdata[3], rawdata[4], rawdata[5]});
		this.receiver = BitHelper.toInt(new byte[] {rawdata[6], rawdata[7], rawdata[8], rawdata[9]});
		
		if(receiver == 0 && rawdata.length == 12) {
			this.loginFrame = true;
			return; //Login frame has not message body
		}
		
		byte[] msg = new byte[rawdata.length - 12];
		System.arraycopy(rawdata, 12, msg, 0, msg.length);
		this.message = StrHelper.toGBKString(msg);
	}

	public ByteBuffer encode() {
		var msgLen = message == null ? 0 : message.length();
		var buffer = new ByteBuf(12 + msgLen);
		buffer.append(new byte[] {0x4d, 0x4d});
		buffer.appendInt(sender);
		buffer.appendInt(receiver);
		if(receiver == 0 && msgLen == 0) {
			return buffer.appendShort((short)0).toByteBuffer();
		}
		buffer.appendShort((short)msgLen);
		buffer.appendUTF8(message);
		return buffer.toByteBuffer();
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