package com.ehelpy.brihaspati4.authenticate ;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JPasswordField;

//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function includes all the GUI being called in the package

public class Gui {

	
	//private static String passwordValue,keystorealias;
 
	public static String getkeystorepass(){
  
		JLabel jPassword = new JLabel("Please enter your keystore password.");
		JPasswordField password = new JPasswordField();
		Object[] ob = {jPassword, password};
		int result = JOptionPane.showConfirmDialog(null, ob, "Keystore password ", JOptionPane.OK_CANCEL_OPTION);

		String passwordValue = null;
		if (result == JOptionPane.OK_OPTION) {
  		char[] passwordValues = password.getPassword();
        //Here is some validation code
        passwordValue=String.valueOf(passwordValues);
         
		}  
		if (result == JOptionPane.CANCEL_OPTION) {
			System.exit(0);
		}
		return passwordValue;    
	}
	
	public static String getemailid(){		  
		  // create a jframe
		  JLabel frame = new JLabel("EmailID");
		  // prompt the user to enter their email id as common name. 
		  //email id is extracted later at server and used to send OTP for verification.
		  String commonname = JOptionPane.showInputDialog(frame, "Please enter your Email-Id");
		  return commonname;
	    
	}
	
	public static String getorganizationalunit(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter Org Unit");
		     
		  // prompt the user to enter their org unit
		  String organizationalunit = JOptionPane.showInputDialog(frame, "Please enter your organizational unit");
		  return organizationalunit;
	    
	}
	
	public static String getorganization(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter Organization");
		     
		  // prompt the user to enter their organisation
		  String organization = JOptionPane.showInputDialog(frame, "Please enter your organization");
		  return organization;
	    
	}	
	
	public static String getcity(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter City");		     
		  // prompt the user to enter their city
		  String city = JOptionPane.showInputDialog(frame, "Please enter your city");
		  return city;	    
	}	
	
	public static String getstate(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter State");		     
		  // prompt the user to enter their city
		  String state = JOptionPane.showInputDialog(frame, "Please enter your state");
		  return state;	    
	}
	
	public static String getcountry(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter Country");		     
		  // prompt the user to enter their country
		  String country = JOptionPane.showInputDialog(frame, "Please enter your country");
		  return country;	    
	}	
	
	/*public static String getemailid(){		  
		  // create a jframe
		  JFrame frame = new JFrame("Enter emailid");		     
		  // prompt the user to enter their country
		  String emailid = JOptionPane.showInputDialog(frame, "Please enter your emailid");
		  return emailid ;	    
	}*/
	
// new modified method to take keystore location from user,inspite of taking from config
public static String getkeystorelocation(){
	  
	  
	  // create a jframe
	  JFrame frame = new JFrame("Enter keystore location");
	     
	  // prompt the user to enter their keystore location
	  String keystorelocation = JOptionPane.showInputDialog(frame, "Please enter your keystore location");
	  System.out.println(keystorelocation);
	  return keystorelocation;
    
}

public static String getaliasname(){
  
  // create a jframe
  JFrame frame = new JFrame("Alias name");
     
  // prompt the user to enter their keystorepass
  String keystorealias = JOptionPane.showInputDialog(frame, "Please enter new alias name");
  return keystorealias;
}
     
public static void showMessageDialogBox(String msg){

 // create a jframe
JFrame frame = new JFrame("Message");

//show a joptionpane dialog using showMessageDialog
JOptionPane.showMessageDialog(frame,msg);  
}
public static String getotp(){		  
	  // create a jframe
	  JLabel frame = new JLabel("OTP");
	     
	  // prompt the user to enter their email id as common name. 
	  //email id is extracted later at server and used to send OTP for verification.
	  String otp = JOptionPane.showInputDialog(frame, "Please Enter the OTP recd");
	  return otp;
  
}
public static String getcalleremailid(){		  
	  // create a jframe
	  JLabel frame = new JLabel("EmailID");
	  // prompt the user to enter their email id as common name. 
	  //email id is extracted later at server and used to send OTP for verification.
	  String commonname = JOptionPane.showInputDialog(frame, "Please enter  Email-Id to be connected");
	  return commonname;
  
}
public static String getip_address(){		  
	  // create a jframe
	  JLabel frame = new JLabel("IP Address");
	  // prompt the user to enter their email id as common name. 
	  //email id is extracted later at server and used to send OTP for verification.
	  String commonname = JOptionPane.showInputDialog(frame, "Please enter  IP Address of the Destination");
	  return commonname;

}
	public static String connect(){
	//get emailid from user
	String usr_name = javax.swing.JOptionPane.showInputDialog("Do u want to connect ");
	
	    //return;
	    
	
	    return usr_name;
	}
}


