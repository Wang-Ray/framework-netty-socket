/**
 * 
 */
package com.allinpay.framework.socket.netty;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

/**
 * @author dgod
 * 
 */
public class AsciiLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

	/**
	 * @param maxFrameLength
	 * @param lengthFieldOffset
	 * @param lengthFieldLength
	 * @param lengthAdjustment
	 * @param initialBytesToStrip
	 */
	public AsciiLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
			int lengthAdjustment, int initialBytesToStrip) {
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, false);
	}

	/**
	 * 解析长度字段（ASCII编码）
	 */
	protected long getUnadjustedFrameLength(ByteBuf buf, int offset, int length, ByteOrder order) {
		byte[] len = new byte[length];
		buf.getBytes(offset, len);
		return Long.parseLong(new String(len));
	}
}
