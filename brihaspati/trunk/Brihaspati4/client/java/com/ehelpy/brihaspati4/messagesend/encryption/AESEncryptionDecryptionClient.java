package com.ehelpy.brihaspati4.messagesend.encryption;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
public class AESEncryptionDecryptionClient {
  private static final String ALGORITHM       = "AES";
 // private String secretKey =WriteToFile.off_sess ;
 // private static final String myEncryptionKey = s ;
  private static final String UNICODE_FORMAT  = "UTF8";
  
  public static String encrypt(String data) throws Exception {

Key key = generateKey();
    byte[] dataToSend = data.getBytes();
    Cipher c = null;
    try {
        c = Cipher.getInstance(ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
  //  SecretKeySpec k =  new SecretKeySpec(key, ALGORITHM);
    try {
        c.init(Cipher.ENCRYPT_MODE, key);
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    byte[] encryptedData = "".getBytes();
    try {
        encryptedData = c.doFinal(dataToSend);
    } catch (IllegalBlockSizeException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    new Base64();
String encryptedByteValue =    Base64.encode(encryptedData);
    return  new String(encryptedByteValue);//.toString();

  }
 
  public static String readFile(String fileName) throws IOException {
   BufferedReader br = new BufferedReader(new FileReader(fileName));
   try {
       StringBuilder sb = new StringBuilder();
       String line = br.readLine();

       while (line != null) {
           sb.append(line);
           //sb.append("\n");
           line = br.readLine();
       }
       return sb.toString();
   } finally {
       br.close();
   }
}
public static String decrypt(String data) throws Exception
{

Key key = generateKey();
    byte[] encryptedData  = new Base64().decode(data);
       Cipher c = null;
       try {
           c = Cipher.getInstance(ALGORITHM);
       } catch (NoSuchAlgorithmException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (NoSuchPaddingException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
     
       try {
           c.init(Cipher.DECRYPT_MODE, key);
       } catch (InvalidKeyException e1) {
           // TODO Auto-generated catch block
           e1.printStackTrace();
       }
       byte[] decrypted = null;
       try {
           decrypted = c.doFinal(encryptedData);
       } catch (IllegalBlockSizeException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       } catch (BadPaddingException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
       return new String(decrypted);
}
public static String decrypt_offsess(byte [] data) throws Exception
{
    byte[] decryptedData = null;
    Key off_key = generateKey2();
    Cipher off_sess = Cipher.getInstance(ALGORITHM);
    off_sess.init(Cipher.DECRYPT_MODE, off_key);
   // byte[] decordedValue1= encryptedValue.getBytes();
    decryptedData = off_sess.doFinal(data);
    String decryptedValue1 = new String(decryptedData);
    return decryptedValue1;
    
}
  public static byte[] encrypt_offsess(String valueToEnc) throws Exception {

	  //  System.out.println(s);
		  byte[] dataToEncrypt = valueToEnc.getBytes(UNICODE_FORMAT);
		  byte[] encryptedData = null;
		  Key off_key = generateKey1();
		  Cipher off_c = Cipher.getInstance("AES");
		  off_c.init(Cipher.ENCRYPT_MODE, off_key); 
		  
		  encryptedData = off_c.doFinal(dataToEncrypt);
		 // String encryptedValue1 = new sun.misc.BASE64Encoder().encode(encValue1);
		  return encryptedData;
  }
private static Key generateKey() throws Exception {
    byte[] keyAsBytes;
    String s =readFile("SessionKey.txt");
    keyAsBytes =s.getBytes(UNICODE_FORMAT);
    Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
    return key;
}
private static Key generateKey2() throws Exception {
    byte[] keyAsBytes;
    String s =readFile("off_session_server.txt");
    keyAsBytes =s.getBytes(UNICODE_FORMAT);
    Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
    return key;
}
private static Key generateKey1() throws Exception {
    byte[] keyAsBytes;
    String s =readFile("off_sess_client.txt");
    keyAsBytes =s.getBytes(UNICODE_FORMAT);
    Key key = new SecretKeySpec(keyAsBytes, ALGORITHM);
    return key;
}
}