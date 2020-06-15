package com.ehelpy.brihaspati4.Encryption;

import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;
import com.ehelpy.brihaspati4.isec.MsgIntegrity;
import static com.ehelpy.brihaspati4.isec.MsgIntegrity.SIGN_SHA_256_RSA;
/**
 * <h1>GenerateKeys</h1>
 * Class responsible for Generating all type of keys
 * <p>
 * This class generates Symmetric key and private-public key pair
 * according to the length specified for encryption.</p>
 * <b>Functions:</b> At the user end of DFS except for verifyHash
 * <b>Note:</b> Change this file to change key length
 * @author  Sidharth Patra
 * @version 2.0
 * @since   2020-02-12
 */
public class GenerateKeys {

    private static KeyPair pair = null;
    /**
     * <h1>GenerateKeyPair </h1>
     * Generates the private-public key pair
     * <p>
     * This method generates the private-public key pair
     * of requisite length and algorithm.</p>
     * @param algo algorithm used for generating the keys
     * @param keylength length of the key desired
     * @throws NoSuchAlgorithmException In case of algorithm is invalid
     * @return keypair consisting of private-public keys
     */
    public static KeyPair GenerateKeyPair(String algo,int keylength)
        throws NoSuchAlgorithmException{
        // Generates a KeyPairGenerator object that implements the algorithm requested
        // initialise the constructor with the requisite keylength
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algo);
        keyGen.initialize(keylength);
        pair = keyGen.generateKeyPair();
        return pair;
    }
    /**
     * <h1>GenerateSymmetricKey </h1>
     * Generates a symmetric key
     * <p>
     * This method generates a symmetric key
     * of requisite length and algorithm.</p>
     * @param algo algorithm used for generating the keys
     * @param length length of the key desired
     * @throws NoSuchAlgorithmException In case of algorithm is invalid
     * @return symmetric key generated using KeyGenrator API
     */
    public static SecretKey GenerateSymmetricKey(String algo, int length)
            throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algo);
        keyGenerator.init(length);
        return keyGenerator.generateKey();//return the secretkey
    }
    /**
     * <h1>signHash </h1>
     * Sign a hash for authentication.
     * <p>
     * This method signs hash of a file using private key of the
     * sender. The hash confirms integrity where as signed hash
     * confirm both integrity and authentication</p>
     * @param input byte array whose hash has to be computed
     * @throws IOException for input output exception
     * @throws SignatureException Incase signature fails
     * @throws InvalidKeyException for tackling invalid key provided
     * @throws InvalidKeySpecException Incase of invalid private key is used
     * @return framed and signed hash of input
     */
    public static byte[] signHash(byte[] input) throws NoSuchAlgorithmException,
            InvalidKeyException, SignatureException, IOException, InvalidKeySpecException {
        //get the private key of user
        PrivateKey privatekey = Encrypt.getPrivate();
        // sign the hash using privatekey with SHA-256
        byte[] signedHash = MsgIntegrity.getSignature(input,privatekey,SIGN_SHA_256_RSA);
        // frame the signed hash in TLV format and return it
        return TLVParser.startFraming(signedHash,1);
    }
    /**
     * <h1>verifyHash </h1>
     * verify Signed hash against unsigned hash.
     * <p>
     * This method checks a signed hash against unsigned hash and
     * verifies integrity</p>
     * @param hashComputed computed hash of data
     * @param signedHash signed hash of data
     * @throws IOException for input output exception
     * @throws SignatureException Incase signature fails
     * @throws InvalidKeyException for tackling invalid key provided
     * @throws InvalidKeySpecException Incase of invalid private key is used
     * @throws NoSuchAlgorithmException Incase the specified algorithm is not found
     * @return hashSigned which is true or false depending on the result of verification
     */
    public static boolean verifyHash(byte[] hashComputed,byte[] signedHash)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException,
            InvalidKeySpecException, IOException {
        // get the users public key
        PublicKey publicKey = Encrypt.getPublic();//TODO - take publickey from certificate
        // call teh verifysignature method to verify  signed hash
        boolean hashSigned = MsgIntegrity.verifySignature
                (hashComputed,signedHash,publicKey,SIGN_SHA_256_RSA);
        return hashSigned;
    }

}
