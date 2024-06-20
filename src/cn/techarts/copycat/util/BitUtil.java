package cn.techarts.copycat.util;

public class BitUtil {
	public static int toInt(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 4) return 0;
		return  (bytes[3] & 0xFF) |
	            (bytes[2] & 0xFF) << 8 |
	            (bytes[1] & 0xFF) << 16 |
	            (bytes[0] & 0xFF) << 24;
	}
	
	public static int toIntLE(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 4) return 0;
		return  (bytes[0] & 0xFF)       |
	            (bytes[1] & 0xFF) << 8  |
	            (bytes[2] & 0xFF) << 16 |
	            (bytes[3] & 0xFF) << 24;
	}
	
	public static long toLong(byte[] bytes) {
		var result = 0L;
		if(bytes == null) return result;
		if(bytes.length != 8) return result;
		for(int i = 0; i < 8; i++) {
			result <<= 8; 
			result |= (bytes[i] & 0xff);
		}
		return result;
	}
	
	public static long toLongLE(byte[] bytes) {
		if(bytes == null) return 0L;
		if(bytes.length != 8) return 0L;
		return 	(bytes[0] & 0xFF)       |
		        (bytes[1] & 0xFF) << 8  |
		        (bytes[2] & 0xFF) << 16 |
		        (bytes[3] & 0xFF) << 24 |
		        (bytes[4] & 0xFF) << 32 |
	            (bytes[5] & 0xFF) << 40 |
	            (bytes[6] & 0xFF) << 48 |
	            (bytes[7] & 0xFF) << 56;
	}
	
	public static short toShort(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 2) return 0;
		var result = (bytes[1] & 0xFF) |
		         (bytes[0] & 0xFF) << 8;		        
		return (short)result;
	}
	
	public static short toShortLE(byte[] bytes) {
		if(bytes == null) return 0;
		if(bytes.length != 2) return 0;
		var result = (bytes[0] & 0xFF) |
		         (bytes[1] & 0xFF) << 8;		        
		return (short)result;
	}
	
	/**
	 * From right to left. For example:<p>
	 * 0x4b -> 01001011 ->{true, true, false, true, false, false, true, false} 
	 */
	public static boolean[] toBooleans(byte arg) {
		var result = new boolean[8];
		for(int i = 0; i < 8; i++) {
			var b = (arg >> i) & 0x01;
			result[i] = b == 1;
		}
		return result;	
	}
	
	public static byte[] toBytes(long val) {
		byte[] result = new byte[8];      
		for (int i = 0; i < 8; i++) {             
			int offset = 64 - (i + 1) * 8;
			result[i] = (byte)((val >> offset) & 0xff);
		}
		return result;
	}
	
	public static byte[] toBytesLE(long val) {
		var result = new byte[8];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		result[2] = (byte)(val >> 16);
		result[3] = (byte)(val >> 24);
		result[4] = (byte)(val >> 32);
		result[5] = (byte)(val >> 40);
		result[6] = (byte)(val >> 48);
		result[7] = (byte)(val >> 56);
		return result;
	}
	
	public static byte[] toBytes(int val) {
		var result = new byte[4];
		result[0] = (byte)(val >> 24);
		result[1] = (byte)(val >> 16);
		result[2] = (byte)(val >> 8);
		result[3] = (byte)val;
		return result;
	}
	
	public static byte[] toBytesLE(int val) {
		var result = new byte[4];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		result[2] = (byte)(val >> 16);
		result[3] = (byte)(val >> 24);
		return result;
	}
	
	public static byte[] toBytes(short val) {
		var result = new byte[2];
		result[0] = (byte)(val >> 8);
		result[1] = (byte)val;
		return result;
	}
	
	public static byte[] toBytesLE(short val) {
		var result = new byte[4];
		result[0] = (byte)val;
		result[1] = (byte)(val >> 8);
		return result;
	}
	
	public static byte[] slice(byte[] arg, int start, int length) {
		if(arg == null || length <= 0) return null;
		int len = length, endIndex = start + len - 1;
		if(endIndex >= arg.length) {
			endIndex = arg.length - 1;
			len = endIndex - start + 1;
		}
		var result = new byte[len];
		System.arraycopy(arg, start, result, 0, len);
		return result;
	}
}