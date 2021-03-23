package com.allinpay.io.framework.netty.socket.server.main;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class ServerBizHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ServerBizHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// logger.debug("channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req);
		logger.info("server received and replyed the same: " + body);

//		ByteBuf resp = Unpooled.copiedBuffer((stuffString(body.length() + "", 4, true, '0') + body).getBytes());
		ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
		ctx.writeAndFlush(resp);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
		InetSocketAddress localInetSocketAddress = (InetSocketAddress) ctx.channel().localAddress();

		StringBuilder sb = new StringBuilder();
		sb.append("异常发生，关闭连接：").append(remoteInetSocketAddress.getAddress().getHostAddress()).append(":")
				.append(remoteInetSocketAddress.getPort()).append(" -> ")
				.append(localInetSocketAddress.getAddress().getHostAddress()).append(":")
				.append(localInetSocketAddress.getPort());
		logger.warn(sb.length() + "");
		logger.warn(sb.toString(), cause);
		// 断开连接
		ctx.close();
	}

	/**
	 * 把字符串src填充到len长度，填充的字符串为padding，填充方向为：当stuffHead为true时 填充到src头部，否则填充到尾部.
	 * 
	 * @param src
	 * @param len
	 * @param stuffHead
	 * @param padding
	 */
	public static String stuffString(String src, int len, boolean stuffHead, char padding) {
		if (len <= 0) {
			return src;
		}
		if (null == src) {
			src = "";
		}
		int srcLen = src.length();
		StringBuffer buf = new StringBuffer(len);
		int paddingLen = len - srcLen;
		for (int i = 0; i < paddingLen; i++) {
			buf.append(padding);
		}
		if (stuffHead) {
			buf.append(src);
		} else {
			buf.insert(0, src);
		}
		return buf.toString();
	}
}
