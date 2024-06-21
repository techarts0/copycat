package cn.techarts.copycat.demo.iot;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Registry;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.RegisterFrame;
import cn.techarts.copycat.ext.mote.ResponseFrame;

public class ServerHandler implements Handler {
	
	
	@Override
	public boolean isSingleton() {
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
		if(frame instanceof RegisterFrame) {
			var f = (RegisterFrame)frame;
			Registry.put(f.getDsn(), socket);
			System.out.println("Meter " + f.getDsn() + " registered sucessfully.");
			this.send(new ResponseFrame(f.getDsn(), (byte)0).serialize(), socket);
		}else if(frame instanceof HBFrame) {
			var f = (HBFrame)frame;
			System.out.println("Received heart-beating from meter" + f.getDsn());
			this.send(new HBFrame(f.getDsn()).serialize(), socket);
		}else if(frame instanceof DataFrame) {
			var f = (DataFrame)frame;
			System.out.println(">> Received data from meter " + f.getDsn());
			this.send(new ResponseFrame(f.getDsn(), (byte)0).serialize(), socket);
		}else if(frame instanceof ResponseFrame) {
			var f = (ResponseFrame)frame;
			System.out.println("Received response meter " + f.getDsn());
		}
	}

	@Override
	public void onFrameSentSuccessfully(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

}
