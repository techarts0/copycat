package cn.techarts.copycat.std.http;

import cn.techarts.copycat.core.ByteBuf;

public class HttpHelper {
	public static int nextHeader(ByteBuf data, int from) {
		for(int i = from; i < data.remaining(); i++) {
			if(data.lend(i) == 0X0D && 
			   data.lend(i + 1) == 0X0A) return i + 1;
		}
		return -1; //Moved to the end of HTTP head fields
	}
}
