package com.cloudrive.server.handlers;
import com.cloudrive.common.*;

import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.server.CloudriveServer;
import com.cloudrive.server.UserProps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PartOfFileInboundHandler extends ChannelInboundHandlerAdapter {

    private UserProps user;

    public PartOfFileInboundHandler(UserProps user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {

        if (((TransferCommon)obj).getType() == TransferObjectType.FILE) {
            // Если прилетела часть какого то файла
            PartOfFileMessage pf = (PartOfFileMessage)obj;

            Path p = Paths.get(CloudriveServer.STORAGE_PATH + File.separator + user.storagename + File.separator + pf.getFilename());

            if (!Files.exists(p)) Files.createFile(p);

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw");) {
                raf.seek(pf.getOffset());
                raf.write(pf.getPartFile());
            }

        } else {
            ctx.fireChannelRead(obj);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
