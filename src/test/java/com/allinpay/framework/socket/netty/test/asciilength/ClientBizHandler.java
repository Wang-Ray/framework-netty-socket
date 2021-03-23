package com.allinpay.framework.socket.netty.test.asciilength;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientBizHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ClientBizHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		String msg = "0016QUERY TIME ORDER";
		ByteBuf firstMessage = Unpooled.copiedBuffer(msg.getBytes());
		// firstMessage.writeShort(16);
		ctx.writeAndFlush(firstMessage);
		logger.info("client send：" + msg);
		ctx.fireChannelActive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		logger.info("client received: " + new String(req));

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.warn("发送异常：" + cause);
		// 释放资源
		ctx.close();
	}
}
