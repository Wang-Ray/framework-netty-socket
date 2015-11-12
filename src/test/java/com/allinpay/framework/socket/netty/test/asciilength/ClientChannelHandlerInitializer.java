package com.allinpay.framework.socket.netty.test.asciilength;

import com.allinpay.framework.socket.netty.AsciiLengthFieldBasedFrameDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new AsciiLengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4)).addLast(new ClientBizHandler());

	}

}
