package com.cloudrive.client.handlers;

import com.cloudrive.client.AppSettings;
import com.cloudrive.client.Client;
import com.cloudrive.common.DirMessage;
import com.cloudrive.common.PartOfFileMessage;
import com.cloudrive.common.interfaces.TransferCommon;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerInboundHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof TransferCommon) {
            switch (((TransferCommon)msg).getType()){
                case DIRLIST: RemoteDirList((DirMessage)msg); break;
                case FILE: downloadFile((PartOfFileMessage)msg); break;
            }

        }
    }


    private void RemoteDirList(DirMessage msg) throws Exception {
            Platform.runLater(() -> Client.getInstance().getMainController().refreshFileList(msg.getFiles()));
    }

    private void downloadFile(PartOfFileMessage msg) throws Exception {
            Path p = Paths.get(AppSettings.getInstance().getPath() + File.separator + msg.getFilename());
            try (RandomAccessFile raf = new RandomAccessFile(p.toFile(), "rw")) {
                raf.seek(msg.getOffset());
                raf.write(msg.getPartFile());
            }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
