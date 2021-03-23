package com.allinpay.io.framework.netty.socket.client.main;

import io.netty.channel.*;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class ClientBizHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ClientBizHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws DecoderException {
        String msg = "FFF4FFFD06";
        ByteBuf resp = Unpooled.copiedBuffer(msg.getBytes());

        logger.info("client sent: " + msg);
        ChannelFuture channelFuture = ctx.writeAndFlush(resp);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture f) throws Exception {
                if (f.isSuccess()) {
                    logger.info("发送成功");
                } else {
                    logger.error("发送失败");
                }
            }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // logger.debug("channelRead");
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req);
        logger.info("client received: " + body);

//		 ByteBuf resp = Unpooled.copiedBuffer(body.getBytes());
//		 ctx.writeAndFlush(resp);
    }

}
