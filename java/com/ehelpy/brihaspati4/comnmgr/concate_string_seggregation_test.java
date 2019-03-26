package com.ehelpy.brihaspati4.comnmgr;

public class concate_string_seggregation_test {

	public static void main(String[] args) 
	{
		read_concate_string();
	}
	
	public static void read_concate_string()
	{

		String Line = "1234manu";
		int number =0;
		String NodeId=null;
		String IPAdd=null;
	
		number=Line.length();
		NodeId=Line.substring(0,4);
		IPAdd=Line.substring(4,number);
		
		
		System.out.println(""+NodeId);
		System.out.println(""+IPAdd);
	}
}
