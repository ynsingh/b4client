package com.ehelpy.brihaspati4.authenticate ;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Base64;
//Last modified by Maj Dushyant Choudhary Dated 8 March 2020 ; 1800 Hrs
//This code forward the newly generated certificate to the identity server for Authentication and Sends the newly created keystore to the server for keystore recovery purposes
// It also carries out the OTP verification for verification of Email Id
public class CertificateSignature {
    @SuppressWarnings("static-access")
    public static  boolean certsign(X509Certificate cert) throws Exception 
    {
        // send cert for signing to server
        //inside cert sign function- get email id from cert at server side
        //send OTP to user email ,ask for OTP from user ,verify OTP
        //generate node id - hex 160 bit - at server
        //sign cert,add node id
        //return back to client
        boolean certsign = false;        
        debug_level.debug(1, "Welcome to CertificateSignature .Sending new cert to Iden Server for sign");        
        String certstring=cert.toString();
        byte[] certbyte = cert.getEncoded();
        String certstringbyte = new String(Base64.getEncoder().encode(certbyte));
        debug_level.debug(1,"String format of recieved certificate for signatue is     :"+certstring);
        //String mserverurl ="http://localhost:8084/beans_b4server";
        String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
        String MSrequrl = mserverurl +"/ProcessRequest?req=sscccertsign&cert=" + URLEncoder.encode(certstring, "UTF-8");
        System.out.println("Certificate String for POSTMAN "+certstring+" "+certstringbyte);
        debug_level.debug(1,MSrequrl);
        createConnection http = new createConnection();
        createotpConnection http_2 = new createotpConnection();
        //boolean server1 = http.sendGet(MSrequrl);
        boolean server1=true;
        try {
            http.sendPost(MSrequrl);
        } catch(Exception e) {
            System.exit(0);
        }
        //PrivateKey priv_client = GenerateCertificate2.priv();
        //System.out.println("Private key is   " + priv_client);
        if(server1)
        {
            final String OTP = Gui.getotp();
            final String MSrequrl2 = mserverurl + "/otp_verification?req=otpverify&OTP=" + URLEncoder.encode(OTP, "UTF-8") + "&cert=" + URLEncoder.encode(certstring, "UTF-8") + "&certstringbyte=" + URLEncoder.encode(certstringbyte, "UTF-8") + "&deviceid=" + ClientMain.Device_Id + "&nodeid=" + ClientMain.Node_Id;
            X509Certificate[] Certs = new X509Certificate[2];
            Certs = createotpConnection.sendPost(MSrequrl2);
            final String alias1 = Gui.getaliasname();
            final String password = Gui.getkeystorepass();
            final char[] keyPass = password.toCharArray();
            final PrivateKey priv_client = GenerateCertificate2.priv();
            final byte[] bprivkey = priv_client.getEncoded();
            final String sprivate = new String(Base64.getEncoder().encode(bprivkey));
            try {
                final FileWriter myWriter = new FileWriter("key.txt");
                myWriter.write(sprivate);
                myWriter.close();
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, keyPass);
            keyStore.setKeyEntry(alias1, priv_client, keyPass, Certs);
            final FileOutputStream fos = new FileOutputStream("SignedClientKeyStore.JKS");
            keyStore.store(fos, keyPass);
            final BufferedReader reader = new BufferedReader(new FileReader("SignedClientKeyStore.JKS"));
            final StringBuilder stringBuilder = new StringBuilder();
            for (char[] buffer = new char[10]; reader.read(buffer) != -1; buffer = new char[10]) {
                stringBuilder.append(new String(buffer));
            }
            reader.close();
            final String content = stringBuilder.toString();
            final createConnection http_3 = new createConnection();
            http_3.sendJKSPost("http://ictwiki.iitk.ac.in:8080/b4server/ProcessRequest", content, certstring);
            final String hash = Integrity.fileHash("SignedClientKeyStore.JKS");
            try {
                final File auth = new File("authenticate.txt");
                if (auth.createNewFile()) {
                    final FileWriter myWriter2 = new FileWriter("authenticate.txt");
                    myWriter2.write(hash);
                    myWriter2.close();
                }
                else {
                    System.out.println("File already exists.");
                    final FileWriter myWriter2 = new FileWriter("authenticate.txt");
                    myWriter2.write(hash);
                    myWriter2.close();
                }
            }
            catch (IOException e3) {
                System.out.println("An error occurred.");
                e3.printStackTrace();
            }
            GenerateCertificate2.saveKeyStore(keyStore, "KeyStore.JKS", password);
            debug_level.debug(1, "New Certificates and Signature saved to Keystore in cert sign");
            certsign = true;
            debug_level.debug(1, "All Certificats and Signatures have been saved to a file and Certificate Signature routine Completed in cert sign");
        }
        return certsign;
    }
    
