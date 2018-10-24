package com.ehelpy.brihaspati4.authenticate ;

import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//this code forward the newly generated certificate to the identity server for Authentication
// It also carries out the OTP verification for verification of Email Id

public class CertificateSignature {
    @SuppressWarnings("static-access")
    public static  boolean certsign(X509Certificate cert) throws Exception {



        // send cert for signing to server
        //inside cert sign function- get email id from cert at server side
        //send OTP to user email ,ask for OTP from user ,verify OTP
        //generate node id - hex 160 bit - at server
        //sign cert,add node id
        //return back to client

        boolean certsign = false;

        debug_level.debug(1, "Welcome to CertificateSignature .Sending new cert to Iden Server for sign");
        //System.out.println("recieved certificate for signatue is     :"+ cert);
        String certstring=cert.toString();
        byte[] certbyte = cert.getEncoded();
        String certstringbyte = new String(Base64.getEncoder().encode(certbyte));


        debug_level.debug(1,"String format of recieved certificate for signatue is     :"+certstring);

        String mserverurl ="http://172.20.82.6:8080/b4server";
        //String mserverurl ="http://localhost:8080/b4server";
        //example-String url = "http://example.com/query?q=" + URLEncoder.encode(q, "UTF-8");
        String MSrequrl = mserverurl +"/ProcessRequest?req=sscccertsign&cert=" + URLEncoder.encode(certstring, "UTF-8");
        debug_level.debug(1,MSrequrl);
        createConnection http = new createConnection();
        createotpConnection http_2 = new createotpConnection();
        //System.out.println("Testing 1 - Send Http GET request");
        boolean server1 = http.sendGet(MSrequrl);

        //System.out.println("\nTesting 2 - Send Http POST request");
        try {
            http.sendPost(MSrequrl);
        } catch(Exception e) {
            System.exit(0);
        }
        PrivateKey priv_client = GenerateCertificate.priv();
        System.out.println("Private key is   " + priv_client);

        if(server1) {
            String OTP = Gui.getotp();
            String MSrequrl1 = mserverurl +"/otp_verification?req=otpverify&OTP=" + URLEncoder.encode(OTP, "UTF-8") + "&cert=" + URLEncoder.encode(certstring, "UTF-8")+"&certstringbyte="+URLEncoder.encode(certstringbyte, "UTF-8");
            http_2.sendGet(MSrequrl1);
            X509Certificate[] Certs	= new  X509Certificate[2];
            Certs = http_2.sendPost(MSrequrl1);
            String alias1 = Gui.getaliasname();
            String password	=	Gui.getkeystorepass();
            //String path = properties_access.read_property("client.properties","home" );

            char[] keyPass = password.toCharArray();
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null,keyPass );
            keyStore.setKeyEntry(alias1, priv_client, keyPass, Certs);
            FileOutputStream fos = new FileOutputStream("SignedClientKeyStore.JKS");
            keyStore.store(fos, keyPass);
            GenerateCertificate.saveKeyStore(keyStore,"KeyStore.JKS", password);
            debug_level.debug(1,"new certs and signature saved to keystore");
            certsign = true;
            debug_level.debug(1, "all the certificats and signature has been saved to a file and certificate signature routine completed ");

        }
        return certsign;
    }
}

