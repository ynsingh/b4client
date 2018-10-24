package com.ehelpy.brihaspati4.voip;

import java.math.BigInteger;
import java.util.Random;
//import java.util.concurrent.ThreadLocalRandom;

public class random_key {
	
	 
	public static BigInteger generaterandom(int len)
    {
        //Generating OTP using numeric values
		
		Random r = new Random();
        byte[] b = new byte[len];
        r.nextBytes(b);
        BigInteger i = new BigInteger(b);
        return i;
        //System.out.println(i);
    }
}