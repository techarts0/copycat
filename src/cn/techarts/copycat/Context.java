package cn.techarts.copycat;

import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

/**
 * Configurations for global or a session
 */
public class Context<T extends Frame> {
	private int port;
	private int maxThreads = 0;
	private int samplePeriod = 5000;
	private boolean directBuffer = true;
	
	private Handler handler;
	private Decoder<T> decoder;
		
	private int rcvBuffer = 0;
	private boolean keepAlive = false;
	private boolean reuseAddr = false;

	private boolean virtualThreadEnabled = false;	
	
	public Context<T> checkRequiredProperties() {
		if(handler == null) {
			throw new CopycatException("The handler is required.");
		}
		if(decoder == null) {
			throw new CopycatException("The decoder is required!");
		}
		if(port <= 0) {
			throw new CopycatException("Port number must be great than 0.");
		}
		return this;
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public void setMaxThreads(int maxThreads) {
		if(maxThreads > 0) {
			this.maxThreads = maxThreads;
		}else {
			this.maxThreads = Runtime.getRuntime().availableProcessors();
		}
	}
	public Decoder<T> getDecoder() {
		if(this.decoder.isSingleton()) {
			return this.decoder;
		}else {
			return this.decoder.clone(); //Deeply Clone?
		}
	}
	public<D extends Decoder<T>> void setDecoder(D decoder, Class<T> frameClass) {
		this.decoder = decoder;
		this.decoder.setFrameClass(frameClass);
	}
	
	public<D extends Decoder<T>> void setSingletonDecoder(D decoder, Class<T> frameClass) {
		this.decoder = decoder;
		this.decoder.setFrameClass(frameClass);
	}
	
	public<D extends Decoder<T>> void setDecoder(D decoder) {
		this.decoder = decoder;
		if(this.decoder.getFrameClass() == null) {
			throw new CopycatException("The frame class is required!");
		}
	}
	
	public Handler getHandler() {
		if(this.handler.isSingleton()) return this.handler;
		try {
			return handler.getClass().getConstructor().newInstance();
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to create a handler.");
		}
	}
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	
	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isReuseAddr() {
		return reuseAddr;
	}

	public void setReuseAddr(boolean reuseAddr) {
		this.reuseAddr = reuseAddr;
	}

	public int getRcvBuffer() {
		return rcvBuffer;
	}

	public void setRcvBuffer(int rcvBuffer) {
		this.rcvBuffer = rcvBuffer;
	}

	public boolean isDirectBuffer() {
		return directBuffer;
	}

	public void setDirectBuffer(boolean directBuffer) {
		this.directBuffer = directBuffer;
	}
	
	/**
	 * Please ensure the version of JDK you installed is greater than 19(21 and later).
	 */
	public void enableVirtualThread() {
		this.virtualThreadEnabled = true;
	}
	
	public boolean isVirtualThreadEnabled() {
		return this.virtualThreadEnabled;
	}

	public int getSamplePeriod() {
		return samplePeriod;
	}
	
	/**Default period (at least) is 5 seconds*/
	public void setSamplePeriod(int samplePeriod) {
		this.samplePeriod = samplePeriod;
		if(samplePeriod < 5000) {
			this.samplePeriod = 5000;
		}
	}	
}