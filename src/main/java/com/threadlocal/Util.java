package com.threadlocal;

import com.entity.User;

public class Util {
	private static final ThreadLocal tl = new ThreadLocal();
	
	public static User getUser(){
		User user = (User) tl.get();
		
		return user;
	}
	
	public static void setUser(User user){
		tl.set(user);
	}
}
