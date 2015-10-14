package com.stackTrace;


public class Client {
	private static final int THREADS_COUNT = 2;
	
	public static void main(String[] args) {
		
		StackTraceUtil.executeTimerTask();
		
		Thread[] threads = new Thread[THREADS_COUNT];
		
		for(int i = 0; i < THREADS_COUNT; i++){
			threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Demo1 demo1 = new Demo1();
					demo1.callMethod();
				}
			});
			
			threads[i].start();
		}
		
		pp();
	}
	
	public static void pp(){
		
		for(long i = 0; i < 1000000000 ; i++){
			long k = 10;
		}	
	}
}
