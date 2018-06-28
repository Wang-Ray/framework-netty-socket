package com.allinpay.framework.socket.netty.test;
import com.allinpay.io.framework.netty.socket.server.NettyTcpServer;

public class TestMain {

	public static void main(String[] args) {
		NettyTcpServer server = new NettyTcpServer();
		server.setLocalHost("127.0.0.1");
		server.setLocalPort(8080);
		ServerChannelHandlerInitializer serverChannelHandler = new ServerChannelHandlerInitializer();
		server.setServerChannelHandlerInitializer(serverChannelHandler);
		
		server.init();
	}

}
