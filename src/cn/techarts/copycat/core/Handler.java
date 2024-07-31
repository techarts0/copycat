package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.techarts.copycat.Panic;
import cn.techarts.copycat.util.Utility;

/**
 * Process the life-cycle of a session in implementations of the interface.<br>
 * The implementation MUST have a default constructor without argument.<br>
 * If the handler works on singleton mode, you MUST guarantee the thread-safety.
 * */
public interface Handler {
	
	/**
	 * To ensure the thread-safety, the handler defaults to non-singleton mode.
	 */
	public default boolean isSingleton() {
		return false;
	}
	
	/**
	 * Fire the event when a new client connection is coming.
	 */
	public void onOpen(AsynchronousSocketChannel socket);
	
	/**
	 * The event will be fired when a connection is closed.
	 */
	public void onClose(AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event if something goes wrong...
	 */
	public void onError(Throwable e, AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event when the message (a frame) is received.<br> 
	 * Normally, you should handle the business here.
	 */
	public<T extends Frame> void onMessage(T frame, AsynchronousSocketChannel socket);
	
	/**
	 * Fire the event when data sent successfully.
	 * @param length The sent data length in bytes.
	 * @param socket The peer socket object.
	 */
	public void onSend(int length, AsynchronousSocketChannel socket);
	
	/**
	 * SYNC
	 */
	default int send(ByteBuffer data, AsynchronousSocketChannel socket) {
    	try {
    		return Utility.sendData(data, socket);
    	}catch(Panic e) {
    		this.onError(e, socket);
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
					onSend(result, socket);
				}
				@Override
				public void failed(Throwable exc, Void v) {
					onError(exc, socket);
				}
    		});
        } catch (Exception e) {
            throw new Panic(e, "Failed to send data.");
        }
    }
}