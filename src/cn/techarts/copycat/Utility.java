package cn.techarts.copycat;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import cn.techarts.copycat.core.Frame;

public class Utility {
	
	@SuppressWarnings("unchecked")
	public static <T> T[] array(Class<T> type, int length) {
		return (T[])Array.newInstance(type, length);
	}
	
	public static<T> T frame(Class<T> type, byte[] data) {
		try {
			var constructor = type.getDeclaredConstructor(byte[].class);
			return constructor != null ? constructor.newInstance(data) : null;
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to create data frame object.");
		}
	}
	
	/**
	 * big_endian
	 */
	public static int toInt(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 4) return 0;
		return  (bytes[3] & 0xFF) |
	            (bytes[2] & 0xFF) << 8 |
	            (bytes[1] & 0xFF) << 16 |
	            (bytes[0] & 0xFF) << 24;
	}
	
//	public static int toInt2(byte[] bytes) {
//		var len = bytes.length;
//		if(len == 1) return bytes[0] & (byte)128;
//		var b = (a & (byte)255);
//	}
	
	
	/**
	 * big_endian
	 */
	public static short toShort(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 2) return 0;
		var result = (bytes[1] & 0xFF) |
		         (bytes[0] & 0xFF) << 8;		        
		return (short)result;
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
	public static ByteBuffer allocateMemory(boolean direct) {
		if(direct) {
			return ByteBuffer.allocate(1024);
		}else {
			return ByteBuffer.allocateDirect(1024);
		}
	}
}