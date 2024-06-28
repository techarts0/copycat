package cn.techarts.copycat.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StrHelper {
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
	
	public static byte[] toASCII(String data) {
		if(data == null) return null;
		if(data.isEmpty()) return null;
		return data.getBytes(StandardCharsets.US_ASCII);
	}
	
	public static byte[] toUTF8(String data) {
		if(data == null) return null;
		if(data.isEmpty()) return null;
		return data.getBytes(StandardCharsets.UTF_8);
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
