package cn.techarts.copycat;

public class Monitor {
	private int incomingInSecond;
	private int aliveConnections;
	private int sentBytesInSecond;
	private int readBytesInSecond;
		
	private long latestSamplingMillis = System.currentTimeMillis();
	
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
	
	public int sentBytesInSecond() {
		return sentBytesInSecond;
	}
	
	public void sentBytesInSecond(int sentBytes) {
		if(expired()) {
			this.sentBytesInSecond = 0;
		}
		this.sentBytesInSecond += sentBytes;
	}
	
	public int readBytesInSecond() {
		return readBytesInSecond;
	}
	
	public void readBytesInSecond(int readBytes) {
		if(expired()) {
			this.readBytesInSecond = 0;
		}
		this.readBytesInSecond += readBytes;
	}
	
	public int incomingInSecond() {
		return incomingInSecond;
	}
	
	private void addNewIncoming() {
		if(expired()){
			incomingInSecond = 0;
		}
		this.incomingInSecond += 1;
	}
	
	private boolean expired() { //1S
		var time = System.currentTimeMillis();
		var result = time - latestSamplingMillis > 1000;
		if(result) {
			this.latestSamplingMillis = time;
		}
		return result;
	}
}