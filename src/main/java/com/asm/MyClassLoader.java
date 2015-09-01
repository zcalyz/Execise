package com.asm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassLoader extends ClassLoader{
	
	public  Class<?> myDefineClass(byte[] b,String name) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException{
	 Class clazz = defineClass(name,b, 0, b.length);
	
	 return clazz;
//	 Method[] methods = clazz.getMethods();
//	 for(Method m : methods){
//		 System.out.println(m);
//	 }
//	 return null;
	}
}
