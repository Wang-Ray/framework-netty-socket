package com.allinpay.io.commonshttpclient;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

public class HttpsTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Protocol easyhttps = new Protocol("https", (ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		HttpClient client = new HttpClient();
		PostMethod httpget = new PostMethod("https://103.22.255.193/payment/main");
		try {
			System.out.println(client.executeMethod(httpget));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
