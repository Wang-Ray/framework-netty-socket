package com.allinpay.io.https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/*
 * 证书过滤器
 */
public class MyX509TrustManager implements X509TrustManager {
	/**
	 * 该方法体为空时信任所有客户端证书
	 * 
	 * @param chain
	 * @param authType
	 * @throws java.security.cert.CertificateException
	 */
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	/**
	 * 该方法体为空时信任所有服务器证书
	 * 
	 * @param chain
	 * @param authType
	 * @throws java.security.cert.CertificateException
	 */

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	/**
	 * 返回信任的证书
	 * 
	 * @return
	 */
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
