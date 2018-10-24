package com.ehelpy.brihaspati4.authenticate;

import java.security.cert.X509Certificate;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//this code is verifies cert recd from any peer
// it carries out a check with respect to validity, signature of server certificate
public class certificate_verification {
    boolean status = true;// flag for certificate status
    static boolean flag = false;
    static X509Certificate Server_cert = null;
    public static boolean clientcert_verify(X509Certificate Client_cert) {
        try {
            Server_cert = ReadVerifyCert.returnServerCert();
            Client_cert.checkValidity();
            Client_cert.verify(Server_cert.getPublicKey());// verify the passed client cert with the server cert public key
            flag = true;
            debug_level.debug(0, "Certificate Verification routine completed");
            return flag;
        } catch (Exception e) {

            System.out.println("Certificate is not Valid");
            return flag;
            //e.printStackTrace();

        }
    }
}
