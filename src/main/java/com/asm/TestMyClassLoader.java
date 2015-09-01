package com.asm;

import java.io.InputStream;
import java.lang.reflect.Method;

public class TestMyClassLoader {
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		ClassLoader sysClassLoader = TestMyClassLoader.class.getClassLoader();
		InputStream in = sysClassLoader.getResourceAsStream("com/asm/Before_Demo1.class");
		try {
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
		
			Class clazz = new MyClassLoader().myDefineClass(bytes,"com.asm.Before_Demo1");
			 Method method = clazz.getMethod("sayHello");
			 method.invoke(clazz.newInstance());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
