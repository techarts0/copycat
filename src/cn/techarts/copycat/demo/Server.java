package cn.techarts.copycat.demo;

import java.util.Scanner;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.Startup;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;

public class Server {
	public static void main(String[] args){
        var config = new Context<NumberFrame>();
        config.setPort(10086);
        config.setMaxThreads(0);
        config.enableVirtualThread();
        config.setDecoder(new LengthFieldFrameDecoder<NumberFrame>(2, 1), NumberFrame.class);
        config.setHandler(new TestDataHandler(), true);
    	var startup = new Startup<>(config);
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
