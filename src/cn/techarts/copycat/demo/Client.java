package cn.techarts.copycat.demo;

import java.nio.ByteBuffer;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class Client {
	
    public static void main(String[] args) throws InterruptedException {
    	var client = new Visitor<EchoFrame>("localhost", 10086);
		var decoder = new LengthFieldFrameDecoder<EchoFrame>(2, 1);
    	client.with(decoder, EchoFrame.class).with(new ClientHandler()).start();
    	var tmp = new byte[] {0, 1, 3, 3, 4, 7, 8, 9, 4, 11, 12, 13, 14, 15, 0, 2, 0x0a, 0x0d, 0, 0};
    	client.send(ByteBuffer.wrap(tmp));
    	while(true) {}       
    }
}