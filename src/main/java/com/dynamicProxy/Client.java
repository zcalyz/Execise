package com.dynamicProxy;

public class Client {
	public static void main(String[] args) {
		UserDao userDao = new UserDaoImpl();
		LogHandler handler = new LogHandler();
		UserDao userDaoProxy = (UserDao)handler.newProxyInstance(userDao);
		userDaoProxy.save();
	}
}
