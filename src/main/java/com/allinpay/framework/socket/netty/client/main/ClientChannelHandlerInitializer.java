package com.allinpay.framework.socket.netty.client.main;

import com.allinpay.framework.socket.netty.AsciiLengthFieldBasedFrameDecoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ClientChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

	/**
	 * 心跳时间间隔，单位：秒，小于0则无心跳
	 */
	private int hearbeatInterval = 10;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {

		ClientHeartbeatHandler clientHeartbeatHandler = new ClientHeartbeatHandler();
		clientHeartbeatHandler.setHeartBeatRequestSent("ping");
		ch.pipeline().addLast(new IdleStateHandler(0, hearbeatInterval, 0)).addLast(clientHeartbeatHandler)
				.addLast(new AsciiLengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4)).addLast(new ClientBizHandler());

	}

}
