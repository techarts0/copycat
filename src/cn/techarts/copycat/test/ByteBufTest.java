package cn.techarts.copycat.test;

import org.junit.Test;

import cn.techarts.copycat.ext.mote.MoteException;
import cn.techarts.copycat.util.BitHelper;

import org.junit.Assert;

public class ByteBufTest {
	@Test
	public void testCRLF() {
		byte r = '\r', n = '\n';
		Assert.assertEquals(r, 0X0D);
		Assert.assertEquals(n, 0X0A);
	}
	
	@Test
	public void testEncodeRemainingLength() {
		var result = BitHelper.toRemainingBytes(128);
		
		var length = decode(result);
		
		var remaining = BitHelper.toRemainingLength(result);		
		Assert.assertEquals(length, remaining);
		System.out.println(result.length);
	}
	
	
	public void testDecoderPerformance() {
		var start = System.currentTimeMillis();
		var result = BitHelper.toRemainingBytes(65532);
		for(int i = 0; i < 1000000; i++) {
			var length = decode(result);
		}
		System.out.println(System.currentTimeMillis() - start);
		
		start = System.currentTimeMillis();
		
		for(int i = 0; i < 1000000; i++) {
			var length = BitHelper.toRemainingLength(result);
		}
		System.out.println(System.currentTimeMillis() - start);
	}
	
	
	 private static int decode(byte[] encodedBytes) {
	        int remainingLength = 0;
	        int multiplier = 1;

	        for (byte b : encodedBytes) {
	            int digit = b & 0x7F; 

	            remainingLength += digit * multiplier;
	            multiplier *= 128;

	            if ((b & 0x80) == 0) {
	                break; // 最后一个字节
	            }
	        }

	        return remainingLength;
	    }
}
