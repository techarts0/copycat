package cn.techarts.copycat;

import java.io.IOException;
import java.net.InetSocketAddress;
import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;
import cn.techarts.copycat.util.Utility;

import java.nio.channels.CompletionHandler;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * A helper simplifies you to build a client application.
 * */
public class Visitor<T extends Frame> {
	private Handler handler = null;
	private Decoder<T> decoder = null;
	private ByteBuf decoderCache = null;
	private InetSocketAddress serverAddr = null;
	private AsynchronousSocketChannel socketChannel;
    
	public Visitor(String ip, int port) {
		this.decoderCache = new ByteBuf(1024);
        serverAddr = new InetSocketAddress(ip, port);
    }
	
	public Visitor<T> with(Decoder<T> decoder, Class<T> frameClass){
		if(decoder == null) {
			throw new CopycatException("The decoder is required.");
		}
		this.decoder = decoder;
		this.decoder.setFrameClass(frameClass);
		return this;
	}
	
	public Visitor<T> with(Handler handler){
		if(decoder == null) {
			throw new CopycatException("The handler is required.");
		}
		this.handler = handler;
		return this;
	}
	
    public Visitor<T> start() {
    	if(decoder == null || handler == null) {
    		throw new CopycatException("The decoder and handler are required.");
    	}
    	if(decoder.getFrameClass() == null) {
    		throw new CopycatException("The frame class in decoder is required.");
    	}
    	
        try {
           socketChannel = AsynchronousSocketChannel.open();
           socketChannel.connect(this.serverAddr);
           this.prepare2ReceiveDataFromServerAsync();
           return this;
        } catch (IOException e) {
            throw new CopycatException(e, "Failed to connect server.");
        }
    }
    
    private void prepare2ReceiveDataFromServerAsync() {
		var buffer = Utility.allocateMemory(false);
		socketChannel.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer length, Void v) {
            	if(length == -1) {
            		handler.onDisconnected(socketChannel);
            	}else {
            		decoderCache.append(buffer);
            		var frames = decoder.decode(decoderCache);
                	if(frames != null && frames.length > 0) {
                		for(var f : frames) {
                			handler.onFrameReceived(f, socketChannel);
                		}
                	}
                	socketChannel.read(buffer, null, this);
            	}
            } 
            @Override
            public void failed(Throwable e, Void v) {
            	handler.onDisconnected(socketChannel);
            	handler.onExceptionCaught(e, socketChannel);
            }
        });
    }
    
    /**
     * Send the frame to server synchronized
     */
    public int send(T data) {
    	return handler.send(data, socketChannel);
    }
    
    /**SYNC*/
    public int send(byte[] data) {
    	return handler.send(data, socketChannel);
    }
    
    public void sendAsync(byte[] data) {
    	handler.sendAsync(data, socketChannel);
    }
    
    public void close() {
    	try {
    	if(socketChannel.isOpen()) {
    		socketChannel.close();
    		handler.onDisconnected(socketChannel);
    	}
    	}catch(IOException e) {
    		throw new CopycatException(e, "Failed to close the connection.");
    	}    	
    }
}