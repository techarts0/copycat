package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.Utility;

/**
 * Process the life-cycle of a session in implementations of the interface.<br>
 * The implementation MUST have a default constructor without argument.<br>
 * If the handler works on singleton mode, you MUST guarantee the thread-safety.
 * */
public interface Handler {
	/**
	 * Just for server side.
	 */
	public void onConnected(AsynchronousSocketChannel socket);
	
	public void onDisconnected(AsynchronousSocketChannel socket);
	
	/**
	 * If something goes wrong...
	 */
	public void onExceptionCaught(Throwable e, AsynchronousSocketChannel socket);
	
	/**
	 * Do your business here
	 */
	public<T extends Frame> void onFrameReceived(T frame, AsynchronousSocketChannel socket);
	
	/**
	 * Do something after writing
	 */
	public void onFrameSentSuccessfully(int length, AsynchronousSocketChannel socket);
	
	/**
	 * SYNC
	 */
	default int send(byte[] data, AsynchronousSocketChannel socket) {
    	return Utility.sendData(data, socket);
    }
	
	/**
     * SYNC
     */
    default<T extends Frame> int send(T data, AsynchronousSocketChannel socket) {
    	if(data == null) return 0;
    	return send(data.serialize(), socket);
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
					onFrameSentSuccessfully(result, socket);
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