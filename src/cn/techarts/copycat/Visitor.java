package cn.techarts.copycat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;

import cn.techarts.copycat.core.ByteBuf;
import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;
import cn.techarts.copycat.util.Utility;

import java.nio.channels.CompletionHandler;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * A helper simplifies to build a client application.
 * */
public class Visitor<T extends Frame> {
	private Handler handler = null;
	private Decoder<T> decoder = null;
	private boolean directBuffer = false;
	private int sockRecvBufferSize = 1024;
	private InetSocketAddress serverAddr = null;
	private AsynchronousSocketChannel socketChannel;
    
	public Visitor(String ip, int port) {
        serverAddr = new InetSocketAddress(ip, port);
    }
	
	public Visitor<T> with(boolean directBuffer, int SOCK_RECV_BUF){
		this.directBuffer = directBuffer;
		this.sockRecvBufferSize = SOCK_RECV_BUF;
		return this;
	}
	
	public Visitor<T> with(Decoder<T> decoder, Class<T> frameClass){
		if(decoder == null) {
			throw new Panic("The decoder is required.");
		}
		this.decoder = decoder;
		this.decoder.setFrameClass(frameClass);
		return this;
	}
	
	public Visitor<T> with(Handler handler){
		if(decoder == null) {
			throw new Panic("The handler is required.");
		}
		this.handler = handler;
		return this;
	}
	
    public Visitor<T> start() {
    	if(decoder == null || handler == null) {
    		throw new Panic("The decoder and handler are required.");
    	}
    	if(decoder.getFrameClass() == null) {
    		throw new Panic("The frame class in decoder is required.");
    	}
    	
        try {
           socketChannel = AsynchronousSocketChannel.open();
           this.setSocketReceiveBufferSize();
           socketChannel.connect(this.serverAddr);
           this.prepare2ReceiveDataFromServerAsync();
           return this;
        } catch (IOException e) {
            throw new Panic(e, "Failed to connect server.");
        }
    }
    
    private void prepare2ReceiveDataFromServerAsync() {
		var buffer = Utility.allocate(directBuffer, sockRecvBufferSize << 1);
		socketChannel.read(buffer, null, new CompletionHandler<Integer, Void>() {
            @Override
            public void completed(Integer length, Void v) {
            	if(length == -1) {
            		handler.onClose(socketChannel);
            	}else {
            		var decoderCache = new ByteBuf(buffer);
            		var frames = decoder.decode(decoderCache);
                	if(frames != null && frames.length > 0) {
                		for(var f : frames) {
                			handler.onMessage(f, socketChannel);
                		}
                	}
                	socketChannel.read(decoderCache.setup(), null, this);
            	}
            } 
            @Override
            public void failed(Throwable e, Void v) {
            	handler.onClose(socketChannel);
            	handler.onError(e, socketChannel);
            }
        });
    }
    
    private void setSocketReceiveBufferSize() throws IOException{
    	if(this.sockRecvBufferSize > 0) {
    		socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, sockRecvBufferSize);
    	}else {
    		this.sockRecvBufferSize = socketChannel.getOption(StandardSocketOptions.SO_RCVBUF);
    	}
    }
    
    /**
     * Send the frame to server synchronized
     */
    public int send(T data) {
    	return handler.send(data, socketChannel);
    }
    
    /**SYNC*/
    public int send(ByteBuffer data) {
    	return handler.send(data, socketChannel);
    }
    
    public void sendAsync(byte[] data) {
    	handler.sendAsync(data, socketChannel);
    }
    
    public void close() {
    	try {
    	if(socketChannel.isOpen()) {
    		socketChannel.close();
    		handler.onClose(socketChannel);
    	}
    	}catch(IOException e) {
    		throw new Panic(e, "Failed to close the connection.");
    	}    	
    }
}