package com.cloudrive.common;

import com.cloudrive.common.interfaces.TransferCommon;

import java.io.Serializable;

public class AuthMessage implements Serializable, TransferCommon {
    public String login;
    public String passsword;

    public AuthMessage(String login, String passsword) {
        this.login = login;
        this.passsword = passsword;
    }

    @Override
    public TransferObjectType getType() {
        return TransferObjectType.AUTH;
    }
}
