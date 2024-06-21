package cn.techarts.copycat.demo.iot;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.MoteDecoder;
import cn.techarts.copycat.ext.mote.MoteFrame;
import cn.techarts.copycat.ext.mote.RegisterFrame;

//TODO
public class SmartMeter {
	 public static void main(String[] args) throws InterruptedException {
	    	var client = new Visitor<MoteFrame>("localhost", 55530);
			var decoder = new MoteDecoder();
	    	var sn = "Meter-001";
			client.with(decoder, MoteFrame.class).with(new MeterHandler()).start();
	    	
	    	client.send(new RegisterFrame(sn, 123456).serialize());
	    	
	    	while(true) {
	    		Thread.sleep(1000);
	    		client.send(new DataFrame(sn, new byte[] {1, 2, 3, 4, 5, 6}).serialize());
	    		Thread.sleep(2000);
	    		client.send(new HBFrame(sn).serialize());
	    	}       
	    }
}
