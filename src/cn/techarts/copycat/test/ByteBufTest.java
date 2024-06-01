package cn.techarts.copycat.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ByteBufTest {
	ExecutorService s = Executors.newVirtualThreadPerTaskExecutor();
	ThreadFactory tf = Thread.ofVirtual().factory();
	Thread t = tf.newThread(null);
}
