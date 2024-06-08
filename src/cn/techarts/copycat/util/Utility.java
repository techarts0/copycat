package cn.techarts.copycat.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
	
	//The following are some string Helpers
	
	public static String toAsciiString(byte[] data) {
		if(data == null || data.length == 0) return null;
		return new String(data, StandardCharsets.US_ASCII);
	}
	
	public static String toUTF8String(byte[] data) {
		if(data == null || data.length == 0) return null;
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public static String toLatin1String(byte[] data) {
		if(data == null || data.length == 0) return null;
		return new String(data, StandardCharsets.ISO_8859_1);
	}
	
	public static String toGBKString(byte[] data) {
		if(data == null || data.length == 0) return null;
		try {
			return new String(data, "GBK");
		}catch(UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static List<String> split(String src, char separator){
		var result = new ArrayList<String>(256);
		if(src == null || src.trim().length() == 0) return result; 
		char[] chars = src.toCharArray();
		int length = chars.length, prev = 0;
		for(int i = 0; i < length; i++){
			if( chars[i] != separator)continue;
			result.add(src.substring(prev, i));
			prev = i + 1;
		}
		if( chars[length - 1] != separator){//result.add("");
			result.add(src.substring(prev, src.length()));
		}
		return result;
	}
}