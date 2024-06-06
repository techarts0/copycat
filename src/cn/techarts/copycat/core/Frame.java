package cn.techarts.copycat.core;

import java.util.Arrays;

import cn.techarts.copycat.CopycatException;

/**
 * The protocol frame structure
 */
public abstract class Frame {
	protected byte[] rawdata;
	
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
	 * You MUST implement the method to convert bytes to your protocol frame structure.
	 */
	protected abstract void parse();
	
	public abstract byte[] serialize();
	
	@Override
	public String toString() {
		return Arrays.toString(rawdata);
	}
}