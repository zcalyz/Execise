package com.threadlocal;

import com.entity.User;

public class MyThread implements Runnable{
	public User user ;
	
//	public void setUser(User user){
//		this.user = user;
//	}
	
	public  MyThread(User user) {
		// TODO Auto-generated constructor stub
		this.user = user;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Util.setUser(user);
		System.out.println(Thread.currentThread().getName() + "setName: " + user.getName());
		System.out.println(Thread.currentThread().getName() + "    :   " + Util.getUser().getName());
	}
	
}
