package com.allinpay.io.https;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class MyHostnameVerifier implements HostnameVerifier {

	/**
	 * 返回true时为通过认证 当方法体为空时，信任所有的主机名
	 * 
	 * @param hostname
	 * @param session
	 * @return
	 */
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}