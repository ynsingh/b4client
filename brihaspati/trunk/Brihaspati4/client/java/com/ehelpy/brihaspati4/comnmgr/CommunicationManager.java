package com.ehelpy.brihaspati4.comnmgr;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.String;

import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.sms.sms_send_rec_management;



public class CommunicationManager extends Thread
{

	// Create Receiving Buffer for putting all the received xml files from previous Comm Manager
	public static LinkedList<File> ReceivingBuffer = new LinkedList<File>();
	public static Object lock_RecBuff_Main = new Object();
	
	// Create TransmittingBuffer for putting all the received xml files for next hop;
	public static LinkedList<File> TransmittingBuffer = new LinkedList<File>();
	public static Object lock_TransBuff_Main = new Object();
	

	// Creating buffers for OM/RT/IM. Xml files will be put based on tag in respective buffer
	public static LinkedList<File> RxBufferOM = new LinkedList<File>();
	public static LinkedList<File> RxBufferRT = new LinkedList<File>();
	
	public static LinkedList<File> RxBufferIM = new LinkedList<File>();
	
	public static LinkedList<File> RxBufferSMS = new LinkedList<File>();
	
	public static Object lock_RxBufferIM = new Object();
	
	public static Map<String, String> myIpTable = new ConcurrentHashMap<>();	
	public static LinkedList<String> fromNodeIdList = new LinkedList<>();
	public static boolean updateTable =false;
	
	public static Lock lock = new ReentrantLock();// this is to protect simultaneously accessing writeIpTable method in xmiFileSeggregation class. 
	
	public static List<String> succ = new LinkedList<String>(); 
	public static List<String> pred = new LinkedList<String>();
	
	public void  run()
	{
	
		Thread t0= new Thread
		(
				new Runnable()
				{
					@Override
					public void run()
					{
						while(GlobalObject.getRunStatus()||sms_send_rec_management.sending_message||!ReceivingBuffer.isEmpty())
			
						{
				
							SysOutCtrl.SysoutSet("xmlfilesegregation thread t0 is running",2);
							synchronized(lock_RecBuff_Main)
							{
								while(!ReceivingBuffer.isEmpty())// if OpBufferIM is not empty this if block will be executed
								{
									SysOutCtrl.SysoutSet(CommunicationManager.fromNodeIdList.size()+"size with comm mgr");			
									XmlFileSegregation.xmlFileSegregator();
									updateTable =true;
									SysOutCtrl.SysoutSet("updateTable in CM"+updateTable);
								}
					        
								SysOutCtrl.SysoutSet("Thread t0 reply- Receiving Buffer is empty");
							}	
							
							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException e)
							{
								e.printStackTrace();
							}
						}
					}	        
				}	
		);
		
		t0.start();

		Thread t1 = new Thread
		(
				new Runnable()
				{				// here implementing threads to start http server, because server will send this file to the first successor
					@Override
					public void run() 
					{
						System.out.println("FileReceiver Thread t1 is running");
				
						file_receiver.main(null);
					}
				}
		);
		
		t1.start();
	        
		Thread t2= new Thread
		(
				new Runnable()
				{
					@Override
					public void run()
					{
						while(GlobalObject.getRunStatus()||!TransmittingBuffer.isEmpty())
						{
							SysOutCtrl.SysoutSet( "Thread-t2 (File Transmitter) is running",1);
							System.out.println("Thread-t2 (File Transmitter) is running");
							
							synchronized(lock_TransBuff_Main)
							{
								CommunicationUtilityMethods.sendXmlToNextNodeId();
								//Sendfile method will be used for sending files to next hop.
							}

							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException e)
							{
								SysOutCtrl.SysoutSet("thread t2 exception");
							}
						}
					}
				}
		);
		
		t2.start();
		
		SysOutCtrl.SysoutSet("my predecessor 4....................."+PredecessorSuccessor.myPredecessors[4]);

		Thread t9= new Thread
				(
						new Runnable()
						{
							@Override
							public void run()
							{
							//	while(true)
							//	{
									SysOutCtrl.SysoutSet( "Thread-t9 (Application Running Status) ",1);
															
								//	CommunicationUtilityMethods.Application_Alive_Response_ByReciever();
									Applivation_alive_response.main(null);
								//}
							}
						}
				);
				
			t9.start();
	
	}
	
	public static void update_my_IpTable(String nodeId, String Ip)
	{
		String mynodeid = OverlayManagement.myNodeId;
		
		if(!nodeId.equals("null")||!nodeId.equals(null)||!nodeId.equals("")||!nodeId.isEmpty()||!nodeId.equals(" "))
		{	
			int comp_nodeId = nodeId.compareTo(mynodeid);
			
			//Making entry in succ and pred  and in iptable if within range of distance 6 in succ and pred
			
			if(comp_nodeId>0)
			{	
				if(!succ.contains(nodeId))
				{	
					succ.add(nodeId);
					
					Collections.sort(succ);
					
					if(succ.indexOf(nodeId)<6)
					{	
						myIpTable.put(nodeId, Ip);
						OverlayManagement.updatPredSec();
					}	
				}	
			}	
		
			else if(comp_nodeId<0)
			{	
				if(!pred.contains(nodeId))
				{
					pred.add(nodeId);
					
					Collections.sort(pred);
					
					if(pred.indexOf(nodeId)<6)
					{	
						myIpTable.put(nodeId, Ip);
						OverlayManagement.updatPredSec();
					}	
				}	
			}
			
			else if(comp_nodeId==0)
				myIpTable.put(nodeId, Ip);
		
			Collections.sort(pred);
			Collections.sort(succ);
		
			//removing if there is any null entry in succ and pred
			
			int size_of_succ = succ.size();
			int size_of_pred = pred.size();
		
			for(int i =0; i<size_of_succ;i++)
			{
				if(succ.get(i).isEmpty())
					succ.remove(i);
			}
			
			for(int i =0; i<size_of_pred;i++)
			{
				if(pred.get(i).isEmpty())
					pred.remove(i);
			}
			
			Collections.sort(pred);
			Collections.sort(succ);
						
			
			//Restricting succ and pred entries to 7
			
			List<String> temp_succ_list = new LinkedList<String>();
			List<String> temp_pred_list = new LinkedList<String>();
			
			int size_of_succ1 = succ.size();
			int size_of_pred1 = pred.size();
			
			if(size_of_succ1>7)
			{	
				for(int i= 0; i<7; i++ )
					temp_succ_list.add(succ.get(i));
							
				succ.clear();
				succ.addAll(temp_succ_list);
			}
		
			if(size_of_pred1>7)
			{	
				for(int i= 0; i<7; i++ )
					temp_pred_list.add(pred.get(i));
							
				pred.clear();
				pred.addAll(temp_pred_list);
			}
	
		}
		
		//printing my iptable in txt file
		
		Set<String> nodeId_extracted = myIpTable.keySet(); /// code to extract hash_id from array by first
		/// converting it into collection then to an array
		Collection<String> ip_extracted = myIpTable.values();

												
		String[] nodeIdArr = nodeId_extracted.toArray(new String[nodeId_extracted.size()]);
		String[] ipAddArr = ip_extracted.toArray(new String[ip_extracted.size()]);

		lock.lock();
		try
		{
			XmlFileSegregation.ipTableWriter(nodeIdArr,ipAddArr);
		}
		finally
		{
			lock.unlock();
		}
		
	}	

	
}




