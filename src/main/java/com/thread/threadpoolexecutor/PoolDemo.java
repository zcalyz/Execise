package com.thread.threadpoolexecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PoolDemo {
	
	public static void main(String[] args) {
		final boolean wantExceptionOnReject = false;
		
		ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(100, true);
		ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 30, 1, TimeUnit.MINUTES, queue);
		if(!wantExceptionOnReject){
			executor.setRejectedExecutionHandler(new RejectHandler());
		}
		
		for(long i = 0; ;++i){
			Task t = new Task(String.valueOf(i));
			System.out.println(Thread.currentThread().getName() + " submitted " + t + ", queue size = "  + executor.getQueue().size());
			
			executor.execute(t);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private static class RejectHandler implements RejectedExecutionHandler {

		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			// TODO Auto-generated method stub
			System.err.println(Thread.currentThread().getName()
					+ " execution rejected: " + r);
		}

	}
	private static class Task implements Runnable{
		private static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		
		private String name;
		
		private Date created;
		
		public Task(String name) {
			// TODO Auto-generated constructor stub
			this.name = name;
			created = new Date();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			final boolean wantOverflow = true;
			System.out.println(Thread.currentThread().getName() + " executing " + this);
			
			try {
				Thread.sleep(wantOverflow? 50: 10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName() + " executed " + this);
		}
		@Override
		public String toString(){
			return name + ",created " + fmt.format(created);				
		}
	}
}
