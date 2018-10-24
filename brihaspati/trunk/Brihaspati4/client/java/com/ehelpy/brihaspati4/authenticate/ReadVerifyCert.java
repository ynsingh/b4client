package com.ehelpy.brihaspati4.authenticate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.swing.JOptionPane;

//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//This function carries out verification of the existing certifcate of the peer
//In case the peer certificate is not validate it prompts the peer to generate a new certificate
//It also forward the certificate for signature to the Identity server
//Entire package whenever either the client certificate or server certificate is required the functions are
// defined in this class

public class ReadVerifyCert {

    private static X509Certificate x509servercert = null;
    private static X509Certificate x509clientcert = null;
    private static KeyStore keystore = null;
    private static String keyStorePass ;
    private static String keystorealias;
    private static	Certificate[] cert = null;

    public static KeyStore loadKeyStore(String keystorealias, String password) {


        //to request a KeyStore object of default type:
        // KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        //Before a key store can be accessed, it must be loaded.

        //KeyStore keystore = null;
        if (password == null || keystorealias == null || keystorealias.equals(""))
        {

            int value = JOptionPane.showConfirmDialog(null, "Do you want to generate a new certificate");
            while (value != JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }

            debug_level.debug(0,"Move on to new Cert Acquisition");

        }
        else
        {
            try {
                keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
            }

            FileInputStream is = null;
            try {
                is = new FileInputStream("SignedClientKeyStore.JKS");

                //Loads this KeyStore from the given input stream location.
                // toCharArray - Converts string to new char array & returns a newly allocated char array
                char[] keypass	=	password.toCharArray() ;
                try {
                    keystore.load(is, keypass);
                }
                catch(Exception e) {
                    debug_level.debug(3, "The password or alias entered is incorrect");
                    pw_alias_wr.id_exist();
                    return keystore;
                }

                // Store away the keystore.
                // keystore.load(null, password.toCharArray());
                FileOutputStream fos = new FileOutputStream("KeyStoreNew.JKS");
                keystore.store(fos, keypass);
                fos.close();

                //FileInputStream(String name)-creates a FileInputStream by opening a connection to an actual file
                //the file named by the path name in the file system.



            } catch (Exception e) {
                e.printStackTrace();
                //Logger add to log with date, time, IP address, timezone - TBD}
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return keystore;
    }

    static boolean verifyCert() throws Exception {

        // This function loads keystore and checks the validity of the
        // certificate
        boolean status = true;// flag for certificate status
        boolean flag = false;
        do {

            // keystore password input from user using Gui class.this will be
            // taken from config finally
            keyStorePass = Gui.getkeystorepass();
            //keyStorePass = "123456";
            //keystorealias = "client1";
            keystorealias = Gui.getaliasname();

            keystore = loadKeyStore(keystorealias, keyStorePass);
            if (keystore == null) {

                X509Certificate newcert = GenerateCertificate.createSelfSignedCert();
                debug_level.debug(0,"new certificate of node is " + newcert);

                // extracting the email id from the new certificate.this will be
                // done at server side.
                // String clientemailid = newcert(((X509Certificate)
                // cert).getSubjectX500Principal()).getCommonName();
                // System.out.println("email id of client is: " + clientemailid);
                // System.out.println("email id of client is " +X500Name.asX500Name(newcert.getSubjectX500Principal()).getCommonName());

                // call Certificate Signature class and forward new certificate
                // for signature to Identity server
                debug_level.debug(0,"new certificate of node will now be sent for signature to Identity Server ");
                boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                debug_level.debug(0,"status = "+ staus);

            } else {
                debug_level.debug(2,"KEYSTORE IS = " + keystore);
                // GenerateCertificate.createSelfSignedCert();


                // Get the alias from key store.
                // Enumeration<String> aliases() - Lists all the alias names of
                // this keystore.


                try {
                    cert =  keystore.getCertificateChain(keystorealias);
                    debug_level.debug(2,"KEYSTORE cert IS = " + cert);
                    if (cert == null) {
                        pw_alias_wr.id_exist();
                        return false;
                    }

                }
                catch (KeyStoreException ex) {
                    pw_alias_wr.id_exist();
                    return false;
                }
                try {
                    x509servercert = (X509Certificate) cert[0];
                    x509clientcert = (X509Certificate) cert[1];

                    //Enumeration<String> aliases1 = keystore.aliases();
                    //while (aliases1.hasMoreElements()) {

                    // hasMoreElements - method call returns 'true' if there
                    // are more tokens; false otherwise.
                    //String alias = (String) aliases1.nextElement();// nextElement
                    // -method
                    // call
                    // returns
                    // the
                    // next
                    // token
                    // in
                    // the
                    // string.

                    // getCertificate(String alias) - Returns the
                    // certificate associated with the given alias
                    //cert = keystore.getCertificateChain(keystorealias);
                    //for(Certificate certs:cert){
                    //}
                } catch (Exception e) {
                    return false;
                }

                if (x509clientcert instanceof X509Certificate) {
                    // String getType() -Returns the type of this keystore.

                    try {

                        // X509Certificate x509cert =
                        // X509Certificate.getInstance(cert.getEncoded());
                        x509clientcert.checkValidity();
                        debug_level.debug(2,"public key  = " + (x509servercert.getPublicKey()));
                        x509clientcert.verify(x509servercert.getPublicKey());
                        debug_level.debug(3,"Checked certificate validity.Certificate is VALID.");
                        // check certificate is already signed by identity
                        // server private key. -TBD
                        // if yes- move to comn appln,if no- go to server for
                        // cert sign -TBD
                        //String clientemailid = X500Name.asX500Name(((X509Certificate) cert).getSubjectX500Principal()).getCommonName();
                        //System.out.println("email id of client is:  " + clientemailid);
                        //CertificateSignature.certsign(x509cert);
                        status = true;
                    } catch (Exception e) {

                        debug_level.debug(3,
                                          "Checked certificate validity.Certificate is EXPIRED(not valid).Move on to new Cert Acquisition");
                        // Logger.add to log with date, time, IP address,
                        // timezone - "The Certificate has been found to be
                        // expired before the hash verification."
                        status = false;
                        flag = false;

                        // Generating and printing new self signed certificate
                        // using GenerateCertificate Class
                        java.security.cert.X509Certificate newcert = GenerateCertificate.createSelfSignedCert();
                        debug_level.debug(0,"new certificate of node is " + newcert);

                        // extracting the email id from the new certificate.
                        // this will be done at server side.

                        // System.out.println("email id of client is: " +
                        // clientemailid);
                        // System.out.println("email id of client is "
                        // +X500Name.asX500Name(newcert.getSubjectX500Principal()).getCommonName());

                        // call Certificate Signature class and forward new
                        // certificate for signature to Identity server
                        debug_level.debug(3,"new certificate of node will now be sent for signature to Identity Server ");
                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        debug_level.debug(0,"New cert Acquistion routine is completed = "+ staus);
                    }
                } else {
                    debug_level.debug(3,"Checked certificate validity.Certificate is not X509 Type.Move on to new Cert Acquisition");

                    // Generating and printing new self signed certificate using
                    // GenerateCertificate Class
                    X509Certificate newcert = GenerateCertificate.createSelfSignedCert();
                    debug_level.debug(0,"new certificate of node is " + newcert);

                    // extracting the email id from the new certificate.this
                    // will be done at server side.
                    // call Certificate Signature class and forward new
                    // certificate for signature to Identity server
                    debug_level.debug(2,"new certificate of node will now be sent for signature to Identity Server ");
                    boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                    debug_level.debug(0,"New cert Acquistion routine is completed = "+ staus);
                }
            }
        } while (flag);

        // } // first else loop closure -when keystore password is not equal to
        // null
        // do loop closure

        return status;// status- returns status of checkValidity method
    }
    public static X509Certificate returnServerCert() throws Exception {
        if(x509servercert==null)x509servercert=createotpConnection.returnServerCert();
        return x509servercert;
    }
    public static X509Certificate returnClientCert() throws Exception {
        if(x509clientcert==null)x509clientcert=createotpConnection.returnClientCert();
        return x509clientcert;
    }
    public static PrivateKey getKeyPair() {
        Key key = null;
        {
            key=GenerateCertificate.priv();
        }

        if(key==null) {
            try {
                key = (PrivateKey) keystore.getKey(keystorealias, keyStorePass.toCharArray());
            } catch (UnrecoverableKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (KeyStoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*  Certificate cert = null;
            try {
            	cert = keystore.getCertificate(keystorealias);
            } catch (KeyStoreException e) {
            	// TODO Auto-generated catch block
            	e.printStackTrace();
            }*/
        }
        //  final PublicKey publicKey = cert.getPublicKey();
        return  (PrivateKey) key;
    }

}
