package cn.techarts.copycat.decoder.http;

import cn.techarts.copycat.core.ByteBuf;

public class HttpHelper {
	public static int nextHeader(ByteBuf data, int from) {
		for(int i = from; i < data.length(); i++) {
			if(data.borrow(i) == 0X0D && 
			   data.borrow(i + 1) == 0X0A) return i + 1;
		}
		return -1; //Moved to the end of HTTP head fields
	}
}
