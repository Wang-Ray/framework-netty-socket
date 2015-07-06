package com.allinpay.framework.socket.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(HeartbeatHandler.class);

	public HeartbeatHandler(String heartbeatMessage) {
		this.heartbeatMessage = heartbeatMessage;
	}

	/**
	 * 心跳消息
	 */
	private String heartbeatMessage = "0000";

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.ALL_IDLE) {
				ctx.writeAndFlush(Unpooled.copiedBuffer((heartbeatMessage
                        + System.getProperty("line.separator")).getBytes()));
				logger.info("发送心跳消息：" + heartbeatMessage);
			}
		} else {
			ctx.fireUserEventTriggered(evt);
		}
	}

}
