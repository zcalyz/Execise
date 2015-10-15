package com.stackTrace;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class StackTraceUtil {
	private static final int PERIOD = 30;
	
	private static final int DELAY = 0;
	
	private static final int INTERVAL_NUMBER = 100;
	
	private static Set<String> preMethodSet  = null;
	
	private static Set<String> currentMethodSet = null;
	
	private static Set<String> currentResultSet = new HashSet<String>();
	
	private static Set<String> result = new HashSet<String>();
	
	private static Set<String> ignoreSet = new HashSet<String>();
	
	private static int  count = 0;
	
	static{
		ignoreSet.add("com.stackTrace.StackTraceUtil");
	
		ignoreSet.add("org.apache");
		ignoreSet.add("java");
		ignoreSet.add("sun");
		ignoreSet.add("javax");
		ignoreSet.add("com.sun");
		ignoreSet.add("org.springframework");
		ignoreSet.add("org.mybatis");
	}
	
	public static boolean isIgnoreMethod(String methodIdentifier){
		boolean sign = false;
		
		for(String elem : ignoreSet){
			if(methodIdentifier.startsWith(elem))
				sign = true;
		}
	    return  sign;
	}
		
	public static void executeTimerTask(){
		new Thread(new Runnable() {		
			@Override
			public void run() {

				Timer timer = new Timer();
				timer.schedule(new StactTraceTimeTask(), DELAY , PERIOD);
			}
		}).start();
		
	}
	
	private static class StactTraceTimeTask extends TimerTask{		
		@Override
		public void run() {
			
			if(preMethodSet == null){
				preMethodSet = getAllMethodIdentifier();
				return;
			}else{
				currentMethodSet = getAllMethodIdentifier();
		/*		//取交集
				currentResult.addAll(preMethods);
				currentResult.retainAll(currentMethods);*/
				currentResultSet = getCurrentResult(preMethodSet, currentMethodSet);
				//将这次得到的慢方法加入到result
			    result.addAll(currentResultSet);
				
			    preMethodSet = currentMethodSet;			    
				currentMethodSet = null;
				
				currentResultSet.clear();
				//每隔一段时间输出执行慢的方法
				if(++count == INTERVAL_NUMBER){
					showSlowMethod();
					result.clear();
					count = 0;
				}
			}	
		}
		
		public static Set<String> getAllMethodIdentifier(){
			Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
			Set<String> methodIdentifierSet = new HashSet<String>();
			
			Set<Thread> threadSet = stackTraces.keySet();
			for(Thread thread : threadSet){
				StackTraceElement[] stackTraceElems = stackTraces.get(thread);
				String threadName = thread.getName();
				
				for(int i = 0; i < stackTraceElems.length; i++){
					String methodIdentifier = stackTraceElems[i].getClassName() + "." + stackTraceElems[i].getMethodName();
					
					if(!isIgnoreMethod(methodIdentifier)){
						//增加调用关系
						methodIdentifier = addCallRelation(methodIdentifier, i, stackTraceElems);
						//增加行信息test
						methodIdentifier = methodIdentifier + "#" + stackTraceElems[i].getLineNumber();
						
						//增加线程信息，字符串格式:  ThreadName:ClassName.MethodName
						methodIdentifier = threadName + ":" + methodIdentifier;
						
						methodIdentifierSet.add(methodIdentifier);
					}
				}
			}
			return methodIdentifierSet;
		}
				
		public static String addCallRelation(String methodIdentifier,
				int position, StackTraceElement[] elements) {

			for (int i = position + 1; i < elements.length; i++) {
				methodIdentifier = elements[i].getClassName() + "."
						+ elements[i].getMethodName() + "->" + methodIdentifier;
			}
			return methodIdentifier;
		}
		/*
		 * 获得性能差的方法，并增加行号信息
		 */
		public static Set<String> getCurrentResult(Set<String> preElems, Set<String> currentElems){
			HashSet<String> tempResult = new HashSet<String>();
			
			for(String preElem : preElems){
				for(String currentElem : currentElems){
					String currentMethodPart = currentElem.substring(0,currentElem.indexOf("#")); 
					String preMethodPart = preElem.substring(0, preElem.indexOf("#"));
					
					if(preMethodPart.equals(currentMethodPart)){
						String preElemLine = preElem.substring(preElem.indexOf("#") + 1);
						String currentElemLine = currentElem.substring(currentElem.indexOf("#") + 1);
						
						if(! preElemLine.equals(currentElemLine)){
							preElem = preElem + "," + currentElemLine;
						}
						tempResult.add(preElem);
					}
				}
			}
			return tempResult;
		}
		
		public void showSlowMethod() {
			printSetElem(result);
			removeDuplicate();

			if (result != null && result.size() > 0) {
				System.out.println("the slow position is:");

				Iterator<String> iterator = result.iterator();
				while (iterator.hasNext()) {
					String elem = iterator.next();
					String[] elems = elem.split("#");
					
					if(elems != null && elems.length > 1)
						System.out.println(elems[0] + " [ " + elems[1] + " ]");
				}
				System.out.println("\n\n");
			}
		}
		
		public void removeDuplicate(){
			Set<String> tempSet = new HashSet<String>();
//			System.out.println("result set before size is " + result.size());
			for(String methodIdentifier : result){
				//去除methodIdentifier中的线程信息
				methodIdentifier = methodIdentifier.substring(methodIdentifier.indexOf(":") + 1);
				tempSet.add(methodIdentifier);
			}
			result = tempSet;
//			System.out.println("after size is " + result.size());
		}
		
		public void printSetElem(Set<String> set){
			if(set.size() > 0){
				System.out.println("输出result:");
				for(String elem : set){
					System.out.println(elem);	
				}
				System.out.println();
			}
		}
	}
}

