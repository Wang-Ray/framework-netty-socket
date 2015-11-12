package com.allinpay.framework.socket.netty.test;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBizHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientBizHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush("2222" + System.getProperty("line.separator"));
		logger.info("发送消息：" + 2222);
		ctx.fireChannelActive();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("发送异常：" + cause);
		// 释放资源
		ctx.close();
	}
}
