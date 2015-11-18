package com.allinpay.framework.socket.netty.server.main;

import java.io.IOException;

import com.allinpay.framework.socket.netty.Constants;
import com.allinpay.framework.socket.netty.server.NettyTcpServer;

public class TestTcpServer {

	public static void main(String[] args) throws IOException, InterruptedException {

		int port = 8099;
		String host = "localhost";
		String whetherMultiConnection = Constants.SUPPORT_MULTI_CONNECTION;
		if (args != null && args.length > 0) {
			try {
				host = args[0];
				port = Integer.valueOf(args[1]);
				whetherMultiConnection = args[2];
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// 采用默认值
			}
		}

		NettyTcpServer server = new NettyTcpServer();
		server.setLocalHost(host);
		server.setLocalPort(port);
		server.setWhetherMultiConnection(whetherMultiConnection);
		ServerChannelHandlerInitializer ServerChannelHandlerInitializer = new ServerChannelHandlerInitializer();
		server.setServerChannelHandlerInitializer(ServerChannelHandlerInitializer);

		server.init();

	}

}
