package com.ehelpy.brihaspati4.mail.mailclient.storage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class test {
    public static void main(String args[]){
        ConfigData ob1=ConfigData.INSTANCE;
        String email=ob1.show("IMAPHOST");
        System.out.println(email);
    }
}

public enum ConfigData{
    INSTANCE;
    String line;
    public String show(String key){
        try (InputStream input = new FileInputStream("Config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            line= prop.getProperty(key);
            // System.out.println(prop.getProperty("Email"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }
};
