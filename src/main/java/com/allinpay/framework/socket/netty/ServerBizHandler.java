package com.allinpay.framework.socket.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerBizHandler extends ChannelHandlerAdapter {

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
		logger.debug("channelRead");
		String message = (String) msg;
		logger.debug(message);
		if (heartbeatMessage.startsWith("1111")) {
			ctx.close();
		}
	}

}
