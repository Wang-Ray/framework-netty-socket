package com.allinpay.framework.socket.netty.test;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.netty.ClientBizHandler;
import com.allinpay.framework.socket.netty.TcpClient;

public class TestTcpClient {
	@Test
	public void TestExceptionReconnect() throws IOException,
			InterruptedException {

		TcpClient client = new TcpClient("127.0.0.1", 8080);

		client.setHearbeatInterval(3);
		client.setReConnnectInterval(3 * 1000);

		client.init();
		
		while(true){
			Thread.sleep(2000);
			
			client.getChannel().writeAndFlush(
					"3333" + System.getProperty("line.separator"));
			System.out.println("发送3333");
		}
		

//		System.in.read();
	}
}
