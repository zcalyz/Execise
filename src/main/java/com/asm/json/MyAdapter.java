package com.asm.json;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
/*
 * http://envy2002.iteye.com/blog/1701568
 * */
public class MyAdapter extends ClassAdapter {

	public MyAdapter(ClassVisitor cv) {
		super(cv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		// TODO Auto-generated method stub
		// 没有对类进行修改
		cv.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		// TODO Auto-generated method stub
		if ("name".equals(name))
			return null;
		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		// TODO Auto-generated method stub
		if ("<init>".equals(name)) { // init为构造方法
			//************************************* 为空表示删除
			return null; 
		}

		// 其他的方法保留
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	//******************************只会执行1次
	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub

		// 会新增加一个方法,Opecodes.ACC_PUBLIC***********************(故要删除原来的方法)
		MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
				null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
				"()V");
	
		// 相当于desc字段********************************Type的使用
		System.out.println(Type.getDescriptor(String.class));
		
		
		// 新增一个字段(Opcodes.ACC_PUBLIC)
		cv.visitField(Opcodes.ACC_PUBLIC, "name", "I", null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.ICONST_2);
		
		String className = Person.class.getName();
		// mv.visitFieldInsn(opcode, owner, name, desc);
		//访问方法或者字段的时,**********************************************类的格式
		mv.visitFieldInsn(Opcodes.PUTFIELD, className.replaceAll("\\.", "/"),
				"name", "I");
		
		//**************************************给字段赋值的方式
		// 给address 赋值
		//**************************************new一个对象并付给引用
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		
		// mv.visitTypeInsn(opcode, type);
		//type和desc格式的区别****************************************************************
		mv.visitTypeInsn(Opcodes.NEW, "java/util/LinkedHashMap");
		//***************************************不能少
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/LinkedHashMap",
				"<init>", "()V");
		mv.visitFieldInsn(Opcodes.PUTFIELD, className.replaceAll("\\.", "/"),
				"address", "Ljava/util/Map;");
		
		
		//************************固定结尾
		mv.visitInsn(Opcodes.RETURN);

		mv.visitMaxs(3, 1);
		mv.visitEnd();
	}

}
