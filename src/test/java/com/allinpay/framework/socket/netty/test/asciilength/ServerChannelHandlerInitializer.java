package com.allinpay.framework.socket.netty.test.asciilength;

import com.allinpay.io.framework.netty.socket.AsciiLengthFieldBasedFrameDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class ServerChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast(new AsciiLengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
				.addLast(new ServerBizHandler("0000"));

	}

}
