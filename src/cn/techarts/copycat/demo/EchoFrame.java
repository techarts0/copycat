package cn.techarts.copycat.demo;

import java.nio.ByteBuffer;

import cn.techarts.copycat.core.Frame;

public class EchoFrame extends Frame {

	public EchoFrame(byte[] raw) {
		super(raw);
	}

	@Override
	protected void decode() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ByteBuffer encode() {
		// TODO Auto-generated method stub
		return null;
	}

}
