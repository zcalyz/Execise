package com.stackTrace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
			/*Collections.sort(resultList, new Comparator<Map.Entry<String, Integer>>() {

				@Override
				public int compare(Entry<String, Integer> o1,
						Entry<String, Integer> o2) {
					// 按降序排序
					return (o2.getValue() - o1.getValue());
				}
				
			});		*/
			mapQuickSort(resultList, 0, resultList.size() - 1);
			return resultList;
		}
		
		public static void mapQuickSort(ArrayList<Entry<String, Integer>> resultList,int start,int end){
			if(start < end){
				int partition = partition(resultList, start, end);
				mapQuickSort(resultList, start, partition - 1);
				mapQuickSort(resultList, partition + 1, end);
			}	
		}
		
		/*
		 * 获得快排的划分位置
		 */
		public static int partition(ArrayList<Entry<String, Integer>> resultList,int start, int end){
			int partitionElem = getEntryValue(resultList, start);
		
			int i = start;
			for(int j = start + 1; j <= end; j++){
				int loopElem = getEntryValue(resultList, j);
				if(loopElem > partitionElem){
					i++;
					exchangeElem(resultList, i, j);
				}
			}
			exchangeElem(resultList, start, i);
			
			return i;
		}
		/*
		 * 交换list中的两个元素
		 */
		public static void exchangeElem(ArrayList<Entry<String, Integer>> resultList, int index1, int index2){
			Entry<String, Integer> temp = resultList.get(index1);
			resultList.set(index1, resultList.get(index2));
			resultList.set(index2, temp);
		}
		
		public static int getEntryValue(ArrayList<Entry<String, Integer>> resultList, int index){
			Entry<String, Integer> entry = resultList.get(index);
			return entry.getValue();
		}
		
		public void showSlowMethod(ArrayList<Entry<String, Integer>> resultList) {
			
			if (resultMap != null && resultMap.size() > 0) {
				System.out.println("the slow position is:");

				for(Map.Entry<String, Integer> elem : resultList){
					System.out.println(elem.getKey() + "  总耗时约为: " + elem.getValue()*30 + "ms");
				}
	
				System.out.println("\n\n");
			}
		}		
	}
}

