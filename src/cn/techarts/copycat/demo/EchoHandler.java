package cn.techarts.copycat.demo;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class EchoHandler implements Handler {

	@Override
	public void onOpen(AsynchronousSocketChannel socket) {
		System.out.println("An incoming from " + socket.hashCode());
	}

	@Override
	public<T extends Frame> void onMessage(T frame, AsynchronousSocketChannel socket) {
		System.out.println(frame.toString());
		//Echo
		this.send(ByteBuffer.wrap(frame.getRawData()), socket);
	}

	@Override
	public void onError(Throwable e, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClose(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}