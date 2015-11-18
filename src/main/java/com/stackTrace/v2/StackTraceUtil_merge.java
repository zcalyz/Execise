package com.stackTrace.v2;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @author Chenzhai
 *
 */
public class StackTraceUtil_merge {
	private static final int PERIOD = 30;
	
	private static final int DELAY = 0;
	
	private static final int INTERVAL_NUMBER = 600;
	
	private static Set<String> preMethodSet  = null;
	
	private static Set<String> currentMethodSet = null;
	
	private static Set<String> currentResultSet = new HashSet<String>();
	
	private static Map<String, Integer> resultMap = new HashMap<String, Integer>();
	
	private static Set<String> ignoreSet = new HashSet<String>();
	
	private static int  count = 0;
	
	static{
		ignoreSet.add("org.vlis.apm");
		ignoreSet.add("org.apache");
		ignoreSet.add("java");
		ignoreSet.add("sun");
/*		ignoreSet.add("org.vlis.apm.start.StackTraceUtil");
	
		ignoreSet.add("org.apache");
		ignoreSet.add("java");
		ignoreSet.add("sun");
		ignoreSet.add("javax");
		ignoreSet.add("com.sun");
		ignoreSet.add("org.springframework");
		ignoreSet.add("org.mybatis");
		ignoreSet.add("com.mchange.v2");
		ignoreSet.add("com.mysql.jdbc");
		ignoreSet.add("ov.org.objectweb.asm");*/
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
		Timer timer = new Timer();
		timer.schedule(new StactTraceTimeTask(timer), DELAY , PERIOD);
	}
	
	public static class StactTraceTimeTask extends TimerTask{		
		private Timer timer;

		public StactTraceTimeTask(Timer timer) {
			// TODO Auto-generated constructor stub
			this.timer = timer;
		}
		
		@Override
		public void run() {
			
			if(preMethodSet == null){
				preMethodSet = getAllMethodIdentifier();
				return;
			}else{
				currentMethodSet = getAllMethodIdentifier();
				//get intersection
				currentResultSet.addAll(preMethodSet);
				currentResultSet.retainAll(currentMethodSet);		
							   
				addToResultMap(currentResultSet);
				
			    preMethodSet = currentMethodSet;			    
				currentMethodSet = null;
				
				currentResultSet.clear();
				//send message/3 min
				if(++count == INTERVAL_NUMBER){
					ArrayList<Entry<String, Integer>> resultList = sortByValue();
					System.out.println("before: " + resultList.size());
					resultList = removeDuplicate(resultList);
					System.out.println("after: " + resultList.size());
					
					showSlowMethod(resultList);
					resultMap.clear();
					count = 0;
					//stop current task
//					timer.cancel();
//					timer.purge();
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
					StringBuilder methodIdentifier = new StringBuilder(stackTraceElems[i].getClassName() + "." + stackTraceElems[i].getMethodName());
					
					if(!isIgnoreMethod(methodIdentifier.toString()) && stackTraceElems[i].getLineNumber() > 0){
						methodIdentifier = addCallRelation(methodIdentifier, i, stackTraceElems);					
						//format:  ThreadName:ClassName.MethodName
						methodIdentifier.insert(0, ":").insert(0, threadName);
						
						methodIdentifierSet.add(methodIdentifier.toString());
					}
				}
			}
			return methodIdentifierSet;
		}
	
		public static StringBuilder addCallRelation(StringBuilder methodIdentifier,
				int position, StackTraceElement[] elements) {

			for (int i = position + 1; i < elements.length; i++) {
				methodIdentifier.insert(0, "->")
						.insert(0, elements[i].getMethodName()).insert(0, ".")
						.insert(0, elements[i].getClassName());
			}
			return methodIdentifier;
		}
		/*
		 * result format is <mothodIdentifier, calltimes>		
		 */
		public static void addToResultMap(Set<String> currentResultSet){
			
			for(String methodIdentifier : currentResultSet){
				//remove thread information
//				methodIdentifier = methodIdentifier.substring(methodIdentifier.indexOf(":") + 1);
				
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
		
		public static int partition(ArrayList<Entry<String, Integer>> resultList,int start, int end){
			int partitionElem = getEntryValue(resultList, start);
		
			int i = start;
			for(int j = start + 1; j <= end; j++){
				int loopElem = getEntryValue(resultList, j);
				if(loopElem > partitionElem){
					i++;
					exchangeEntry(resultList, i, j);
				}
			}
			exchangeEntry(resultList, start, i);
			
			return i;
		}
		
		public static void exchangeEntry(ArrayList<Entry<String, Integer>> resultList, int index1, int index2){
			Entry<String, Integer> temp = resultList.get(index1);
			resultList.set(index1, resultList.get(index2));
			resultList.set(index2, temp);
		}
		
		public static int getEntryValue(ArrayList<Entry<String, Integer>> resultList, int index){
			Entry<String, Integer> entry = resultList.get(index);
			return entry.getValue();
		}
		
		public ArrayList<Entry<String, Integer>> removeDuplicate(ArrayList<Entry<String, Integer>> resultList){
			ArrayList<Entry<String, Integer>> tempList = new ArrayList<Map.Entry<String,Integer>>();
			int listSize = resultList.size();
//			listSize = (listSize > 100)? listSize/3: listSize;
			
			Entry tempEntry = getTempEntry();
			
			for(int i = 0; i < listSize-1; i++){
				Entry<String, Integer> elem1 = resultList.get(i);
				String methodIdentifier = elem1.getKey();
				int time = elem1.getValue();
				
				if(time != -1){
					for(int j = i + 1; j < listSize; j++){
						Entry<String, Integer> elem2 = resultList.get(j);
						
						if(time == elem2.getValue()){
							if(methodIdentifier.contains(elem2.getKey())){
//								resultList.remove(j);
								resultList.set(j, tempEntry);
							}else if(elem2.getKey().contains(methodIdentifier)){
								resultList.set(i, elem2);
//								resultList.remove(j);
								resultList.set(j, tempEntry);
							}
						}
					}
					tempList.add(resultList.get(i));
				}
			}
			return tempList;
		}
		
		public Entry getTempEntry(){
			HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
			tempMap.put("-1", -1);
			Set<Entry<String, Integer>> entrySet = tempMap.entrySet();
			Entry tempEntry = null;
			for(Entry entry : entrySet){
				tempEntry = entry;
			}
			return tempEntry;
		}
		
		public void showSlowMethod(ArrayList<Entry<String, Integer>> resultList) {
			List<StackTraceMessage> messages = new ArrayList<StackTraceMessage>();
			int num = 1;
			
			FileWriter out = null;
			try {
				out = new FileWriter("/home/zc/t/slow",true);
				out.write("the slow position is: \n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			
			if (resultMap != null && resultMap.size() > 0) {
				for(Map.Entry<String, Integer> elem : resultList){
					int partitonIndex = elem.getKey().indexOf(":");
					if(partitonIndex < 0){
						System.out.println("error");
					}
					String threadName = elem.getKey().substring(0, partitonIndex);
					String transactionId = ThreadTransactionRelation.getTransactionId(threadName);
					
					//remove thread information
					String callRelation = elem.getKey().substring(partitonIndex + 1);
					long time = elem.getValue()*PERIOD;
					
					try {
						out.write(num++ + " transactionId:" +transactionId +"  "+ callRelation+ "  总耗时约为: " + time + "ms\n\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					StackTraceMessage message = new StackTraceMessage(transactionId, callRelation, time, System.currentTimeMillis());
					messages.add(message);
				}
				// send messages.....
				messages.clear();
				try {
					out.write("\n\n\n\n");
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}

