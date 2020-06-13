package com.ehelpy.brihaspati4.isec;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public final class RightsMgmt {
	private       static boolean  newObj                        = false;    // restricts creation of new instances
	private       static byte[]   tempSalt;
	static {
		try {
			SecureRandom.getInstanceStrong().nextBytes(tempSalt);
		} catch (NoSuchAlgorithmException e) {
			tempSalt = "asjfnaKLJdgkjb".getBytes();
		}
	}
	private final static byte[]   PBE_SALT                      = tempSalt;
	private final static String   PBE_HMAC_SHA256_AES_256       = "PBEWithHmacSHA256AndAES_256";
	
	public  final static String[] AES_CBC_NOPAD_128             = {"AES/CBC/NoPadding"                    , "128" };
	public  final static String[] AES_CBC_PKCS5_128             = {"AES/CBC/PKCS5Padding"                 , "128" };
	public  final static String[] AES_ECB_NOPAD_128             = {"AES/ECB/NoPadding"                    , "128" };
	public  final static String[] AES_ECB_PKCS5_128             = {"AES/ECB/PKCS5Padding"                 , "128" };
	public  final static String[] AES_GCM_NOPAD_128             = {"AES/GCM/NoPadding"                    , "128" };
    
	public  final static String[] DES_CBC_NOPAD_56              = {"DES/CBC/NoPadding"                    , "56"  };
    public  final static String[] DES_CBC_PKCS5_56              = {"DES/CBC/PKCS5Padding"                 , "56"  };
    public  final static String[] DES_ECB_NOPAD_56              = {"DES/ECB/NoPadding"                    , "56"  };
    public  final static String[] DES_ECB_PKCS5_56              = {"DES/ECB/PKCS5Padding"                 , "56"  };
    public  final static String[] DES_EDE_CBC_NOPAD_168         = {"DESede/CBC/NoPadding"                 , "168" };
    public  final static String[] DES_EDE_CBC_PKCS5_168         = {"DESede/CBC/PKCS5Padding"              , "168" };
    public  final static String[] DES_EDE_ECB_NOPAD_168         = {"DESede/ECB/NoPadding"                 , "168" };
    public  final static String[] DES_EDE_ECB_PKCS5_168         = {"DESede/ECB/PKCS5Padding"              , "168" };
    
    public  final static String[] RSA_ECB_PKCS1_1024            = {"RSA/ECB/PKCS1Padding"                 , "1024"};
    public  final static String[] RSA_ECB_PKCS1_2048            = {"RSA/ECB/PKCS1Padding"                 , "2048"};
    public  final static String[] RSA_ECB_OAEP_SHA1_MGF1_1024   = {"RSA/ECB/OAEPWithSHA-1AndMGF1Padding"  , "1024"}; 
    public  final static String[] RSA_ECB_OAEP_SHA1_MGF1_2048   = {"RSA/ECB/OAEPWithSHA-1AndMGF1Padding"  , "1024"};
    public  final static String[] RSA_ECB_OAEP_SHA256_MGF1_1024 = {"RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "1024"};
    public  final static String[] RSA_ECB_OAEP_SHA256_MGF1_2048 = {"RSA/ECB/OAEPWithSHA-256AndMGF1Padding", "1024"};
    
    /**
	 * private constructor to enforce non-instantiability by external code
	 * @throws AssertionError ensures that constructor is NOT invoked accidently (by sub-class, reflection etc)
	 */
    private RightsMgmt() throws AssertionError {if (RightsMgmt.newObj == false) throw new AssertionError();}
    
    public static byte[] encrypt(byte[] data, String[] algoTransKey, Key key) 
    		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
    		IllegalBlockSizeException, BadPaddingException {
    	Cipher cipher = Cipher.getInstance(algoTransKey[0]);
    	cipher.init(Cipher.ENCRYPT_MODE, key);
    	return cipher.doFinal(data);
    }
    
    public static byte[] encrypt(byte[] data, String[] algoTransKey, Certificate certificate) 
    		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
    		IllegalBlockSizeException, BadPaddingException {
    	Cipher cipher = Cipher.getInstance(algoTransKey[0]);
    	cipher.init(Cipher.ENCRYPT_MODE, certificate);
    	return cipher.doFinal(data);
    }
    
    public static byte[] decrypt(byte[] data, String[] algoTransKey, Key key) 
    		throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, 
    		IllegalBlockSizeException, BadPaddingException {
    	Cipher cipher = Cipher.getInstance(algoTransKey[0]);
    	cipher.init(Cipher.DECRYPT_MODE, key);
    	return cipher.doFinal(data);
    }
    
    public static byte[] encryptPBE(byte[] data, char[] password, int itrCount) 
    		throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, 
    		InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	// TODO - Add code to overwrite password array
    	PBEParameterSpec paramSpec = new PBEParameterSpec(PBE_SALT, itrCount);
    	PBEKeySpec keySpec = new PBEKeySpec(password);
    	SecretKey secKey = SecretKeyFactory.getInstance(PBE_HMAC_SHA256_AES_256).generateSecret(keySpec);
    	Cipher cipher = Cipher.getInstance(PBE_HMAC_SHA256_AES_256);
    	cipher.init(Cipher.ENCRYPT_MODE, secKey, paramSpec);
    	return cipher.doFinal(data);
    }
    
    public static byte[] decryptPBE(byte[] data, char[] password, int itrCount) 
    		throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, 
    		InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    	// TODO - Add code to overwrite password array
    	PBEParameterSpec paramSpec = new PBEParameterSpec(PBE_SALT, itrCount);
    	PBEKeySpec keySpec = new PBEKeySpec(password);
    	SecretKey secKey = SecretKeyFactory.getInstance(PBE_HMAC_SHA256_AES_256).generateSecret(keySpec);
    	Cipher cipher = Cipher.getInstance(PBE_HMAC_SHA256_AES_256);
    	cipher.init(Cipher.DECRYPT_MODE, secKey, paramSpec);
    	return cipher.doFinal(data);
    }

    public static byte[] randomPWD(int length) throws NoSuchAlgorithmException {
    	SecureRandom random = SecureRandom.getInstanceStrong();
    	byte[] b = new byte[length];
    	random.nextBytes(b);
    	return b;
	}
}
