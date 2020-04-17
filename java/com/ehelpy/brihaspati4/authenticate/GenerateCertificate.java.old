package com.ehelpy.brihaspati4.authenticate ;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import sun.security.x509.X500Name;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function generates X.509 certificate using the user's credentials
//This function is only called when a user for a first time joins the network
public class GenerateCertificate {

    private static sun.security.tools.keytool.CertAndKeyGen keypair = null;

    //private static final long validity = 1096; // 3 years
    private static final long validity = 365; // 1 year

    private static  String Emailid = null;
    //private static final String Emailid = "vijit@iitk.ac.in";

    //private static final String organizationalUnit = "IIT KANPUR";
    private static String organizationalUnit = null;

    //private static final String organization = "EE DEPT";
    private static String organization = null;

    //private static final String city = "KANPUR";
    private static String city = null;

    //private static final String state = "UTTAR PRADESH";
    private static  String state = null;

    //private static final String country = "IN";
    private static  String country = null;

    //private static final String alias = "tomcat";
    //static final String alias1 = Gui.getaliasname();
    static final String alias1 = null;
    //static final String password	=	Gui.getkeystorepass();
    //static final char[] keyPass = password.toCharArray();
    static final char[] keyPass =null;
    public static PrivateKey privKey = null;
    // method to generate self signed certificate

    public static X509Certificate createSelfSignedCert() {
        Emailid = Gui.getemailid();
        organizationalUnit = Gui.getorganizationalunit();
        organization = Gui.getorganization();
        city = Gui.getcity();
        state = Gui.getstate();
        country = Gui.getcountry();
        try {

            KeyStore keyStore = null;
            try {
                keyStore = KeyStore.getInstance("JKS");
                //keyStore = KeyStore.getInstance("KEYSTORE");
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
            }

            try {
                keyStore.load(null,keyPass );
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (CertificateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            // OPTIONS OF USING DIFFERENT ALGORITHM BELOW.USE ONE AND COMMENT OTHERS.
            //sun.security.tools.keytool.CertAndKeyGen keypair = new sun.security.tools.keytool.CertAndKeyGen("RSA", "MD5WithRSA", null);

            try {
                keypair = new sun.security.tools.keytool.CertAndKeyGen("RSA", "SHA1WithRSA", null);
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchProviderException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //CertAndKeyGen keypair = new CertAndKeyGen("RSA", "SHAWithRSA", null);
            //System.out.println(keypair);
            X500Name x500Name = new X500Name(Emailid, organizationalUnit, organization, city, state, country);

            try {
                keypair.generate(2048);
            } catch (InvalidKeyException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            //KeyPair keyPair = keyGen.generateKeyPair();

            privKey =   keypair.getPrivateKey();
            PublicKey pubKey = null;
            pubKey = keypair.getPublicKey();
            //keystore_save adam = new keystore_save();
            //String path = properties_access.read_property("client.properties","home" );
            //keystore_save.dumpKeyPair(keypair);
            //adam.SaveKeyPair(path, keypair);

            //keypair.generate(1024);
            debug_level.debug(0,"key pair is:		" + keypair);

            debug_level.debug(0,"private key  is:	" + privKey);

            debug_level.debug(0,"public key  is:		 " + pubKey);

            debug_level.debug(0,"alias  is:		" + alias1);

            X509Certificate[] chain = new X509Certificate[1];

            try {
                chain[0] = keypair.getSelfCertificate(x500Name, new Date(), (long) validity * 24 * 60 * 60);

            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
            } catch (CertificateException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (SignatureException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchProviderException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }


            /*try {
            keyStore.setKeyEntry(alias1, privKey, keyPass, chain);
            } catch (KeyStoreException e1) {
            	// TODO Auto-generated catch block
            	e1.printStackTrace();
            }

                   /*public final void setKeyEntry(String alias,byte[] key,Certificate[] chain) throws KeyStoreException
            	Assigns the given key (that has already been protected) to the given alias.
            	Parameters:
            		alias - the alias name
            		key - the key (in protected format) to be associated with the alias
            		chain - the certificate chain for the corresponding public key (only useful if the protected key is of type java.security.PrivateKey).*/

            /*  FileOutputStream fos = new FileOutputStream("ClientKeyStore.JKS");

            try {
            	keyStore.store(fos, keyPass);
            } catch (KeyStoreException e1) {
            	// TODO Auto-generated catch block
            	e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
            	// TODO Auto-generated catch block
            	e1.printStackTrace();
            } catch (CertificateException e1) {
            	// TODO Auto-generated catch block
            	e1.printStackTrace();
            }


            try {
            	saveKeyStore(keyStore,"KeyStore.JKS", password);
            } catch (GeneralSecurityException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            }
            System.out.println("new cert saved to keystore");*/
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

    /**
     * Creates a keystore with a single key and saves it to a file.
     *
     * @param filename String file to save
     * @param password String store password to set on keystore
     * @param keyPassword String key password to set on key
     * @param alias String alias to use for the key
     * @param privateKey Key to save in keystore
     * @param cert Certificate to use as certificate chain associated to key
     * @throws GeneralSecurityException for any error with the security APIs
     * @throws IOException if there is an I/O error saving the file
     */
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
        if(keypair!=null)privKey = keypair.getPrivateKey();
        return (PrivateKey) privKey;
    }
}

