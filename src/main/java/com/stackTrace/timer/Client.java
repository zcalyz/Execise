package com.stackTrace.timer;

import java.util.Map;
import java.util.Set;

public class Client {
	public static int count = 0;
	public static void main(String[] args) {
		TimerDemo1.execute();
		
		while(true){
			showAllThreads();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void showAllThreads(){
		Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
		Set<Thread> keySet = allStackTraces.keySet();
		System.out.println("\n All Threads: ");
		for(Thread thread : keySet){
			System.out.println(thread.getName() + " : " + allStackTraces.get(thread).length);
			if(thread.getName().equals("Timer-0")){
				StackTraceElement[] elems = allStackTraces.get(thread);
				for(StackTraceElement elem : elems){
					System.out.println(elem.getClassName() + "." + elem.getMethodName());
				}
			}
		}
		System.out.println("--------------------------");
	}
}
