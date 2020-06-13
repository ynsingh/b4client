package com.ehelpy.brihaspati4.isec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class KeyMgmt {
	private       static boolean  newObj           = false;         // restricts creation of new instances
	private final static int      ASM_KEY_SIZE     = 1024;
	private final static int      SYM_KEY_SIZE     = 128;
	
	public  final static String[] ASM_DH_1024      = {"DiffieHellman", "1024"   };
	public  final static String[] ASM_DH_2048      = {"DiffieHellman", "2048"   };
	public  final static String[] ASM_DH_4096      = {"DiffieHellman", "4096"   };
	public  final static String[] ASM_DSA_1024     = {"DSA"          , "1024"   };
	public  final static String[] ASM_DSA_2048     = {"DSA"          , "2048"   };
	public  final static String[] ASM_RSA_1024     = {"RSA"          , "1024"   };
	public  final static String[] ASM_RSA_2048     = {"RSA"          , "2048"   };
	public  final static String[] ASM_RSA_4096     = {"RSA"          , "4096"   };
	
	public  final static String[] SYM_AES_128      = {"AES"          , "128"    };
	public  final static String[] SYM_DES_56       = {"DES"          , "56"     };
	public  final static String[] SYM_DES_EDE_168  = {"DESede"       , "168"    };
	public  final static String[] SYM_HMAC_SHA1    = {"HmacSHA1"     , ""       };
	public  final static String[] SYM_HMAC_SHA256  = {"HmacSHA256"   , ""       };
	
	public  final static String[] CRT_X509_PKCS7   = {"X.509"        , "PKCS7"  };
	public  final static String[] CRT_X509_PKIPATH = {"X.509"        , "PKIPATH"};

	/**
	 * private constructor to enforce non-instantiability by external code
	 * @throws AssertionError ensures that constructor is NOT invoked accidently (by sub-class, reflection etc)
	 */
    private KeyMgmt() throws AssertionError {if (KeyMgmt.newObj == false) throw new AssertionError();}
    
	public static KeyPair genKeyPair(String[] algoKey) 
			throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(algoKey[0]);
		SecureRandom sRandom = SecureRandom.getInstanceStrong();
		keyPairGen.initialize(Integer.getInteger(algoKey[1], ASM_KEY_SIZE), sRandom);
		System.out.println("you are here");
		return keyPairGen.generateKeyPair();
	}
	
	public static SecretKey genKeySecret(String[] algoKey) 
			throws NoSuchAlgorithmException {
		KeyGenerator keySecGen = KeyGenerator.getInstance(algoKey[0]);
		SecureRandom sRandom = SecureRandom.getInstanceStrong();
		keySecGen.init(Integer.getInteger(algoKey[1], SYM_KEY_SIZE), sRandom);
		return keySecGen.generateKey();
	}
	
	//public static Certificate genCertificate(String[] typeEncode) {
	//	CertificateFactory certFac = CertificateFactory.getInstance(typeEncode[0]);
	//}
	
}
