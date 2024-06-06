package cn.techarts.copycat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * It' a useful helper for some special scenarios. For example, 
 * If you want to send instructions to a client actively in an IoT application.
 * So, you should hold these connections in a global cache. Normally, the client IP address
 * is the KEY and the socket connection is the VALUE.
 */
public class Registry {
	private static Map<String, AsynchronousSocketChannel> clients;
	
	public static void init(int capacity) {
		if(clients != null) return;
		clients = new ConcurrentHashMap<>(capacity <= 0 ? 128 : capacity);
	}
	
	private static Map<String, AsynchronousSocketChannel> getClients() {
		init(0);
		return clients;
	}
	
	public static void put(AsynchronousSocketChannel client) {
		try {
			var address = (InetSocketAddress)client.getRemoteAddress();
			getClients().put(address.getAddress().getHostAddress(), client);
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to get the client IP.");
		}		
	}
	
	public static AsynchronousSocketChannel get(String ip) {
		if(ip == null) return null;
		var client = getClients().get(ip);
		if(client == null) return null;
		if(client.isOpen()) return client;
		clients.remove(ip);
		return null;
	}
	
	public static void remove(String ip) {
		if(ip == null) return;
		getClients().remove(ip);
	}
	
	public static void clear() {
		getClients().clear();
	}
	
	public static int send(byte[] data, String ip) {
		return Utility.sendData(data, get(ip));
	}
	
	public static void close(String ip) {
		var client = get(ip);
		try {
			if(client != null) client.close();
		}catch(IOException e) {
			throw new CopycatException(e, "Failed to close the client.");
		}
	}
}