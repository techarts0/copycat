package cn.techarts.copycat.demo.chat;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Registry;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

public class ServerChatHandler implements Handler {

	@Override
	public void onOpen(AsynchronousSocketChannel socket) {
		System.out.println("A user goes online.");
	}

	@Override
	public<T extends Frame> void onMessage(T frame, AsynchronousSocketChannel socket) {
		var t = (ChatFrame)frame;
		if(t.isLoginFrame()) { //Sender-ID: Socket
			Registry.put(t.getSender(), socket);
			var text = t.getSender() + ", you are online.";
			this.send(new ChatFrame(0, t.getReceiver(), text), socket);
		}else {
			var client = Registry.get(t.getReceiver());
			if(client != null) {
				send(ByteBuffer.wrap(t.getRawData()), client);
			}else {
				var text = t.getReceiver() + " is offline.";
				send(new ChatFrame(0, t.getReceiver(), text), socket);
			}
		}
	}

	@Override
	public void onError(Throwable e, AsynchronousSocketChannel socket) {
		Registry.remove(socket);
		
	}

	@Override
	public void onClose(AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSend(int length, AsynchronousSocketChannel socket) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}