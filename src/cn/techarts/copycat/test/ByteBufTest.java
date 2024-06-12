package cn.techarts.copycat.test;

import org.junit.Test;

import cn.techarts.copycat.util.Utility;

import java.nio.ByteBuffer;
import java.util.Arrays;

import org.junit.Assert;

public class ByteBufTest {
	@Test
	public void testCRLF() {
		byte r = '\r', n = '\n';
		Assert.assertEquals(r, 0X0D);
		Assert.assertEquals(n, 0X0A);
	}
	
	@Test
	public void testByteBuffer() {
		var buf = ByteBuffer.allocate(8);
		buf.put(new byte[] {0, 1, 2, 3, 4, 5});
		buf.flip();
		
		System.out.println("Pos: " + buf.position());
		System.out.println("Lmt: " + buf.limit());
		System.out.println("Rmn: " + buf.remaining());
		System.out.println(Arrays.toString(buf.array()));
		
		buf = resize(buf);
		
		System.out.println("Pos: " + buf.position());
		System.out.println("Lmt: " + buf.limit());
		System.out.println("Rmn: " + buf.remaining());
		System.out.println(Arrays.toString(buf.array()));
	}
	
	private ByteBuffer resize(ByteBuffer buffer) {
		var direct = buffer.isDirect();
		var capacity = buffer.capacity();
		var length = capacity << 1; // 2 times
		var newBuffer = Utility.allocate(direct, length);
		return newBuffer.put(buffer);
	}
	
}
