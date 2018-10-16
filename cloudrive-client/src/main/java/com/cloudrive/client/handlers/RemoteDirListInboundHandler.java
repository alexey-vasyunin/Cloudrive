package com.cloudrive.client.handlers;

import com.cloudrive.client.Client;
import com.cloudrive.common.DirMessage;
import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.common.TransferObjectType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

public class RemoteDirListInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("RemoteDirListInboundHandler");
        if (((TransferCommon)msg).getType() == TransferObjectType.DIRLIST){
            System.out.println("RemoteDirListInboundHandler DirList");
            DirMessage dm = (DirMessage)msg;
            System.out.println("####" + Client.getInstance().getController());
            Platform.runLater(() -> Client.getInstance().getController().refreshFileList(dm.getFiles()));

         } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
