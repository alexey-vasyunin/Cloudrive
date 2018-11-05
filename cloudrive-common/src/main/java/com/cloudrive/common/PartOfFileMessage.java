package com.cloudrive.common;


import com.cloudrive.common.interfaces.FileMessage;
import com.cloudrive.common.interfaces.TransferCommon;

import java.io.Serializable;

public class PartOfFileMessage implements FileMessage, TransferCommon, Serializable {
    private static final long serialVersionUID = 2033056743230273977L;
    private byte[] part;
    private byte[] digest;
    private int count;
    private int lastcount;
    private long offset;
    private String filename;
    private int lenght;
    private int threadId; // Пригодится для поиска треда, что бы передать ему ответ от сервера

    public PartOfFileMessage(String filename, byte[] part, int count, int lastcount, long offset, int lenght, int threadId) {
        this.filename = filename;
        this.part = part;
        this.count = count;
        this.lastcount = lastcount;
        this.offset = offset;
        this.lenght = lenght;
        this.threadId = threadId;
    }

    public TransferObjectType getType() {
        return TransferObjectType.FILE;
    }

    public int getPartSize() {
        return part.length;
    }

    public int getCount() {
        return count;
    }

    public byte[] getPartFile() {
        return part;
    }

    public boolean isPacked() {
        return false;
    }

    public byte[] getDigest() {
        return digest;
    }

    public boolean isReadyToTransfer(){
        return count > -1 && part != null;
    }

    public void setPart(byte[] part) {
        this.part = part;
    }

    public void setDigest(byte[] digest) {
        this.digest = digest;
    }

    public long getOffset() {
        return offset;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isLastPart(){
        System.out.println(count + " # " + lastcount);
        return count == lastcount-1;
    }
}
