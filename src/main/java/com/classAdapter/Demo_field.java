package com.classAdapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.asm.Before_Demo1;
import com.asm.MyClassLoader;
import com.asm_classwriter.MyClassAdapter;

public class Demo_field {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, NoSuchFieldException {
		try {
			ClassReader cr = new ClassReader(Demo.class.getName());
			ClassWriter cw = new ClassWriter(1);
			cr.accept(cw, 0);
			
			FieldVisitor field = cw.visitField(Opcodes.ACC_PUBLIC, "a", Type.getDescriptor(int.class), null, null);
			
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "t", "()V", null, null);
		    mv.visitCode();
			

			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitInsn(Opcodes.ICONST_2);
			mv.visitFieldInsn(Opcodes.PUTFIELD, Demo.class.getName().replaceAll("\\.", "/"), "a", Type.getDescriptor(int.class));
			//将字符串压入栈中
//			mv.visitLdcInsn("Before execute");
			//调用PrintStream类的实例方法println(String) ,改方法的返回值为void(即V)

			
//			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//			 mv.visitVarInsn(Opcodes.ALOAD, 0); 
//			 mv.visitInsn(Opcodes.ICONST_2); 
//			 
//			mv.visitFieldInsn(Opcodes.PUTFIELD, Demo.class.getName(), "a", "I");
		
			mv.visitInsn(Opcodes.RETURN);	
			mv.visitMaxs(2, 1);
			mv.visitEnd();
			
			byte[] byteArray = cw.toByteArray();
			//通过自定义classLoader来定义新的类
			Class<?> clazz = new MyClassLoader().myDefineClass(byteArray, "com.classAdapter.Demo");
			
			Object newInstance = clazz.newInstance();
			Method method = clazz.getMethod("t", null);
			method.invoke(newInstance, null);
			
			//通过反射执行该类
		    Field f = clazz.getField("a");
			System.out.println(f.get(newInstance));		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
