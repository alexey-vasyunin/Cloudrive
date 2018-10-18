package com.cloudrive.server;

import com.cloudrive.common.AuthMessage;
import org.postgresql.PGStatement;

import java.sql.*;

public class AuthDB {
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/cloudrive";
    private static final String USER = "postgres";
    private static final String PASS = "11111";

    private Connection connection;


    private static AuthDB instance = new AuthDB();

    public static AuthDB getInstance() {
        return instance;
    }

    private AuthDB() {
        // Подключаем к БД
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            this.connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    public String checkUser(AuthMessage msg){

        try {
            PreparedStatement ps = connection.prepareStatement("SELECT storagename FROM users WHERE email=? AND password=? AND active=true");
            ps.setString(1, msg.email);
            ps.setString(2, msg.passsword);
            ResultSet result = ps.executeQuery();
            if (result.next()){
                System.out.println("User record has found");
                return result.getString("storagename");
            } else {
                System.out.println("User record has not found");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
