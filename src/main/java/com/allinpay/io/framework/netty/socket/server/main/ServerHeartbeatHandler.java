package com.allinpay.io.framework.netty.socket.server.main;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ServerHeartbeatHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ServerHeartbeatHandler.class);

	/**
	 * 发送心跳请求消息
	 */
	private Object heartBeatRequestSent;
	/**
	 * 接收心跳响应消息
	 */
	private Object heartBeatResponseReceived;

	/**
	 * 接收心跳请求消息
	 */
	private Object heartBeatRequestReceived;
	/**
	 * 发送心跳响应消息
	 * 
	 */
	private String heartBeatResponseSent;

	private boolean forwardEvent;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				InetSocketAddress remoteInetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
				InetSocketAddress localInetSocketAddress = (InetSocketAddress) ctx.channel().localAddress();

				logger.warn(new StringBuilder(42).append("读空闲，关闭连接：")
						.append(remoteInetSocketAddress.getAddress().getHostAddress()).append(":")
						.append(remoteInetSocketAddress.getPort()).append(" -> ")
						.append(localInetSocketAddress.getAddress().getHostAddress()).append(":")
						.append(localInetSocketAddress.getPort()).toString());
				ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				logger.info(ctx.channel().isActive() + "");
				logger.info("write idle.");
				// if (null != heartBeatRequestSent) {
				// logger.info("发送心跳请求消息：" + heartBeatRequestSent);
				// ctx.writeAndFlush(Unpooled.copiedBuffer(((String)heartBeatRequestSent).getBytes())).addListener(new
				// ChannelFutureListener() {
				// public void operationComplete(ChannelFuture f) throws
				// Exception {
				// if (f.isSuccess()) {
				// logger.info("发送成功");
				// } else {
				// logger.error("发送失败",f.cause());
				// }
				// }
				// });
				// }
			} else {
				logger.info("all idle.");
				if (null != heartBeatRequestSent) {
					logger.info("发送心跳请求消息：" + heartBeatRequestSent);
					ctx.writeAndFlush(heartBeatRequestSent);
				}
			}
		}

		if (forwardEvent) {
			ctx.fireUserEventTriggered(evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.getBytes(0,req);
		String body = new String(req);
		if (body.equals(heartBeatResponseReceived)) {
			buf.release();
			// 接收到心跳响应消息
			logger.info("接收到心跳响应消息：" + heartBeatResponseReceived);
		} else if (body.equals(heartBeatRequestReceived)) {
			buf.release();
			// 接收到心跳请求消息
			logger.info("接收到心跳请求消息：" + heartBeatRequestReceived);
			if (null != heartBeatResponseSent) {
				// 发送心跳响应消息
				ByteBuf resp = Unpooled.copiedBuffer((stuffString(heartBeatResponseSent.length() + "", 4, true, '0') + heartBeatResponseSent).getBytes());
				ctx.writeAndFlush(resp);
				logger.info("发送心跳响应消息：" + heartBeatResponseSent);
			}
		} else {
			// 非心跳相关消息
			ctx.fireChannelRead(msg);
		}
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

	public Object getHeartBeatRequestSent() {
		return heartBeatRequestSent;
	}

	public void setHeartBeatRequestSent(Object heartBeatRequestSent) {
		this.heartBeatRequestSent = heartBeatRequestSent;
	}

	public Object getHeartBeatResponseReceived() {
		return heartBeatResponseReceived;
	}

	public void setHeartBeatResponseReceived(Object heartBeatResponseReceived) {
		this.heartBeatResponseReceived = heartBeatResponseReceived;
	}

	public Object getHeartBeatRequestReceived() {
		return heartBeatRequestReceived;
	}

	public void setHeartBeatRequestReceived(Object heartBeatRequestReceived) {
		this.heartBeatRequestReceived = heartBeatRequestReceived;
	}

	public String getHeartBeatResponseSent() {
		return heartBeatResponseSent;
	}

	public void setHeartBeatResponseSent(String heartBeatResponseSent) {
		this.heartBeatResponseSent = heartBeatResponseSent;
	}

	public boolean isForwardEvent() {
		return forwardEvent;
	}

	public void setForwardEvent(boolean forwardEvent) {
		this.forwardEvent = forwardEvent;
	}

}
