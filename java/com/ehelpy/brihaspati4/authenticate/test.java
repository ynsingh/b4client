package com.ehelpy.brihaspati4.authenticate ; 
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.GeneralSecurityException;

public class test {

	public static KeyStore loadKeyStore(String keystorelocation, String password){
		
		//request a KeyStore object of default type.
		// default type:  KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		//Before a keystore can be accessed, it must be loaded.
	    KeyStore keystore = null;
		try {
				keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e1) {
			e1.printStackTrace();
		}
 	  	FileInputStream is = null;
      	try {
      			//FileInputStream(String name)-This creates a FileInputStream by opening a connection to an actual file,
      			//the file named by the path name name in the file system.
      			is = new FileInputStream(keystorelocation);
      			
         		//Loads this KeyStore from the given input stream location.
	     		keystore.load(is, password.toCharArray()); 
	     		// toCharArray - Converts this string to a new character array.
	     		//Returns:a newly allocated character array whose length is the length of this string and 
	     		//whose contents are  character sequence represented by this string
	     		
      	} catch (Exception e) {
 			//Logger add to log with date, time, IP address, timezone - TBD}
      	}finally { 	
      		if (null != is) {
     		 	try {
     		 			is.close();
     		 	} catch (IOException e) {
     		 		e.printStackTrace();
     		 	}
     		 }
      	} 
        return keystore;  
	}

		 
	public static void main(String[] args) throws GeneralSecurityException, IOException {
		String keystorelocation="C:\bCodeRepository\bIS\btest5may.JKS";
		String keystorepass="vikhyat5may";
		KeyStore keystore = loadKeyStore(keystorelocation,keystorepass);
		System.out.println("keystore is loaded.keystore is =   "+ keystore);
		System.out.println("aliases of this keystore are is =   "+ keystore.aliases());
		//sun.security.tools.keytool.CertAndKeyGen keypair = new sun.security.tools.keytool.CertAndKeyGen("RSA", "SHA1WithRSA", null);
		//PrivateKey pvtKey = keypair.getPrivateKey();
		//createKeyStore("test4.JKS", "123456", "VIKH", pvtKey, null);
	}

}
