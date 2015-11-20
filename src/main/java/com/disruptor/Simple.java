package com.disruptor;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

//相关网站: http://www.cnblogs.com/haiq/p/4112689.html
public class Simple {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		//Thread pool
		ExecutorService execPool = Executors.newCachedThreadPool();
		//EVENT_FACTORY 实现了特定接口的用于创建实践的工厂
		Disruptor<ValueEvent> disruptor = new Disruptor<ValueEvent>(ValueEvent.EVENT_FACTORY, 1024, execPool);
//		new Disruptor<T>(eventFactory, executor, claimStrategy, waitStrategy)
		
		//用户定义的用于处理事件的类
		EventHandler<ValueEvent> eventHandler = new EventHandler<ValueEvent>() {

			@Override
			public void onEvent(ValueEvent event, long sequence,
					boolean endOfBatch) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Sequence: " + sequence);
				System.out.println("ValueEvent: " + event.getValue());
			}
		};
		
		disruptor.handleEventsWith(eventHandler);
		RingBuffer<ValueEvent> ringBuffer = disruptor.start();
		
		for(long i = 10; i < 2000; i++){
			String uuid = UUID.randomUUID().toString();
			//1 获得下一个空事件的事件号
			long seq = ringBuffer.next();
			//2 根据事件号从ringBuffer中获得一个事件
			ValueEvent valueEvent = ringBuffer.get(seq);
			//3 填充需要处理的数据
			valueEvent.setValue(uuid);
			//4 发布执行
			ringBuffer.publish(seq);
		}
		disruptor.shutdown();
		execPool.shutdown();
	}
}
