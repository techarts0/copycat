package cn.techarts.copycat.demo.chat;

import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Registry;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ServerChatHandler implements Handler {

	@Override
	public void onConnected(AsynchronousSocketChannel socket) {
		System.out.println("A user goes online.");
	}

	@Override
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket) {
		var t = (ChatFrame)frame;
		if(t.isLoginFrame()) { //Sender-ID: Socket
			Registry.put(t.getSender(), socket);
			var text = t.getSender() + ", you are online.";
			this.send(new ChatFrame(0, t.getReceiver(), text), socket);
		}else {
			var client = Registry.get(t.getReceiver());
			if(client != null) {
				send(t.getRawData(), client);
			}else {
				var text = t.getReceiver() + " is offline.";
				send(new ChatFrame(0, t.getReceiver(), text), socket);
			}
		}
	}

	@Override
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket) {
		Registry.remove(socket);
		
	}

	@Override
	public void onDisconnected(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFrameTransmitted(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}