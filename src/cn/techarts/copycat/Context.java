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
	private boolean directBuffer = false;
	private boolean singletonHandler = false;
	private boolean singletonDecoder = false;
	
	private Handler handler;
	private Decoder<T> decoder;
		
	private int sendBuffer = 0;
	private int recvBuffer = 0;
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
		if(isSingletonDecoder()) {
			return this.decoder;
		}else {
			return this.decoder.clone();
		}
	}
	public<D extends Decoder<T>> void setDecoder(D decoder, Class<T> frameClass) {
		this.decoder = decoder;
		this.singletonDecoder = false;
		this.decoder.setFrameClass(frameClass);
	}
	
	public<D extends Decoder<T>> void setSingletonDecoder(D decoder, Class<T> frameClass) {
		this.decoder = decoder;
		this.singletonDecoder = true;
		this.decoder.setFrameClass(frameClass);
	}
	
	public<D extends Decoder<T>> void setDecoder(D decoder) {
		this.decoder = decoder;
		if(this.decoder.getFrameClass() == null) {
			throw new CopycatException("The frame class is required!");
		}
	}
	
	public Handler getHandler() {
		if(this.isSingletonHanlder()) return this.handler;
		try {
			return handler.getClass().getConstructor().newInstance();
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to create a handler.");
		}
	}
	public void setHandler(Handler handler, boolean singleton) {
		this.handler = handler;
		this.singletonHandler = singleton;
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

	public int getSendBuffer() {
		return sendBuffer;
	}

	public void setSendBuffer(int sendBuffer) {
		this.sendBuffer = sendBuffer;
	}

	public int getRecvBuffer() {
		return recvBuffer;
	}

	public void setRecvBuffer(int recvBuffer) {
		this.recvBuffer = recvBuffer;
	}

	public boolean isDirectBuffer() {
		return directBuffer;
	}

	public void setDirectBuffer(boolean directBuffer) {
		this.directBuffer = directBuffer;
	}

	public boolean isSingletonHanlder() {
		return singletonHandler;
	}

	public void setSingletonHanlder(boolean singletonHanlder) {
		this.singletonHandler = singletonHanlder;
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

	public boolean isSingletonDecoder() {
		return singletonDecoder;
	}

	public void setSingletonDecoder(boolean singletonDecoder) {
		this.singletonDecoder = singletonDecoder;
	}
}