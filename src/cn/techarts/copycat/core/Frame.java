package cn.techarts.copycat.core;

import java.util.Arrays;

/**
 * The protocol frame structure
 */
public abstract class Frame {
	protected byte[] rawdata;
	
	public Frame(byte[] raw) {
		this.rawdata = raw;
		parse(this.rawdata);
	}
	
	public int length() {
		if(rawdata == null) return 0;
		return this.rawdata.length;
	}
	
	/**
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 */
	public abstract void parse(byte[] raw);
	
	
	@Override
	public String toString() {
		return Arrays.toString(rawdata);
	}
}