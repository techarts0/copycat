package cn.techarts.copycat.core;

/**
 * The decoder runs on SINGLETON mode.
 * Receive the byte stream from network and convert to protocol frame structure.<br>
 * We provide 3 basic protocol frame style(delimiter, fixed-length and length-field).<br>
 * Mostly, you can use these decoders directly, don't need to implement a decoder.
 */
public abstract class Decoder<T extends Frame> {
	
	protected Class<T> frameClass = null;
	
	public abstract  T[] decode(ByteBuf data);
	
	public Class<T> getFrameClass(){
		return this.frameClass;
	}
	
	public void setFrameClass(Class<T> frameClass) {
		this.frameClass = frameClass;
	}
}
