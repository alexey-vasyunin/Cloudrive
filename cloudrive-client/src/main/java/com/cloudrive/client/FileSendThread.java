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
        System.out.println("parts: " + parts);
        byte[] partbytes = new byte[Settings.PART_FILE_SIZE];
        for (int i = 0; i < parts; i++) {
            System.out.println("i: " + i);
            try {
                System.out.println("" + i + " " + i * Settings.PART_FILE_SIZE + " " + this.hashCode());
                RandomAccessFile raf = new RandomAccessFile(file.getAbsoluteFile(), "r");
                raf.seek(i * Settings.PART_FILE_SIZE);
                int lenght = raf.read(partbytes, 0, Settings.PART_FILE_SIZE);
                System.out.println("Length" + lenght);
                // Создаем и отправляем объект на сервер
                channel.writeAndFlush(
                        new PartOfFileMessage(
                                file.getName(),
                                Arrays.copyOf(partbytes, lenght),
                                i,
                                i * Settings.PART_FILE_SIZE,
                                lenght,
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

            // пока мы не умеем получать из отдельного треда обратную связь о получении
            // того или иного объекта (но упорно думаем над этим), то делаем небольшую
            // паузу что бы конвеер успевал хоть немного разгузиться
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public FileSendThread(File file, Channel channel) {
        this.file = file;
        this.channel = channel;
    }
}
