package com.ehelpy.brihaspati4.overlaymgmt;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Random;
import java.io.File;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Byte;

public class CorrectHex {
	int num_start= 47;
	int num_end = 58;
	int char_start = 86;
	int char_end = 103;
	public void CorrectHex(){
	}
	
	// this method prepares the Unicode values recognized by Java( esp. 'a' to 'f')
	//for the the mathematical calculations in the code.It serializes 
	//the entire set of Hex characters from 0 to f on our Hex ring.
	public byte CorrectHexDigit(byte test1){
		int test1_int = (int)(test1);
		// For "0" to "9"
		if (test1<58){
			test1 = (byte)(test1_int-48);
			// "0" in Unicode becomes "0" in 						
			// decimal/binary(byte).
			// Similarly "1" in Unicode becomes "1" 
			// in decimal/binary(byte)and so on.
		}
		// For "a" to "f"
		else if (test1>58){
		test1 = (byte)(test1_int-87);
			// "a" in Unicode becomes "10" in 						
			// decimal/binary(byte).
			// Similarly "f" in Unicode becomes "15" 
			// in decimal/binary(byte)and so on.
		}
	
	return test1;
	}

	/*public byte CorrectHexDigit(byte test1){
		int test1_int = (int)(test1);
		if (test1<58){
			test1 = (byte)(test1_int-48);// "0" in unicode  becomes "0" in decimal/binary.
		}						// Similarly "1" in unicode  becomes "1" in decimal/binary etc.
		else if (test1>58){
			test1 = (byte)(test1_int-87); //"a" in unicode  becomes "a" in decimal/binary value etc.
			
		}
		
		return test1;
	
	}*/
	
	//#########################################################################
	// Similarly as above method but for converting the complete hex character String
	// into corresponding modified decimal/binary(byte) values.
	public byte[] CorrectHexString(String test2){
		byte[] test2bytearray = test2.getBytes();// convert to String of bytes
		for(int g=0; g<test2.length(); g++){
			int test2_int = (int)(test2bytearray[g]);
				if (test2bytearray[g]<58){
					test2bytearray[g] = (byte)(test2_int-48);
				}
				else if (test2bytearray[g]>58){
					test2bytearray[g] = (byte)(test2_int-87);
			
				}				
		}
		return test2bytearray;  //string converted to corrected byte array(follows numeric rules)
	}



//##################################################################################
	// Similarly as above method but for converting a hex character array
		// into corresponding modified decimal/binary(byte) values.
	public byte[] CorrectHexArray(char[] test3){
		
	byte[] test3bytearray = new byte[4];
	
	for(int g=0; g<test3.length; g++){	
		test3bytearray[g] = (byte)(test3[g]);// convert to array of bytes
		int test3_int = (int)(test3bytearray[g]);// convert to int
			if (test3bytearray[g]<58){
				test3bytearray[g] = (byte)(test3_int-48);
			}
			else if (test3bytearray[g]>58){
				test3bytearray[g] = (byte)(test3_int-87);
		
			}				
	}return test3bytearray;
	
	} 
}


