package com.allinpay.framework.socket.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpServer {

	private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

	public static final String NOT_SUPPORT_MULTI_CONNECTION = "0";

	public static final String SUPPORT_MULTI_CONNECTION = "1";

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private ServerBootstrap bootstrap;

	/**
	 * 服务端监听本地地址
	 */
	private String localHost;

	/**
	 * 服务端监听本地端口
	 */
	private int localPort;

	/**
	 * 心跳
	 */
	private String heartbeatMessage = "0000";

	/**
	 * 是否答复心跳
	 */
	private boolean whetherReturnHeartbeat;

	private String whetherMultiConnection = SUPPORT_MULTI_CONNECTION;

	/**
	 * 服务端Channel，负责监听
	 */
	private Channel serverChannel;

	private ChannelHandler serverChannelHandler;

	public TcpServer(String localHost, int localPort) {
		this.localHost = localHost;
		this.localPort = localPort;
	}

	public void close() {

		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();

		logger.info("Stopped Tcp Server: " + localPort);
	}

	public void init() {

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new ChannelHandlerAdapter() {
					@Override
					public void channelInactive(ChannelHandlerContext ctx) throws Exception {
						InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
						logger.info("连接被关闭：" + remoteInetSocketAddress.getAddress().getHostAddress() + ":"
								+ remoteInetSocketAddress.getPort() + " -> " + localHost + ":" + localPort);
						if (NOT_SUPPORT_MULTI_CONNECTION.equals(whetherMultiConnection)) {
							// 连接断开，开启绑定监听
							doBind();
						}
					}

					@Override
					public void channelActive(ChannelHandlerContext ctx) throws Exception {
						InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
						logger.info("被连接成功：" + remoteInetSocketAddress.getAddress().getHostAddress() + ":"
								+ remoteInetSocketAddress.getPort() + " -> " + localHost + ":" + localPort);
						if (NOT_SUPPORT_MULTI_CONNECTION.equals(whetherMultiConnection)) {
							// 连接建立，取消绑定监听
							unBind();
						}
					}
				}).addLast(serverChannelHandler);
			}
		});

		doBind();
	}

	/**
	 * 执行绑定，开启监听
	 */
	protected void doBind() {
		logger.debug("start to listening: " + localHost + ":" + localPort);
		bootstrap.bind(localHost, localPort).addListener(new ChannelFutureListener() {
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
					}, 1, TimeUnit.SECONDS);
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

	public String getHeartbeatMessage() {
		return heartbeatMessage;
	}

	public void setHeartbeatMessage(String heartbeatMessage) {
		this.heartbeatMessage = heartbeatMessage;
	}

	public ChannelHandler getServerChannelHandler() {
		return serverChannelHandler;
	}

	public void setServerChannelHandler(ChannelHandler serverChannelHandler) {
		this.serverChannelHandler = serverChannelHandler;
	}

}
