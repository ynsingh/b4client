package com.ehelpy.brihaspati4.authenticate;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
//This was last modified by Lt Col Ankit Singhal Dated 24 May 2019 ; 1600 Hrs
//This function convert String formatted X.509 Certificate into its original form
// It is able to convert the same either into a Certificate chain or individual certificate
public class convertcerttox509 {
    public static X509Certificate[] convertToX509Certarray(String certEntry) throws IOException {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        InputStream in = null;
        X509Certificate cert = null;
        try {
            byte[] certEntryBytes = Base64.getDecoder().decode(certEntry);
            in = new ByteArrayInputStream(certEntryBytes);
            debug_level.debug(1, "I am converting cert from string to X509 format. The converted byte array stream  format cert is:   "+in);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
            cert = (X509Certificate) certFactory.generateCertificate(in);
            System.out.println("I am converting cert from string to X509 format. The converted cert is:   "+cert);
            debug_level.debug(1, "Certificate to signature routine is completed ");
        } catch (CertificateException ex) {
            ex.getMessage();
            return new X509Certificate[] {};
        } finally {
            if (in != null) {
                in.close();
            }
        }
               return new X509Certificate[] {cert};
    }
    public static X509Certificate convertToX509Cert(String certEntry) throws IOException {
    	InputStream in = null;
        X509Certificate cert = null;
        try {
            byte[] certEntryBytes = Base64.getDecoder().decode(certEntry);
            debug_level.debug(1, "THE RECIEVED CERT FOR CONVERSION IS:   "+certEntry);
            in = new ByteArrayInputStream(certEntryBytes);
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509", new BouncyCastleProvider());
            cert = (X509Certificate) certFactory.generateCertificate(in);
            debug_level.debug(1,"I am converting cert from string to X509 format. The converted cert is:   "+cert);
        } catch (CertificateException ex) {
            ex.getMessage();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return cert;
    }
}
