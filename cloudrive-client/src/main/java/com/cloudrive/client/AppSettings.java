package com.cloudrive.client;

import java.io.*;

public class AppSettings implements Serializable {
    private static final long serialVersionUID = 313415604036352714L;
    private static AppSettings ourInstance = new AppSettings();

    public static AppSettings getInstance() {
        return ourInstance;
    }


    private String path;
    private boolean connectOnStart;
    private boolean startOnSystem;
    private String email;
    private String password;

    String configFilename = System.getProperty("user.home") + File.separator + "cloudrive.cfg";

    private boolean isLoaded;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isConnectOnStart() {
        return connectOnStart;
    }

    public void setConnectOnStart(boolean connectOnStart) {
        this.connectOnStart = connectOnStart;
    }

    public boolean isStartOnSystem() {
        return startOnSystem;
    }

    public void setStartOnSystem(boolean startOnSystem) {
        this.startOnSystem = startOnSystem;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    private AppSettings() {
        // Инициализируем на случай если загрузка конфига не случится
        email = "";
        password = "";
        startOnSystem = false;
        connectOnStart = false;
        path = "";
        isLoaded = load();
    }

    public boolean save(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFilename))){
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("Saved to " + configFilename);
        return true;
    }


    private boolean load(){
        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(configFilename))){
            AppSettings client = (AppSettings) oos.readObject();
            this.path = client.getPath();
            this.email = client.getEmail();
            this.connectOnStart = client.isConnectOnStart();
            this.startOnSystem = client.isStartOnSystem();
            this.password = client.getPassword();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
