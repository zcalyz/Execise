package com.asm_classwriter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

public class MyClassVisitorCopy implements ClassVisitor{

	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		// TODO Auto-generated method stub
		System.out.println("Version: "+version);
		System.out.println("access: "+access);
		System.out.println("name: "+name);
		System.out.println("signature: "+signature);
		System.out.println("superName: "+superName);
		for(String in : interfaces)
		{
			System.out.println("interface: "+in);
		}
		
	}

	@Override
	public void visitSource(String source, String debug) {
		// TODO Auto-generated method stub
		System.out.println("source");
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		// TODO Auto-generated method stub
		System.out.println("outerClass");
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		// TODO Auto-generated method stub
		System.out.println("annotation");
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
		// TODO Auto-generated method stub
		System.out.println("attribute");
	}

	@Override
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		// TODO Auto-generated method stub
		System.err.println("field: " + name);
		return null;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		// TODO Auto-generated method stub
		System.out.println("method:  "+name);
		return null;
	}

	@Override
	public void visitEnd() {
		// TODO Auto-generated method stub
		System.out.println("end");
	}

	

}
