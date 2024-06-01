package cn.techarts.copycat.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;

public class Client implements Runnable {
 
    private int id;
	private AsynchronousSocketChannel socketChannel;
    
    public Client(String ip, int port, int id) {
        try {
            this.id = id;
        	socketChannel = AsynchronousSocketChannel.open();
            socketChannel.connect(new InetSocketAddress(ip, port));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public void send(String request) {
    	if(request == null) return;
    	this.send(request.getBytes());
    	read();
    }
    
    public void send(byte[] data) {
    	if(socketChannel == null) return;
    	if(data == null || data.length == 0) return;
    	try {
    		var buf = ByteBuffer.wrap(data);
    		socketChannel.write(buf).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 读取数据
     */
    private void read() {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        try {
            //读取数据到缓冲区
        	socketChannel.read(buf).get();
            //缓冲区数据复位
            buf.flip();
            //根据缓冲区可用长度初始化byte数据
            byte[] respByte = new byte[buf.remaining()];
            //读取缓冲区数据到respByte
            buf.get(respByte);
            //打印读取到的数据
            System.out.println("读取服务端返回数据:" + new String(respByte, "utf-8").trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    @Override
    public void run(){
    	try {
	    	//this.send(new byte[] {0, 1,2,3,4,0x0a, 0x0d, 7, 8, 9, 0, 0x0a, 0x0d, 11, 12, 13, 14});
	    	this.send(new byte[] {0, 1, 3, 3, 4, 7, 8, 9, 4, 11, 12, 13, 14, 15, 0, 2, 0x0a, 0x0d, 0, 0});
	    	
	    	//Thread.sleep(10000);
	    	//this.send(new byte[] {15, 16, 17, 18,19, 20});
	    	while(true) {}
    	}catch(Exception e) {
    		
    	}
    }
 
    public static void main(String[] args) throws InterruptedException {
 
        Client c1 = new Client("localhost", 10086, 1);
        //Client c2 = new Client("localhost", 10086, 2);
        //Client c3 = new Client("localhost", 10086, 3);
       
        new Thread(c1, "c1").start();
        //new Thread(c2, "c2").start();
        //new Thread(c3, "c3").start();
    }
}