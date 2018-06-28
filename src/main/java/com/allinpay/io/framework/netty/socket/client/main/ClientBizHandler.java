package com.allinpay.io.framework.netty.socket.client.main;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientBizHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ClientBizHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws DecoderException {
		String msg = "FFF4FFFD06";
		ByteBuf resp = Unpooled.copiedBuffer(msg.getBytes());

		logger.info("client sent: " + msg);
		ChannelFuture channelFuture = ctx.writeAndFlush(resp);
		channelFuture.addListener(new ChannelFutureListener() {
			public void operationComplete(ChannelFuture f) throws Exception {
				if (f.isSuccess()) {
					logger.info("发送成功");
				} else {
					logger.error("发送失败");
				}
			}
		});
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// logger.debug("channelRead");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req);
		logger.info("client received: " + body);

//		 ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
//		 ctx.writeAndFlush(resp);
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
