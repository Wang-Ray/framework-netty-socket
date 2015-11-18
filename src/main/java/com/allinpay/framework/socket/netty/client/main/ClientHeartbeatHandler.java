package com.allinpay.framework.socket.netty.client.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class ClientHeartbeatHandler extends ChannelHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ClientHeartbeatHandler.class);

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
	private Object heartBeatResponseSent;

	private boolean forwardEvent;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent e = (IdleStateEvent) evt;
			if (e.state() == IdleState.READER_IDLE) {
				logger.info("read idle.");
				ctx.close();
			} else if (e.state() == IdleState.WRITER_IDLE) {
				logger.info("write idle.");
				if (null != heartBeatRequestSent) {
					logger.info("发送心跳请求消息：" + heartBeatRequestSent);
					ctx.writeAndFlush(heartBeatRequestSent);
				}
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
		if (msg.equals(heartBeatResponseReceived)) {
			// 接收到心跳响应消息
			logger.info("接收到心跳响应消息：" + heartBeatResponseReceived);
		} else if (msg.equals(heartBeatRequestReceived)) {
			// 接收到心跳请求消息
			logger.info("接收到心跳请求消息：" + heartBeatRequestReceived);
			if (null != heartBeatResponseSent) {
				// 发送心跳响应消息
				ctx.writeAndFlush(msg);
				logger.info("发送心跳响应消息：" + heartBeatResponseSent);
			}
		} else {
			// 非心跳相关消息
			ctx.fireChannelRead(msg);
		}
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

	public Object getHeartBeatResponseSent() {
		return heartBeatResponseSent;
	}

	public void setHeartBeatResponseSent(Object heartBeatResponseSent) {
		this.heartBeatResponseSent = heartBeatResponseSent;
	}

	public boolean isForwardEvent() {
		return forwardEvent;
	}

	public void setForwardEvent(boolean forwardEvent) {
		this.forwardEvent = forwardEvent;
	}

}
