package com.stackTrace.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
public class StackTraceUtil {
	private static final int PERIOD = 30;

	private static final int DELAY = 0;

	private static final int INTERVAL_NUMBER = 4000;
	
	private static final double PERCENT = 0.90;

	private static Set<String> preMethodSet = null;

	private static Set<String> currentMethodSet = null;

	private static Set<String> currentResultSet = new HashSet<String>();

	private static Map<String, Integer> resultMap = new HashMap<String, Integer>();

	private static Set<String> ignoreSet = new HashSet<String>();

	private static int count = 0;

	static {
		ignoreSet.add("org.vlis.apm");
		ignoreSet.add("org.apache");
		ignoreSet.add("java");
		ignoreSet.add("sun");
		ignoreSet.add("com.sun");
		ignoreSet.add("javax");
		ignoreSet.add("com.mchange.v2");
		ignoreSet.add("org.springframework");
		ignoreSet.add("org.mybatis");
		/*
		 * ignoreSet.add("com.mysql.jdbc");
		 * ignoreSet.add("ov.org.objectweb.asm");
		 */
	}

	public static boolean isIgnoreMethod(String methodIdentifier) {
		boolean sign = false;

		for (String elem : ignoreSet) {
			if (methodIdentifier.startsWith(elem)) {
				sign = true;
				break;
			}
		}
		return sign;
	}

	public static void executeTimerTask() {
		Timer timer = new Timer();
		timer.schedule(new StackTraceTimeTask(timer), DELAY, PERIOD);
	}

	public static class StackTraceTimeTask extends TimerTask {
		private Timer timer;

		public StackTraceTimeTask(Timer timer) {
			// TODO Auto-generated constructor stub
			this.timer = timer;
		}

		@Override
		public void run() {

			if (preMethodSet == null) {
				preMethodSet = getAllMethodIdentifier();
				return;
			} else {
				currentMethodSet = getAllMethodIdentifier();
				// get intersection
				currentResultSet.addAll(preMethodSet);
				currentResultSet.retainAll(currentMethodSet);

				addToResultMap(currentResultSet);

				preMethodSet = currentMethodSet;
				currentMethodSet = null;

				currentResultSet.clear();
				// send message/2 min
				if (++count >= INTERVAL_NUMBER) {
					ArrayList<Entry<String, Integer>> resultList = sortByValue();

					resultList = removeDuplicate(resultList);
					showSlowMethod(resultList);
					resultMap.clear();
					count = 0;
					// stop current task
					/*
					 * timer.cancel(); timer.purge();
					 */
				}
			}
		}

		public static Set<String> getAllMethodIdentifier() {
			Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
			Set<String> methodIdentifierSet = new HashSet<String>();

			Set<Thread> threadSet = stackTraces.keySet();
			for (Thread thread : threadSet) {
				StackTraceElement[] stackTraceElems = stackTraces.get(thread);

				String threadName = thread.getName();
				String transactionId = ThreadTransactionRelation.getTransactionId(threadName);

				if (transactionId != null) {
					StringBuilder methodIdentifier = new StringBuilder();
					for (int i = stackTraceElems.length - 1; i >= 0; i--) {
						StackTraceElement stackTraceElement = stackTraceElems[i];
						if (!isIgnoreMethod(stackTraceElement.getClassName())) {
							if (methodIdentifier.length() == 0) {
								// format: ThreadName:ClassName.MethodName
								methodIdentifier.append(transactionId).append("&");
							} else {
								methodIdentifier.append("->");
							}
							methodIdentifier.append(stackTraceElement.getClassName()).append(".")
									.append(stackTraceElement.getMethodName());
							methodIdentifierSet.add(methodIdentifier.toString());
						}
					}
				}
			}
			return methodIdentifierSet;
		}

		/*
		 * result format is <mothodIdentifier, calltimes>
		 */
		public static void addToResultMap(Set<String> currentResultSet) {

			for (String methodIdentifier : currentResultSet) {
				// remove thread information
				// methodIdentifier =
				// methodIdentifier.substring(methodIdentifier.indexOf(":") +
				// 1);

				Integer value = resultMap.get(methodIdentifier);
				if (value != null) {
					resultMap.put(methodIdentifier, ++value);
				} else {
					resultMap.put(methodIdentifier, 1);
				}
			}
		}

