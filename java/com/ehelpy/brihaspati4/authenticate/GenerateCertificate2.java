package com.ehelpy.brihaspati4.authenticate ;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
//Program rebuilt by Lt Col Ankit Singhal Dated 15 April 2019 ; 0600 Hrs
//The private key generated is in PKCS8 format and public key in X509 format.
//The Certificate uses SHA256WithRSA algorithm for signature, 2048 bit keys and is Version 3.
//No extensions have been added to the generated certificate as of now and same may be added in future when the network grows 
// to multiple layers as proper chaining of certificates will be required then. 
@SuppressWarnings("deprecation")

public class GenerateCertificate2 
{	
    private static  String E = null;
    private static String OU = null;
    private static String O = null;
    private static String L = null;
    private static  String ST = null;
    private static  String C = null;
    static final String alias1 = null;
    static final char[] keyPass =null;
    public static PrivateKey privKey = null;
    static KeyPair keypair = null;
    
    public static X509Certificate createSelfSignedCert() throws Exception 
    {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	
    	String formdata=Gui.createCertificateGUI();
    	System.out.println("FORM DATA "+formdata);
    	E = formdata.split(",")[0];
      	OU = formdata.split(",")[1];
        O = formdata.split(",")[2];
        L = formdata.split(",")[3];
        ST = formdata.split(",")[4];
        C = formdata.split(",")[5];
        try {
            KeyStore keyStore = null;
            try 
            {
                keyStore = KeyStore.getInstance("JKS");
            } 
            catch (KeyStoreException e1) 
            {
                e1.printStackTrace();
            }
            try {
                keyStore.load(null,keyPass);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (CertificateException e1) {
                e1.printStackTrace();
            }
            try {
            	KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA","BC");
                keyGen.initialize(2048,new SecureRandom());
                keypair = keyGen.generateKeyPair();
           	} catch (Exception e) {
        		e.printStackTrace();
        	}            
            privKey = priv();
            PublicKey pubKey = keypair.getPublic();
            X509V3CertificateGenerator x500Name = new X509V3CertificateGenerator();
            Vector<ASN1ObjectIdentifier> order = new Vector<>();
            Hashtable<ASN1ObjectIdentifier, String> attributeMap = new Hashtable<>();
            if (E != null) {
                attributeMap.put(X509Name.CN, E);
                order.add(X509Name.CN);
              }
            if (OU != null) {
                attributeMap.put(X509Name.OU, OU);
                order.add(X509Name.OU);
              }
              if (O != null) {
                attributeMap.put(X509Name.O, O);
                order.add(X509Name.O);
              }
              if (L != null) {
                attributeMap.put(X509Name.L, L);
                order.add(X509Name.L);
              }
              if (ST != null) {
                attributeMap.put(X509Name.ST, ST);
                order.add(X509Name.ST);
              }
              if (C != null) {
                attributeMap.put(X509Name.C, C);
                order.add(X509Name.C);
              }              
            X509Name issuerDN = new X509Name(order, attributeMap);
            Calendar c = Calendar.getInstance();
            x500Name.setNotBefore(c.getTime());
            c.add(Calendar.YEAR, 1);
            x500Name.setNotAfter(c.getTime());
            x500Name.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
           // x500Name.setSignatureAlgorithm("SHA256WithRSAEncryption");
            x500Name.setSignatureAlgorithm("SHA1WithRSAEncryption");
            x500Name.setIssuerDN(issuerDN);
            x500Name.setSubjectDN(issuerDN);
            x500Name.setPublicKey(pubKey);
            //x500Name.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
            //x500Name.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
            //x500Name.addExtension(X509Extensions.ExtendedKeyUsage, true, new ExtendedKeyUsage(KeyPurposeId.id_kp_serverAuth));*/
            /*debug_level.debug(0,"key pair is:		\n" + keypair);
            debug_level.debug(0,"private key  is:	\n" + privKey);
            debug_level.debug(0,"public key  is:    \n" + pubKey);
            debug_level.debug(0,"alias  is:		\n" + alias1);*/
            X509Certificate[] chain = new X509Certificate[1];
            try {
            	chain[0] = x500Name.generate(privKey, "BC");
            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
            } //catch (CertificateException e1) {
                // TODO Auto-generated catch block
                //e1.printStackTrace();
             catch (SignatureException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
               // TODO Auto-generated catch block
               // e1.printStackTrace();
            } catch (NoSuchProviderException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
         return chain[0];
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    static KeyStore createEmptyKeyStore()
    	    throws GeneralSecurityException, IOException {
    	        KeyStore ks = KeyStore.getInstance("JKS");
    	        ks.load(null, null); // initialize
    	        return ks;
    	    }
    	    static void saveKeyStore(KeyStore ks, String filename, String password)
    	    throws GeneralSecurityException, IOException {
    	        FileOutputStream out = new FileOutputStream(filename);
    	        try {
    	            ks.store(out, password.toCharArray());
    	        } finally {
    	            out.close();
    	        }
    	    }
    	    public static void createKeyStore(String filename, String password, String alias, Key privateKey, Certificate cert)
    	    throws GeneralSecurityException, IOException {
    	        KeyStore ks = createEmptyKeyStore();
    	        ks.setKeyEntry(alias1, privateKey, password.toCharArray(),
    	                       new Certificate[] {cert});
    	        saveKeyStore(ks, filename, password);
    	    }
    	    public static void createKeyStore(String filename,
    	                                      String password, String keyPassword, String alias,
    	                                      Key privateKey, Certificate cert)
    	    throws GeneralSecurityException, IOException {
    	        KeyStore ks = createEmptyKeyStore();
    	        ks.setKeyEntry(alias, privateKey, keyPassword.toCharArray(),
    	                       new Certificate[] {cert});
    	        saveKeyStore(ks, filename, password);
    	    }
    	    public static PrivateKey priv() {
    	        if(keypair!=null)privKey = keypair.getPrivate();
    	        return (PrivateKey) privKey;
    	    }
    	}