package cn.techarts.copycat.demo.iot;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Registry;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.RegisterFrame;
import cn.techarts.copycat.ext.mote.StatusFrame;

public class ServerHandler implements Handler {
	
	
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
		if(frame instanceof RegisterFrame) {
			var f = (RegisterFrame)frame;
			Registry.put(f.getSn(), socket);
			System.out.println(f.getSn() + " registered sucessfully.");
			this.send(new StatusFrame(f.getSn(), (byte)0).encode(), socket);
		}else if(frame instanceof HBFrame) {
			var f = (HBFrame)frame;
			System.out.println("Received heart-beating from " + f.getSn());
			this.send(new HBFrame(f.getSn()).encode(), socket);
		}else if(frame instanceof DataFrame) {
			var f = (DataFrame)frame;
			System.out.println(">> Received data from " + f.getSn());
			this.send(new StatusFrame(f.getSn(), (byte)0).encode(), socket);
		}else if(frame instanceof StatusFrame) {
			var f = (StatusFrame)frame;
			System.out.println("Received response " + f.getSn());
		}else {
			System.out.println("The frame type is unsupported.");
		}
	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub

	}

}
