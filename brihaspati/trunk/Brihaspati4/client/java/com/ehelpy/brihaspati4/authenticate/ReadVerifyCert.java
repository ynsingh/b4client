package com.ehelpy.brihaspati4.authenticate;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import javax.swing.JOptionPane;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
//Last modified by Lt Col Ankit Singhal Dated 26 May 2019 2019 ; 1500 Hrs
//This function carries out verification of the existing certificate of the peer
//15 days prior to certificate expiry, the system will prompt the user for renewing the certificate
//In case the peer certificate is not valid, it prompts the peer to generate a new certificate
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
        //Before a key store can be accessed, it must be loaded.
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
                //toCharArray - Converts string to new char array & returns a newly allocated char array
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
    @SuppressWarnings("static-access")
	static boolean verifyCert() throws Exception {
    	// This function loads keystore and checks the validity of the certificate
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	boolean status = true;// flag for certificate status
        boolean flag = false;
        do {
            keyStorePass = Gui.getkeystorepass();
            keystorealias = Gui.getaliasname();
            keystore = loadKeyStore(keystorealias, keyStorePass);
            if (keystore == null) {
                X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                debug_level.debug(0,"New certificate of node is " + newcert);
                debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                debug_level.debug(0,"status = "+ staus);
            } else {
                debug_level.debug(2,"KEYSTORE IS = " + keystore);
                try {
                	
                	cert = keystore.getCertificateChain(keystorealias);// Returns the cert associated with the given alias
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
                	x509servercert =   (X509Certificate) cert[0];
                    x509clientcert =  (X509Certificate) cert[1];
                } catch (Exception e) {
                    return false;
                }
                if (x509clientcert instanceof X509Certificate) { 
                	try {                   	
                    	 x509clientcert.checkValidity();
                       	 x509clientcert.verify(x509servercert.getPublicKey(), new BouncyCastleProvider());
                         debug_level.debug(3,"Checked certificate validity.Certificate is VALID.");
                        status = true;
                        Date certNotAfter = x509clientcert.getNotAfter();
                        Date now = new Date();
                        long timeLeft = certNotAfter.getTime() - now.getTime(); // Time left in ms
                        long days = timeLeft / (24 * 3600 * 1000);
                            System.out.println("Your Certificate is now valid for only " + days +" days");
                            if(days<16) {                        
                            String msgg = "Acquire a new certificate immediately";
                            Gui.showMessageDialogBox(msgg); 
                            }                   
                    } catch (Exception e) {
                        debug_level.debug(3,"Checked certificate validity.Certificate has EXPIRED (not valid).Move on to new Cert Acquisition");
                        String messg = "Your Certificate has Expired ";
                        Gui.showMessageDialogBox(messg);
                        String msg1 = "ALERT: Delete old certificate entries from the keystore before new cert acquisition";
                        Gui.showMessageDialogBox(msg1);
                        status = false;
                        flag = false;
                        // Generating and printing new self signed certificate using GenerateCertificate2 Class
                        
                        X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                        debug_level.debug(0,"new certificate of node is " + newcert);
                        debug_level.debug(3,"new certificate of node will now be sent for signature to Identity Server ");
                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        debug_level.debug(0,"New cert Acquistion routine is completed = "+ staus);
                    }
                } else {
                    debug_level.debug(3,"Checked certificate validity.Certificate is not X509 Type.Move on to new Cert Acquisition");
                    X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                    debug_level.debug(0,"new certificate of node is " + newcert);
                    debug_level.debug(2,"new certificate of node will now be sent for signature to Identity Server ");
                    boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                    debug_level.debug(0,"New cert Acquistion routine is completed = "+ staus);
                }
            }
        } while (flag);
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
            key=GenerateCertificate2.priv();
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
        }
        //  final PublicKey publicKey = cert.getPublicKey();
        return  (PrivateKey) key;
    }    
}
