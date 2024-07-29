package cn.techarts.copycat.util;

import java.nio.ByteBuffer;
import java.lang.reflect.Array;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.Panic;
import cn.techarts.copycat.core.Frame;

/**
 * An internal utility class that contains series helper methods.
 */
public class Utility {
	
	/**
	 * @return An array with specific type and length
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Class<T> type, int length) {
		return (T[])Array.newInstance(type, length);
	}
	
	/**
	 * @return An array and length is 0
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Class<T> type) {
		return (T[])Array.newInstance(type, 0);
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
			throw new Panic(e, "Failed to create frame object.");
		}
	}
	
	/**
	 * SYNC
	 */
	public static int sendData(ByteBuffer data, AsynchronousSocketChannel socket) {
		if(socket == null || !socket.isOpen()) return -1;
		if(data == null || data.remaining() == 0) return 0;
		try {
    		return socket.write(data).get(); //Blocking here
        } catch (Exception e) {
        	throw new Panic(e, "Failed to send data.");
        }
	}
	
	/**
	 * SYNC
	 */
	public static<T extends Frame>  int sendData(T data, AsynchronousSocketChannel socket) {
		if(data == null) return 0;
		return sendData(data.encode(), socket);
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