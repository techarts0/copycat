package cn.techarts.copycat.demo.iot;

import java.util.Scanner;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.MoteDecoder;
import cn.techarts.copycat.ext.mote.MoteFrame;
import cn.techarts.copycat.ext.mote.RegisterFrame;

public class SmartMeter {
	 public static void main(String[] args) throws InterruptedException {
	    	var client = new Visitor<MoteFrame>("localhost", 55530);
			var decoder = new MoteDecoder();
	    	
	    	String sn = null; //Device SN
			try(var scanner = new Scanner(System.in)){
				System.out.print("Device SN: ");
				sn = scanner.nextLine();
			}
			boolean dtuts = true;
	    	
			client.with(decoder, MoteFrame.class).with(new MeterHandler()).start();
			client.send(new RegisterFrame(sn, "123456").encode());
	    	
	    	var data = new byte[] {1, 2, 3, 4, 5};
	    	
	    	while(true) {
	    		Thread.sleep(2000);
	    		client.send(new DataFrame(sn, data, dtuts).encode());
	    		Thread.sleep(3000);
	    		client.send(new HBFrame(sn).encode());
	    	}       
	    }
}