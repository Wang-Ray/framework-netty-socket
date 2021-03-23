package com.allinpay.framework.socket.netty.test.asciilength;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;

import com.allinpay.io.framework.netty.socket.client.NettyTcpClient;
import com.allinpay.io.framework.netty.socket.server.NettyTcpServer;

public class TestTcp {
	@Test
	@Ignore
	/**
	 * 连接被服务端异常关闭，比如服务端关闭
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestServerClose() throws IOException, InterruptedException {
		NettyTcpServer server = new NettyTcpServer();
		server.setLocalHost("127.0.0.1");
		server.setLocalPort(8080);
		ServerChannelHandlerInitializer serverChannelHandler = new ServerChannelHandlerInitializer();
		server.setServerChannelHandlerInitializer(serverChannelHandler);

		NettyTcpClient client = new NettyTcpClient();
		client.setRemoteServerHost("127.0.0.1");
		client.setRemoteServerPort(8080);
		ClientChannelHandlerInitializer clientChannelHandlerInitializer = new ClientChannelHandlerInitializer();
		client.setClientChannelHandlerInitializer(clientChannelHandlerInitializer);
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
	@Ignore
	/**
	 * 心跳测试
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestHeartbeat() throws IOException, InterruptedException {
		NettyTcpServer server = new NettyTcpServer();
		server.setLocalHost("127.0.0.1");
		server.setLocalPort(8080);
		ServerChannelHandlerInitializer serverChannelHandler = new ServerChannelHandlerInitializer();
		server.setServerChannelHandlerInitializer(serverChannelHandler);

		NettyTcpClient client = new NettyTcpClient();
		client.setRemoteServerHost("127.0.0.1");
		client.setRemoteServerPort(8080);
		ClientChannelHandlerInitializer clientChannelHandlerInitializer = new ClientChannelHandlerInitializer();
		client.setClientChannelHandlerInitializer(clientChannelHandlerInitializer);

		System.out.println("==========Start Server First==========");
		server.init();
		client.init();
		// 不断心跳
		Thread.sleep(30000);

		client.close();
		server.close();
	}
}
