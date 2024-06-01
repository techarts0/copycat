package cn.techarts.copycat.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.net.StandardSocketOptions;
import java.nio.channels.CompletionHandler;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Context;
import cn.techarts.copycat.CopycatException;

/**
 * A connection wrapper
 */
public class Session<T extends Frame> implements Runnable{
	private Handler handler = null;
	private Decoder<T> decoder = null;
	private ByteBuf decoderCache = null;
	private boolean directBuffer = false;
	private AsynchronousSocketChannel connection;
	
	public Session(AsynchronousSocketChannel connection, Context<T> context) {
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
		this.handler.onConnected(connection); //Just calls once during a session
	}
	
	public AsynchronousSocketChannel getConnection() {
		return this.connection;
	}
	
	private ByteBuffer allocateMemoryOnHeapOrPhysical() {
		if(directBuffer) {
			return ByteBuffer.allocate(1024);
		}else {
			return ByteBuffer.allocateDirect(1024);
		}
	}
	
	private void onDataArriving() {
		var buffer = allocateMemoryOnHeapOrPhysical();
		connection.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer length, Void v) {
            	decoderCache.append(buffer);
            	var frames = decoder.decode(decoderCache);
            	if(frames != null && frames.length > 0) {
            		for(var f : frames) {
            			handler.onFrameReceived(f, connection);
            		}
            	}
            	connection.read(buffer, null, this);
            }
 
            @Override
            public void failed(Throwable e, Void v) {
                handler.onExceptionCaused(e, connection);
            }
        });
    }

	@Override
	public void run() {
		this.onDataArriving();
	}
}