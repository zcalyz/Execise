package com.threadlocal;

public class Test {
	
	ThreadLocal<Long> longLocal = new ThreadLocal<Long>();
	
	ThreadLocal<String> stringLocal = new ThreadLocal<String>();
	
	public void set(){
		longLocal.set(Thread.currentThread().getId());
		stringLocal.set(Thread.currentThread().getName());
	}
	
	public long getLong(){
		return longLocal.get();
	}
	
	public String getString(){
		return stringLocal.get();
	}
	
	public static void main(String[] args) {
		final Test test = new Test();
		
		test.set();
		System.out.println(test.getLong());
		System.out.println(test.getString());
		
		Thread thread1 = new Thread(){
			public void run(){
				test.set();
				System.out.println(test.getLong());
				System.out.println(test.getString());
			}
		};
		
		thread1.start();
	}
}
