package com.ehelpy.brihaspati4.indexmanager;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

public class test_my_index 
{
	public static String node_id = "";
	public static void main(String[] args)
	{
		String email = "manurajpal@gmail.com";
		String hash_email = SHA1.getSha1(email);
		System.out.println("hash of email : "+hash_email);
		System.out.println("N"+OverlayManagement.myNodeId);
		node(hash_email);
	}
	
	public static void node(String hash_email)
	{
		node_id = IndexManagementUtilityMethods.Search_in_Index(hash_email);
		System.out.println("Node id : "+node_id);
		System.out.println(""+IndexManagement.myindex);
	}
}
