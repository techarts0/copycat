package cn.techarts.copycat.core;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.CompletionHandler;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.Monitor;
import cn.techarts.copycat.Utility;

/**
 * A connection wrapper
 */
public class Session<T extends Frame> implements Runnable{
	private Monitor monitor = null;
	private Handler handler = null;
	private Decoder<T> decoder = null;
	private ByteBuf decoderCache = null;
	private boolean directBuffer = false;
	private AsynchronousSocketChannel connection;
	
	public Session(AsynchronousSocketChannel connection, Context<T> context, Monitor monitor) {
		this.monitor = monitor;
		this.connection = connection;
		this.handler = context.getHandler();
		this.decoder = context.getDecoder();
		this.directBuffer = context.isDirectBuffer();
		decoderCache = new ByteBuf(context.getDecoderCacheSize());
		try {
			if(context.isKeepAlive()) {
				connection.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			}
		}catch(IOException e) {
			throw new CopycatException(e, "Failed to set socket keepalive.");
		}
		this.monitor.activeConnection(false);	//Active and in second connections
		this.handler.onConnected(connection); 	//Just calls once during a session
	}
	
	public AsynchronousSocketChannel getConnection() {
		return this.connection;
	}
	
	private void prepare2ReceiveDataFromPeerAsync() {
		var buffer = Utility.allocateMemory(directBuffer);
		connection.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer length, Void v) {
            	if(length == -1) {
            		monitor.activeConnection(true);
            		handler.onDisconnected(connection);
            		Thread.currentThread().interrupt();
            	}else {
            		decoderCache.append(buffer);
            		monitor.readBytesInSecond(length);
            		var frames = decoder.decode(decoderCache);
                	if(frames != null && frames.length > 0) {
                		for(var f : frames) {
                			handler.onFrameReceived(f, connection);
                		}
                	}
                	connection.read(buffer, null, this);
            	}
            }
 
            @Override
            public void failed(Throwable e, Void v) {
            	monitor.activeConnection(true);
                handler.onDisconnected(connection);
            	handler.onExceptionCaught(e, connection);
                Thread.currentThread().interrupt();
            }
        });
    }

	@Override
	public void run() {
		this.prepare2ReceiveDataFromPeerAsync();
	}
}