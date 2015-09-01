package com.asm;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AopClassAdapter  extends ClassAdapter{

	public AopClassAdapter(ClassVisitor cv) {
	  super(cv);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			          String superName, String[] interfaces){
//		cv.visit(version, access, name, signature, superName, interfaces);
		MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC,"sayHello", "()V", null, null);
		mv.visitCode();
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/asm/AopInterceptor", "before", "()V");
		
		mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn("hello");
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(2, 1);
		mv.visitEnd();
	}


}
