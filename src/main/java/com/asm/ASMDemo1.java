package com.asm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ASMDemo1 {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		try {
			ClassReader classReader = new ClassReader("com/asm/Before_Demo1");
		    ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			ClassAdapter ca = new AopClassAdapter(cw);
			
			classReader.accept(ca, ClassReader.SKIP_DEBUG);
			
			byte[] byteArray = cw.toByteArray();
			
			MyClassLoader myClassLoader = new MyClassLoader();
			Class<?> clazz = myClassLoader.myDefineClass(byteArray,"com.asm.Before_Demo1");
			 Method method = clazz.getMethod("sayHello");
			 method.invoke(clazz.newInstance());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
