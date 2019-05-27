package com.ehelpy.brihaspati4.authenticate;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//This program was last modified by Lt Col Ankit Singhal on 15 May 2019
public class emailid {
    private static String email_id = null;
    private static X509Certificate owncert = null;
    private static int c=0;
    public static String getemaild() {
        try {
            owncert = ReadVerifyCert.returnClientCert();
            if(owncert!=null) {
                Matcher m = Pattern.compile("[a-zA-Z0-90_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(owncert.toString());
                while (m.find())
                {
                	if (c==1) { return email_id;}
                	email_id = m.group().toString();
                	c=c+1;
                } 
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return email_id;
    }

}
