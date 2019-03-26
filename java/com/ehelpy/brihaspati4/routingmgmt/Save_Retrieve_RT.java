package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 26 Apr 2018
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;

public class Save_Retrieve_RT extends UpdateTabFromQuery
{

	
	public static class Save_RT
	{
		public void Save_RTNow() 
		{ 
			Set<String> nodeId_extracted = Routing_Table.keySet(); /// code to extract hash_id from array by first
			/// converting it into collection then to an array
			Collection<String> ip_extracted = Routing_Table.values();
		
			String[] nodeIdArr = nodeId_extracted.toArray(new String[nodeId_extracted.size()]);
			String[] ipAddArr = ip_extracted.toArray(new String[ip_extracted.size()]);
			
			RoutingTableWriter(nodeIdArr,ipAddArr);
		}

		public static void RoutingTableWriter(String[] nodeId, String[] ipAdd)
		{
			FileWriter write = null;
			String[] NodeIpArr=new String[nodeId.length];
			
			for (int i=0; i<nodeId.length;i++)
			{
				String NodeID = nodeId[i];
				String IpAdd = ipAdd[i];
				String NodeIp=NodeID.concat(IpAdd);
				NodeIpArr[i]=NodeIp;
			}
			try
			{
				write = new FileWriter("RTP2P.txt");
			}
			catch (IOException e1) 
			{
		
				e1.printStackTrace();
			}
			
			PrintWriter wr = new PrintWriter(write);
		
			for(int j=0; j<NodeIpArr.length;j++)
			{
				wr.write(NodeIpArr[j]);
				wr.println("");
			}
		
			SysOutCtrl.SysoutSet("Routing table written to file");
			wr.flush();
			
		}
	}
	
	public static class Retrieve_RT
	{
		public String[][] Retrieve_RT_Now() throws IOException
		{
			CheckRTExists CheckRT = new CheckRTExists();
			boolean RTExists = CheckRT.ImportRT();
			
		if(RTExists==true)
		{
			try 
			{

				BR = new BufferedReader(new FileReader("RTP2P.txt"));
				
//				BR = new BufferedReader(new FileReader("D:\\roy\\aaaaaaroy\\IITK\\Java practice\\Wk space prac\\Trial\\RTP2P.txt"));
//				Change address above for testing purposes
			
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
				
					 } 	
				}	
				}catch (NullPointerException e) 
				{
				}catch(IndexOutOfBoundsException e)
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
					RoutingInptBuff[m][0]=null;
					SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m][0],3);
			    }
			}
		}	
		
		else 
		{
			SysOutCtrl.SysoutSet("RT NOT FOUND",1);
			SysOutCtrl.SysoutSet("Initialising NULL RT...",1);
			
			for(int m=0;m<120;m++)
		    {
				RoutingInptBuff[m][0]=null;
		    	SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m][0], 3);
		    }
		}
		return RoutingInptBuff;
		}
	}
}

