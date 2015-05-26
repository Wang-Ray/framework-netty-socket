package com.allinpay.io.socket.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaSocketClient {

	static Logger logger = LoggerFactory.getLogger(MinaSocketClient.class);

	public static void main(String[] args) throws Exception {

		// Create TCP/IP connector.
		NioSocketConnector connector = new NioSocketConnector();
		// 创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();
		// 设定这个过滤器将一行一行(/r/n)的读取数据
		chain.addLast("protocolCodecFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
		// 设定服务器端的消息处理器:一个MinaClientHandler对象,
		connector.setHandler(new MinaClientHandler());
		// Set connect timeout.
		connector.setConnectTimeoutMillis(30 * 1000);
		// 连结到服务器:
		ConnectFuture cf = connector.connect(new InetSocketAddress("localhost", 9988));
		// Wait for the connection attempt to be finished.
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
