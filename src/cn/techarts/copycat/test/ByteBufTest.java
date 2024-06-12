package cn.techarts.copycat.test;

import org.junit.Test;

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
		var buf = ByteBuffer.allocate(16);
		buf.put(new byte[] {0, 1, 2, 3, 4, 5});
		buf.reset();
		//buf.flip();
		//buf.position(6);
		buf.limit(buf.position());
		System.out.println("Pos: " + buf.position());
		System.out.println("Lmt: " + buf.limit());
		System.out.println("Rmn: " + buf.remaining());
		System.out.println(Arrays.toString(buf.array()));
		buf.put((byte)6);
		
		System.out.println("Pos: " + buf.position());
		System.out.println("Lmt: " + buf.limit());
		System.out.println("Rmn: " + buf.remaining());
		System.out.println(Arrays.toString(buf.array()));
	}
	
	
}
