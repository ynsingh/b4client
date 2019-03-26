package com.ehelpy.brihaspati4.routingmgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIp_New_for_testing;;

public class test_one 
{
	
	public static String[][] NodesInRT = new String[120][2];
	static int LenP;
	public static String Succ[][] = new String[40][2];
	public static String Pred[][] = new String[40][2];
	public static String Mid[][] = new String[40][2];
	public static String[][] Matrix = new String[3][40];
	public static String[][] mat = new String[3][40];
	public static BufferedReader BR = null;
	public static String[][] RoutingInptBuff = new String[120][2];

	 String nodeid= OverlayManagement.myNodeId;
	
	
	
		static int LenS;
		static FileWriter RTWriter = null;
	public static void main(String[] args) throws InterruptedException, IOException
	{
		Pred[1][0]="E2AF6DE804BE0AD29154A6C15654371E08E3AD72";
		Pred[1][1]="172.25.75.56";
		Pred[2][0]="9999999999999999999999999999999999999999";
		Pred[2][1]="172.20.160.56";
		Succ[1][0]="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		Succ[1][1]="172.25.75.56";
		Mid[2][0]="BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
		Mid[2][1]="172.20.160.56";
		try {
		RTWriter = new FileWriter("RTP2P.txt");
		
		
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		PrintWriter wri = new PrintWriter(RTWriter);
		for (int i = 0; i < Pred.length; i++)
		{
			if(Pred[i][0]!=null)
			{
    		NodesInRT[i][0] = Pred[i][0];
    		NodesInRT[i][1] = Pred[i][1];
			}
			else
    		{}
    	}
		LenP = 39;
	 	SysOutCtrl.SysoutSet("Lenght in Pred is :"+LenP,3);
    		
		for (int i = 0; i < Succ.length; i++)
		{	
			if(Succ[i][0]!=null)
			{
				SysOutCtrl.SysoutSet("Lenght in Pred is :"+LenP,3);
				NodesInRT[LenP+i][0] = Succ[i][0];
				NodesInRT[LenP+i][1] = Succ[i][1];
			}	 
			else
			{
    		/*LenS=LenP+i;
    		SysOutCtrl.SysoutSet("LenP + LenS is :"+ LenS,3);
    		break;*/
			}
    	}
    	LenS=79;	
		for (int i = 0; i < Mid.length; i++)
		{
			if(Mid[i][0]!=null)
			{
    		NodesInRT[LenS+i][0] = Mid[i][0];
    		NodesInRT[LenS+i][1] = Mid[i][1];
			} 
		}
    
		for(int i=0;i<NodesInRT.length;i++)
		{
			if(NodesInRT[i][0]!=null)
			{
    		SysOutCtrl.SysoutSet(i+" Node in RT :"+ NodesInRT[i][0],3);
    			try 
    			{
    			String a= 	NodesInRT[i][0];
    			a=a.concat(NodesInRT[i][1]);
    			wri.write(a);
    			wri.println("");
    			}
    			catch (NullPointerException  e) 
    			{
    			//e.printStackTrace();
    			}
			}	
		}
		
		try 
		{
		RTWriter.flush();
		} 
		catch (IOException e) 
		{
		// TODO Auto-generated catch block
//		e.printStackTrace();
		}
		try 
		{
			BR = new BufferedReader(new FileReader("RTP2P.txt"));
			
//			BR = new BufferedReader(new FileReader("D:\\roy\\aaaaaaroy\\IITK\\Java practice\\Wk space prac\\Trial\\RTP2P.xml"));
//			Change address above for testing purposes
			
			
			String Line;
			String NodeId;
			String Ipadd;
			try
			{	
		
			for(int l=0;l<120;l++)
			{
				
				Line = BR.readLine();
				int number=Line.length();
				if (Line!=null)
				{
					NodeId = Line.substring(0,40);
					Ipadd= Line.substring(40,number);
					RoutingInptBuff[l][0] = NodeId;
					RoutingInptBuff[l][1] = Ipadd;
					SysOutCtrl.SysoutSet(l+" Entry in RotingInputBuff :"+RoutingInptBuff[l][0]+RoutingInptBuff[l][1],3);
					System.out.println(l+" Entry in RotingInputBuff :"+RoutingInptBuff[l][0]+RoutingInptBuff[l][1]);
				/*int Length = Line.length();
				int Iterator = Length/40;
					for(int l=0,j=0;l<Iterator;j=j+40,l++)
					{
							NodeId = Line.substring(j,j+40);
//							String Node = line.substring(j,j+40);
							RoutingInptBuff[l] = NodeId;
							SysOutCtrl.SysoutSet(l+" Entry in RotingInputBuff :"+RoutingInptBuff[l],3);
					}*/	
				 } 	
			}	
			}catch (NullPointerException e) 
			{
			}
		}
			
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			//				e.printStackTrace();
			SysOutCtrl.SysoutSet("RT NOT FOUND, by Save_Retrieve_RT",3);
			SysOutCtrl.SysoutSet("Initialising NULL RT...",3);
			
			for(int m=0;m<120;m++)
			{
				RoutingInptBuff[m]=null;
				SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m],3);
		    }
		}
		
		}
	
	}
	



	
			
	

