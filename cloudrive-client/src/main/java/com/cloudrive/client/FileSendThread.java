package com.cloudrive.client;

import com.cloudrive.common.*;
import io.netty.channel.Channel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class FileSendThread extends Thread {

    private File file;
    private Channel channel;

    @Override
    public void run() {
        if (file.length() > Settings.MAX_FILE_SIZE) {
            System.out.println("Слишком большой файл. Максимальный размер файла " + Settings.MAX_FILE_SIZE + " байт");
            return;
        }

        int parts = (int)Math.ceil((double) file.length() / Settings.PART_FILE_SIZE );
        byte[] buf = new byte[Settings.PART_FILE_SIZE];
        for (int i = 0; i < parts; i++) {
            try {
                RandomAccessFile raf = new RandomAccessFile(file.getAbsoluteFile(), "r");
                raf.seek(i * Settings.PART_FILE_SIZE);
                int hasRead = raf.read(buf, 0, Settings.PART_FILE_SIZE);
                // Создаем и отправляем объект на сервер
                channel.writeAndFlush(
                        new PartOfFileMessage(
                                file.getName(),
                                (hasRead == buf.length) ? buf : Arrays.copyOf(buf, hasRead),
                                i,
                                i * Settings.PART_FILE_SIZE,
                                hasRead,
                                this.hashCode()
                        )
                );

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // Если файл вдруг исчез во время отправки, то удаляем то что отправили и выходим из отправки этого файла
                if (i != 0) channel.writeAndFlush(new Command(TransferCommandType.DELETE, file.getAbsolutePath()));
                return;
            } catch (IOException e) {
                //TODO Что то пошло не так, надо обработать
                e.printStackTrace();
            }
        }

    }

    public FileSendThread(File file, Channel channel) {
        this.file = file;
        this.channel = channel;
    }
}
