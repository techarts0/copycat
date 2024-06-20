package cn.techarts.copycat.ext.mars;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class MarsHandler implements Handler {

	@Override
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFrameSentSuccessfully(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

}
