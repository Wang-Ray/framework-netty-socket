package com.allinpay.io.socket.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaSocketServer {
	public static void main(String[] args) throws Exception {
		// 创建一个非阻塞的Server端Socket,用NIO
		SocketAcceptor acceptor = new NioSocketAcceptor();
		// 创建接收数据的过滤器
		DefaultIoFilterChainBuilder chain = acceptor.getFilterChain();
		// 设定这个过滤器将一行一行(/r/n)的读取数据
		chain.addLast("protocolCodecFilter", new ProtocolCodecFilter(new TextLineCodecFactory()));
		// 设定服务器端的消息处理器:一个MinaServerHandler对象,
		acceptor.setHandler(new MinaServerHandler());
		// 服务器端绑定的端口
		int bindPort = 9988;
		// 绑定端口,启动服务器
		acceptor.bind(new InetSocketAddress(bindPort));
		System.out.println("Mina Server is Listing on:= " + bindPort);
	}
}
