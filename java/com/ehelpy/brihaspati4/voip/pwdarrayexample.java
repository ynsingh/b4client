package com.ehelpy.brihaspati4.voip;

import java.util.Arrays;

public class pwdarrayexample

{
	public static long symkeyArray[] = new long[10];
	
	public static long[] main() {

		Long symkey = null;

		for(int i = 0; i<10; i++) {
			symkey = encrypt_msg.generaterandomnumber(9);
			symkeyArray[i] = symkey;
			//System.out.println("Array symkey"+i+" = " + symkeyArray[i]);
			
		}
		
		//System.out.println("Array symkey is = " + Arrays.toString(symkeyArray));
		return symkeyArray;
		
	}
}
