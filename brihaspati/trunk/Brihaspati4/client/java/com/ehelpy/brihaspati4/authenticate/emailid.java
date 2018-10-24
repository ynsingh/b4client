package com.ehelpy.brihaspati4.authenticate;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class emailid {
	private static String email_id = null;
	private static X509Certificate owncert = null;
	public static String getemaild() {
		try {
			owncert = ReadVerifyCert.returnClientCert();
			if(owncert!=null) {
			Matcher m = Pattern.compile("[a-zA-Z0-90_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(owncert.toString());
       	 while (m.find()) 
       	{
       		email_id = m.group().toString(); 
       	}// get own email id
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return email_id;
	}

}
