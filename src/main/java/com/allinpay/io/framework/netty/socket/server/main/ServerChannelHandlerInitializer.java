package com.allinpay.io.framework.netty.socket.server.main;

import com.allinpay.io.framework.netty.socket.AsciiLengthFieldBasedFrameDecoder;

import com.allinpay.io.framework.netty.socket.AsciiLengthFieldPrepender;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class ServerChannelHandlerInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 心跳时间间隔，单位：秒，小于0则无心跳
     */
    private int hearbeatInterval = 0;

    private int lengthFieldLength = 4;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ServerHeartbeatHandler serverHeartbeatHandler = new ServerHeartbeatHandler();
        serverHeartbeatHandler.setHeartBeatRequestReceived("0000");
        ch.pipeline().addLast(new IdleStateHandler(hearbeatInterval, 0, 0))
                .addLast(new AsciiLengthFieldBasedFrameDecoder(99999999, 0, lengthFieldLength, 0, lengthFieldLength)).addLast(serverHeartbeatHandler)
                .addLast(new AsciiLengthFieldPrepender(lengthFieldLength))
                .addLast(new ServerBizHandler());
    }

    public int getHearbeatInterval() {
        return hearbeatInterval;
    }

    public void setHearbeatInterval(int hearbeatInterval) {
        this.hearbeatInterval = hearbeatInterval;
    }

    public int getLengthFieldLength() {
        return lengthFieldLength;
    }

    public void setLengthFieldLength(int lengthFieldLength) {
        this.lengthFieldLength = lengthFieldLength;
    }
}
