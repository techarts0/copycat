package cn.techarts.copycat.demo;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class EchoHandler implements Handler {

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		System.out.println("An incoming from " + socket.hashCode());
	}

	@Override
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		System.out.println(frame.toString());
		//Echo
		this.send(frame.getData(), socket);
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

	@Override
	public boolean isSingleton() {
		return false;
	}
}