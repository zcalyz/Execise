package com.bytecode;

import com.entity.User;

public class Demo1 {
	public int a;
	
//	public static void main(String[] args) {
//		new Demo1().t();
//	}
	
	
	public void add(Demo1 d){
		synchronized (d) {
			int a = 1;
		}
	}
	
	
	public void t(){
		int x;
		x = 1;
		System.out.println(x-2);
//		User user = new User();
//		user.setAge(1);
//		
//		int x,y,z;
//		x = -1;
//		z = 2;
//		y = 20;
////		System.err.println(x);
//		z = x + 1;
//		
////		long l;
//		char c;
//		l = 12;
//		x = (int)l;
//		c = '1';
//		x = c;
//		l = 1;
//		l++;
//		String a;
//		a="123";
//		x = 5;
//		y=20;
//		y++;
//		y = y+10;
//		z = x + y;
//		
//		z= x * 2;
		
	
	}
}
	