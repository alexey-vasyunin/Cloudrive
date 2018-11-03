package com.cloudrive.common;

import com.cloudrive.common.interfaces.TransferCommand;
import com.cloudrive.common.interfaces.TransferCommon;

import java.io.Serializable;

public class Command implements TransferCommon, TransferCommand, Serializable {

    private TransferCommandType command;
    private String[] params;

    public TransferObjectType getType() {
        return TransferObjectType.COMMAND;
    }

    public TransferCommandType getCommandType() {
        return command;
    }

    public Command(TransferCommandType command) {
        this.command = command;
    }

    public Command(TransferCommandType command, String... params) {
        this.command = command;
        this.params = params;
    }

    public boolean isReadyToTransfer(){
        return command != null;
    }

    public String[] getParams() {
        return params;
    }
}
