package com.ehelpy.brihaspati4.routingmgmt;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetPresentIP extends UpdateIP{

//	Returns present IP after clipping the machine name etc
//	IP returned is in the correct format
//	used for generating queries etc by OM and IM
	
		public static String MyPresentIP = null;
		
		public static String MyPresentIP()
		{
			String IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
			 
				if(!IPAdd.isEmpty()&& !IPAdd.equals("127.0.0.1"))
				{
					Connected = true;
					SysOutCtrl.SysoutSet("IP Update Running. My IP :"+MyIP,3);
					MyPresentIP =IPAdd;
				}
				else 
				{
					
				}
			
//			MyPresentIP = MyIP.substring(CutAt, MyIP.length());
			return MyPresentIP;
		}
		
	}

