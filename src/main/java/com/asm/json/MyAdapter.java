package com.asm.json;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


public class MyAdapter extends ClassAdapter {

	public MyAdapter(ClassVisitor cv) {
		super(cv);
	}
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		// 没有对类进行修改
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	//修改类的方法
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		//删除构造方法
		if ("<init>".equals(name)) {
			return null; 
		}
		// 其他的方法保留
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	//修改类的字段
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		// 删除name字段
		if ("name".equals(name))
			return null;
		return super.visitField(access, name, desc, signature, value);
	}
	
	
	//visitEnd只会执行1次,可以增加方法和字段
	@Override
	public void visitEnd() {
		// 增加一个无参的构造方法,并执行
		MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V",
				null, null);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>",
				"()V");
		
		// 新增一个字段 int name ;
		cv.visitField(Opcodes.ACC_PUBLIC, "name", "I", null, null);
		
		//给name赋值 name = 2;
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitInsn(Opcodes.ICONST_2);
		// mv.visitFieldInsn(opcode, owner, name, desc);
		//访问方法或者字段的时,类的格式
		String className = Person.class.getName();
		mv.visitFieldInsn(Opcodes.PUTFIELD, className.replaceAll("\\.", "/"),
				"name", "I");
		
		//将this压入栈中
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		// mv.visitTypeInsn(opcode, type);
		//type和desc格式的区别
		//给person中的address字段初始化  address = new LinkedHashMap();
		mv.visitTypeInsn(Opcodes.NEW, "java/util/LinkedHashMap");
		//不能少
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/LinkedHashMap",
				"<init>", "()V");
		mv.visitFieldInsn(Opcodes.PUTFIELD, className.replaceAll("\\.", "/"),
				"address", "Ljava/util/Map;");	
		
		//固定结尾
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(3, 1);
		mv.visitEnd();
	}

}
