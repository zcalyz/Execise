package com.asm_classwriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.asm.Before_Demo1;
import com.asm.MyClassLoader;

public class ClassReaderExample {
	public static void main(String[] args) {
		try {
			//改写了方法sayHello的名字
			ClassWriter cw = new ClassWriter(0);
			ClassReader cr = new ClassReader("com.asm.Before_Demo1");
			cr.accept(new MyClassAdapter(cw), 0);
			//增加execute()方法
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "execute", "()V", null, null);
			//开始增加代码
			mv.visitCode();
			//增加System.out.prinln("Before execute");
			//将类Sytem的静态引用out(类型为PrintStream)类压入栈中
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			//将字符串压入栈中
			mv.visitLdcInsn("Before execute");
			//调用PrintStream类的实例方法println(String) ,改方法的返回值为void(即V)
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
			
			//将局部变量表slot0处的引用压入栈中,即this
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			//调用其实例方法sayHelloNew
			mv.visitLdcInsn("world");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Before_Demo1.class.getName().replaceAll("\\.", "/"), "sayHelloNew", "(Ljava/lang/String;)V");
			
			mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("after execute");
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
			
			mv.visitInsn(Opcodes.RETURN);
			//(最大的栈空间,最大的局部变量表空间)
			mv.visitMaxs(2, 1);
			mv.visitEnd();
			//返回改写类的byte[]
			byte[] byteArray1 = cw.toByteArray();
			//通过自定义classLoader来定义新的类
			Class<?> clazz1 = new MyClassLoader().myDefineClass(byteArray1, Before_Demo1.class.getName());
			
			//通过反射执行该类
			Method method1 = clazz1.getMethod("execute",null);
			method1.invoke(clazz1.newInstance(),null);			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
