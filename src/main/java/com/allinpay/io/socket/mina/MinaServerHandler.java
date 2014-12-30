package com.allinpay.io.socket.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class MinaServerHandler extends IoHandlerAdapter {

	// 当一个客端端连结进入时
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		System.out.println("incomming client: " + session.getRemoteAddress());
	}

	// 当一个客户端关闭时
	@Override
	public void sessionClosed(IoSession session) {
		System.out.println("one client disconnect!");
	}

	// 当客户端发送的消息到达时:
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		// 我们己设定了服务器解析消息的规则是一行一行读取,这里就可转为String:
		String s = (String) message;
		// Write the received data back to remote peer
		System.out.println("收到客户机发来的消息: " + s);
		// 测试将消息回送给客户端
		if (count == 5)
			return;
		session.write(s + count);
		count++;
	}

	private int count = 0;
}