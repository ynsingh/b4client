package com.ehelpy.brihaspati4.voip;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class decrypt_msg {
	public static byte[] decrypt_message(PrivateKey privateKey,byte[] input) {
		//String output = null;
		Cipher cipher = null;
		byte[] part_output = null;
		try {
			debug_level.debug(1,"The length of input stream is" + input.length);
			cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}  
        try {
        	debug_level.debug(1,"The decrypt msg routine entered decrypt mode");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
        try {
        	debug_level.debug(1,"Decryption completed");
        	
			part_output = cipher.doFinal(input);
			return part_output;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return part_output;
       	
		
	}
	@SuppressWarnings("null")
	public static byte[] decrypt_calldata(SecretKey seckey, byte[] packet) 
	{
		byte[] original = null;
		
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher.init(Cipher.DECRYPT_MODE, seckey);
		    original = cipher.doFinal(packet);
		    debug_level.debug(1,"The apcket to be decrypted is" + original);
		    return original;
		} catch (InvalidKeyException e1) {
				e1.printStackTrace();
			}  

	      catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			}  
		return original;
	}
	public static byte[] unpadZeros(byte[] in) {
	    int i = 0;
	    while(in[i] == 0)
	    { 	i++;}
	    return Arrays.copyOfRange(in, i, in.length);
	}

}
