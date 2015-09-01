package com.asm_classwriter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyClassAdapter extends ClassAdapter{

	public MyClassAdapter(ClassVisitor cv) {
		super(cv);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		// TODO Auto-generated method stub
		if("sayHello".equals(name)){
			//可以直接改方法名(sayHello->sayHelloNew),参数等
			return cv.visitMethod(access, name+"New", desc, signature, exceptions);
		}
		return cv.visitMethod(access, name, desc, signature, exceptions);
	}
	
	

}
