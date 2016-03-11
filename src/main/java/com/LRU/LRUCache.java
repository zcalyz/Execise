package com.LRU;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LRUCache<K,V> {
	//装载因子
	// 阀值 = table.length * 装载因子
	// 当map中的元素大于阀值的时候,会对table进行扩容; 容量为 2 * table.length
	private static final float loadFactor = 0.75f;
	
	//能实现两种存储顺序
	//default: 插入顺序
	//访问顺序 true,访问元素相当于新插入
	private LinkedHashMap<K, V> map;
	
	private int cacheSize;
	
	public LRUCache(int cacheSize) {
		// TODO Auto-generated constructor stub
		this.cacheSize = cacheSize;
		int capacity = (int)(cacheSize / loadFactor) + 1;
		//LinkedHashMap的静态内部类Entry 继承了 Map.Entry
		//Entry里面有三个指针 before, after 和next;
		//LinkedHashMap用双向循环链表维护顺序
			//如果是default:每次插入会将元素放到链表最后 即head.before
			//如果是访问顺序,每次访问和插入都会将元素放到最后 即head.before
		map = new LinkedHashMap<K, V>(capacity, loadFactor, true){

			private static final long serialVersionUID = 1L;
			//覆盖该方法,eldest是链表头部的元素
			//返回true,就会把它移除
			@Override
			protected boolean removeEldestEntry(Entry<K, V> eldest) {
				// TODO Auto-generated method stub
				return size() > LRUCache.this.cacheSize;
			}
		};
		
	}
	
	public synchronized V get(K key){
		return map.get(key);
	}
	
	public synchronized void put(K key, V value){
		map.put(key, value);
	}
	
	public synchronized void clear(){
		map.clear();
	}
	
	public int usedEntries(){
		return map.size();
	}
	
	public ArrayList<Map.Entry<K,V>> getAll(){
		return new ArrayList<Entry<K, V>>(map.entrySet());
	}
	
	public static void main(String[] args) {
		LRUCache<String, String> lru = new LRUCache<String, String>(3);
		lru.put("1", "one");
		lru.put("2", "two");
		lru.put("3", "three");
		lru.put("4", "four");
		lru.diplayAll(lru.getAll());
		System.out.println(lru.get("2"));
		lru.put("5", "five");
		lru.diplayAll(lru.getAll());
	}
	
	public void diplayAll(ArrayList<Map.Entry<K,V>>  array){
		//越后面的删除的越晚
		for(Entry<K, V> entry : array){
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
		
	}
}
