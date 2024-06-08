package cn.techarts.copycat.core;

import java.util.Arrays;

import cn.techarts.copycat.CopycatException;

/**
 * The protocol frame structure
 */
public abstract class Frame {
	/**The raw byte data*/
	protected byte[] data;
	
	//Default constructor
	public Frame() {}
	
	public Frame(byte[] raw) {
		this.data = raw;
		if(raw == null || raw.length == 0) {
			throw new CopycatException("Failed to contruct a null frame.");
		}
		this.parse();
	}
	
	public int length() {
		if(data == null) return 0;
		return this.data.length;
	}
	
	/**
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 */
	protected abstract void parse();
	
	public abstract byte[] serialize();
	
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
	
	protected byte[] slice(int pos, int length) {
		if(length > data.length - pos) {
			return null;
		}
		var result = new byte[length];
		System.arraycopy(data, pos, result, 0, length);
		return result;
	}
}