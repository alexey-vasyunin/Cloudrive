package com.cloudrive.server.handlers;

import com.cloudrive.common.*;
import com.cloudrive.common.interfaces.TransferCommand;
import com.cloudrive.common.interfaces.TransferCommon;
import com.cloudrive.server.CloudriveServer;
import com.cloudrive.server.UserProps;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {
    private UserProps user;
    private ChannelHandlerContext ctx;

    public ServerInboundHandler(UserProps user) {
        this.user = user;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.ctx = ctx;
        if (msg instanceof TransferCommon) {
            switch (((TransferCommon) msg).getType()){
                case FILE: partOfFile((PartOfFileMessage) msg); break;
                case COMMAND: commandSwitcher(msg); break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void commandSwitcher(Object msg){
        switch (((TransferCommand)msg).getCommandType()){
            case GETDIRLIST: dirList(); break;
            case FILEREQUEST: requestFile((PartOfFileRequest)msg); break;
            case RENAME: renameFile(((Command)msg).getParams()); break;
            case DELETE: deleteFile(((Command)msg).getParams());
        }
    }

    private void deleteFile(String... params){
        if (params.length <= 0) return;
        File f = getPath(params[0]).toFile();
        if (f.exists() && f.delete()) dirList();
    }

    private void renameFile(String... params){
        if (params.length < 1) return;
        Path source = getPath(params[0]);

        try {
            Files.move(source, source.resolveSibling(params[1]));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dirList();
        }

    }

    private void partOfFile(PartOfFileMessage msg) throws Exception {
            Path p = getPath(msg.getFilename());

            if (!Files.exists(p)) Files.createFile(p);

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw");) {
                raf.seek(msg.getOffset());
                raf.write(msg.getPartFile());
            }

            if (msg.isLastPart()) dirList();
    }

    private void dirList() {
            ctx.writeAndFlush(new DirMessage(CloudriveServer.STORAGE_PATH + File.separator + user.storagename ));
    }

    private void requestFile(PartOfFileRequest pof) {
        // Если это запрос на часть файла с сервера
            Path p = getPath(pof.filename);

            if (!Files.exists(p)) return;

            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "r")){
                byte[] buf = new byte[Settings.PART_FILE_SIZE];
                raf.seek(pof.offset);
                int hasRead = raf.read(buf, 0, pof.lenght);
                ctx.writeAndFlush(new PartOfFileMessage(
                        pof.filename,
                        (hasRead == buf.length) ? buf : Arrays.copyOf(buf, hasRead),
                        0,
                        0,
                        pof.offset,
                        Settings.PART_FILE_SIZE,
                        0
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private Path getPath(String filename){
        return Paths.get(CloudriveServer.STORAGE_PATH + File.separator + user.storagename + File.separator + filename);
    }
}
