package cn.techarts.copycat.demo.chat;

import java.util.Scanner;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.Booster;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class ChatServer {
	public static void main(String[] args){
        var config = new Context<ChatFrame>();
        config.setPort(9977);
        config.setMaxThreads(0);
        config.setDirectBuffer(true);
        config.enableVirtualThread();
        config.setDecoder(new LengthFieldFrameDecoder<ChatFrame>(10, 2), ChatFrame.class);
        config.setHandler(new ServerChatHandler());
    	var startup = new Booster<>(config);
    	processCommandLineInstruction();
    	startup.releaseResourcesAndCleanup();
	}
	
	public static void processCommandLineInstruction() {
    	try {
			var scanner = new Scanner(System.in);
	        while(true) {
		    	System.out.println("------Chatting Server is Running------");
		    	String command = scanner.nextLine();
		        if("exit".equals(command)) {
		        	System.out.print("Goodbye!");
		        	break;
		        }
	        }
	        scanner.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }

}
