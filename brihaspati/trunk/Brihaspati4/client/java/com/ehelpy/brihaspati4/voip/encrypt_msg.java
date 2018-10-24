package com.ehelpy.brihaspati4.voip;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.ehelpy.brihaspati4.authenticate.debug_level;

public class encrypt_msg {
	public static byte[] encrypt_message(PublicKey publicKey, byte[] input) 
	{
		byte[] output = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/NoPadding ");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			output = cipher.doFinal(input);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}  
	      catch (InvalidKeyException e1) {
				e1.printStackTrace();
			}  

	      catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			}
		debug_level.debug(1," The encrpty msg routine has completed");
		
		return output;
	}
	public static byte[] encrypt_calldata(SecretKey seckey, byte[] packet) 
	{
		byte[] output = null;
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/NoPadding ");
			cipher.init(Cipher.ENCRYPT_MODE, seckey);
			output = cipher.doFinal(packet);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		}  
	      catch (InvalidKeyException e1) {
			e1.printStackTrace();
			}  

	      catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			} catch (BadPaddingException e) {
			e.printStackTrace();
			}
		debug_level.debug(1,"The encrypt call data subroutine completed");
		return output;
	}

	public static long generaterandomnumber(int len)
    {
        //Generating OTP using numeric values
		
        String numbers = "0123456789";
 
        // Using random method
        Random rndm_method = new Random();
 
        char[] rand = new char[len];
	 
        for (int i = 0; i < len; i++)
        {
            // Use of charAt() method : to get character value
            // Use of nextInt() as it is scanning the value as int
        	rand[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
        }
        String stringValueOfOTP = String.valueOf(rand);
        long random = Long.valueOf(stringValueOfOTP);
        return random;
    }

}
