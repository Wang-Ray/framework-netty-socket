package com.allinpay.io.framework.netty.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Ascii长度域添加编码器
 *
 * @author angi
 */
public class AsciiLengthFieldPrepender extends MessageToMessageEncoder<ByteBuf> {

    private final int lengthFieldLength;

    public AsciiLengthFieldPrepender(int lengthFieldLength) {
        this.lengthFieldLength = lengthFieldLength;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        int length = msg.readableBytes();
        out.add(ctx.alloc().buffer(lengthFieldLength).writeBytes(Utils.stuffString(String.valueOf(length), lengthFieldLength, true, '0').getBytes()));
        out.add(msg.retain());
    }
}
