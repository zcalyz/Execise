package com.thread;

public class Client {
	public static void main(String[] args) {
		SubThread subThread = new SubThread();
		subThread.start();
		
		new Thread(new InThread()).start();;
		
	}
}
