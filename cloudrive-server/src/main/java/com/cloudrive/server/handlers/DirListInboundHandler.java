package com.cloudrive.server.handlers;

import com.cloudrive.common.*;
import com.cloudrive.common.interfaces.TransferCommand;
import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.server.CloudriveServer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DirListInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (((TransferCommon) msg).getType() == TransferObjectType.COMMAND && ((TransferCommand) msg).getCommandType() == TransferCommandType.GETDIRLIST) {
            System.out.println("GetDisrList");
            ctx.write(new DirMessage(CloudriveServer.STORAGE_PATH));
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
