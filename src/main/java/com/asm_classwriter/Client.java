package com.asm_classwriter;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.asm.json.Person;

public class Client {
	public static void main(String[] args) {
		try {
			ClassReader cr = new ClassReader(Person.class.getName());
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			MyClassVisitorCopy cv = new MyClassVisitorCopy();
			cr.accept(cv, ClassReader.SKIP_DEBUG);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
