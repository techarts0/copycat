package cn.techarts.copycat.core;

import java.nio.ByteBuffer;

/**
 * It's a wrapper of JDK ByteBuffer but easier to use.
 */
public final class ByteBuf {
	private byte[] data = new byte[1024];
	private int position = 0, current = 0;
	
	public ByteBuf(int capacity) {
		if(capacity > 0) {
			data = new byte[capacity];
		}
	}
	
	public ByteBuf(ByteBuffer bs) {
		this.append(bs);
	}
	
	public ByteBuf(byte[] bs) {
		this.append(bs);
	}
	
	/**
	 * The remaining valid data length excluding consumed bytes.
	 */
	public int length() {
		return this.position - current;
	}
	
	/**
	 * The start index number of the remaining valid bytes.
	 */
	public int current() {
		return this.current;
	}
	
	/**
	 * The total length of the bytes array.
	 */
	public int capacity() {
		return this.data.length;
	}
	
	/**
	 * The remaining valid bytes length is great than your expectation at least.
	 */
	public boolean test(int expectation) {
		return length() >= expectation;
	}
	
	public void append(ByteBuffer bs) {
		if(bs == null) return;
		if(bs.limit() == 0) return;
		bs.flip();
		var tmp = new byte[bs.limit()];
		bs.get(tmp).clear();
		this.append(tmp);
	}
	
	public void append(byte[] bs) {
		if(bs == null || bs.length == 0) return;
		if(!this.enough(bs.length)) resize();
		System.arraycopy(bs, 0, data, position, bs.length);
		this.position += bs.length; //limit in ByteBuffer
	}
	
	/**
	 * The consumed bytes will be discarded (invalid).
	 */
	public byte[] consume(int length) {
		int remaining = position - current;
		if(remaining < length) return null; //Not enough
		var result = new byte[length];
		System.arraycopy(data, current, result, 0, length);
		this.current += length;
		arrangeByteLayout2ReleaseSpace();
		return result;
	}
	
	/**
	 * Consumes the given bytes and moves the current pointer to next {@value skip}
	 */
	public byte[] consume(int length, int skip) {
		var result = consume(length);
		this.skip(skip);
		return result;
	}
	
	/**
	 * Read specific length of bytes but don't move the current pointer
	 */
	public byte[] borrow(int pos, int length) {
		if(pos + length > position) return null;
		var result = new byte[length];
		System.arraycopy(data, pos, result, 0, length);
		return result;
	}
	
	/**
	 * Read a byte but don't move the current pointer
	 */
	public byte borrow(int pos) {
		return pos > position ? 0 : data[pos];
	}
	
	/**
	 * Move the current pointer to {@value next}
	 */
	public void skip(int next) {
		this.current += next;
	}
	
	private void arrangeByteLayout2ReleaseSpace() {
		var free = data.length - position;
		if((free << 1) < this.current) {
			int len = position - current;
			System.arraycopy(data, current, data, 0, len);
			this.current = 0;
			this.position = len;
		}
	}
	
	private boolean enough(int len) {
		return data.length - position > len;
	}
	
	//Extends the array capacity
	private void resize() {
		var swap = new byte[data.length << 1];
		System.arraycopy(data, 0, swap, 0, data.length);
		this.data = swap;
	}
}