package com.ehelpy.brihaspati4.authenticate;

import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class emailid {
    private static String email_id = null;
    private static X509Certificate owncert = null;
    private static int c=0;
    public static String getemaild() {
        try {
            owncert = ReadVerifyCert.returnClientCert();
            debug_level.debug(0,"new certificate of node is " + owncert);
            if(owncert!=null) {
                Matcher m = Pattern.compile("[a-zA-Z0-90_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(owncert.toString());
                while (m.find())
                {
                	if (c==1) { return email_id;}
                	email_id = m.group().toString();
                	//debug_level.debug(0,"my email is =" + email_id);
                	c=c+1;
                }// get own email id
                //debug_level.debug(0,"counter is =" + c ); 
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return email_id;
    }

}
