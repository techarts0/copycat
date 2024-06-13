package cn.techarts.copycat.util;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import cn.techarts.copycat.CopycatException;
import cn.techarts.copycat.core.Frame;

public class Utility {
	
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Class<T> type, int length) {
		return (T[])Array.newInstance(type, length);
	}
	
	/**
	 *@param data If it's null, the default constructor will be called. Please ensure
	 *there is a default constructor in the frame class before calling the method. 
	 */
	public static<T> T frame(Class<T> type, byte[] data) {
		try {
			if(data == null) { //Default constructor
				var constructor = type.getDeclaredConstructor();
				return constructor != null ? constructor.newInstance() : null;
			}else { //Specific Constructor
				var constructor = type.getDeclaredConstructor(byte[].class);
				return constructor != null ? constructor.newInstance(data) : null;
			}
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to create data frame object.");
		}
	}
	
	/**
	 * SYNC
	 */
	public static int sendData(byte[] data, AsynchronousSocketChannel socket) {
		if(data == null || data.length == 0) return 0;
		if(socket == null || !socket.isOpen()) return -1;
		try {
    		var buf = ByteBuffer.wrap(data);
        	return socket.write(buf).get();
        } catch (Exception e) {
        	throw new CopycatException(e, "Failed to send data.");
        }
	}
	
	/**
	 * SYNC
	 */
	public static<T extends Frame>  int sendData(T data, AsynchronousSocketChannel socket) {
		if(data == null) return 0;
		return sendData(data.serialize(), socket);
	}
	
	//On JVM heap or physical memory
	public static ByteBuffer allocate(boolean direct, int capacity) {
		if(!direct) {
			return ByteBuffer.allocate(capacity);
		}else {
			return ByteBuffer.allocateDirect(capacity);
		}
	}
}