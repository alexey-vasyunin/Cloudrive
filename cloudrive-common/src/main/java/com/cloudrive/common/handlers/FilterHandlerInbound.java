package com.cloudrive.common.handlers;


import com.cloudrive.common.interfaces.TransferCommon;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Пропускаем, только то, что реализует интерфейс TransferCommon
 */
public class FilterHandlerInbound extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransferCommon) {
            System.out.println("Filter Inbound" + msg.getClass().toString());
            ctx.fireChannelRead(msg);
        }

    }
}
