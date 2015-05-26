package com.allinpay.io.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) {
		String s = null;
		ServerSocket serverSocket = null;
		// 一个Socket对象对应一个连接
		Socket socketServer = null;
		DataInputStream in = null;
		DataOutputStream out = null;

		try {
			serverSocket = new ServerSocket(4331);

			// 建立连接即完成Socket对象的创建
			socketServer = serverSocket.accept();

			in = new DataInputStream(socketServer.getInputStream());
			out = new DataOutputStream(socketServer.getOutputStream());

			while (true) {
				s = in.readUTF();
				System.out.println("SocketServer received: " + s);
				out.writeUTF("Hello, Client!");
				out.writeUTF("" + Math.random());
				Thread.sleep(5000);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
