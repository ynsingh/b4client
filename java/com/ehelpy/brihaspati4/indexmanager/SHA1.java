package com.ehelpy.brihaspati4.indexmanager;

import java.math.BigInteger;
import java.security.MessageDigest;

import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class SHA1 {

    public static String getSha1(String value)
    {

        String sha1 = "";

        // With the java libraries
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes("utf8"));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String SHA1 =sha1.toUpperCase();
        //	SysOutCtrl.SysoutSet("hash of email id is :"+sha1);
        SysOutCtrl.SysoutSet("hash of email id is :"+SHA1,3);

        return SHA1;

        // With Apache commons
        //sha1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex( value );

//		System.out.println( "The sha1 of \""+ value + "\" is:");
//		System.out.println( sha1 );

    }
}
