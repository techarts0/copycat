package cn.techarts.copycat.demo;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ServerDataHandler implements Handler {

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		System.out.println("An incoming from " + socket.hashCode());
	}

	@Override
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		System.out.println(frame.toString());
		send(new byte[] {0, 1, 3, 3, 4, 7, 8, 9, 4, 11, 12, 13, 14, 15, 0, 2, 0x0a, 0x0d, 0, 0}, socket);
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