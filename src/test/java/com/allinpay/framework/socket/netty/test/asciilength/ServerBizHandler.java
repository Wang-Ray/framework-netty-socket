package com.allinpay.framework.socket.netty.test.asciilength;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerBizHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ServerBizHandler.class);

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
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		logger.info("server received: " + new String(req));

		String respString = "0016REPLY TIME ORDER";
		ByteBuf resp = Unpooled.copiedBuffer(respString.getBytes());
		logger.info("server send" + resp);
		ctx.writeAndFlush(resp);

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("发生异常：" + cause);
		// 释放资源
		ctx.close();
	}
}
