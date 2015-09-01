package com.classAdapter;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddTimeAdapter extends ClassAdapter {
	private String currentClassName;
	public AddTimeAdapter(ClassVisitor cv) {
		super(cv);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
			cv.visit(version, access, name, signature, superName, interfaces);
		
			currentClassName = name;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {

		MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
				exceptions);
		if ("timeCount".equals(name)) {
			mv = new addTimeMethodAdapter(mv);
		}
		return mv;
	}
	@Override
	public void visitEnd() {
		FieldVisitor fv = cv.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "interval", "J", null, null);
		if(fv!=null){
			System.out.println("not null");
			fv.visitEnd();
		}
		cv.visitEnd();
	}
	class addTimeMethodAdapter extends MethodAdapter {

		public addTimeMethodAdapter(MethodVisitor mv) {
			super(mv);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void visitCode() {
			mv.visitCode();
//			mv.visitFieldInsn(Opcodes.GETSTATIC, currentClassName, "interval", "J");
//			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
			mv.visitIntInsn(Opcodes.BIPUSH, 10);
			
//			mv.visitInsn(Opcodes.LSUB);
			mv.visitFieldInsn(Opcodes.PUTSTATIC, currentClassName, "interval", "J");
		}

		@Override
		public void visitInsn(int opcode) {
			
//			mv.visitFieldInsn(Opcodes.GETSTATIC, currentClassName, "interval", "J");
//			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J");
//			
//			mv.visitInsn(Opcodes.LADD);
//			mv.visitFieldInsn(Opcodes.PUTSTATIC, currentClassName, "interval", "J");
//			
//			mv.visitInsn(opcode);
		}

		@Override
		public void visitMaxs(int maxStack, int maxLocal) {
			System.out.println("end");
			System.out.println(maxStack+"  "+maxLocal);
			mv.visitMaxs(10, 10);
		}
	}
}
