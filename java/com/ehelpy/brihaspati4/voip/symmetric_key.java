package com.ehelpy.brihaspati4.voip;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class symmetric_key {
    private static String s1,s2,s3=null;
    private static long symkey = 0 ;
    public static long longkey(long ownkey, long farendkey) {
        s1 = String.valueOf(ownkey) ;
        s2 = String.valueOf(farendkey);
        s3 =s1 + s2;
        symkey = Long.valueOf(s3);
        debug_level.debug(0,"the symmetric key is" + symkey);
        return symkey;
    }
    public static SecretKey sym_keygenr(long input) {
        byte[] sym_byte_key = longtobyte_bytetolongarray.longtobytearraysymkey(input);
        SecretKey secret_Key = new SecretKeySpec(sym_byte_key, "AES");
        System.out.println("secret key is " + secret_Key );
        return secret_Key;
    }

}
