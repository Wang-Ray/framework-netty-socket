package com.allinpay.framework.socket.netty.test;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.TcpClient;

public class TestTcpClient {
	@Test
	public void TestExceptionReconnect() throws IOException,
			InterruptedException {

		TcpClient client = new TcpClient("127.0.0.1", 8080);

		client.setHearbeatInterval(3);
		client.setReConnnectInterval(3 * 1000);

		client.init();

		int i = 0;
		while (i < 10) {
			Thread.sleep(2000);

			client.getChannel().writeAndFlush(
					"3333" + System.getProperty("line.separator"));
			System.out.println("发送3333");
			i++;
		}

		System.in.read();
	}
}
