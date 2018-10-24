package com.ehelpy.brihaspati4.authenticate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function convert String formatted X.509 Certificate into its original form
// It is able to convert the same either into a Certificate chain or individual certificate
public class convertcerttox509 {
	public static X509Certificate[] convertToX509Certarray(String certEntry) throws IOException {
		 				
        InputStream in = null;
        X509Certificate cert = null;
        try {
        	
        	//System.out.println("tHE RECIEVED CERT FOR CONVERSION IS:   "+certEntry);
            byte[] certEntryBytes = Base64.getDecoder().decode(certEntry);
            //for(int j =0; j<i; j++)
            //System.out.println("I am converting cert from string to X509 format. THe converted byte format cert is:   "+certEntryBytes[j]);
            
            in = new ByteArrayInputStream(certEntryBytes);
            debug_level.debug(1, "I am converting cert from string to X509 format. THe converted byte array stream  format cert is:   "+in);
            
            CertificateFactory certFactory = null;
			certFactory = CertificateFactory.getInstance("X.509"); 
            cert = (X509Certificate) certFactory.generateCertificate(in);
            System.out.println("I am converting cert from string to X509 format. THe converted cert is:   "+cert);
            //PublicKey pubkey = cert.getPublicKey();
            debug_level.debug(1, "Certificate to signature routine is completed ");
			
        } catch (CertificateException ex) {
        	ex.getMessage();
        	return new X509Certificate[]{};
 
        } finally {
            if (in != null) {
                    in.close();
            }
        }
        //System.out.println("I am converting cert from string to X509 format. THe converted cert is:   "+cert);
       // return cert;
        return new X509Certificate[]{cert};
    }
	public static X509Certificate convertToX509Cert(String certEntry) throws IOException {
			
        InputStream in = null;
        X509Certificate cert = null;
        try {
        	byte[] certEntryBytes = Base64.getDecoder().decode(certEntry);
        	debug_level.debug(1, "tHE RECIEVED CERT FOR CONVERSION IS:   "+certEntry);
        	
            
            in = new ByteArrayInputStream(certEntryBytes);
            
            CertificateFactory certFactory = null;
			certFactory = CertificateFactory.getInstance("X.509"); 
            cert = (X509Certificate) certFactory.generateCertificate(in);
            debug_level.debug(1,"I am converting cert from string to X509 format. THe converted cert is:   "+cert);
            
        } catch (CertificateException ex) {
        	ex.getMessage();
        	//return new X509Certificate[]{};
 
        } finally {
            if (in != null) {
                    in.close();
            }
        }
        return cert;
    }

	
}
