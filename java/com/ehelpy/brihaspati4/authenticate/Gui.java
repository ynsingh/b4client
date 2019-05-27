package com.ehelpy.brihaspati4.authenticate ;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
//Last modified by Lt Col Ankit Singhal Dated 26 MAY 2019 ; 1530 Hrs
//This function includes all the GUI being called in the package

public class Gui {
	//private static String keystorealias = null;
    public static String getkeystorepass() throws Exception {
    	String msg1 = "ALERT: Remember KEYSTORE PASSWORD.";
    	showMessageDialogBox(msg1);
       	JPanel panel = new JPanel();
    	JLabel jPassword = new JLabel("PASSWORD");
        JPasswordField password = new JPasswordField(20);
        panel.add(jPassword);
        panel.add(password);
        Object[] options = {"OK", "CANCEL"};
        int result = JOptionPane.showOptionDialog(null, panel, "KEYSTORE PASSWORD ",JOptionPane.OK_CANCEL_OPTION,
        			 JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String passwordValue = null;
        if (result == 0) {
            char[] passwordValues = password.getPassword();
            passwordValue=String.valueOf(passwordValues);
        }
        if (result == 1) {
        	ReadVerifyCert.loadKeyStore(null,null);
        }
        return passwordValue; 
    }
    public static String getaliasname() {
    	JFrame frame = new JFrame("Alias Name");
        String keystorealias = JOptionPane.showInputDialog(frame, "Please enter new Alias Name");
        return keystorealias;
    }
    public static String getemailid() {        
        JLabel frame = new JLabel("EmailID");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter your Email-Id");
        return commonname;
    }
    public static String getorganizationalunit() {
        JFrame frame = new JFrame("Enter Org Unit");
        String organizationalunit = JOptionPane.showInputDialog(frame, "Please enter your Organizational Unit");
        return organizationalunit;
    }
    public static String getorganization() {
        JFrame frame = new JFrame("Enter Organization");
        String organization = JOptionPane.showInputDialog(frame, "Please enter your Organization");
        return organization;
    }
    public static String getcity() {
        JFrame frame = new JFrame("Enter City");
        String city = JOptionPane.showInputDialog(frame, "Please enter your City");
        return city;
    }
    public static String getstate() {
    	JFrame frame = new JFrame("Enter State");
        String state = JOptionPane.showInputDialog(frame, "Please enter your State");
        return state;
    }
    public static String getcountry() {
        JFrame frame = new JFrame("Enter Country");
        String country = JOptionPane.showInputDialog(frame, "Please enter your Country");
        return country;
    }
    public static String getkeystorelocation() {
        JFrame frame = new JFrame("Enter keystore location");
        String keystorelocation = JOptionPane.showInputDialog(frame, "Please enter your keystore location");
        System.out.println(keystorelocation);
        return keystorelocation;
    }
    public static void showMessageDialogBox(String msg) {
        JFrame frame = new JFrame("Message");
        JOptionPane.showMessageDialog(frame,msg);
    }
    public static String getotp() {
        JLabel frame = new JLabel("OTP");
        String otp = JOptionPane.showInputDialog(frame, "Please Enter the OTP Recieved");
        return otp;
    }
    public static String getcalleremailid() {
        JLabel frame = new JLabel("EmailID");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter  Email-Id to be connected");
        return commonname;
    }
    public static String getip_address() {
        JLabel frame = new JLabel("IP Address");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter  IP Address of the Destination");
        return commonname;
    }
    public static String connect() {
        String usr_name = javax.swing.JOptionPane.showInputDialog("Do u want to connect ");
        return usr_name;
    }
}


