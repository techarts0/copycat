package cn.techarts.copycat.demo.iot;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;
import cn.techarts.copycat.ext.mote.ControlFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.TimingFrame;

public class MeterHandler implements Handler {
	
	
	@Override
	public boolean isSingleton() {
		return false;
	}

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
		if(frame instanceof HBFrame) {
			System.out.println(">> Received heart-beating from server");
		}else if(frame instanceof TimingFrame) {
			System.out.println("Received UTC time-stamp from server.");
		}else if(frame instanceof ControlFrame) {
			System.out.println("Received an control instruction from server.");
		}else {
			System.out.println("The frame type is unsupported.");
		}
	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

}
