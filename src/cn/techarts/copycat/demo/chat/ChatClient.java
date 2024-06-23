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
    		int myId = 0, peerId = 0;
    		var scanner = new Scanner(System.in);
    		System.out.println("-------Login--------");
    		System.out.println("Your id:");
    		myId = Integer.parseInt(scanner.nextLine());
    		System.out.println("Friend id:");
    		peerId = Integer.parseInt(scanner.nextLine());
    		if(myId <= 0 || peerId <= 0 || myId == peerId) {
    			System.out.print("Illegal user(s) id, bye!");
    		}else {
    			client.send(new ChatFrame(myId, 0, null).encode());
    			while(true) {
    	        	String text = scanner.nextLine();
    		        if("exit".equals(text)) {
    		        	System.out.print("Goodbye!");
    		        	break;
    		        }else {
    		        	client.send(new ChatFrame(myId, peerId, text).encode());
    		        }
    	        }
    		}
	        scanner.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}