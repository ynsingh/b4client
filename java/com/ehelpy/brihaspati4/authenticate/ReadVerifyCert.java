package com.ehelpy.brihaspati4.authenticate;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
//Last modified by Maj Dushyant Choudhary May 2020
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
    private static boolean authflag=false;

    public static KeyStore loadKeyStore(String keystorealias, String password)
    {
        //Before a key store can be accessed, it must be loaded.
        if (password == null || keystorealias == null || keystorealias.equals(""))
        {
            int value = JOptionPane.showConfirmDialog(null, "Alias and Password Cannot be null....Move on to new Cert Acquisition");
            while (value != JOptionPane.YES_OPTION)
            {
                System.exit(0);
            }
            debug_level.debug(0,"Move on to new Cert Acquisition");
        }
        else
        {
            try {
                System.out.println("KeyStore Default type "+KeyStore.getDefaultType());
                keystore = KeyStore.getInstance("JKS");
            } catch (KeyStoreException e1) {
                e1.printStackTrace();
            }
            FileInputStream is = null;
            try
            {
                //is = new FileInputStream("SignedClientKeyStore.JKS");
                //Loads this KeyStore from the given input stream location.
                //toCharArray - Converts string to new char array & returns a newly allocated char array
                char[] keypass	=	password.toCharArray() ;

                File f = new File("authenticate.txt");
                if(f.exists() && !f.isDirectory())
                {
                    System.out.println("FILE EXISTS");
                    File file = new File("authenticate.txt");

                    BufferedReader br = new BufferedReader(new FileReader(file));

                    String st,fhash = null;
                    while ((st = br.readLine()) != null)
                    {
                        System.out.println(st);
                        fhash=st;
                    }

                    String hash=Integrity.fileHash("SignedClientKeyStore.JKS");

                    System.out.println("File : "+fhash+" HASH : "+hash);
                    if(!fhash.equals(hash))
                    {
                        System.out.println("KEYSTORE CORRUPT");
                        authflag=false;
                        String emailid=Gui.getkeystorerecovery();
                        String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
                        //String mserverurl ="http://localhost:8084/beans_b4server";
                        String MSrequrl = mserverurl +"/KeystoreRecovery?req=keystoreotpgen&emailid="+emailid;

                        createKeystoreRecoveryConnection http_2 = new createKeystoreRecoveryConnection ();

                        if(http_2.sendPost(MSrequrl,"keystoreotpgen")==1)
                        {
                            String otp=Gui.getotp();
                            MSrequrl = mserverurl +"/KeystoreRecovery?req=keystoreotpverify&emailid="+emailid+"&otp="+otp;
                            X509Certificate[] Certs	= new  X509Certificate[2];
                            Certs = http_2.sendPost(MSrequrl);
                            //String jks=http_2.sendPostJKS(MSrequrl);
                            System.out.println("SERVER CERT :"+Certs[0]);
                            System.out.println("CLIENT CERT :"+Certs[1]);
                            //System.out.println("JKS - "+ jks);

                            try {

                                ReadVerifyCert.keystorealias = keystorealias;
                                final char[] keyPass = ReadVerifyCert.keyStorePass.toCharArray();
                                ReadVerifyCert.keystore.load(null, keyPass);
                                final String pkey = new String(Files.readAllBytes(Paths.get("key.txt", new String[0])));
                                final byte[] keybytes = Base64.getDecoder().decode(pkey);
                                final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keybytes);
                                final KeyFactory fact = KeyFactory.getInstance("RSA");
                                final PrivateKey priv_client = fact.generatePrivate(keySpec);
                                ReadVerifyCert.keystore.setKeyEntry(keystorealias, priv_client, keyPass, Certs);
                                final FileOutputStream fos = new FileOutputStream("SignedClientKeyStore.JKS");
                                ReadVerifyCert.keystore.store(fos, keyPass);
                                final String h = Integrity.fileHash("SignedClientKeyStore.JKS");
                                try {
                                    final File auth = new File("authenticate.txt");
                                    final FileWriter myWriter = new FileWriter("authenticate.txt");
                                    myWriter.write(h);
                                    myWriter.close();
                                }
                                catch (IOException e2) {
                                    System.out.println("An error occurred.");
                                    e2.printStackTrace();
                                }
                                ClientMain.main(null);
                            } catch(IOException e) {
                                e.printStackTrace();
                            }

                        }

                        /*
                        X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                        System.out.println("New certificate of node is " + newcert);
                        System.out.println("\"New certificate of node will now be sent for signature to Identity Server");

                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        System.out.println("STATUS :"+staus);*/
                    }
                    else
                    {
                        System.out.println("KEYSTORE NOT CORRUPT");
                        authflag=true;
                    }

                }
                else
                {
                    System.out.println("FILE NOT EXISTS");
                    System.out.println(Integrity.stringHash(keystorealias));
                    System.out.println(Integrity.stringHash(password));
                    System.out.println(Integrity.fileHash("SignedClientKeyStore.JKS"));

                    String hash=Integrity.fileHash("SignedClientKeyStore.JKS");
                    try {
                        File auth = new File("authenticate.txt");
                        if (auth.createNewFile())
                        {
                            FileWriter myWriter = new FileWriter("authenticate.txt");
                            myWriter.write(hash);
                            myWriter.close();
                            authflag=true;
                        } else {
                            System.out.println("File already exists.");
                        }
                    } catch (IOException e) {
                        System.out.println("An error occurred.");
                        e.printStackTrace();
                    }
                }

                if(authflag)
                {
                    try {
                        is = new FileInputStream("SignedClientKeyStore.JKS");

                        /*
                         * Enumeration<String> e=keystore.aliases(); while (e.hasMoreElements())
                         * System.out.println("Alias is: " + e.nextElement());
                         */

                        keystore.load(is,keypass);
                        System.out.println("KEYSTORE LOADED SUCCESSFULLY");
                    }
                    catch(Exception e) {
                        debug_level.debug(3, "The password or alias entered is incorrect");
                        e.printStackTrace();
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
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //Logger add to log with date, time, IP address, timezone - TBD}
            }
            finally
            {
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
    static boolean verifyCert() throws Exception
    {
        // This function loads keystore and checks the validity of the certificate
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        boolean status = true;// flag for certificate status
        boolean flag = false;
        do {
            keyStorePass = Gui.getkeystorepass();
            if(keyStorePass.equals("1"))
            {
                String emailid=Gui.getemailid();
                String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
                //String mserverurl ="http://localhost:8084/beans_b4server";
                String MSrequrl = mserverurl +"/ProcessRequest?req=forgotpasswordotpgen&emailid="+emailid;

                createConnection http_1 = new createConnection ();

                if(http_1.sendPost(MSrequrl,"forgotpasswordotpgen")==1)
                {
                    String otp=Gui.getotp();
                    MSrequrl = mserverurl +"/ProcessRequest?req=forgotpasswordotpverify&emailid="+emailid+"&otp="+otp;
                    X509Certificate[] Certs	= new  X509Certificate[2];
                    int val=http_1.sendPost(MSrequrl,"forgotpasswordotpverify");
                    //System.out.println(val);
                    if(val==5)
                    {
                        X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                        debug_level.debug(0,"New certificate of node is " + newcert);
                        debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        debug_level.debug(0,"status = "+ staus);
                    }
                }
            }
            else if(keyStorePass.equals("2"))
            {
                String emailid=Gui.getemailid();
                String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
                //String mserverurl ="http://localhost:8084/beans_b4server";
                String MSrequrl = mserverurl +"/ProcessRequest?req=certificaterevocationotpgen&emailid="+emailid;

                createConnection http_1 = new createConnection ();

                if(http_1.sendPost(MSrequrl,"certificaterevocationotpgen")==1)
                {
                    String otp=Gui.getotp();
                    MSrequrl = mserverurl +"/ProcessRequest?req=certificaterevocationotpverify&emailid="+emailid+"&otp="+otp;
                    int val=http_1.sendPost(MSrequrl,"certificaterevocationotpverify");
                    //System.out.println(val);
                    if(val==6)
                    {

                        String revocationreason=Gui.getrevocationreason();
                        MSrequrl = mserverurl +"/ProcessRequest?req=certificaterevocationreason&emailid="+emailid+"&reason="+revocationreason;
                        int val2=http_1.sendPost(MSrequrl,"certificaterevocationreason");
                        if(val2==7)
                        {

                            X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                            debug_level.debug(0,"New certificate of node is " + newcert);
                            debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                            boolean staus = CertificateSignature.revokecertsign((X509Certificate) newcert);
                            debug_level.debug(0,"status = "+ staus);

                        }

                    }
                    else if(val==4)
                    {
                        Gui.showMessageDialogBox("Authenticity of Email not confirmed");
                    }
                    else if(val==3)
                    {
                        Gui.showMessageDialogBox("Record does not exists");
                    }
                }
            }
            else if(keyStorePass.equals("3"))
            {
                X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                debug_level.debug(0,"New certificate of node is " + newcert);
                debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                debug_level.debug(0,"status = "+ staus);
            }
            else {
                keystorealias = Gui.getaliasname();
                keystore = loadKeyStore(keystorealias, keyStorePass);
                if (keystore == null)
                {

                    X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                    debug_level.debug(0,"New certificate of node is " + newcert);
                    debug_level.debug(0,"New certificate of node will now be sent for signature to Identity Server ");
                    boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                    debug_level.debug(0,"status = "+ staus);
                }
                else
                {
                    debug_level.debug(2,"KEYSTORE IS = " + keystore);
                    try
                    {

                        cert = keystore.getCertificateChain(keystorealias);// Returns the cert associated with the given alias
                        debug_level.debug(2,"KEYSTORE cert IS = " + cert);
                        if (cert == null) {
                            pw_alias_wr.id_exist();
                            return false;
                        }
                    }
                    catch (KeyStoreException ex)
                    {
                        pw_alias_wr.id_exist();
                        return false;
                    }
                    try
                    {
                        x509servercert =   (X509Certificate) cert[0];
                        x509clientcert =  (X509Certificate) cert[1];
                    } catch (Exception e) {
                        return false;
                    }
                    if (x509clientcert instanceof X509Certificate)
                    {
                        try {
                            x509clientcert.checkValidity();
                            // x509clientcert.verify(x509servercert.getPublicKey(), new BouncyCastleProvider());
                            String srnum = x509clientcert.getSerialNumber().toString();
                            String mserverurl ="http://ictwiki.iitk.ac.in:8080/b4server";
                            //String mserverurl ="http://localhost:8084/beans_b4server";
                            String MSrequrl = mserverurl +"/ProcessRequest?req=checkcrl&certsrno="+srnum;

                            createConnection http_1 = new createConnection ();

                            int val=http_1.sendPost(MSrequrl,"checkcrl");

                            if(val==24)
                            {
                                String msgg = "Certificate Compromised.... Quitting";
                                Gui.showMessageDialogBox(msgg);
                            }

                            debug_level.debug(3,"Checked certificate validity.Certificate is VALID.");
                            status = true;
                            Date certNotAfter = x509clientcert.getNotAfter();
                            Date now = new Date();
                            long timeLeft = certNotAfter.getTime() - now.getTime(); // Time left in ms
                            long days = timeLeft / (24 * 3600 * 1000);
                            System.out.println("Your Certificate is now valid for only " + days +" days");
                            if(days<16)
                            {
                                String msgg = "Acquire a new certificate immediately";
                                Gui.showMessageDialogBox(msgg);
                            }

                        } catch (Exception e)
                        {   e.printStackTrace();
                            debug_level.debug(4,e.getMessage());
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
                    }
                    else
                    {
                        debug_level.debug(3,"Checked certificate validity.Certificate is not X509 Type.Move on to new Cert Acquisition");
                        X509Certificate newcert = GenerateCertificate2.createSelfSignedCert();
                        debug_level.debug(0,"new certificate of node is " + newcert);
                        debug_level.debug(2,"new certificate of node will now be sent for signature to Identity Server ");
                        boolean staus = CertificateSignature.certsign((X509Certificate) newcert);
                        debug_level.debug(0,"New cert Acquistion routine is completed = "+ staus);
                    }
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
