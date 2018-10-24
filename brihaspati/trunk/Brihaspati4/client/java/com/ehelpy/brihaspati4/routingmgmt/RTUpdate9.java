package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 24 Feb 2018
import java.io.BufferedReader;
import java.util.LinkedList;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;



//import threadimplementation.Save_Retrieve_RT;

public class RTUpdate9 extends Thread
{
			
		public static String Succ[] = new String[40];
		public static String Pred[] = new String[40];
		public static String Mid[] = new String[40];
		public static String[][] Matrix = new String[3][40];
		public static String[][] mat = new String[3][40];
		public static BufferedReader BR = null;
		public static String[] RoutingInptBuff = new String[120];

		 String nodeid= OverlayManagement.myNodeId;
	    
		public synchronized void InitiateRT()
		
		{
			SysOutCtrl.SysoutSet("mynodeid in RTUpdate9"+nodeid,3);
			Save_Retrieve_RT.Retrieve_RT Read = new Save_Retrieve_RT.Retrieve_RT();
			RoutingInptBuff=Read.Retrieve_RT_Now();	
			
			
			UpdateTabFromQuery PopRT = new UpdateTabFromQuery();
			
			String RoutingQuery;
			for(int i=0;i<120;i++)//RoutingInptBuff.size();i++)
			{
				if(RoutingInptBuff[i]!=null)
				{
					RoutingQuery = RoutingInptBuff[i].toString();
					SysOutCtrl.SysoutSet(i+" "+RoutingQuery,3);
					PopRT.NextEntry(RoutingQuery);
					SysOutCtrl.SysoutSet("i :"+i,3);
				}
			
				else
				{}
		}

//	 		Uncomment the following to check output of RT Update.
			
//		SysOutCtrl.SysoutSet("following from RTUpdate9", 3);	
//		PrintRT9 PrintMat = new PrintRT9();
//		PrintMat.PrintMatrix();

//		SysOutCtrl.SysoutSet("RT Saved thorugh RTUpdate9", 3);	
//		Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
//		save.Save_RTNow();
//		
//		SysOutCtrl.SysoutSet("DONE",3);
		
		}	
	}		
			

