package com.ehelpy.brihaspati4.routingmgmt;

import com.ehelpy.brihaspati4.indexmanager.SHA1;

public class test_next_hop {

	public static void main(String[] args) 
	{
		String selfHashId= SHA1.getSha1("abhsingh@iitk.ac.in");
		System.out.println("Hash of email : "+selfHashId);
		String tonodeId = com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(selfHashId);
		System.out.println("Node Id : "+tonodeId);
	//	System.out.println("List A : "+com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.A);

	}

}
