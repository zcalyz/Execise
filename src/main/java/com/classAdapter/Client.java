package com.classAdapter;

import java.io.IOException;
import java.lang.reflect.Field;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.asm.MyClassLoader;

public class Client {
	public static void main(String[] args) throws Exception {
		ClassReader cr = new ClassReader(Demo.class.getName());
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		ClassAdapter ca = new AddTimeAdapter(cw);
		
		cr.accept(ca, ClassReader.SKIP_DEBUG);
		
		
		byte[] bytes = cw.toByteArray();
		MyClassLoader myClassLoader = new MyClassLoader();
		
		Class<?> clazz = myClassLoader.myDefineClass(bytes, Demo.class.getName());
		Field field = clazz.getField("interval");
		System.err.println(field.get(clazz.newInstance()));
	}
}
