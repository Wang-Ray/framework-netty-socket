package com.allinpay.io.framework.netty.socket;

public class Constants {
	/**
	 * 同一个端口不支持多连接
	 */
	public static final String NOT_SUPPORT_MULTI_CONNECTION = "0";

	/**
	 * 同一个端口支持多连接
	 */
	public static final String SUPPORT_MULTI_CONNECTION = "1";

	/**
	 * 重新监听间隔时间
	 */
	public static final int RE_BIND_INTERVAL = 1;
}
