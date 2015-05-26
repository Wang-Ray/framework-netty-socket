package com.allinpay.io.socket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaServerHandler extends IoHandlerAdapter {

	Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);

	// 当一个连接建立时
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		session.write("server says：我来啦........");
		logger.debug("connected: " + session.getRemoteAddress());
	}

	// 当一个连接关闭时
	@Override
	public void sessionClosed(IoSession session) {
		// 已经无法发送消息了，所以下面这句无效
		// session.write("server says：我走啦........");
		logger.debug("disconnect!" + session.getRemoteAddress());
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		logger.debug("connection error:" + session.getRemoteAddress());
	}

	// 当客户端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String s = (String) message;
		logger.debug(s);
		// 测试将消息回送给客户端
		if (count == 1)
			return;
		session.write(s + count);
		count++;
	}

	private int count = 0;
}