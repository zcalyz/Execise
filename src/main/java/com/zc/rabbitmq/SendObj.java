package com.zc.rabbitmq;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendObj {
	private final static String QUEUE_NAME = "object";
	
	public void send(){
		ConnectionFactory factory = new ConnectionFactory();
		// broker address
		factory.setHost("localhost");

		try {
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			
			User user = new User();
			user.setName("zz");
			user.setAge(20);
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(user);

			channel.basicPublish("", QUEUE_NAME, null, bo.toByteArray());
			System.out.println("Send Obj ");

			channel.close();
			connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
