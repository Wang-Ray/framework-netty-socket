package com.allinpay.framework.socket.netty.client.main;

import java.io.IOException;

import com.allinpay.framework.socket.netty.client.NettyTcpClient;

public class TestNettyTcpClient {

	public static void main(String[] args) throws IOException, InterruptedException {

		int port = 8099;
		String host = "localhost";
		if (args != null && args.length > 0) {
			try {
				host = args[0];
				port = Integer.valueOf(args[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				// 采用默认值
			}
		}

		NettyTcpClient client = new NettyTcpClient();
		client.setRemoteServerHost(host);
		client.setRemoteServerPort(port);
		ClientChannelHandlerInitializer clientChannelHandlerInitializer = new ClientChannelHandlerInitializer();
		client.setClientChannelHandlerInitializer(clientChannelHandlerInitializer);

		client.init();

	}

}
