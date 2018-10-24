package com.ehelpy.brihaspati4.authenticate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import sun.security.tools.keytool.CertAndKeyGen;

public class keystore_save {
    static int datacesstype;
    //dataacesstype=1 database
    // 2 filemethod

    @SuppressWarnings("static-access")
    public static void main(String args[]) {
        keystore_save adam = new keystore_save();
        try {
            String path = properties_access.read_property("client.properties", "home");
            sun.security.tools.keytool.CertAndKeyGen keypair = null;
            keypair = new sun.security.tools.keytool.CertAndKeyGen("RSA", "SHA1WithRSA", null);

            keypair.generate(1024);

            debug_level.debug(0, "Generated Key Pair");
            adam.dumpKeyPair(keypair);
            adam.SaveKeyPair(path, keypair);
            @SuppressWarnings("unused")
            PrivateKey loadedprivatekey = adam.LoadKeyPair(path, "RSA");
            debug_level.debug(0,"Loaded Key Pair");
            adam.dumpKeyPair(keypair);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public static void dumpKeyPair(CertAndKeyGen loadedKeyPair) {
        PublicKey pub = loadedKeyPair.getPublicKey();
        debug_level.debug(0,"Public Key: " + getHexString(pub.getEncoded()));

        PrivateKey priv = loadedKeyPair.getPrivateKey();
        debug_level.debug(0,"Private Key: " + getHexString(priv.getEncoded()));
    }

    public static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    public void SaveKeyPair(String path, CertAndKeyGen key) throws IOException {
        PrivateKey privateKey = key.getPrivateKey();
        PublicKey publicKey = key.getPublicKey();

        // Store Public Key.
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        FileOutputStream fos = new FileOutputStream(path + "/public.key");
        fos.write(x509EncodedKeySpec.getEncoded());
        fos.close();

        // Store Private Key.
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        fos = new FileOutputStream(path + "/private.key");
        fos.write(pkcs8EncodedKeySpec.getEncoded());
        fos.close();
    }

    public static PrivateKey LoadKeyPair(String path, String algorithm)
    throws IOException, NoSuchAlgorithmException,
        InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path + "/public.key");
        FileInputStream fis = new FileInputStream(path + "/public.key");
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Read Private Key.
        File filePrivateKey = new File(path + "/private.key");
        fis = new FileInputStream(path + "/private.key");
        byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
        fis.read(encodedPrivateKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        @SuppressWarnings("unused")
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        return privateKey;
    }
}
