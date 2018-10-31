package com.cloudrive.common;

import com.cloudrive.common.interfaces.TransferCommand;
import com.cloudrive.common.interfaces.TransferCommon;

import java.io.Serializable;

public class PartOfFileRequest implements TransferCommon, TransferCommand, Serializable {

    private static final long serialVersionUID = 1274699496487263275L;
    public String filename;
    public long offset;
    public int lenght;

    public PartOfFileRequest(String filename, long offset, int lenght) {
        this.filename = filename;
        this.offset = offset;
        this.lenght = lenght;
    }

    @Override
    public TransferObjectType getType() {
        return TransferObjectType.COMMAND;
    }

    @Override
    public TransferCommandType getCommandType() {
        return TransferCommandType.FILEREQUEST;
    }
}
