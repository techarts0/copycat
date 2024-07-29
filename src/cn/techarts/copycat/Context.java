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
	private boolean tlsEnabled = false;
	private boolean virtualThreadEnabled = false;	
	
	public Context<T> checkRequiredProperties() {
		if(handler == null) {
			throw new Panic("The handler is required.");
		}
		if(decoder == null) {
			throw new Panic("The decoder is required!");
		}
		if(port <= 0) {
			throw new Panic("Port number must be great than 0.");
		}
		return this;
	}
	
	public int getPort() {
		return port;
	}
	public Context<T> setPort(int port) {
		this.port = port;
		return this;
	}
	public int getMaxThreads() {
		return maxThreads;
	}
	public Context<T> setMaxThreads(int maxThreads) {
		if(maxThreads > 0) {
			this.maxThreads = maxThreads;
		}else {
			this.maxThreads = Runtime.getRuntime().availableProcessors();
		}
		return this;
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
	
	public Handler getHandler() {
		if(this.handler.isSingleton()) return this.handler;
		try {
			return handler.getClass().getConstructor().newInstance();
		}catch(Exception e) {
			throw new Panic(e, "Failed to create a handler.");
		}
	}
	public Context<T> setHandler(Handler handler) {
		this.handler = handler;
		return this;
	}
	
	public boolean isKeepAlive() {
		return keepAlive;
	}

	public Context<T> setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
		return this;
	}

	public boolean isReuseAddr() {
		return reuseAddr;
	}

	public Context<T> setReuseAddr(boolean reuseAddr) {
		this.reuseAddr = reuseAddr;
		return this;
	}

	public int getRcvBuffer() {
		return rcvBuffer;
	}

	public Context<T> setRcvBuffer(int rcvBuffer) {
		this.rcvBuffer = rcvBuffer;
		return this;
	}

	public boolean isDirectBuffer() {
		return directBuffer;
	}

	public Context<T> setDirectBuffer(boolean directBuffer) {
		this.directBuffer = directBuffer;
		return this;
	}
	
	/**
	 * Please ensure the version of JDK you installed is greater than 19(21 and later).
	 */
	public Context<T> enableVirtualThread() {
		this.virtualThreadEnabled = true;
		return this;
	}
	
	public boolean isVirtualThreadEnabled() {
		return this.virtualThreadEnabled;
	}

	public int getSamplePeriod() {
		return samplePeriod;
	}
	
	/**Default period (at least) is 5 seconds*/
	public Context<T> setSamplePeriod(int samplePeriod) {
		this.samplePeriod = samplePeriod;
		if(samplePeriod < 5000) {
			this.samplePeriod = 5000;
		}
		return this;
	}

	public boolean isTlsEnabled() {
		return tlsEnabled;
	}

	public Context<T> setTlsEnabled(boolean tlsEnabled) {
		this.tlsEnabled = tlsEnabled;
		return this;
	}
}