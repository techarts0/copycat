package cn.techarts.copycat.test;

import org.junit.Test;
import org.junit.Assert;

public class ByteBufTest {
	@Test
	public void testCRLF() {
		byte r = '\r', n = '\n';
		Assert.assertEquals(r, 0X0D);
		Assert.assertEquals(n, 0X0A);
	}
}
