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

/**
 * The server bootstrap class
 * 
 */
public class Booster<T extends Frame> {
	private Monitor monitor = null;
	private Context<T> context = null;
	private ExecutorService executorService;
    private ExecutorService workerExecutorService;
    private AsynchronousChannelGroup channelGroup;
    private AsynchronousServerSocketChannel serverSocketChannel;
 
    public Booster(Context<T> context) throws CopycatException{
    	try {
    		this.context = context.checkRequiredProperties();
            this.monitor = new Monitor(context.getSamplePeriod());
            if(context.isVirtualThreadEnabled()) {
            	executorService = Executors.newVirtualThreadPerTaskExecutor();
            	workerExecutorService = Executors.newVirtualThreadPerTaskExecutor();
            }else {
            	executorService = Executors.newCachedThreadPool();
            	workerExecutorService = Executors.newFixedThreadPool(context.getMaxThreads());
            }
            channelGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService, 1);
            serverSocketChannel = AsynchronousServerSocketChannel.open(channelGroup);
            this.setServerSocketOptions();
            serverSocketChannel.bind(new InetSocketAddress(context.getPort()));
            serverSocketChannel.accept(serverSocketChannel, new ConnectionAcceptor());
        } catch (IOException e) {
            throw new CopycatException(e);
        }
    }
    
    class ConnectionAcceptor implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>{
		@Override
		public void completed(AsynchronousSocketChannel client, AsynchronousServerSocketChannel server) {
			var session = new Session<T>(client, context, monitor);
			if(context.isVirtualThreadEnabled()) {
				workerExecutorService.submit(session);
			}else {
				workerExecutorService.execute(session);
			}
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
	    	this.workerExecutorService.shutdown();
    	}catch(Exception e) {
    		throw new CopycatException(e, "Failed to release resource while shutdown.");
    	}
    }
    
    private void setServerSocketOptions() throws IOException {
    	if(context.getRcvBuffer() > 0) {
    		serverSocketChannel.setOption(StandardSocketOptions.SO_RCVBUF, context.getRcvBuffer());
    	}
    	this.context.setRcvBuffer(serverSocketChannel.getOption(StandardSocketOptions.SO_RCVBUF));
    	
    	if(context.isReuseAddr()) { //default: disabled
    		serverSocketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, context.isReuseAddr());
    	}
    	if(context.isKeepAlive()) { //default: disabled
    		serverSocketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, context.isKeepAlive());
    	}
    }
    
    public Monitor getMonitorObject() {
    	return this.monitor;
    }
}