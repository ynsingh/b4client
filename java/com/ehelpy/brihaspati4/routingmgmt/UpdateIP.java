package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 12 Apr 2018

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// 03rd April 2018 1440h

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

public class UpdateIP extends RTManager
{
//	this keeps a handle on the IP and updates 
//	the moment a change in IP is detected it informs all the nodes that self node is aware of the 
//	changed IP, the same gets updated in all the nodes.
	 
	public static String MyIP = null;
	public static boolean Connected = true;
	public static String my_previous_ip;
	 
 public synchronized void run() //String UpdateThisIP()// throws UnknownHostException
 {
	  	try 
	 	{
			Scanner s1 = new Scanner(new File("MyPreviousIP.txt"));
			my_previous_ip =  s1.next();
		}
	 	catch (FileNotFoundException e2) 
	 	{
			e2.printStackTrace();
		}
	  	catch (NoSuchElementException e2) 
	 	{
			e2.printStackTrace();
		}

		System.out.println("My previous ip was : "+my_previous_ip);
	 
	 	String IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
		 
		
		MyIP = IPAdd;
		 
		System.out.println("My present ip : "+MyIP); 	
		FileWriter write = null;
		
		try {
			write = new FileWriter("MyPreviousIP.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		PrintWriter wr = new PrintWriter(write);
		
		wr.write(MyIP);
		wr.flush();
 }	 
}	 
 
