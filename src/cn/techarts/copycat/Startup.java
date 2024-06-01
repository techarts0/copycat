package cn.techarts.copycat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Session;
 
public class Startup<T extends Frame> {
 
    private Context<T> config = null;
	private ExecutorService executorService;
    private ExecutorService workerkExecutorService;
    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverSocketChannel;
 
    public Startup(Context<T> context) throws CopycatException{
    	try {
        	this.config = context.checkRequiredProperties();
            executorService = Executors.newCachedThreadPool();
            workerkExecutorService = Executors.newFixedThreadPool(context.getMaxThreads());
            channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            this.setServerSocketOptions();
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            serverSocketChannel.bind(new InetSocketAddress(context.getPort()));
            serverSocketChannel.accept(serverSocketChannel, new ConnectionAcceptor());
        } catch (IOException e) {
            throw new CopycatException(e);
        }
    }
    
    class ConnectionAcceptor implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>{
		@Override
		public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
			var session = new Session<T>(client, config);
			workerkExecutorService.execute(session);
			server.accept(serverSocketChannel, this);
		}

		@Override
		public void failed(Throwable e, AsynchronousServerSocketChannel server) {
			throw new CopycatException(e, "The server failed to accept connection.");
		}
    }
    
    public void releaseResourcesAndCleanup() throws CopycatException {
    	try {
	    	if(channelGroup == null) return;
	    	if(channelGroup.isShutdown()) return;
	    	this.channelGroup.shutdown();
	    	this.executorService.shutdown();
	    	this.workerkExecutorService.shutdown();
    	}catch(Exception e) {
    		throw new CopycatException(e, "Failed to release resource while shutdown.");
    	}
    }
    
    private void setServerSocketOptions() throws IOException {
    	if(config.getSendBuffer() > 0) { //default: 16384(512K) bytes
    		serverSocketChannel.setOption(StandardSocketOptions.SO_SNDBUF, config.getSendBuffer());
    	}
    	if(config.getRedvBuffer() > 0) { //default: 1048576(1M) bytes
    		serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, config.getRedvBuffer());
    	}
    	if(config.isReuseAddr()) { //default: disabled
    		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, config.isReuseAddr());
    	}
    	if(config.isKeepAlive()) { //default: disabled
    		serverSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, config.isKeepAlive());
    	}
    }
}