package cn.techarts.copycat.core;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.CompletionHandler;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.Monitor;
import cn.techarts.copycat.util.Utility;

/**
 * A connection wrapper
 */
public class Session<T extends Frame> implements Runnable{
	private Monitor monitor = null;
	private Handler handler = null;
	private Decoder<T> decoder = null;
	private int recvBufferSize = 1024;
	private boolean directBuffer = false;
	private AsynchronousSocketChannel connection;
	
	public Session(AsynchronousSocketChannel connection, Context<T> context, Monitor monitor) {
		this.monitor = monitor;
		this.connection = connection;
		this.handler = context.getHandler();
		this.decoder = context.getDecoder();
		this.directBuffer = context.isDirectBuffer();
		this.recvBufferSize = context.getRcvBuffer();
		try {
			if(context.isKeepAlive()) {
				connection.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			}
		}catch(IOException e) {
			throw new CopycatException(e, "Failed to set socket keepalive.");
		}
		this.monitor.activeConnection(false);	//Active and in second connections
		this.handler.onOpen(this.connection); 	//Just calls once during a session
	}
	
	/**
	 * Returns the raw socket object.
	 */
	public AsynchronousSocketChannel getConnection() {
		return this.connection;
	}
	
	/**
	 * To ensure having enough capacity, the decode cache is 2 times of SO_RCVBUF as default. 
	 */
	private void initialize() {
		var buffer = Utility.allocate(directBuffer, recvBufferSize << 1);
		connection.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer length, Void v) {
            	if(length == -1) {
            		monitor.activeConnection(true);
            		handler.onClose(connection);
            		Thread.currentThread().interrupt();
            	}else {
            		var cache = new ByteBuf(buffer);
            		monitor.readBytesInPeriod(length);
            		var frames = decoder.decode(cache);
                	if(frames != null && frames.length > 0) {
                		for(var f : frames) {
                			handler.onMessage(f, connection);
                		}
                	}
                	connection.read(cache.setup(), null, this);
            	}
            }
 
            @Override
            public void failed(Throwable e, Void v) {
            	monitor.activeConnection(true);
                handler.onClose(connection);
            	handler.onError(e, connection);
                Thread.currentThread().interrupt();
            }
        });
    }	
	
	@Override
	public void run() {
		this.initialize();
	}
}