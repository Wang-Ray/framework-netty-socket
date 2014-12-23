package com.allinpay.io.httpclient;

import static org.junit.Assert.assertEquals;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

public class HttpClientTrustingAllCertsTest {

    @Test
    public void shouldAcceptUnsafeCerts() throws Exception {
        DefaultHttpClient httpclient = httpClientTrustingAllSSLCerts();
        HttpGet httpGet = new HttpGet("https://ssodev.test.allinpay.com:8443/sso/login");
        HttpResponse response = httpclient.execute( httpGet );
        assertEquals("HTTP/1.1 200 OK", response.getStatusLine().toString());
    }

    public static DefaultHttpClient httpClientTrustingAllSSLCerts() throws NoSuchAlgorithmException, KeyManagementException {
        DefaultHttpClient httpclient = new DefaultHttpClient();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, getTrustingManager(), new java.security.SecureRandom());

        SSLSocketFactory sSLSocketFactory = new SSLSocketFactory(sc,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        Scheme scheme = new Scheme("https", 443, sSLSocketFactory);
        httpclient.getConnectionManager().getSchemeRegistry().register(scheme);
        return httpclient;
    }

    public static TrustManager[] getTrustingManager() {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // Do nothing
            }

        } };
        return trustAllCerts;
    }
}