package cn.techarts.copycat.demo;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ClientHandler implements Handler {

	@Override
	public void onOpen(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClose(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable e, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends Frame> void onMessage(T frame, AsynchronousSocketChannel socket) {
		System.out.println(frame.toString());

	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}