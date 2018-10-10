package com.cloudrive.server;
import com.cloudrive.common.*;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.Arrays;

public class PartOfFileInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof PartOfFileMessage){
            // Если прилетела часть какого то файла
            PartOfFileMessage pf = (PartOfFileMessage)obj;

            System.out.println(pf.getFilename());
            System.out.println(pf.getCount());
            System.out.println(pf.getOffset());
            System.out.println(pf.getPartSize());
            System.out.println(pf.getType());

            Path p = Paths.get(CloudriveServer.STORAGE_PATH + "\\" + pf.getFilename());

            System.out.println(p.toString());
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
