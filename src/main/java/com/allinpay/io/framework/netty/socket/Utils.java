package com.allinpay.io.framework.netty.socket;

/**
 * 工具
 */
public class Utils {

    /**
     * 把字符串src填充到len长度，填充的字符串为padding，填充方向为：当stuffHead为true时 填充到src头部，否则填充到尾部.
     *
     * @param src       源串
     * @param len       长度
     * @param stuffHead 是否头部填充
     * @param padding   填充字符
     * @return 填充后的字符串
     */
    public static String stuffString(String src, int len, boolean stuffHead, char padding) {
        if (len <= 0) {
            return src;
        }
        if (null == src) {
            src = "";
        }
        int srcLen = src.length();
        StringBuffer buf = new StringBuffer(len);
        int paddingLen = len - srcLen;
        for (int i = 0; i < paddingLen; i++) {
            buf.append(padding);
        }
        if (stuffHead) {
            buf.append(src);
        } else {
            buf.insert(0, src);
        }
        return buf.toString();
    }
}
