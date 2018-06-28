package com.allinpay.io.framework.netty.socket.client.main;

import java.io.IOException;

import com.allinpay.io.framework.netty.socket.client.NettyTcpClient;

public class TestNettyTcpClient {

	public static void main(String[] args) throws IOException, InterruptedException {

		int port = 8099;
		String host = "localhost";
//		String host = "192.168.107.203";
//		String host = "192.168.103.13";
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
