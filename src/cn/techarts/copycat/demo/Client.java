package cn.techarts.copycat.demo;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class Client {
	
    public static void main(String[] args) throws InterruptedException {
    	var client = new Visitor<NumberFrame>("localhost", 10086);
		var decoder = new LengthFieldFrameDecoder<NumberFrame>(2, 1);
    	client.with(decoder, NumberFrame.class).with(new ClientDataHandler()).start();
    	client.send(new byte[] {0, 1, 3, 3, 4, 7, 8, 9, 4, 11, 12, 13, 14, 15, 0, 2, 0x0a, 0x0d, 0, 0});
    	while(true) {}       
    }
}