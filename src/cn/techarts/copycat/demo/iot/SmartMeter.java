package cn.techarts.copycat.demo.iot;

import java.util.Scanner;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.MoteDecoder;
import cn.techarts.copycat.ext.mote.MoteFrame;
import cn.techarts.copycat.ext.mote.Precision;
import cn.techarts.copycat.ext.mote.RegisterFrame;
import cn.techarts.copycat.util.BitHelper;

public class SmartMeter {
	 public static void main(String[] args) throws InterruptedException {
	    	var client = new Visitor<MoteFrame>("localhost", 55530);
			var decoder = new MoteDecoder();
	    	
	    	String sn = null; //Device SN
			try(var scanner = new Scanner(System.in)){
				System.out.print("Device SN: ");
				sn = scanner.nextLine();
			}
			client.with(decoder, MoteFrame.class).with(new MeterHandler()).start();
			client.send(new RegisterFrame(sn, "123456").encode());
	    	
			var degree = 0;
			
	    	while(true) {
	    		var data = BitHelper.toBytes(degree++);
	    		client.send(new DataFrame(sn, data, Precision.NUL).encode());
	    		Thread.sleep(3000);
	    		client.send(new HBFrame(sn).encode());
	    	}       
	    }
}