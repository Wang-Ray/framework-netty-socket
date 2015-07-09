package com.allinpay.framework.socket.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClient {

	private static final Logger logger = LoggerFactory
			.getLogger(TcpClient.class);

	/**
	 * 远程服务端地址
	 */
	private String remoteServerHost;

	/**
	 * 远程服务端端口
	 */
	private int remoteServerPort;

	/**
	 * 心跳消息
	 */
	private String heartbeatMessage = "0000";

	/**
	 * 心跳时间间隔，单位：秒
	 */
	private int hearbeatInterval = 60;

	/**
	 * 重连时间间隔，单位：毫秒
	 */
	private long reConnnectInterval = 60 * 1000L;

	private volatile EventLoopGroup workerGroup;

	private volatile Bootstrap bootstrap;

	private ChannelFuture channelFuture;

	public TcpClient(String remoteHost, int remotePort) {
		this.remoteServerHost = remoteHost;
		this.remoteServerPort = remotePort;
	}

	public void close() {
		workerGroup.shutdownGracefully();
	}

	public void init() {
		workerGroup = new NioEventLoopGroup();
		bootstrap = new Bootstrap();
		bootstrap.group(workerGroup);
		bootstrap.channel(NioSocketChannel.class);

		bootstrap.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) throws Exception {
				ch.pipeline()
						// 定时重连
						.addLast(new ChannelHandlerAdapter() {
							@Override
							public void channelInactive(
									ChannelHandlerContext ctx) throws Exception {
								logger.error("连接被关闭：" + getServerInfo());
								scheduleConnect();
							}
						})
						// 下面两个handler用于心跳
						.addLast(new IdleStateHandler(0, 0, hearbeatInterval))
						.addLast(new HeartbeatHandler(heartbeatMessage))
						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new StringDecoder())
						.addLast(new StringEncoder())
						.addLast(new ClientBizHandler());
			}
		});

		doConnect();
	}

	private void doConnect() {
		logger.info("开始连接：" + getServerInfo());
		channelFuture = bootstrap.connect(new InetSocketAddress(
				remoteServerHost, remoteServerPort));

		channelFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					logger.info("连接成功：" + getServerInfo());
				} else {
					logger.error("连接失败：" + getServerInfo() + "，原因：" + f.cause());
					// 启动连接失败时定时重连
					scheduleConnect();
				}
			}
		});
	}

	/**
	 * 定时重连
	 */
	private void scheduleConnect() {
		workerGroup.schedule(new Runnable() {
			@Override
			public void run() {
				doConnect();
			}
		}, reConnnectInterval, TimeUnit.MILLISECONDS);
	}

	private String getServerInfo() {
		return String.format("%s:%d", remoteServerHost, remoteServerPort);
	}

	public EventLoopGroup getWorkerGroup() {
		return workerGroup;
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	public ChannelFuture getChannelFuture() {
		return channelFuture;
	}

	public Channel getChannel() {
		return channelFuture.channel();
	}

	public String getRemoteServerHost() {
		return remoteServerHost;
	}

	public void setRemoteServerHost(String remoteServerHost) {
		this.remoteServerHost = remoteServerHost;
	}

	public int getRemoteServerPort() {
		return remoteServerPort;
	}

	public void setRemoteServerPort(int remoteServerPort) {
		this.remoteServerPort = remoteServerPort;
	}

	public String getHeartbeatMessage() {
		return heartbeatMessage;
	}

	public void setHeartbeatMessage(String heartbeatMessage) {
		this.heartbeatMessage = heartbeatMessage;
	}

	public int getHearbeatInterval() {
		return hearbeatInterval;
	}

	public void setHearbeatInterval(int hearbeatInterval) {
		this.hearbeatInterval = hearbeatInterval;
	}

	public long getReConnnectInterval() {
		return reConnnectInterval;
	}

	public void setReConnnectInterval(long reConnnectInterval) {
		this.reConnnectInterval = reConnnectInterval;
	}

}
