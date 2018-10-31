package com.cloudrive.client.handlers;

import com.cloudrive.client.AppSettings;
import com.cloudrive.common.PartOfFileMessage;
import com.cloudrive.common.TransferObjectType;
import com.cloudrive.common.interfaces.TransferCommon;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DownloadFileInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("DownloadFileInboundHandler");
        if (((TransferCommon)msg).getType() == TransferObjectType.FILE) {
            PartOfFileMessage pf = (PartOfFileMessage)msg;

            Path p = Paths.get(AppSettings.getInstance().getPath() + File.separator + pf.getFilename());

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw");) {
                raf.seek(pf.getOffset());
                raf.write(pf.getPartFile());
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
