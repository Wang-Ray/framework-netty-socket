package com.allinpay.framework.socket.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.TimeUnit;

public final class TcpServer {
	private volatile EventLoopGroup bossGroup;

	private volatile EventLoopGroup workerGroup;

	private volatile ServerBootstrap bootstrap;

	private volatile boolean closed = false;

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
	private String heartbeatMessage;

	/**
	 * 是否答复心跳
	 */
	private boolean whetherReturnHeartbeat;

	public TcpServer(String localHost, int localPort) {
		this.localHost = localHost;
		this.localPort = localPort;
	}

	public void close() {
		closed = true;

		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();

		System.out.println("Stopped Tcp Server: " + localPort);
	}

	public void init() {
		closed = false;

		bossGroup = new NioEventLoopGroup();
		workerGroup = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup);

		bootstrap.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.handler(new LoggingHandler(LogLevel.DEBUG));

		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG))
						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new StringDecoder())
						.addLast(new ServerBizHandler(heartbeatMessage));
			}
		});

		doBind();
	}

	protected void doBind() {
		if (closed) {
			return;
		}

		bootstrap.bind(localHost, localPort).addListener(
				new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f)
							throws Exception {
						if (f.isSuccess()) {
							System.out.println("Started Tcp Server: "
									+ localPort);
						} else {
							System.out.println("Started Tcp Server Failed: "
									+ localPort);

							f.channel().eventLoop().schedule(new Runnable() {

								@Override
								public void run() {
									doBind();
								}

							}, 1, TimeUnit.SECONDS);
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

}