		public static ArrayList<Entry<String, Integer>> sortByValue() {
			ArrayList<Entry<String, Integer>> resultList = new ArrayList<Map.Entry<String, Integer>>(
					resultMap.entrySet());
			mapQuickSort(resultList, 0, resultList.size() - 1);
			return resultList;
		}

		public static void mapQuickSort(ArrayList<Entry<String, Integer>> resultList, int start, int end) {
			if (start < end) {
				int partition = partition(resultList, start, end);
				mapQuickSort(resultList, start, partition - 1);
				mapQuickSort(resultList, partition + 1, end);
			}
		}

		public static int partition(ArrayList<Entry<String, Integer>> resultList, int start, int end) {
			int partitionElem = getEntryValue(resultList, start);

			int i = start;
			for (int j = start + 1; j <= end; j++) {
				int loopElem = getEntryValue(resultList, j);
				if (loopElem > partitionElem) {
					i++;
					exchangeEntry(resultList, i, j);
				}
			}
			exchangeEntry(resultList, start, i);

			return i;
		}

		public static void exchangeEntry(ArrayList<Entry<String, Integer>> resultList, int index1, int index2) {
			Entry<String, Integer> temp = resultList.get(index1);
			resultList.set(index1, resultList.get(index2));
			resultList.set(index2, temp);
		}

		public static int getEntryValue(ArrayList<Entry<String, Integer>> resultList, int index) {
			Entry<String, Integer> entry = resultList.get(index);
			return entry.getValue();
		}

		public ArrayList<Entry<String, Integer>> removeDuplicate(ArrayList<Entry<String, Integer>> duplicateList) {
			ArrayList<Entry<String, Integer>> resultList = new ArrayList<Map.Entry<String, Integer>>();
			int listSize = duplicateList.size();
			// Use tempEntry represent removed .
			Entry<String, Integer> tempEntry = getTempEntry();

			for (int i = 0; i < listSize - 1; i++) {
				Entry<String, Integer> elem1 = duplicateList.get(i);
				String methodIdentifier = elem1.getKey();
				int time = elem1.getValue();
				// elem1 is exist
				if (time != -1) {
					for (int j = i + 1; j < listSize; j++) {
						Entry<String, Integer> elem2 = duplicateList.get(j);

						if (elem2.getValue() >= time*PERCENT) {
							if (methodIdentifier.contains(elem2.getKey())) {
								duplicateList.set(j, tempEntry);
							} else if (elem2.getKey().contains(methodIdentifier)) {
								duplicateList.set(i, elem2);
								duplicateList.set(j, tempEntry);
							}
						} else if (elem2.getValue() != -1) {
							break;
						}
					}
					resultList.add(duplicateList.get(i));
				}
			}
			return resultList;
		}

		public Entry<String, Integer> getTempEntry() {
			HashMap<String, Integer> tempMap = new HashMap<String, Integer>();
			tempMap.put("-1", -1);
			Set<Entry<String, Integer>> entrySet = tempMap.entrySet();
			Entry<String, Integer> tempEntry = null;
			for (Entry<String, Integer> entry : entrySet) {
				tempEntry = entry;
			}
			return tempEntry;
		}

		public void showSlowMethod(ArrayList<Entry<String, Integer>> resultList) {

			Map<String, Object> messageCache = new HashMap<String, Object>();

			if (resultMap != null && resultMap.size() > 0) {
				for (Map.Entry<String, Integer> elem : resultList) {
					int partitonIndex = elem.getKey().indexOf("&");
					String transactionId = elem.getKey().substring(0, partitonIndex);
					// remove thread information
					String callRelation = elem.getKey().substring(partitonIndex + 1);
					long time = elem.getValue() * PERIOD;

					StackTraceMessage message = new StackTraceMessage(transactionId, callRelation,
							ThreadTransactionRelation.agentId, time, System.currentTimeMillis());

					messageCache.put("stackTraceMessage", message);
					try {
/*						SenderFactory.createSender()
								.send(new ZeromqMessage(DataCoverter.getJsonFromObject(messageCache)));*/
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
			}
			
		}
	}
}
