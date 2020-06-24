package com.ehelpy.brihaspati4.authenticate;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function gives access to the any text file included in the package as per requirement
public class properties_access {
    @SuppressWarnings("finally")
    public static String read_property(String File_name, String Property_name) {

        Properties prop = new Properties();
        InputStream input = null;
        String Return_str = null;
        try {

            input = new FileInputStream(File_name);
            //  input = getClass().getResourceAsStream("/"+File_name);

            // load a properties file
            prop.load(input);
            Return_str = prop.getProperty(Property_name);
            // get the property value and print it out
            //System.out.println(prop.getProperty("database"));
            //System.out.println(prop.getProperty("dbuser"));
            //System.out.println(prop.getProperty("dbpassword"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return Return_str;
        }

    }
    public static int read_debuglevel(String File_name, String Property_name) {

        Properties prop = new Properties();
        InputStream input = null;
        String Return_str = null;
        int level = 0 ;
        try {

            input = new FileInputStream(File_name);

            // load a properties file
            prop.load(input);
            Return_str = prop.getProperty(Property_name);
            level = Integer.parseInt(Return_str.trim());


        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return level;
    }
}
