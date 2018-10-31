package com.cloudrive.server.handlers;

import com.cloudrive.common.*;
import com.cloudrive.common.interfaces.TransferCommand;
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
import java.util.Arrays;

public class RequestFileInboundHandler extends ChannelInboundHandlerAdapter {
    private UserProps user;

    public RequestFileInboundHandler(UserProps user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Если это запрос на часть файла с сервера
        System.out.println("RequestFileInboundHandler");
        if (((TransferCommon) msg).getType() == TransferObjectType.COMMAND && ((TransferCommand) msg).getCommandType() == TransferCommandType.FILEREQUEST) {
            PartOfFileRequest pof = (PartOfFileRequest)msg;
            Path p = Paths.get(CloudriveServer.STORAGE_PATH + File.separator + user.storagename + File.separator + pof.filename);

            if (!Files.exists(p)) return;

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r")){
                byte[] buf = new byte[Settings.PART_FILE_SIZE];
                raf.seek(pof.offset);
                int hasRead = raf.read(buf, 0, pof.lenght);
                ctx.writeAndFlush(new PartOfFileMessage(
                        pof.filename,
                        Arrays.copyOf(buf, hasRead),
                        0,
                        pof.offset,
                        Settings.PART_FILE_SIZE,
                        0
                ));
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
