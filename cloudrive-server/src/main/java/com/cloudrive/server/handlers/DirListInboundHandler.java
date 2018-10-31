package com.cloudrive.server.handlers;

import com.cloudrive.common.*;
import com.cloudrive.common.interfaces.TransferCommand;
import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.server.CloudriveServer;
import com.cloudrive.server.UserProps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;

public class DirListInboundHandler extends ChannelInboundHandlerAdapter {

    private UserProps user;

    public DirListInboundHandler(UserProps user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (((TransferCommon) msg).getType() == TransferObjectType.COMMAND && ((TransferCommand) msg).getCommandType() == TransferCommandType.GETDIRLIST) {
            ctx.writeAndFlush(new DirMessage(CloudriveServer.STORAGE_PATH + File.separator + user.storagename ));
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
