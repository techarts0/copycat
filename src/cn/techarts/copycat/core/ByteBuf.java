package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
import java.nio.InvalidMarkException;

/**
 * It's a wrapper of JDK ByteBuffer but easier to use.<br>
 */
public final class ByteBuf {
	
	private ByteBuffer buffer = null;
	
	public ByteBuf(ByteBuffer buffer) {
		var pos = buffer.position();
		try {
			buffer.reset();
		}catch(InvalidMarkException e){
			buffer.position(0);//First time
		}
		buffer.limit(pos);
		this.buffer = buffer;
	}
	
	/**
	 * {@link ByteBuffer.remaining} <br>
	 * The remaining valid data length excluding consumed bytes.
	 */
	public int length() {
		return buffer.remaining();
	}
	
	/**
	 * {@link ByteBuffer.position}<br>
	 * The start index number of the remaining valid bytes.
	 */
	public int current() {
		return buffer.position();
	}
	
	/**
	 * {@link ByteBuffer.position}<br>
	 * Move the current pointer to the specific position().<br>
	 * IMPORTANT!!! It's very dangers to handle this pointer manually.
	 */
	public boolean current(int pos) {
		if(pos < 0) return false;
		if(pos >= buffer.limit()) return false;
		this.buffer.position(pos);
		return true;
	}
	
	/**
	 * {@link ByteBuffer.capacity}<br>
	 * The total length of the bytes array.
	 */
	public int capacity() {
		return this.buffer.capacity();
	}
	
	/**
	 * The remaining valid bytes length is great than your expectation at least.
	 */
	public boolean test(int expectation) {
		return buffer.remaining() >= expectation;
	}
	
	/**
	 * {@link ByteBuffer.get}<br>
	 * The consumed bytes will be discarded (invalid).
	 */
	public byte[] consume(int length) {
		if(buffer.remaining() < length) {
			return null; //Not enough
		}
		var result = new byte[length];
		this.buffer.get(result);
		compactBufferIfNecessary();
		return result;
	}
	
	/**
	 * {@link ByteBuffer.get}<br>
	 * Consumes the given bytes and moves the current pointer to next {@value skip}
	 */
	public byte[] consume(int length, int skip) {
		var result = consume(length);
		this.skip(skip);
		this.compactBufferIfNecessary();
		return result;
	}
	
	/**
	 * Consumes the given bytes but actually return (length - backspace) bytes.<br>
	 * but the current pointer does not move backwards(same to) {@link consume(length)}
	 */ 
	public byte[] consume2(int length, int backspace) {
		int remaining = length();
		if(remaining < length) return null; //Not enough
		var result = new byte[length - backspace];
		this.buffer.get(result);
		this.skip(backspace);
		this.compactBufferIfNecessary();
		return result;
	}
	
	/**
	 * Read specific length of bytes but don't move the current pointer
	 */
	public byte[] borrow(int pos, int length) {
		if(pos + length > buffer.limit()) return null;
		var result = new byte[length];
		this.buffer.mark();
		this.buffer.position(pos);
		this.buffer.get(result).reset();
		return result;
	}
	
	/**
	 * Read a byte but don't move the current pointer
	 */
	public byte borrow(int pos) {
		this.buffer.mark();
		this.buffer.position(pos);
		var result = this.buffer.get();
		this.buffer.reset();
		return result;
	}
	
	/**
	 * Move the current pointer to {@value next}
	 */
	private void skip(int next) {
		var newPos = buffer.position() + next;
		if(newPos < buffer.limit()) {
			this.buffer.position(newPos);
		}
	}
	
	/**
	 * {@link ByteBuffer.compact}
	 */	
	private void compactBufferIfNecessary() {
		var free = buffer.capacity() - buffer.limit();
		if((free << 1) < buffer.position()) {
			this.buffer.compact();
		}
	}
	
	/**
	 * It's very important!!!
	 */
	public ByteBuffer recovery() {
		this.buffer.mark(); //Start Point
		this.buffer.position(buffer.limit());
		this.buffer.limit(buffer.capacity());
		return this.buffer;
	}
}