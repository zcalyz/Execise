package com.stackTrace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

public class StackTraceUtil_Statictis {
	private static final int PERIOD = 30;
	
	private static final int DELAY = 0;
	
	private static final int INTERVAL_NUMBER = 100;
	
	private static Set<String> preMethodSet  = null;
	
	private static Set<String> currentMethodSet = null;
	
	private static Set<String> currentResultSet = new HashSet<String>();
	
//	private static Set<String> result = new HashSet<String>();
	private static Map<String, Integer> resultMap = new HashMap<String, Integer>();
	
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
		ignoreSet.add("com.mchange.v2");
		ignoreSet.add("com.mysql.jdbc");
		ignoreSet.add("ov.org.objectweb.asm");
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
				//取交集
				currentResultSet.addAll(preMethodSet);
				currentResultSet.retainAll(currentMethodSet);
//				currentResultSet = getCurrentResult(preMethodSet, currentMethodSet);
				
				//将这次得到的慢方法加入到result
//			    result.addAll(currentResultSet);
				addToResultMap(currentResultSet);
				
			    preMethodSet = currentMethodSet;			    
				currentMethodSet = null;
				
				currentResultSet.clear();
				//每隔一段时间输出执行慢的方法
				if(++count == INTERVAL_NUMBER){
					ArrayList<Entry<String, Integer>> resultList = sortByValue();
					showSlowMethod(resultList);
					resultMap.clear();
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
					
					if(!isIgnoreMethod(methodIdentifier) && stackTraceElems[i].getLineNumber() > 0){
						//增加调用关系
						methodIdentifier = addCallRelation(methodIdentifier, i, stackTraceElems);
						//增加行信息test
//						methodIdentifier = methodIdentifier + "#" + stackTraceElems[i].getLineNumber();
						
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
		 * 将此次结果加入到result中，格式为<mothodIdentifier,当前调用该方法总次数>		
		 */
		public static void addToResultMap(Set<String> currentResultSet){
			
			for(String methodIdentifier : currentResultSet){
				//移除线程信息，统计所有线程调用该方法的总耗时
				methodIdentifier = methodIdentifier.substring(methodIdentifier.indexOf(":") + 1);
				
				Integer value = resultMap.get(methodIdentifier);
				if(value != null){
					resultMap.put(methodIdentifier, ++value);
				}else{
					resultMap.put(methodIdentifier, 1);
				}
			}
			
		}
		
		public static ArrayList<Entry<String, Integer>> sortByValue(){
			ArrayList<Entry<String, Integer>> resultList = new ArrayList<Map.Entry<String, Integer>>(resultMap.entrySet());
			
			Collections.sort(resultList, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					// 按降序排序
					return (o2.getValue() - o1.getValue());
				}
				
			});		
			return resultList;
		}
		
		public void showSlowMethod(ArrayList<Entry<String, Integer>> resultList) {
			
			if (resultMap != null && resultMap.size() > 0) {
				System.out.println("the slow position is:");

				for(Map.Entry<String, Integer> elem : resultList){
					System.out.println(elem.getKey() + "  总耗时约为: " + elem.getValue()*30 + "毫秒");
				}
	
				System.out.println("\n\n");
			}
		}
		
		/*
		 * 获得性能差的方法，并增加行号信息 
		public static Set<String> getCurrentResult(Set<String> preElems, Set<String> currentElems){
			HashSet<String> tempResult = new HashSet<String>();
			
			for(String preElem : preElems){
				for(String currentElem : currentElems){
					String currentMethodPart = currentElem.substring(0,currentElem.lastIndexOf("#")); 
					String preMethodPart = preElem.substring(0, preElem.lastIndexOf("#"));
					
					if(preMethodPart.equals(currentMethodPart)){
						String preElemLine = preElem.substring(preElem.lastIndexOf("#") + 1);
						String currentElemLine = currentElem.substring(currentElem.lastIndexOf("#") + 1);
						
						if(! preElemLine.equals(currentElemLine)){
							preElem = preElem + "," + currentElemLine;
						}
						tempResult.add(preElem);
					}
				}
			}
			return tempResult;
		}*/
		
	}
}

