package com.allinpay.framework.socket.netty.test;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.TcpClient;

public class TestTcpClient {
	@Test
	public void TestExceptionReconnect() throws IOException,
			InterruptedException {

		TcpClient client = new TcpClient("127.0.0.1", 8080);

		client.init();

		System.in.read();
	}
}
