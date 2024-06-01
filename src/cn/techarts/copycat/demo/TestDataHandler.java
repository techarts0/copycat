package cn.techarts.copycat.demo;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class TestDataHandler implements Handler {

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		System.out.println("An incoming from " + socket.hashCode());
		
	}

	@Override
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		System.out.println(frame.toString());
	}

	@Override
	public void onExceptionCaused(Throwable e, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

}
