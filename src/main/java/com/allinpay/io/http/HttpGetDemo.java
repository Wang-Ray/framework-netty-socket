package com.allinpay.io.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Angi on 14-1-26.
 */
public class HttpGetDemo {
    public static void main(String[] args) throws Exception {

        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String getURL = "http://www.baidu.com/s?wd=" + URLEncoder.encode("马尔代夫", "utf-8");
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) getUrl.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream(), "utf-8"));// 设置编码,否则中文乱码
        System.out.println("=============================");
        System.out.println("Contents of get request");
        System.out.println("=============================");
        String lines;
        while ((lines = reader.readLine()) != null) {
            // lines = new String(lines.getBytes(), "utf-8");
            System.out.println(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        System.out.println("=============================");
        System.out.println("Contents of get request ends");
        System.out.println("=============================");

    }
}
