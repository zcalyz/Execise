package com.threadlocal;

import com.entity.User;

public class client {
	public static void main(String[] args) {
		User user1 = new User();
		user1.setName("u1");
		User user2 = new User();
		user2.setName("u2");
	
		MyThread myThread = new MyThread(user1);
		MyThread myThread2 = new MyThread(user2);
	
		new Thread(myThread).start();
		new Thread(myThread2).start();
	}
}
