package com.ehelpy.brihaspati4.routingmgmt;

import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

import java.io.File;
import java.util.Collection;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

public class UpdateIp_New extends test_one
{

	public static String MyIP = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
	public static String IPAdd;
	public static boolean CheckIP=true;
	
	public static void Change_In_IP_Check()
	{
		while (CheckIP==true)
		{
			IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
			if(!MyIP.equals(IPAdd))
			{
				CheckIP=false;
			}
		}	
		
		System.out.println("MY old IP was : "+MyIP);
				
		MyIP = IPAdd;
			
		//String email_hash = IndexManagement.selfHashId;
	    //String myNodeId = OverlayManagement.myNodeId;
		//IndexManagementUtilityMethods.addIndexRequest(email_hash, myNodeId);*/
		
	/*	Collection<String> Node_id_extracted = CommunicationManager.myIpTable.keySet();
		Object[] Nodeid_array = Node_id_extracted.toArray();

		for(int i=0; i<Nodeid_array.length;i++) 
		{
			String Node_id= (String) Nodeid_array[i];
			System.out.println(" "+Node_id);
			File IPUpdate = IndexManagementUtilityMethods.createXmlUpdate_IP(Node_id);
			CommunicationManager.TransmittingBuffer.add(IPUpdate);
			SysOutCtrl.SysoutSet("Updated IP file sent to TransmittingBuffer :"+Node_id, 2);
			SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
		}*/
		
		System.out.println("Updating my new Ip at Resposible Node");
		System.out.println("My New IP is : "+MyIP);
			
		CheckIP=true;
				
		Change_In_IP_Check();
		
	}
}
