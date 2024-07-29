package cn.techarts.copycat.demo;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.Booster;
import cn.techarts.copycat.decoder.LengthFieldFrameDecoder;
import cn.techarts.copycat.util.cli.Action;
import cn.techarts.copycat.util.cli.CLI;

public class Server {
	public static void main(String[] args){
        var config = new Context<EchoFrame>();
        config.port(10086);
        config.maxThreads(0);
        config.enableVirtualThread();
        config.decoder(new LengthFieldFrameDecoder<>(2, 1), EchoFrame.class);
        config.handler(new EchoHandler());
    	try(var startup = new Booster<>(config)){
    		new CLI(new Command()).start(null, "copycat>");
    	}
 	}
}

class Command{
	@Action(name="exit")
	public void exit(String line) {
		System.out.println("Goodbye!");
		System.exit(0);
	}
}