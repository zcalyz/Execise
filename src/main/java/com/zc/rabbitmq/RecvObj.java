package com.zc.rabbitmq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

public class RecvObj {
	private final static String QUEUE_NAME = "object";
	
	public void receive(){
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		
		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			channel.basicQos(1);
			System.out.println("Waiting for message.....");
			DefaultConsumer consumer = new DefaultConsumer(channel){

				@Override
				public void handleDelivery(String consumerTag,
						Envelope envelope, BasicProperties properties,
						byte[] body) throws IOException {
					// TODO Auto-generated method stub
					ByteArrayInputStream in = new ByteArrayInputStream(body);
					ObjectInputStream oin = new ObjectInputStream(in);
					try {
						User user = (User) oin.readObject();
						System.out.println("receive:" + user.getName() + " : " + user.getAge());
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			/*		System.out.println(num + " Receive message: " + message);*/
				}
			};
			
			String result = channel.basicConsume(QUEUE_NAME,true, consumer);
//			System.out.println(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
