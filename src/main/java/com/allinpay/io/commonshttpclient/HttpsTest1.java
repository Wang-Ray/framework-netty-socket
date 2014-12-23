package com.allinpay.io.commonshttpclient;

import java.io.IOException;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;

public class HttpsTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);

		URI uri;
		try {
			uri = new URI("https://125.35.4.160/BOCCASH/merCheck!checkStatus", true);

			// use relative url only
			GetMethod httpget = new GetMethod(uri.getPathQuery());
			HostConfiguration hc = new HostConfiguration();
			hc.setHost(uri.getHost(), uri.getPort(), easyhttps);
			HttpClient client = new HttpClient();

			System.out.println(client.executeMethod(hc, httpget));

		} catch (URIException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
