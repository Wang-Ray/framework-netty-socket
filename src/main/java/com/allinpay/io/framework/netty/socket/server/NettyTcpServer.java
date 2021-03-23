package com.allinpay.io.framework.netty.socket.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import io.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.allinpay.io.framework.netty.socket.Constants;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyTcpServer {

	private static final Logger logger = LoggerFactory.getLogger(NettyTcpServer.class);

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private ServerBootstrap serverBootstrap;

	/**
	 * 服务端监听地址
	 */
	private String localHost;

	/**
	 * 服务端监听端口
	 */
	private int localPort;

	/**
	 * 同一个端口是否支持多连接
	 */
	private String whetherMultiConnection = Constants.SUPPORT_MULTI_CONNECTION;

	/**
	 * 服务端Channel，负责监听
	 */
	private Channel serverChannel;

	/**
	 * 服务端业务处理器
	 */
	private ChannelHandler serverChannelHandlerInitializer;

	/**
	 * 关闭服务
	 */
	public void close() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		logger.info("Stopped Netty Tcp Server: " + localHost + ":" + localPort);
	}

	/**
	 * 初始化并启动监听
	 */
	public void init() {

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);

		serverBootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		serverBootstrap.handler(new LoggingHandler(LogLevel.DEBUG));

		serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
				ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
					@Override
					public void channelInactive(ChannelHandlerContext ctx) throws Exception {
						InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
						logger.info("连接被关闭：" + remoteInetSocketAddress.getAddress().getHostAddress() + ":"
								+ remoteInetSocketAddress.getPort() + " -> " + localHost + ":" + localPort);
						if (Constants.NOT_SUPPORT_MULTI_CONNECTION.equals(whetherMultiConnection)) {
							// 连接断开，开启绑定监听
							doBind();
						}
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
						logger.info("被连接成功：" + remoteInetSocketAddress.getAddress().getHostAddress() + ":"
								+ remoteInetSocketAddress.getPort() + " -> " + localHost + ":" + localPort);
						if (Constants.NOT_SUPPORT_MULTI_CONNECTION.equals(whetherMultiConnection)) {
							// 连接建立，取消绑定监听
							unBind();
						}
					}
				}).addLast(serverChannelHandlerInitializer);
			}
		});

		doBind();
	}

	/**
	 * 执行绑定，开启监听
	 */
	protected void doBind() {
		logger.debug("start to listening: " + localHost + ":" + localPort);
		serverBootstrap.bind(localHost, localPort).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture bindChannelFuture) throws Exception {
				if (bindChannelFuture.isSuccess()) {
					// 绑定成功，设置服务端监听Channel
					serverChannel = bindChannelFuture.channel();
					logger.info("start listening successfully: " + localHost + ":" + localPort);
				} else {
					logger.error("failed in starting listening : " + localHost + ":" + localPort,
							bindChannelFuture.cause());
					// 失败重试
					bindChannelFuture.channel().eventLoop().schedule(new Runnable() {
						@Override
						public void run() {
							doBind();
						}
					}, Constants.RE_BIND_INTERVAL, TimeUnit.SECONDS);
				}
			}
		});
	}

	/**
	 * 解除绑定，取消监听
	 */
	protected void unBind() {
		logger.debug("stop to listening: " + localHost + ":" + localPort);
		serverChannel.close().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture closeChannelFuture) throws Exception {
				if (closeChannelFuture.isSuccess()) {
					logger.info("stop listening successfully: " + localHost + ":" + localPort);
				} else {
					logger.error("failed in stopping listening: " + localHost + ":" + localPort,
							closeChannelFuture.cause());
				}
			}
		});
	}

	public String getLocalHost() {
		return localHost;
	}

	public void setLocalHost(String localHost) {
		this.localHost = localHost;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}

	public ChannelHandler getServerChannelHandlerInitializer() {
		return serverChannelHandlerInitializer;
	}

	public void setServerChannelHandlerInitializer(ChannelHandler serverChannelHandlerInitializer) {
		this.serverChannelHandlerInitializer = serverChannelHandlerInitializer;
	}

	public String getWhetherMultiConnection() {
		return whetherMultiConnection;
	}

	public void setWhetherMultiConnection(String whetherMultiConnection) {
		this.whetherMultiConnection = whetherMultiConnection;
	}

}
