package com.ehelpy.brihaspati4.sms;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionTest {

	  public static void main(String[] args) {
	    try {

	      String key = "ThisIsASecretKey";
	      byte[] ciphertext = encrypt(key, "hi this is msg");
	      
	      String encr_msg = Base64.getEncoder().encodeToString(ciphertext);
	      byte[] bytes_cipher = Base64.getDecoder().decode(encr_msg);
	      
	      System.out.println("encrpted value: "+encr_msg );
	      System.out.println("decrypted value:" + (decrypt(key, bytes_cipher)));

	    } catch (GeneralSecurityException e) {
	      e.printStackTrace();
	    }
	  }

	  public static byte[] encrypt(String key, String value)
	      throws GeneralSecurityException {

	    byte[] raw1 = key.getBytes(Charset.forName("UTF-8"));
	    
	    String encr_msg = Base64.getEncoder().encodeToString(raw1);
	    byte[] raw = Base64.getDecoder().decode(encr_msg);
	    
	    if (raw.length != 16) {
	      throw new IllegalArgumentException("Invalid key size.");
	    }

	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec,
	        new IvParameterSpec(new byte[16]));
	    return cipher.doFinal(value.getBytes(Charset.forName("UTF-8")));
	  }

	  public static String decrypt(String key, byte[] encrypted)
	      throws GeneralSecurityException {
		  
		  byte[] raw = key.getBytes(Charset.forName("UTF-8"));
		    if (raw.length != 16) {
		      throw new IllegalArgumentException("Invalid key size.");
		    }
		    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		    cipher.init(Cipher.DECRYPT_MODE, skeySpec,
		        new IvParameterSpec(new byte[16]));
		    byte[] original = cipher.doFinal(encrypted);

		    return new String(original, Charset.forName("UTF-8"));
		  }
		}
	  
