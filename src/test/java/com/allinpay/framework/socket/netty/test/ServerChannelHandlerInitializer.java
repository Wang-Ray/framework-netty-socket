package com.allinpay.framework.socket.netty.test;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class ServerChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast().addLast(new LineBasedFrameDecoder(1024)).addLast(new StringDecoder())
				.addLast(new ServerBizHandler("0000"));

	}

}
