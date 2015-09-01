package com.log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class IpTest {
	public static void main(String[] args) {
		for(int i=0;i<10;i++){
			int nextInt = new Random().nextInt(7);
			System.out.println(nextInt);
		}
		
//		try {
//			InetAddress thisIp = InetAddress.getLocalHost();
//			System.out.println(thisIp);
//			String hostAddress = thisIp.getHostAddress();
//			System.out.println(hostAddress);
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
}
