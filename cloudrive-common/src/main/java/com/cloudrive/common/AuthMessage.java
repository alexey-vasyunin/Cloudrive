package com.cloudrive.common;

import com.cloudrive.common.interfaces.TransferCommon;

import java.io.Serializable;

public class AuthMessage implements Serializable, TransferCommon {
    public String email;
    public String passsword;

    public AuthMessage(String email, String passsword) {
        this.email = email;
        this.passsword = passsword;
    }

    @Override
    public TransferObjectType getType() {
        return TransferObjectType.AUTH;
    }
}
