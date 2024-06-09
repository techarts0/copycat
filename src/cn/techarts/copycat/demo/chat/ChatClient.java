package cn.techarts.copycat.demo.chat;

import java.util.Scanner;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class ChatClient {
	
    public static void main(String[] args) throws InterruptedException {
    	var client = new Visitor<ChatFrame>("localhost", 9977);
		var decoder = new LengthFieldFrameDecoder<ChatFrame>(10, 2);
    	client.with(decoder, ChatFrame.class).with(new ClientChatHandler()).start();
    	processCommandLineInstruction(client);
    }
    
    public static void processCommandLineInstruction(Visitor<ChatFrame> client) {
    	try {
			var scanner = new Scanner(System.in);
	        while(true) {
		    	System.out.print("Chat Client>");
		    	String text = scanner.nextLine();
		        if("exit".equals(text)) {
		        	System.out.print("Goodbye!");
		        	break;
		        }else {
		        	client.send(new ChatFrame(0, 0, text).serialize());
		        }
	        }
	        scanner.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}