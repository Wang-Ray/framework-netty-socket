package com.allinpay.io.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketClient {

	public static void main(String[] args) {
		String s = null;
		Socket socketClient = null;
		DataInputStream in = null;
		DataOutputStream out = null;

		try {
			socketClient = new Socket("localhost", 4331);
			in = new DataInputStream(socketClient.getInputStream());
			out = new DataOutputStream(socketClient.getOutputStream());

			out.writeUTF("Hello, Server!");

			while (true) {
				s = in.readUTF();
				System.out.println("SocketClient received: " + s);
				out.writeUTF("" + Math.random());
				Thread.sleep(5000);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
