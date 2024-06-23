package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.util.Utility;

/**
 * Process the life-cycle of a session in implementations of the interface.<br>
 * The implementation MUST have a default constructor without argument.<br>
 * If the handler works on singleton mode, you MUST guarantee the thread-safety.
 * */
public interface Handler {
	
	public boolean isSingleton();
	
	/**
	 * Fire the event when a new client connection is coming.
	 */
	public void onConnected(AsynchronousSocketChannel socket);
	
	/**
	 * The event will be fired when a connection is closed.
	 */
	public void onDisconnected(AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event if something goes wrong...
	 */
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event when a frame is received.<br> 
	 * Normally, you should handle the business here.
	 */
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event when data sent successfully.
	 * @param length The sent data length in bytes.
	 * @param socket The peer socket object.
	 */
	public void onFrameTransmitted(int length, AsynchronousSocketChannel socket);
	
	/**
	 * SYNC
	 */
	default int send(byte[] data, AsynchronousSocketChannel socket) {
    	try {
    		return Utility.sendData(data, socket);
    	}catch(CopycatException e) {
    		this.onExceptionCaught(e, socket);
    		return -1; //An exception is threw.
    	}
    }
	
	/**
     * SYNC
     */
    default<T extends Frame> int send(T data, AsynchronousSocketChannel socket) {
    	if(data == null) return 0;
    	return send(data.encode(), socket);
    }
	
    /**
     * ASYNC
     */
    default void sendAsync(byte[] data, AsynchronousSocketChannel socket) {
    	if(socket == null) return;
    	if(data == null || data.length == 0) return;
    	try {
    		var buf = ByteBuffer.wrap(data);
    		socket.write(buf, null, new CompletionHandler<Integer, Void>(){
				@Override
				public void completed(Integer result, Void v) {
					onFrameTransmitted(result, socket);
				}
				@Override
				public void failed(Throwable exc, Void v) {
					onExceptionCaught(exc, socket);
				}
    		});
        } catch (Exception e) {
            throw new CopycatException(e, "Failed to write data");
        }
    }
}