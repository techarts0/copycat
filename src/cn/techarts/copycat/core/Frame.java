package cn.techarts.copycat.core;

import java.util.Arrays;

import cn.techarts.copycat.CopycatException;

/**
 * The protocol frame structure
 */
public abstract class Frame {
	/**The raw byte data*/
	protected byte[] rawdata;
	
	//Default constructor
	public Frame() {}
	
	public Frame(byte[] raw) {
		this.rawdata = raw;
		if(raw == null || raw.length == 0) {
			throw new CopycatException("Failed to contruct a null frame.");
		}
		this.parse();
	}
	
	public int length() {
		if(rawdata == null) return 0;
		return this.rawdata.length;
	}
	
	/**
	 * @return The raw data received from peer.
	 */
	public byte[] getData() {
		return this.rawdata;
	}
	
	/**
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 */
	protected abstract void parse();
	
	public abstract byte[] serialize();
	
	@Override
	public String toString() {
		return Arrays.toString(rawdata);
	}
	
	protected byte[] slice(int pos, int length) {
		if(length > rawdata.length - pos) {
			return null;
		}
		var result = new byte[length];
		System.arraycopy(rawdata, pos, result, 0, length);
		return result;
	}
}