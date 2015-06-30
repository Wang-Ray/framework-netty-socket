package com.allinpay.framework.socket.netty.test;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.TcpServer;

public class TestTcpServer {
	@Test
	public void TestExceptionReconnect() throws IOException,
			InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);

		server.init();

		System.in.read();
	}
}
