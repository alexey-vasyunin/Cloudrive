package com.cloudrive.common.handlers;

import com.cloudrive.common.interfaces.TransferCommon;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Пропускаем, только то, что реализует интерфейс TransferCommon
 */
public class FilterHandlerOutbound extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof TransferCommon) {
            System.out.println("Filter Outbound " + msg.getClass().toString());
            ctx.write(msg, promise);
            ctx.flush();
        }
    }
}
