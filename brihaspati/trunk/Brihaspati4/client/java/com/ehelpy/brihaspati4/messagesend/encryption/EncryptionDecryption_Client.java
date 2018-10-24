package com.ehelpy.brihaspati4.messagesend.encryption;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.crypto.Cipher.*;

import com.ehelpy.brihaspati4.messagesend.client.WriteToFile;
import com.sun.crypto.provider.RSACipher.*;
import java.io.BufferedReader;
import java.io.FileReader;
import sun.security.rsa.RSAPadding.*;

public class EncryptionDecryption_Client 
{

       public static final String PUBLIC_KEY_FILE = "client_pub.key";
       public static final String PRIVATE_KEY_FILE = "client_pri.key";
       public void saveKeys(String fileName,BigInteger mod,BigInteger exp) throws IOException{
       FileOutputStream fos = null;
       ObjectOutputStream oos = null; 
       fos = new FileOutputStream(fileName);
       oos = new ObjectOutputStream(new BufferedOutputStream(fos));   
       oos.writeObject(mod);
       oos.writeObject(exp);
       if(oos != null)
       {
      oos.close(); 
    if(fos != null)
     {
       fos.close();
          }
       }  
 }
        /**
        * Encrypt Data
        * @param data
        * @throws IOException
        */
 public void encryptData(String data) throws IOException
 {
               
        byte[] dataToEncrypt = data.getBytes();
        byte[] encryptedData = null;
        try {
         PublicKey pubKey = readPublicKeyFromFile("server_pub.key");
         Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         cipher.init(Cipher.ENCRYPT_MODE, pubKey);
         encryptedData = cipher.doFinal(dataToEncrypt);
         
            try (FileOutputStream fos = new FileOutputStream("client_encrypt.txt"))
            {
             fos.write(encryptedData);
             } 
        } 
        catch (Exception e) 
           {
               e.printStackTrace();
           } 
        
      System.out.println("----------------ENCRYPTION COMPLETED------------");  
  }

       /**
        * Encrypt Data
        * @param datacom.ehelpy.brihaspati4.messagesend.clientB
        * @throws IOException
        */
public void decryptData(byte [] data) throws IOException 
 {
        byte[] descryptedData = null;  
        
        try 
        {
         PrivateKey privateKey = readPrivateKeyFromFile("client_pri.key");
         Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         cipher.init(Cipher.DECRYPT_MODE, privateKey);
         descryptedData = cipher.doFinal(data);
          try(  PrintWriter out = new PrintWriter( "Rand_server.txt" )  )
            {
               out.println( new String( descryptedData));
            }
         
        }
        catch (Exception e) 
           {
              e.printStackTrace();
           } 
        
        System.out.println("----------------DECRYPTION COMPLETED------------");  
 }
public void encryptoff_sess(String data) throws IOException
{      
   byte[] dataToEncrypt = data.getBytes();
   byte[] encryptedData = null;
   try {
           PublicKey pubKey = readPublicKeyFromFile("server_pub.key");
           Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
           cipher.init(Cipher.ENCRYPT_MODE, pubKey);
           encryptedData = cipher.doFinal(dataToEncrypt);
           try (FileOutputStream fos = new FileOutputStream("off_session.txt")) {
               fos.write(encryptedData);}
               
           
          } catch (Exception e) {
           e.printStackTrace();
          }
     
}
public void decryptoff_sess(byte [] data1){
     byte[] descryptedData = null;  
        
        try 
        {
         PrivateKey privateKey = readPrivateKeyFromFile("client_pri.key");
         Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
         cipher.init(Cipher.DECRYPT_MODE, privateKey);
         descryptedData = cipher.doFinal(data1);
          try(  PrintWriter out = new PrintWriter( "off_session_server.txt" )  )
            {
               out.println( new String( descryptedData));
            }
         
        }
        catch (Exception e) 
           {
              e.printStackTrace();
           } 
        
        System.out.println("----------------DECRYPTION COMPLETED------------");
    
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
 /**
  * read Public Key From File
  * @param fileName
  * @return PublicKey
  * @throws IOException
  */
 public PublicKey readPublicKeyFromFile(String fileName) throws IOException
 {
     FileInputStream fis = null;
     ObjectInputStream ois = null;
    try 
    {
        fis = new FileInputStream(new File(fileName));
        ois = new ObjectInputStream(fis);
   
        BigInteger modulus = (BigInteger) ois.readObject();
        BigInteger exponent = (BigInteger) ois.readObject();
   
      //Get Public Key
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);
            
      return publicKey;
      
     } 
    catch (Exception e) 
    {
         e.printStackTrace();
    }
  finally{
   if(ois != null){
    ois.close();
    if(fis != null){
     fis.close();
    }
   }
  }
  return null;
 }
 
 /**
  * read Public Key From File
  * @param fileName
  * @return
  * @throws IOException
  */
 public PrivateKey readPrivateKeyFromFile(String fileName) throws IOException
 {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
  try 
  {
        fis = new FileInputStream(new File(fileName));
        ois = new ObjectInputStream(fis);
   
        BigInteger modulus = (BigInteger) ois.readObject();
        BigInteger exponent = (BigInteger) ois.readObject();
   
      //Get Private Key
        RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);
            
      return privateKey;
      
  } 
  catch (Exception e) 
  {
       e.printStackTrace();
  }
  finally
  {
   if(ois != null)
   {
    ois.close();
   if(fis != null)
   {
     fis.close();
    }
   }
  }
  return null;
 }
}
