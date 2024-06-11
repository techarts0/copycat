package cn.techarts.copycat.demo;

import java.util.Scanner;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.Booster;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class Server {
	public static void main(String[] args){
        var config = new Context<EchoFrame>();
        config.setPort(10086);
        config.setMaxThreads(0);
        config.enableVirtualThread();
        config.setDecoder(new LengthFieldFrameDecoder<EchoFrame>(2, 1), EchoFrame.class);
        config.setHandler(new EchoHandler());
    	var startup = new Booster<>(config);
    	processCommandLineInstruction();
    	startup.releaseResourcesAndCleanup();
	}
	
	public static void processCommandLineInstruction() {
    	try {
			var scanner = new Scanner(System.in);
	        while(true) {
		    	System.out.print("copycat>");
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