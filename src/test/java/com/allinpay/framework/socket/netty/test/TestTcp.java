package com.allinpay.framework.socket.netty.test;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.ClientBizHandler;
import com.allinpay.framework.socket.netty.TcpClient;
import com.allinpay.framework.socket.netty.TcpServer;

public class TestTcp {
	@Test
	/**
	 * 连接被服务端异常关闭，比如服务端关机
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestServerClose() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		TcpClient client = new TcpClient("127.0.0.1", 8080);
		client.setReConnnectInterval(3 * 1000);

		server.init();
		client.init();

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
	 * 	心跳测试
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestHeartbeat() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		TcpClient client = new TcpClient("127.0.0.1", 8080);
		client.setHearbeatInterval(3);

		System.out.println("==========Start Server First==========");
		server.init();
		client.init();
		// 不断心跳
		Thread.sleep(30000);

		client.close();
		server.close();
	}

	@Test
	/**
	 * 连接被服务端正常关闭
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void TestServerDisconnectReconnect() throws IOException,
			InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		server.setHeartbeatMessage("1111");
		TcpClient client = new TcpClient("127.0.0.1", 8080);
		client.setHeartbeatMessage("1111");
		client.setHearbeatInterval(3);
		client.setReConnnectInterval(3 * 1000);

		System.out.println("==========Start Server First==========");
		server.init();
		client.init();
		// 服务端收到心跳后disconnect
		Thread.sleep(60000);

		client.close();
		server.close();
	}

	@Test
	public void TestReconnect() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		TcpClient client = new TcpClient("127.0.0.1", 8080);

		client.setHearbeatInterval(3);
		client.setReConnnectInterval(3 * 1000);

		server.init();
		client.init();
		Thread.sleep(300000);

		client.close();
		// 正常关闭
		server.close();
		Thread.sleep(2000);
	}

	@Test
	public void TestTcpClient() throws IOException, InterruptedException {
		TcpServer server = new TcpServer("127.0.0.1", 8080);
		TcpClient client = new TcpClient("127.0.0.1", 8080);

		client.setHearbeatInterval(3);
		client.setReConnnectInterval(3 * 1000);

		server.init();
		client.init();

		client.getChannel().pipeline().addLast(new LineBasedFrameDecoder(1024))
				.addLast(new StringDecoder())
				.addLast(new StringEncoder())
				.addLast(new ClientBizHandler());

		client.getChannel().writeAndFlush(
				"3333" + System.getProperty("line.separator"));

		Thread.sleep(30000);

		client.close();
		// 正常关闭
		server.close();
		Thread.sleep(2000);
	}
}
