package com.ehelpy.brihaspati4.authenticate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function convert String formatted X.509 Certificate into its original form
//It is able to convert the same either into a Certificate chain or individual certificate
public class ConvertStringCertToX509 {
    public static X509Certificate[] convertToX509Certarray(String certEntry) throws IOException {

        InputStream in = null;
        X509Certificate cert = null;
        try {

            debug_level.debug(1,"tHE RECIEVED CERT FOR CONVERSION IS:   "+certEntry);
            byte[] certEntryBytes = Base64.getDecoder().decode(certEntry);
            int i = certEntryBytes.length;
            debug_level.debug(0,"the byte length" + i);
            //for(int j =0; j<i; j++)
            //System.out.println("I am converting cert from string to X509 format. THe converted byte format cert is:   "+certEntryBytes[j]);

            in = new ByteArrayInputStream(certEntryBytes);
            CertificateFactory certFactory = null;
            certFactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) certFactory.generateCertificate(in);

        } catch (CertificateException ex) {
            ex.getMessage();
            return new X509Certificate[] {};

        } finally {
            if (in != null) {
                in.close();
            }
        }
        //System.out.println("I am converting cert from string to X509 format. THe converted cert is:   "+cert);
        // return cert;
        return new X509Certificate[] {cert};
    }
    public static X509Certificate convertToX509Cert(String certEntry) throws IOException {

        InputStream in = null;
        X509Certificate cert = null;
        try {

            CertificateFactory certFactory = null;
            certFactory = CertificateFactory.getInstance("X.509");
            byte[] certEntryBytes = Base64.getEncoder().encode(certEntry.getBytes());
            System.out.println("I am converting cert from string to X509 format. THe converted byte format cert is:   "+certEntryBytes);

            in = new ByteArrayInputStream(certEntryBytes);

            cert = (X509Certificate) certFactory.generateCertificate(in);
            System.out.println("I am converting cert from string to X509 format. THe converted cert is:   "+cert);
            PublicKey pubkey = cert.getPublicKey();
            System.out.println(pubkey);
            return cert;
        } catch (CertificateException ex) {
            ex.getMessage();
            //return new X509Certificate[]{};

        } finally {
            if (in != null) {
                in.close();
            }
        }
        //System.out.println("I am converting cert from string to X509 format. THe converted cert is:   "+cert);
        // return cert;
        return cert;
    }

}
