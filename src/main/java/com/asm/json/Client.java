package com.asm.json;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.asm.MyClassLoader;

public class Client {
	public static void main(String[] args) throws NoSuchMethodException,
			SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException, NoSuchFieldException {
	
		try {
			ClassReader cr = new ClassReader(Person.class.getName());
			//COMPUTE_MAX会自动计算最大栈和最大局部变量
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			MyAdapter ca = new MyAdapter(cw);
			cr.accept(ca, ClassReader.SKIP_DEBUG);
			
			byte[] byteArray = cw.toByteArray();
			Class<?> clazz = new MyClassLoader().myDefineClass(byteArray,
					Person.class.getName());
			
			Field field = clazz.getField("name");
			Field field2 = clazz.getField("address");
			//输出address的值
			System.out.println(field2.get(clazz.newInstance()));
				
			/* 将类写到文件中
			 * FileOutputStream out = new FileOutputStream("/home/zc/Person.class");
			out.write(byteArray, 0, byteArray.length);
			out.close();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
