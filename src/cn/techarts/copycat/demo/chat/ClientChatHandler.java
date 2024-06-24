package cn.techarts.copycat.demo.chat;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ClientChatHandler implements Handler {

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
		var f = (ChatFrame)frame;
		System.out.println(">>" +  f.getMessage());
	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
