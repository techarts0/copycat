/*
 * Copyright (C) 2024 techarts.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.techarts.copycat;

/**
 * @author rocwon@gmail.com
 */
public class Monitor {
	private int period = 5000;
	private int aliveConnections;
	private volatile int incomingInPeriod;
	private volatile int sentBytesInPeriod;
	private volatile int readBytesInPeriod;
		
	private long latestSamplingMillis = System.currentTimeMillis();
	
	/**
	 * Default period is (at least) 5 seconds
	 */
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