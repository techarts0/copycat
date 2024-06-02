package cn.techarts.copycat.core;

import java.nio.channels.AsynchronousSocketChannel;

/**
 * Process your business in implementations of Handler interface.<br>
 * The implementation MUST have a default constructor without argument.<br>
 * If the handler works on singleton mode, you MUST guarantee the thread-safety.
 * */
public interface Handler {
	/**
	 * ^_^ You can cache the connection into {@link cn.techarts.copycat.Clients}
	 */
	public void onConnected(AsynchronousSocketChannel socket);
	
	public void onDisconnected(AsynchronousSocketChannel socket);
	
	/**
	 * Do your business here
	 */
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket);
	
	/**
	 * If something goes wrong...
	 */
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket);
	
}
