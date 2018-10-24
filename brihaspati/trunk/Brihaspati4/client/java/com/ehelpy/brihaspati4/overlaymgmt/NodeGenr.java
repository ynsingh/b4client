package com.ehelpy.brihaspati4.overlaymgmt;
import java.util.Arrays;
import java.util.Random;


	public class NodeGenr {

		//public static void main(String args[]) {
			// TODO Auto-generated method stub
			
			
			
			//NodeGen node = new NodeGen();
			//node.generateNewNode(40);
			//char[] RandNode= node.generateNewNode(40);
			//System.out.println("New Query Node id is " + RandNode);
			//char[] query = node.generateOTP(40);
			//System.out.println(query);
			
	//}
	
//public static void main(String[] args)

//{
//	String str =generateNewNodeId(40);
//	System.out.println(str);
//}
	 public static String generateNewNodeId(int len)
	{
	    //Generating OTP using numeric values
		
	    String numbers = "ABCDEF0123456789";

	    // Using random method
	    Random rndm_method = new Random();

	    char[] NewNode = new char[len];
	 
	    for (int i = 0; i < len; i++)
	    {
	        // Use of charAt() method : to get character value
	        // Use of nextInt() as it is scanning the value as int
	    	NewNode[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
	    }
	    //String charArrayAsString = Arrays.toString(NewNode);
	    //System.out.println(charArrayAsString);
	    
		String newNodeid = String.valueOf(NewNode);
		System.out.println(newNodeid);
		
	    return newNodeid;
	    //NodeGenr myNode = new NodeGenr();
				
	} 
	
	}
	


