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
	private static Map<Object, AsynchronousSocketChannel> clients;
	
	public static void init(int capacity) {
		if(clients != null) return;
		clients = new ConcurrentHashMap<>(capacity <= 0 ? 128 : capacity);
	}
	
	private static Map<Object, AsynchronousSocketChannel> getClients() {
		init(0);
		return clients;
	}
	
	/**
	 * The remote IP address(String) is the default key.
	 */
	public static void put(AsynchronousSocketChannel client) {
		try {
			var address = (InetSocketAddress)client.getRemoteAddress();
			getClients().put(address.getAddress().getHostAddress(), client);
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to get the client IP.");
		}		
	}
	
	/**
	 * The remote IP address(String) is the default key.
	 */
	public static void put(Object key, AsynchronousSocketChannel client) {
		try {
			if(key == null) return;
			getClients().put(key, client);
		}catch(Exception e) {
			throw new CopycatException(e, "Failed to get the client IP.");
		}		
	}
	
	public static AsynchronousSocketChannel get(Object key) {
		if(key == null) return null;
		var client = getClients().get(key);
		if(client == null) return null;
		if(client.isOpen()) return client;
		clients.remove(key);
		return null;
	}
	
	public static void remove(Object key) {
		if(key == null) return;
		getClients().remove(key);
	}
	
	public static void clear() {
		getClients().clear();
	}
	
	public static void close(Object key) {
		var client = get(key);
		try {
			if(client != null) client.close();
		}catch(IOException e) {
			throw new CopycatException(e, "Failed to close the client.");
		}
	}
}