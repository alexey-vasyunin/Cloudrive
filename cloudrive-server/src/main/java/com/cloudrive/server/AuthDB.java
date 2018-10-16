package com.cloudrive.server;

import com.cloudrive.common.AuthMessage;

public class AuthDB {
    private static AuthDB instance = new AuthDB();

    public static AuthDB getInstance() {
        return instance;
    }

    private AuthDB() {
    }

    public boolean checkUser(AuthMessage msg){
        return msg.login.equals("user1") && msg.passsword.equals("password1");
    }
}
