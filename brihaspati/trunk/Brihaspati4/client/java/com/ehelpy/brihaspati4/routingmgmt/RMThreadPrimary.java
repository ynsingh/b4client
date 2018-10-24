package com.ehelpy.brihaspati4.routingmgmt;

//import sml.CommunicationManager;
//import utilityconfig.SysOutCtrl;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
// 11th April 2018 1245h

public class RMThreadPrimary extends Thread
{
//	public static int CtrlConsoleOut=com.ehelpy.brihaspati4.authenticate.ClientMain.print_control;
//	public static int CtrlConsoleOut= 3;

	public void run()
	{
		RTUpdate9 RTUpdate = new RTUpdate9();
		RTUpdate.InitiateRT();
		
		UpdateTabFromQuery UpdateRTfromNodeList = new UpdateTabFromQuery();
		UpdateRTfromNodeList.start();
		UpdateRTfromNodeList.setName("Update from Query");
		
		SysOutCtrl.SysoutSet("Starting Routing Module...", 1);

		while(true) 
		{
//		First Thread(RM1) will update the self IP add and get Node ID from OM, presently self ID is hard coded
//		-	It will intimate OM of any changes in the IP address, presently it is checking the IP every 3 sec,
//			will be changed to 15 min.
		
		
//		Second (RM2) will check for any saved RTs, 
//		-	if yes then will load after comparing each of them with self node otherwise will start with null values

		
//		Third Thread(RM3) will take inputs from the query Buffer to return next HOP based on query.
//		-	Query will be parsed from the main RM buff(containing xml files)
//		-	These files will then be parsed to get IP add of the asking node and the query node string		
//		-   The String node will be saved in the GiveNextHop i/p buff along with the Asking node id.
//		-	Next Hop will be returned and saved in GiveNextHop o/p buff along with asking node id.
		
		

//		THREAD RM1(Shifted to P2P main, comn mgr)
		
//		UpdateIP IPUpdate = new UpdateIP();
//		
//		IPUpdate.start();
//		IPUpdate.setName("IPUpdate");
//		SysOutCtrl.SysoutSet("Thread Id : "+IPUpdate.getName(), 1);
		
		
//		THREAD RM2
		
//		RTUpdate9 RTUpdate = new RTUpdate9();
//		RTUpdate.InitiateRT();

//		Thread RMUpdate through fromNodeIdList Comn Mgr
//		The dummy List is instantiated in RTUpdate9,
		
		
//		UpdateTabFromQuery UpdateRTfromNodeList = new UpdateTabFromQuery();
//		UpdateRTfromNodeList.start();
//		UpdateRTfromNodeList.setName("Update from Query");
		SysOutCtrl.SysoutSet("Thread Id : "+UpdateRTfromNodeList.getName(), 1);
		
		//GiveNextHop.NextHop("6666666666666666666666666666666666666666");

		SysOutCtrl.SysoutSet("RMThread running", 1);
		
		boolean OM_Purge; // to be set by OM when a node is to be purged
		
//			if(com.ehelpy.brihaspati4.comnmgr.CommunicationManager.updateTable=true)
//			{
					while(CommunicationManager.fromNodeIdList.size()>0)
					{
					UpdateRTfromNodeList.NextEntry(CommunicationManager.fromNodeIdList.removeFirst());
					
					Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
					save.Save_RTNow();
					SysOutCtrl.SysoutSet("Table saved from RM Thread Primary", 2);
			
					PrintRT9 PrintRT = new PrintRT9();
					PrintRT.PrintMatrix();
						if(CommunicationManager.fromNodeIdList.size()==0)
						{
							com.ehelpy.brihaspati4.comnmgr.CommunicationManager.updateTable=false;
//							break;
						}
					}
					
//			}

		
//				SML OM_Purge
				
//				OM_Purge = false; // set OM_Purge value from OM, when any Node is dead from Heart Beat monitor,
//								 //	this is set to true.		
//		        
//				SysOutCtrl.SysoutSet("Purge value :"+OM_Purge, 2);
//				
//				while(OM_Purge)
//				{
//						PurgeEntry CleanRT = new PurgeEntry();
////						CleanRT.purge("F8055D147FA3CEA159468FDDF7147C003CCF5BB4");
//		
//						CleanRT.purge("C8055D147FA3CEA159468FDDF7147C003CCF5BB3");
//						
//						OM_Purge =false;
//						SysOutCtrl.SysoutSet("Purge Value set to "+OM_Purge+" after updating Table", 2);
//				}

			SysOutCtrl.SysoutSet("------------------------------------------------", 3);
			SysOutCtrl.SysoutSet("------------------------------------------------", 3);
			
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				SysOutCtrl.SysoutSet("Sleep 1500");
			}

	}

}
}

