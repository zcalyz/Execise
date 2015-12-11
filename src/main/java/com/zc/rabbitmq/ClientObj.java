package com.zc.rabbitmq;

public class ClientObj {
	public static void main(String[] args) {
		new SendObj().send();
		new RecvObj().receive();
	}
}