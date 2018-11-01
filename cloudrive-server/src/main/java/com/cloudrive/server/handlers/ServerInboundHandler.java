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

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    UserProps user;
    ChannelHandlerContext ctx;

    public ServerInboundHandler(UserProps user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        if (msg instanceof TransferCommon) {
            switch (((TransferCommon) msg).getType()){
                case FILE: partOfFile((PartOfFileMessage) msg); break;
                case COMMAND: dirList((TransferCommand) msg); requestFile(msg); break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void partOfFile(PartOfFileMessage msg) throws Exception {
            Path p = Paths.get(CloudriveServer.STORAGE_PATH + File.separator + user.storagename + File.separator + msg.getFilename());

            if (!Files.exists(p)) Files.createFile(p);

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw");) {
                raf.seek(msg.getOffset());
                raf.write(msg.getPartFile());
            }
    }

    private void dirList(TransferCommand msg) throws Exception {
        if (msg.getCommandType() == TransferCommandType.GETDIRLIST) {
            ctx.writeAndFlush(new DirMessage(CloudriveServer.STORAGE_PATH + File.separator + user.storagename ));
        }
    }

    private void requestFile(Object msg) throws Exception {
        // Если это запрос на часть файла с сервера
        if (((TransferCommand) msg).getCommandType() == TransferCommandType.FILEREQUEST) {
            PartOfFileRequest pof = (PartOfFileRequest)msg;
            Path p = Paths.get(CloudriveServer.STORAGE_PATH + File.separator + user.storagename + File.separator + pof.filename);

            if (!Files.exists(p)) return;

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r")){
                byte[] buf = new byte[Settings.PART_FILE_SIZE];
                raf.seek(pof.offset);
                int hasRead = raf.read(buf, 0, pof.lenght);
                ctx.writeAndFlush(new PartOfFileMessage(
                        pof.filename,
                        (hasRead == Settings.PART_FILE_SIZE) ? buf : Arrays.copyOf(buf, hasRead),
                        0,
                        pof.offset,
                        Settings.PART_FILE_SIZE,
                        0
                ));
            }
        }
    }
}
