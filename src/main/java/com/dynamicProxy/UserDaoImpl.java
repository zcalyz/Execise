package com.dynamicProxy;

public class UserDaoImpl implements UserDao{

	@Override
	public void save() {
		// TODO Auto-generated method stub
		System.out.println("save");
	}

	@Override
	public void save(String name) {
		// TODO Auto-generated method stub
		System.out.println("save:"+name);
	}

}
