package com.allinpay.framework.socket.netty.test.asciilength;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.client.TcpClient;
import com.allinpay.framework.socket.netty.server.TcpServer;

public class TestTcp {
	@Test
	/**
	 * 连接被服务端异常关闭，比如服务端关闭
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestServerClose() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		ServerChannelHandlerInitializer serverChannelHandler = new ServerChannelHandlerInitializer();
		server.setServerChannelHandler(serverChannelHandler);

		TcpClient client = new TcpClient("127.0.0.1", 8080);
		ClientChannelHandlerInitializer clientChannelHandlerInitializer = new ClientChannelHandlerInitializer();
		client.setClientChannelHandler(clientChannelHandlerInitializer);
		client.setReConnnectInterval(3 * 1000);

		server.init();
		client.init();

		Thread.sleep(5000);
		// 正常关闭
		server.close();
		// 客户端不断重连
		Thread.sleep(30000);
		server.init();
		Thread.sleep(5000);
		client.close();
		server.close();
	}

	@Test
	/**
	 * 心跳测试
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestHeartbeat() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		ServerChannelHandlerInitializer serverChannelHandler = new ServerChannelHandlerInitializer();
		server.setServerChannelHandler(serverChannelHandler);

		TcpClient client = new TcpClient("127.0.0.1", 8080);
		ClientChannelHandlerInitializer clientChannelHandlerInitializer = new ClientChannelHandlerInitializer();
		client.setClientChannelHandler(clientChannelHandlerInitializer);
		client.setHearbeatInterval(3);

		System.out.println("==========Start Server First==========");
		server.init();
		client.init();
		// 不断心跳
		Thread.sleep(30000);

		client.close();
		server.close();
	}
}
