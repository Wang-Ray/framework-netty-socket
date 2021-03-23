package com.allinpay.framework.socket.netty.test;

import io.netty.channel.ChannelHandlerContext;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBizHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerBizHandler.class);

	public ServerBizHandler() {
	}

	public ServerBizHandler(String heartbeatMessage) {
		this.heartbeatMessage = heartbeatMessage;
	}

	/**
	 * 心跳消息
	 */
	private String heartbeatMessage = "0000";

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		String message = (String) msg;
		logger.debug("收到消息：" + message);
		if (heartbeatMessage.startsWith("1111")) {
			ctx.close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("发生异常：" + cause);
		// 释放资源
		ctx.close();
	}
}
