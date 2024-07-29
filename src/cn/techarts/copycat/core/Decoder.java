package cn.techarts.copycat.core;

import cn.techarts.copycat.Panic;

/**
 * The decoder runs on SINGLETON mode.
 * Receives the byte stream from network and convert to protocol frame structure.<br>
 * We provide 4 basic protocol frame style(delimiter, fixed-length, length-field and variable-length-field).<br>
 * Mostly, you can use these decoders directly, don't need to implement a decoder.
 */
public abstract class Decoder<T extends Frame> implements Cloneable{
	
	protected Class<T> frameClass = null;
	
	public abstract boolean isSingleton();
	
	public abstract  T[] decode(ByteBuf data);
	
	public Class<T> getFrameClass(){
		return this.frameClass;
	}
	
	public Decoder<T> setFrameClass(Class<T> frameClass) {
		this.frameClass = frameClass;
		return this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Decoder<T> clone(){
		try {
			return (Decoder<T>)super.clone();
		}catch(CloneNotSupportedException e) {
			throw new Panic("Failed to create decoder.");
		}
	}
}