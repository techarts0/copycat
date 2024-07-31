package cn.techarts.copycat.demo.iot;

import java.util.Scanner;

import cn.techarts.copycat.Booster;
import cn.techarts.copycat.Context;
import cn.techarts.copycat.ext.mote.MoteDecoder;
import cn.techarts.copycat.ext.mote.MoteFrame;

public class SmartMeterServer {
	public static void main(String[] args){
        var config = new Context<MoteFrame>();
        config.port(55530);
        config.maxThreads(0);
        config.enableVirtualThread();
        config.decoder(new MoteDecoder(), MoteFrame.class);
        config.handler(new ServerHandler());
    	var startup = new Booster<>(config);
    	processCommandLineInstruction();
    	startup.releaseResourcesAndCleanup();
	}
	
	public static void processCommandLineInstruction() {
    	try {
			var scanner = new Scanner(System.in);
	        while(true) {
		    	System.out.println("------IoT Server is Running------");
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
