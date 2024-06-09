package cn.techarts.copycat.demo.chat;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ServerChatHandler implements Handler {

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		System.out.println("An incoming from " + socket.hashCode());
	}

	@Override
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		var t = (ChatFrame)frame;
		System.out.println(t.getMessage());
		this.send(new ChatFrame(0, 0, "I got your message."), socket);
	}

	@Override
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisconnected(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFrameSentSuccessfully(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}
}