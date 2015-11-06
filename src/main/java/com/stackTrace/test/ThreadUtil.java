package com.stackTrace.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.stackTrace.StackTraceUtil;

public class ThreadUtil {
	
	public static Set<String> getAllMethod(){
		 Set<StackTraceElement> stackTraces = getAllStackTracesElements();
		 
		 Set<String> allMethods = new HashSet<String>();
		 
		 Iterator<StackTraceElement> iterator = stackTraces.iterator();
		 while(iterator.hasNext()){
			 StackTraceElement next = iterator.next();
			 String methodModifier = next.getClassName() + "." + next.getMethodName();
			 if(!StackTraceUtil.isIgnoreMethod(methodModifier)){
				 allMethods.add(methodModifier + "@" + next.hashCode());
			 }	
		 }	 
		 return allMethods;
	}
	
	public static Set<StackTraceElement> getAllStackTracesElements(){
		Map<Thread, StackTraceElement[]> stackTraces =  Thread.getAllStackTraces();
		
		HashSet<StackTraceElement>  stackTraceSets = new HashSet<StackTraceElement>();
		
		Set<Thread> keys = stackTraces.keySet();
		for(Thread key : keys){
			StackTraceElement[] elems = stackTraces.get(key);
			
			for(StackTraceElement temp : elems){
				stackTraceSets.add(temp);
			}
		}
		
		return stackTraceSets;
	}
	
	public static Set<String> getAllMethodIdentifier(){
		Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
		Set<String> methodIdentifierSet = new HashSet<String>();
		
		Set<Thread> threadSet = stackTraces.keySet();
		for(Thread thread : threadSet){
			StackTraceElement[] stackTraceElems = stackTraces.get(thread);
			String threadName = thread.getName();
			
			for(StackTraceElement stackTraceElem : stackTraceElems ){
				String methodIdentifier = stackTraceElem.getClassName() + "." + stackTraceElem.getMethodName();
				
				if(!StackTraceUtil.isIgnoreMethod(methodIdentifier)){
					methodIdentifier = threadName + ":" + methodIdentifier;
					methodIdentifierSet.add(methodIdentifier);
				}
			}
		}
		return methodIdentifierSet;
	}
}
