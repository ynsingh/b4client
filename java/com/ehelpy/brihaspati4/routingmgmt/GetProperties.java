package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 1205h 7 Jun 2018 
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;

public class GetProperties 
{
	public static BufferedReader BR1=null;
	public static String Property_NodeId1;
	public static String Property_IP1;
	public static String Property_NodeId2;
	public static String Property_IP2;
	public static int Property_sysout;
	public static boolean Backbone;	
	
	public static void main(String[] args) throws IOException  
	{
		// TODO Auto-generated method stub
		 
		Debug A = new Debug();
		A.Properties();
		SysOutCtrl.SysoutSet("This is a backbone machine :"+Backbone);
		SysOutCtrl.SysoutSet("Node id 1 :"+Property_NodeId1);
		SysOutCtrl.SysoutSet("IP for Node id 1 :"+Property_IP1);
		SysOutCtrl.SysoutSet("Node id 2 :"+Property_NodeId2);
		SysOutCtrl.SysoutSet("IP for Node id 2 :"+Property_IP2);
		SysOutCtrl.SysoutSet("SysOutCtrl set at :"+Property_sysout);
		
		System.out.println("This is a backbone machine :"+Backbone);
		System.out.println("Node id 1 :"+Property_NodeId1);
		System.out.println("IP for Node id 1 :"+Property_IP1);
		System.out.println("Node id 2 :"+Property_NodeId2);
		System.out.println("IP for Node id 2 :"+Property_IP2);
		System.out.println("SysOutCtrl set at :"+Property_sysout);
		
		
	}

public static class Debug 
// used for filling up specific NodeIp table for debugging 
// Also we can set up  the console sysout for deployment or debugging
{
	public static void Properties() throws IOException
	{
		try 
		{
			
			BR1 = new BufferedReader(new FileReader("Debug.txt"));
					
			String Line;
			String number =null;
			
			try 
				{
				Line = BR1.readLine();
				
				if(Line.startsWith("t"))
					{
				Backbone = true;
				SysOutCtrl.SysoutSet("we are in a Backbone machine");

					}
				
				else 
					{
				Backbone = false;
				SysOutCtrl.SysoutSet("we are in a peer machine");
					}
				
				
				for(int i=1;i<4;i++) 
					{
						Line = BR1.readLine();
						
								if(Backbone == true)
								{	
									if (i==1)
									{
										int Length = Line.length();
										Property_NodeId1 	= Line.substring(0,40);
										Property_IP1 		= Line.substring(40,Length);
									}
							
									if (i==2)
									{
										int Length = Line.length();
										Property_NodeId2 	= Line.substring(0,40);
										Property_IP2 		= Line.substring(40,Length);
									}
								
									if (i==3)
									{
										number = Line;
										Property_sysout =Integer.parseInt(number);
									}
								}
								
								else
								{	

									if (i==3)
									{
										number = Line;
										Property_sysout =Integer.parseInt(number);
									}
								}	
						}
						 	
					} 
				
			
			catch (IOException e) 
				{
				// TODO Auto-generated catch block
//				e.printStackTrace();
				}
		} 
		
		
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
//			e.printStackTrace();
			SysOutCtrl.SysoutSet("Properties file not found by GetProperties",0);

		}
	}

}

}
