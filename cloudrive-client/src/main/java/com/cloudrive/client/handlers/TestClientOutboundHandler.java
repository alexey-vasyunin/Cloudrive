package com.cloudrive.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TestClientOutboundHandler extends ChannelOutboundHandlerAdapter {

    private int num;

    public TestClientOutboundHandler(int num) {
        super();
        this.num = num;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("[" + this.getClass().getSimpleName().toString() + num + "]" + msg.getClass().getSimpleName().toString());
        ctx.write(msg);
        ctx.flush();
    }
}
