package com.ehelpy.brihaspati4.Encryption;

import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
/**
 * <h1>Encrypt</h1>
 * Class responsible for encryption and decryption activities
 * <p>
 * This class carries out all the encryption and decryption
 * related activities implemented in the software.</p>
 * <b>Functions:</b> At the user end of DFS
 * <b>Note:</b> Change this file to change functionality
 * related to encryption
 * @author  Sidharth Patra
 * @version 2.0
 * @since   2020-02-12
 */
public class Encrypt {

    private static Cipher cipher;
    /**
     * <h1>StartEnc </h1>
     * Starts the Encryption process
     * <p>
     * This method starts the encryption process by
     * calling functions to generate keys and storing them
     * in the file system.</p>
     * @param data byte array required to be encrypted
     * @throws IOException for input output exception
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return a frame containing encrypted key followed by encrypted data
     */
    public static byte[] startEnc(byte[] data) throws GeneralSecurityException, IOException {

        KeyPair keypair ;
        //PrivateKey privatekey = null;
        PublicKey publickey = null;

        GenerateKeys keys = new GenerateKeys();
        //generate symmetric key
        SecretKey symKey = GenerateKeys.GenerateSymmetricKey("AES",128);
        // encrypt the data with symmetric key
        byte[] encryptedData = EncryptData(data,symKey,"AES");
        //Operations pertaining to public pvt key pairs
        File f;
        f = new File("KeyVault");
        // if a key pair has been generated then read from files containing the key
        if(f.exists()&& f.isDirectory()){
            //privatekey = getPrivate();
            publickey = getPublic();
        }else{
            // if the keys have not been generated already create a public private key pair
            keypair = keys.GenerateKeyPair("RSA",1024);
            // write the keypair to disk then get the keys
            writekeys(keypair);
            publickey = getPublic();
        }
        // convert symmetric key to byte array
        byte[] byteKey = symKey.getEncoded();
        // encrypt the symmetric key with own public key
        byte[] encryptedKey = EncryptKey(byteKey,publickey,"RSA");
        // frame the encrypted data in TLV format
        byte[] serialisedData = TLVParser.startFraming(encryptedData,3);
        // frame the ecrypted key in TLV format
        byte[] serialisedKey = TLVParser.startFraming(encryptedKey,2);
        // return the combined frame
        return concat(serialisedKey,serialisedData);
    }
    /**
     * <h1>StartDec </h1>
     * Starts the Decryption process
     * <p>
     * This method starts the decryption process by
     * calling functions to retrieve keys and decrypting the data. </p>
     * @param encdata byte array required to be decrypted
     * @param encKey encrypted symmetric key in byte array format
     * @throws IOException for input output exception
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return decrypted data
     */
    public static byte[] startDec(byte[] encdata,byte[] encKey)
            throws GeneralSecurityException, IOException {
        // get the private key for decrypting the symmetric key
        PrivateKey pvtkey = getPrivate();
        // decrypt the symmetric key
        SecretKey symKey = DecryptKey(encKey,pvtkey,"RSA");
        // decrypt data and return to calling function
        return DecryptData(encdata,symKey,"AES");
    }
    /**
     * <h1>EncryptData </h1>
     * Encrypts the byte array provided to it
     * <p>
     * This method receives the plain byte array,
     * symmetric key and algorithm and returns encrypted byte stream.</p>
     * @param input byte array required to be encrypted
     * @param secretKey symmetric key used for encryption
     * @param cipherAlgorithm algorithm provided for encryption
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return encrypted data
     */
    public static byte[] EncryptData(byte[] input, SecretKey secretKey, String cipherAlgorithm)
        throws GeneralSecurityException{
        cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(input);
    }
    /**
     * <h1>EncryptKey </h1>
     * Encrypts the symmetric key provided to it
     * <p>
     * This method receives the plain symmetric key in form of byte array,
     * public key and algorithm and returns encrypted symmetric key as byte array.</p>
     * @param input byte array required to be encrypted
     * @param pubKey public key used for encryption of key
     * @param cipherAlgorithm algorithm provided for encryption
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return encrypted key
     */
    public static byte[] EncryptKey(byte[] input, PublicKey pubKey, String cipherAlgorithm)
            throws GeneralSecurityException{
        cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(input);
    }
    /**
     * <h1>DecryptData </h1>
     * Decrypts the data provided to it
     * <p>
     * This method receives the encrypted data in form of byte array,
     * symmetric key and algorithm and returns decrypted data as byte array.</p>
     * @param input byte array required to be decrypted
     * @param secretKey symmetric key used for decrypting data
     * @param cipherAlgorithm algorithm provided for decryption
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return decrypted data
     */
    public static byte[] DecryptData(byte[] input, SecretKey secretKey, String cipherAlgorithm)
            throws GeneralSecurityException{
        cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(input);
    }
    /**
     * <h1>DecryptKey </h1>
     * Decrypts the encrypted key provided to it
     * <p>
     * This method receives the encrypted symmetric key in form of byte array,
     * private key and algorithm and returns decrypted symmetric key.</p>
     * @param input byte array required to be encrypted
     * @param privKey private key used for decryption of encrypted symmetric key
     * @param cipherAlgorithm algorithm provided for encryption
     * @throws GeneralSecurityException In case of general security violation occurs
     * @return decrypted symmetric key
     */
    public static SecretKey DecryptKey(byte[] input, PrivateKey privKey,
                                       String cipherAlgorithm)
            throws GeneralSecurityException{
        cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] output = cipher.doFinal(input);
        // this line converts the byte array to secret key
        return new SecretKeySpec(output, 0, output.length, "AES");
    }
    /**
     * <h1>concat </h1>
     * concats two arrays into a single array
     * @param first first byte array
     * @param second second byte array
     * @return concated array
     */
    public static byte[] concat(byte[] first, byte[] second){
        byte[] concat = new byte[first.length + second.length];
        System.arraycopy(first, 0, concat, 0, first.length);
        System.arraycopy(second, 0,concat,first.length,second.length);
        //System.arraycopy(hash, 0, concat, data.length+key.length, hash.length);
        return concat;
    }
    /**
     * <h1>writekeys </h1>
     * writes the generated keys to disk
     * <p>
     * This method is responsible for writing the private and public key
     * once a key pair has been generated into a directory  called KeyValult.</p>
     * @param keypair the key pair to be written into disk
     * @throws IOException In case of any anomaly in input or output.
     */
    public static void writekeys(KeyPair keypair) throws IOException {

        // create a directory called KeyValult
        String path = System.getProperty("user.dir") +
                System.getProperty("file.separator")+"KeyVault";
        new File(path).mkdirs();
        // get the private and public key from the key pair
        PrivateKey pvtkey = keypair.getPrivate();
        PublicKey pubkey = keypair.getPublic();
        // write the private key to file PrivateKey
        String fileName;
        fileName = path+System.getProperty("file.separator")+"PrivateKey";
        FileOutputStream fos = new FileOutputStream( fileName);
        fos.write(pvtkey.getEncoded());
        // write the public key to file PublicKey
        fileName = path+System.getProperty("file.separator")+ "PublicKey";
        fos = new FileOutputStream( fileName);
        fos.write(pubkey.getEncoded());
    }
    /**
     * <h1>getPrivate </h1>
     * Reads the private key from file and returns to the caller
     * @throws NoSuchAlgorithmException In case of Algorithm provided is not valid
     * @throws InvalidKeySpecException In case of invalid key spec provide to encode the bytes into key
     * @throws NoSuchAlgorithmException In case of invalid algorithm
     * @return private key
     */
    public static PrivateKey getPrivate() throws IOException, InvalidKeySpecException,
            NoSuchAlgorithmException {
        // read file private key
        byte[] keyBytes = Files.readAllBytes(new File("KeyVault/PrivateKey").toPath());
        // generate key out of byte array read from file
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    /**
     * <h1>getPublic </h1>
     * Reads the public key from file and returns to the caller
     * @throws NoSuchAlgorithmException In case of Algorithm provided is not valid
     * @throws InvalidKeySpecException In case of invalid key spec provide to encode the bytes into key
     * @throws NoSuchAlgorithmException In case of invalid algorithm
     * @return public key
     */
    public static PublicKey getPublic() throws InvalidKeySpecException,
            NoSuchAlgorithmException, IOException {
        byte[] keyBytes = Files.readAllBytes(new File("KeyVault/PublicKey").toPath());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}

