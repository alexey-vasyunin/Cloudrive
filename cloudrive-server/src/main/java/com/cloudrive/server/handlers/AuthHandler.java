package com.cloudrive.server.handlers;

import com.cloudrive.common.AuthMessage;
import com.cloudrive.common.TransferObjectType;
import com.cloudrive.common.handlers.FilterHandlerInbound;
import com.cloudrive.common.handlers.FilterHandlerOutbound;
import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.server.AuthDB;
import com.cloudrive.server.UserProps;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class AuthHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Auth Inbound" + msg.getClass().toString());
        if (((TransferCommon) msg).getType() == TransferObjectType.AUTH) {
            String storagename = AuthDB.getInstance().checkUser((AuthMessage) msg);
            if (storagename != null) {
                ctx.pipeline()
                        .addLast(new PartOfFileInboundHandler(new UserProps(storagename)))
                        .addLast(new DirListInboundHandler(new UserProps(storagename)))
                        .remove(this); // Мы авторизовались и хендлер авторизации более не нужен
            } else {
                System.out.println("Wrong username or password!");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
