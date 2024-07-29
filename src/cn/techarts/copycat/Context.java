package cn.techarts.copycat;

import cn.techarts.copycat.core.Decoder;
import cn.techarts.copycat.core.Frame;
import cn.techarts.copycat.core.Handler;

/**
 * Configurations for global or a session
 */
public class Context<T extends Frame> {
	private int port;
	private int rcvBuffer = 0;
	private int maxThreads = 0;
	private int samplePeriod = 5000;
	
	private Handler handler;
	private Decoder<T> decoder;

	private boolean directBuffer = true;
	private boolean keepAlive = false;
	private boolean reuseAddr = false;
	private boolean tlsEnabled = false;
	private boolean virtualThreadEnabled = false;	
	
	public Context<T> requiredProperties() {
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
	
	public int port() {
		return port;
	}
	public Context<T> port(int port) {
		this.port = port;
		return this;
	}
	public int maxThreads() {
		return maxThreads;
	}
	public Context<T> maxThreads(int maxThreads) {
		if(maxThreads > 0) {
			this.maxThreads = maxThreads;
		}else {
			this.maxThreads = Runtime.getRuntime().availableProcessors();
		}
		return this;
	}
	public Decoder<T> decoder() {
		if(this.decoder.isSingleton()) {
			return this.decoder;
		}else {
			return this.decoder.clone(); //Deeply Clone?
		}
	}
	public<D extends Decoder<T>> Context<T> decoder(D decoder, Class<T> frameClass) {
		this.decoder = decoder;
		this.decoder.setFrameClass(frameClass);
		return this;
	}
	
	public Handler handler() {
		if(this.handler.isSingleton()) return this.handler;
		try {
			return handler.getClass().getConstructor().newInstance();
		}catch(Exception e) {
			throw new Panic(e, "Failed to create a handler.");
		}
	}
	public Context<T> handler(Handler handler) {
		this.handler = handler;
		return this;
	}
	
	public boolean keepAliveEnabled() {
		return keepAlive;
	}

	public Context<T> enableKeepAlive() {
		this.keepAlive = true;
		return this;
	}

	public boolean addrReuseEnabled() {
		return reuseAddr;
	}

	public Context<T> enableAddrReuse() {
		this.reuseAddr = true;
		return this;
	}

	public int rcvBuffer() {
		return rcvBuffer;
	}

	public Context<T> rcvBuffer(int rcvBuffer) {
		this.rcvBuffer = rcvBuffer;
		return this;
	}

	public boolean directBufferEnabled() {
		return directBuffer;
	}

	public Context<T> disableDirectBuffer() {
		this.directBuffer = false;
		return this;
	}
	
	/**
	 * Please ensure the version of JDK you installed is greater than 19(21 and later).
	 */
	public Context<T> enableVirtualThread() {
		this.virtualThreadEnabled = true;
		return this;
	}
	
	public boolean virtualThreadEnabled() {
		return this.virtualThreadEnabled;
	}

	public int samplePeriod() {
		return samplePeriod;
	}
	
	/**Default period (at least) is 5 seconds*/
	public Context<T> samplePeriod(int samplePeriod) {
		this.samplePeriod = samplePeriod;
		if(samplePeriod < 5000) {
			this.samplePeriod = 5000;
		}
		return this;
	}

	public boolean tlsEnabled() {
		return tlsEnabled;
	}

	public Context<T> enableTLS() {
		this.tlsEnabled = true;
		return this;
	}
}