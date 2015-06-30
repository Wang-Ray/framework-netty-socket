package com.allinpay.framework.socket.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientBizHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(ClientBizHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.debug("exceptionCaught");
		// 释放资源
		logger.warn("Unexpected exception from downstream : "
				+ cause.getMessage());
		ctx.close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelRegistered");
		ctx.fireChannelRegistered();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		logger.debug("channelActive");
		ctx.writeAndFlush(Unpooled.copiedBuffer(("2222" + System
				.getProperty("line.separator")).getBytes()));
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelInactive");
		ctx.fireChannelInactive();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		logger.debug("channelRead");
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.debug("channelReadComplete");
		// 继续传递
		ctx.fireChannelReadComplete();
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
			throws Exception {
		logger.debug("userEventTriggered");
		ctx.fireUserEventTriggered(evt);

	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx)
			throws Exception {
		logger.debug("channelWritabilityChanged");
		ctx.fireChannelWritabilityChanged();
	}

	@Override
	@Skip
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress,
			SocketAddress localAddress, ChannelPromise promise)
			throws Exception {
		logger.debug("connect");
		ctx.connect(remoteAddress, localAddress, promise);

	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		logger.debug("disconnect");
		ctx.disconnect();
	}

	// 跳过
	@Skip
	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise)
			throws Exception {
		logger.debug("close");
		ctx.close(promise);
	}

	@Override
	public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.debug("flush");
		ctx.flush();
	}

}
