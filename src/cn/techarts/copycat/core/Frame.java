package cn.techarts.copycat.core;

import java.nio.ByteBuffer;
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
			throw new CopycatException("Raw data is null.");
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
	public byte[] getRawData() {
		return this.rawdata;
	}
	
	/**
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 */
	protected abstract void parse();
	
	/**
	 * Serialize the properties (as a byte array) to send to peer.
	 */
	public abstract ByteBuffer encode();
	
	@Override
	public String toString() {
		return Arrays.toString(rawdata);
	}
	
	protected byte[] slice(int pos, int length) {
		if(pos < 0 || length <= 0) return null;
		int len = length; 
		int remaining = rawdata.length - pos; 
		if(len > remaining) len = remaining;
		var result = new byte[len];
		System.arraycopy(rawdata, pos, result, 0, len);
		return result;
	}
}