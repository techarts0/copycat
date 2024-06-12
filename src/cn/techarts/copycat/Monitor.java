package cn.techarts.copycat;

public class Monitor {
	private int period = 5000;
	private int incomingInPeriod;
	private int aliveConnections;
	private int sentBytesInPeriod;
	private int readBytesInPeriod;
		
	private long latestSamplingMillis = System.currentTimeMillis();
	
	public Monitor(int period) {
		this.period = period;
		if(period <= 0) {
			this.period = 5000;
		}
	}
	
	public int aliveConnections() {
		return aliveConnections;
	}
	
	public void activeConnection(boolean inactive) {
		if(inactive) {
			this.aliveConnections -= 1;
		}else {
			this.addNewIncoming();
			this.aliveConnections += 1;
		}
	}
	
	public int sentBytesInPeirod() {
		return sentBytesInPeriod;
	}
	
	public void sentBytesInPeriod(int sentBytes) {
		if(expired()) {
			this.sentBytesInPeriod = 0;
		}
		this.sentBytesInPeriod += sentBytes;
	}
	
	public int readBytesInPeriod() {
		return readBytesInPeriod;
	}
	
	public void readBytesInPeriod(int readBytes) {
		if(expired()) {
			this.readBytesInPeriod = 0;
		}
		this.readBytesInPeriod += readBytes;
	}
	
	public int incomingInPeriod() {
		return incomingInPeriod;
	}
	
	private void addNewIncoming() {
		if(expired()){
			incomingInPeriod = 0;
		}
		this.incomingInPeriod += 1;
	}
	
	private boolean expired() { //1S
		var time = System.currentTimeMillis();
		var result = time - latestSamplingMillis > period;
		if(result) {
			this.latestSamplingMillis = time;
		}
		return result;
	}
}