    @SuppressWarnings("static-access")
    public static  boolean revokecertsign(X509Certificate cert) throws Exception 
    {
        // send cert for signing to server
        //inside cert sign function- get email id from cert at server side
        //send OTP to user email ,ask for OTP from user ,verify OTP
        //generate node id - hex 160 bit - at server
        //sign cert,add node id
        //return back to client
        boolean certsign = false;
        debug_level.debug(1, "Welcome to Revoke CertificateSignature .Sending new cert to Iden Server for sign");
        final String certstring = cert.toString();
        final byte[] certbyte = cert.getEncoded();
        final String certstringbyte = new String(Base64.getEncoder().encode(certbyte));
        debug_level.debug(1, "String format of recieved certificate for signatue is     :" + certstring);
        final String mserverurl = "http://ictwiki.iitk.ac.in:8080/b4server";
        final String MSrequrl = mserverurl + "/ProcessRequest?req=ssccrevokecertsign&cert=" + URLEncoder.encode(certstring, "UTF-8") + "&certstringbyte=" + URLEncoder.encode(certstringbyte, "UTF-8") + "&deviceid=" + ClientMain.Device_Id + "&nodeid=" + ClientMain.Node_Id;
        System.out.println("Certificate String for POSTMAN " + certstring + " " + certstringbyte);
        debug_level.debug(1, MSrequrl);
        final createConnection http = new createConnection();
        final boolean server1 = true;
        final PrivateKey priv_client = GenerateCertificate2.priv();
        System.out.println("Private key is   " + priv_client);
        if (server1) {
            X509Certificate[] Certs = new X509Certificate[2];
            Certs = createConnection.sendRevokePost(MSrequrl);
            final String alias1 = Gui.getaliasname();
            final String password = Gui.getkeystorepass();
            final char[] keyPass = password.toCharArray();
            KeyStore keyStore = null;
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(null, keyPass);
            keyStore.setKeyEntry(alias1, priv_client, keyPass, Certs);
            final FileOutputStream fos = new FileOutputStream("SignedClientKeyStore.JKS");
            keyStore.store(fos, keyPass);
            final BufferedReader reader = new BufferedReader(new FileReader("SignedClientKeyStore.JKS"));
            final StringBuilder stringBuilder = new StringBuilder();
            for (char[] buffer = new char[10]; reader.read(buffer) != -1; buffer = new char[10]) {
                stringBuilder.append(new String(buffer));
            }
            reader.close();
            final String content = stringBuilder.toString();
            final createConnection http_3 = new createConnection();
            http_3.sendJKSPost("http://ictwiki.iitk.ac.in:8080/b4server/ProcessRequest", content, certstring);
            final String hash = Integrity.fileHash("SignedClientKeyStore.JKS");
            try {
                final File auth = new File("authenticate.txt");
                if (auth.createNewFile()) {
                    final FileWriter myWriter = new FileWriter("authenticate.txt");
                    myWriter.write(hash);
                    myWriter.close();
                }
                else {
                    System.out.println("File already exists.");
                    final FileWriter myWriter = new FileWriter("authenticate.txt");
                    myWriter.write(hash);
                    myWriter.close();
                }
            }
            catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            GenerateCertificate2.saveKeyStore(keyStore, "KeyStore.JKS", password);
            debug_level.debug(1, "New Certificates and Signature saved to Keystore via revoke cert sign");
            certsign = true;
            debug_level.debug(1, "All Certificats and Signatures have been saved to a file and Certificate Signature routine Completed via revoke cert sign");
        }
        return certsign;
    }
}

