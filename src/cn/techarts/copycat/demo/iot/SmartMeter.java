package cn.techarts.copycat.demo.iot;

import java.util.Random;
import java.util.Scanner;

import cn.techarts.copycat.Visitor;
import cn.techarts.copycat.ext.mote.DataFrame;
import cn.techarts.copycat.ext.mote.HBFrame;
import cn.techarts.copycat.ext.mote.MoteDecoder;
import cn.techarts.copycat.ext.mote.MoteFrame;
import cn.techarts.copycat.ext.mote.Precision;
import cn.techarts.copycat.ext.mote.RegisterFrame;
import cn.techarts.copycat.util.BitHelper;

public class SmartMeter implements Runnable{
	private String sn;
	
	public SmartMeter(String sn) {
		this.sn = sn;
	}
	public void run(){
		var client = new Visitor<MoteFrame>("localhost", 55530);
		client.with(new MoteDecoder(), MoteFrame.class);
    	client.with(new MeterHandler()).start();
		client.send(new RegisterFrame(sn, "123456").encode());
		client.send(new HBFrame(sn).encode());
		var degree = 0;
		var random = new Random();
    	while(true) {
    		var data = BitHelper.toBytes(degree++);
    		client.send(new DataFrame(sn, data, Precision.NUL).encode());
    		try {
    			Thread.sleep((random.nextInt(5) + 1) * 1000);
    		}catch(Exception e) {
    			
    		}
    	}
	}

	public static void main(String[] args) {
		System.out.println("Please tell me the number of meters:");
		var scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		scanner.close();
		for(int i = 0; i < n; i++) {
			var sn = "Meter-00" + (i + 1);
			new Thread(new SmartMeter(sn)).start();
		}
	
	}
}