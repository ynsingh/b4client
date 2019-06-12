package com.ehelpy.brihaspati4.routingmgmt;

import java.io.IOException;
import java.util.Set;


import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;


public class RMThreadPrimary
{
	public static void update_rt()
	{
		SysOutCtrl.SysoutSet("Starting Routing Module...", 1);

		
		SysOutCtrl.SysoutSet("RMThread running", 1);
		

		String ip_check = null;
		Set<String> keys1 = CommunicationManager.myIpTable.keySet();
	
		for(String key : keys1)   
		{
			ip_check = CommunicationManager.myIpTable.get(key);
			if(!key.equals(OverlayManagement.myNodeId))
				UpdateTabFromQuery.NextEntry(key,ip_check);
		}
        
		SysOutCtrl.SysoutSet("Table saved from RM Thread Primary", 2);
		
		com.ehelpy.brihaspati4.comnmgr.CommunicationManager.updateTable=false;
		
		SysOutCtrl.SysoutSet("------------------------------------------------", 3);
		SysOutCtrl.SysoutSet("------------------------------------------------", 3);
			
	}
	
}

