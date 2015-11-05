package com.stackTrace.timer;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


public class TimerDemo1 {
	
	public static void execute(){
		Timer timer = new Timer();
		timer.schedule(new MyTimeTask(timer), 0, 1000);
	}
	
	private static class MyTimeTask extends TimerTask{
		private static int num = 0;
		
		private static Timer timer;
		
		public MyTimeTask(Timer timer) {
			// TODO Auto-generated constructor stub
			this.timer = timer;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println("TimerThread: " + Thread.currentThread().getName());
			System.out.println(++num);
			if(num == 5){
				System.out.println("timerThread cancel!!!!!!!!!!!!!!!!");
				timer.cancel();
				timer.purge();
			}
			System.out.println(Thread.currentThread().isInterrupted());
		}
		
		
	}
}
