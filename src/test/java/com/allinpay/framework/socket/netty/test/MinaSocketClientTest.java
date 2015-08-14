package com.allinpay.framework.socket.netty.test;

import java.io.IOException;

import org.junit.Test;

import com.allinpay.framework.socket.mina.MinaClientHandler;
import com.allinpay.framework.socket.mina.MinaSocketClient;

public class MinaSocketClientTest {

	/**
	 * 
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void TestMinaSocketClient() throws IOException, InterruptedException {

		MinaSocketClient minaSocketClient = new MinaSocketClient("localhost", 9988);
		minaSocketClient.setIoHandler(new MinaClientHandler());
		minaSocketClient.init();
		Thread.sleep(20000*1000);
	}

}
