package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.routingmgmt.*;
import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.authenticate.properties_access;

// Major Piyush Tiwari Dated: 
// This class is the main class of the overlay management in which all the methods of OM are called.

public class OverlayManagement extends Thread {  

      
		public static  boolean iAmNewlyJoinedNode = false;//flag is set true if a node joins for the first time.
		public static boolean flagMyPredecessorsUpdatedForIndexManager = false;
		public static boolean flagMyPredecessorsUpdatedForRoutingManager = false;
		public static boolean flagMySuccessorsUpdatedForIndexManager = false;
		public static boolean flagMySuccessorsUpdatedForRoutingManager = false;
		public static String[] nodeId=null;		
		public static boolean updateFromTag31=false;
		public static boolean updateFromTimer = false;
		static CommunicationManager cm = new CommunicationManager();
		static CommunicationUtilityMethods cum = new CommunicationUtilityMethods();
		public static Map<String, String> AliveNodes = new ConcurrentHashMap<String, String>();
		
		public static Map<String, String> HashRT  = new ConcurrentHashMap<String, String>();
			
		public static String BootstrapNodeId = properties_access.read_property("client.properties","BotstrpND");
		
 		public static Map<String, String>  myIpTable1 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable2 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable3 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable4 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable5 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable6 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable7 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable8 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable9 = new ConcurrentHashMap<String, String>();
 		static Map<String, String>  myIpTable10 = new ConcurrentHashMap<String, String>();
		
 		public static boolean newNodeSearchReplyReceived=false;
 		static Map<String, String>  emailSha1 = new ConcurrentHashMap<String, String>();
 	
 		public static String myNodeId="";
		//private static String myIp = "";
		public static BufferedReader br = null;
		public static boolean nodeAlreadyExists = false;//for the index manager.
		public static boolean NodeIDExists = false;//for txt file.
		public static boolean changeFlagIntimationForIndexTableTransmit=false;
		public static boolean changeFlagIntimationForIndexTableUpdate=false;
		public static boolean changeFlagIntimationForIpTableUpdate=false;
		//public static String BootstrapIP="172.26.185.66";// this ip to be replaced with bootstrap server ip address
		public static String BootstrapIP=properties_access.read_property("client.properties","BotstrpIP");// this ip to be replaced with bootstrap server ip address
		
		public static String newNodeId;
		public static Map<String, String>  TempRouting_Table = new ConcurrentHashMap<String, String>();    //for optimising RT exchanges and reduce traffic
		
		public  void run() {
		//public static void main(String[] args) throws Exception {
			
			String mynodeid = OverlayManagement.myNodeId;
			int comp;
			
			Set<String> keys1 = CommunicationManager.myIpTable.keySet();
			for(String key : keys1) 
			{	
				if(!key.equals("null")||!key.equals(null)||!key.equals("")||!key.isEmpty()||!key.equals(" "))
				{	
					comp = key.compareTo(mynodeid);
				
					if(comp<0)
						CommunicationManager.pred.add(key);
					if(comp>0)
						CommunicationManager.succ.add(key);
				}	
			}
			
			updatPredSec();
			
			SysOutCtrl.SysoutSet("bootstrap ip iiiiiiiiiiiiipppppppppppppppp " +BootstrapIP);
			
			SysOutCtrl.SysoutSet("Overlay Management Thread is running");
			SysOutCtrl.SysoutSet("sysout int"+com.ehelpy.brihaspati4.routingmgmt.GetProperties.Property_sysout);
			if(com.ehelpy.brihaspati4.routingmgmt.GetProperties.Backbone==true)
			{
				OverlayManagementUtilityMethods.fillMyIptable();
				SysOutCtrl.SysoutSet("Backbone = true");
				SysOutCtrl.SysoutSet("Backbone = true");
			}
			else
			{
				SysOutCtrl.SysoutSet("Backbone = false");
			}
				OverlayManagementUtilityMethods.fillRoutingTable();
			
			
			
			
			Thread t7= new Thread(new Runnable() 
			{
				@Override
				public void run()
				{
					while(GlobalObject.getRunStatus()||!CommunicationManager.RxBufferOM.isEmpty())// if OpBufferIM is not empty this if block will be executed
						
					{		
						System.out.println("t7 thread is running");	
						while(!CommunicationManager.RxBufferOM.isEmpty())// if OpBufferIM is not empty this if block will be executed
						{
			
							SysOutCtrl.SysoutSet("Getting next file "+" from RxBufferOM for parsing and deciding next course of action", 2);
							File inFile=CommunicationManager.RxBufferOM.removeFirst();
				
							String[] info_from_xml = ParseXmlFile.ParseXml(inFile);		//	System.out.println("Parsing returning hash id : " +xmldetail);// calling parsexml method of readxmlfile and storing its return value in a string array
				
							if(info_from_xml[0].equals( "0021"))	// information received from Xml is node ID of newly generated node.
							{
								SysOutCtrl.SysoutSet("Request with tag 0021 received");
								String hashIdFromxml = info_from_xml[1];// here node id will be passed as hash id to search_in_index method
					
								String reply=OverlayManagementUtilityMethods.searchNodeId(hashIdFromxml);
					
				
								SysOutCtrl.SysoutSet("new nodeid received");
							}
							else if(info_from_xml[0].equals( "0031"))	//Reply from index manager indicating whether the newly generated node ID already existed or not. 
							{
				
								if(info_from_xml[2].equals("true")) {
									SysOutCtrl.SysoutSet("NodeId already exists in the network generating again tag21");
									newNodeId=Generate_newnode.generateNewNodeId(40);
									updateByTimer(30000);
									while(!UpdateIP.Connected)
									{
										SysOutCtrl.SysoutSet("Aquiring IP for OM for sending tag21");
									}
									String MyIP = PresentIP.MyPresentIP();
									File NewNodeXml = null;
									try {
										NewNodeXml = Generate_newnode.createNewNodeSearchQuery(newNodeId, "yyyyy", newNodeId, MyIP, "2222");
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (TransformerException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					
									OverlayManagementUtilityMethods.sendFileDirect( BootstrapIP,  NewNodeXml);
			
								}
				
								else 
								{
									iAmNewlyJoinedNode = true;
								}
							}
							else if(info_from_xml[0].equals( "0022"))//tag for request for Iplist from BS	
							{
					
								String ipadd = info_from_xml[4];
								while(!UpdateIP.Connected)
								{
									SysOutCtrl.SysoutSet("Aquiring IP for OM for tag22 reply");
								}
								String MyIP = PresentIP.MyPresentIP();
							
					
								File ipTableReply = null;
								try {
									ipTableReply = OverlayManagementUtilityMethods.convert_hashmap_toxml(  RTManager.Routing_Table, info_from_xml[3],"yyyy" , myNodeId, MyIP, "2222");
									SysOutCtrl.SysoutSet("reply for tag 22 has been generated");
								} catch (FileNotFoundException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (TransformerException e) 
								{
									// TODO Auto-generated catch block 
									e.printStackTrace();
								} catch (ParserConfigurationException e) 
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								SysOutCtrl.SysoutSet("before send file direct method in overlay");
								OverlayManagementUtilityMethods.sendFileDirect(ipadd, ipTableReply);
								SysOutCtrl.SysoutSet("iptable has been sent to the node");
							}
					
							else if(info_from_xml[0].equals( "0205"))//tag for request for Iplist from BS	
							{
								String newNodeId = info_from_xml[1];
								if(newNodeId==myNodeId)
								{
									SysOutCtrl.SysoutSet("NewNode already exists in the network");
									while(!UpdateIP.Connected)
									{
										SysOutCtrl.SysoutSet("Aquiring IP for OM");
									}
									String MyIP = PresentIP.MyPresentIP();
									
									File searchNewNodeReply;
									try {
										searchNewNodeReply = OverlayManagementUtilityMethods.searchNewNodeReply(info_from_xml[3], "true", myNodeId, MyIP, info_from_xml[1]);
										//	CommunicationManager.TransmittingBuffer.add(searchNewNodeReply);
										com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(searchNewNodeReply);
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (TransformerException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						
						
									// genereate xml and address it to bootstrap ie info_from_xml[3]
									//put true in to_node_id
									//put searched node id in port no
						
								}
								else
								{
									SysOutCtrl.SysoutSet("NewNode already does not in the network");
									while(!UpdateIP.Connected)
									{
										SysOutCtrl.SysoutSet("Aquiring IP for OM");
									}
									String MyIP = PresentIP.MyPresentIP();
									
									File searchNewNodeReply;
									try {
										searchNewNodeReply = OverlayManagementUtilityMethods.searchNewNodeReply(info_from_xml[3], "false", myNodeId, MyIP, info_from_xml[1]);
										//CommunicationManager.TransmittingBuffer.add(searchNewNodeReply);
										com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(searchNewNodeReply);
									} catch (FileNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (TransformerException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									// else false
								}
							}
			
							else if(info_from_xml[0].equals( "0305"))//tag for request for Iplist from BS	
							{
								String reply=info_from_xml[2];
								newNodeSearchReplyReceived=true;
								OverlayManagementUtilityMethods.newNodeCheckReply.put(info_from_xml[5], info_from_xml[2]);//Node
						
							}
				
					
							
							else if(info_from_xml[0].equals( "0032"))//The Xml file contains the iplist.
							{
								//////////////////////////////////////////////////////////////////////////////////////////////////////
								// this is for sending acknowledgement to all nodes which are not present in my routing table
								System.out.println("uuuuuuuuuuuuuuuuu ");
								AliveNodes.put(info_from_xml[3], info_from_xml[4]);
								
								System.out.println("cccccccccccccccccccc "+AliveNodes);
							
								try {
					
									List<String> myRTTable_3 = RTManager.Routing_Table.keySet().stream().collect(Collectors.toList());
									if(myRTTable_3.contains(info_from_xml[3])) {
									}
									else {
										try {
											OverlayManagementUtilityMethods.SendAckToNode(info_from_xml[3],info_from_xml[4]);
										} catch (TransformerException | ParserConfigurationException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									} 
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								/////////////////////////////////////////////////////////////////////////////////////////////////////////
								// comparing the hash of routing table (which has send 0032) with HashRT 
						//		List<String> hashrt = HashRT.keySet().stream().collect(Collectors.toList());
								if(HashRT.containsKey(info_from_xml[3])&&(HashRT.get(info_from_xml[3]).equals(info_from_xml[2]))) 
									//if HashRT map contains the nodeid and the hash of RT stored corresponding to this nodeid is same as previous stored value
								{
									System.out.println("bbbbbbbbbbbbbbbbb");
									AliveNodes.put(info_from_xml[3], info_from_xml[4]);
								}
								else {
									HashRT.put(info_from_xml[3], info_from_xml[2]);// map of nodeid and hash of routing table
									
									Map<String, String>	ipList = IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
									System.out.println("aaaaaaaaaaaaaaaaaaa");
								
									Collection<String> key_extracted = ipList.keySet();/// code to extract hash_id from array by first converting it into collection then to an array
									Object[] key_array = key_extracted.toArray();
			
									for(int i=0;i<key_array.length;i++)
									{
										if(!CommunicationUtilityMethods.myIpTable.containsKey((String)key_array[i]))
										{	
									//		if(CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ipList.get(key_array[i])))
										//	{	
												// second argument will extract value iro this key i.e. self_node_id
												CommunicationManager.update_my_IpTable((String) key_array[i], ipList.get(key_array[i]));
												UpdateTabFromQuery.NextEntry((String) key_array[i],ipList.get(key_array[i]));
												AliveNodes.put((String) key_array[i],ipList.get(key_array[i]));
										//	}
											SysOutCtrl.SysoutSet("key array entry :"+(String) key_array[i]);
										}	
									}
								
									iAmNewlyJoinedNode = true;
								}
							}
							else if(info_from_xml[0].equals( "0098"))//Acknowledgement from other nodes for routing table
							{
								AliveNodes.put(info_from_xml[3], info_from_xml[4]);
							}
					
							SysOutCtrl.SysoutSet("Thread t7 reply- Rx BufferOM  is empty");
			
							
						}
						try {
							
							
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
					);
			t7.start();

	

//if(CommunicationManager.myIpTable.size()>0)
//{
		
			
		Timer timer = new Timer();
		TimerTask startHM = new TimerTask() {
			@Override
			public void run() 
			{	
			
				if(GlobalObject.getRunStatus())
				{
					SysOutCtrl.SysoutSet("Heartbeat monitoring has started", 1);
					
					HeartbeatMonitoring.heartbeatCheck();
				}
				
			}
					
		};
					
		timer.schedule(startHM, 300,50000);
		
		Timer heartbeatcheck1 = new Timer();
		TimerTask startHM1 = new TimerTask() {
			@Override
			public void run() 
			{	
				if(GlobalObject.getRunStatus())
				{	
						try {
							HeartbeatMonitoring.heartbeatCheck1();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
							
			}
					
		};
					
		heartbeatcheck1.schedule(startHM1, 120000,120000);
		
		Timer update_pred_sec = new Timer();
		TimerTask task_update_pred_sec = new TimerTask() {
			@Override
			public void run()
			{
				if(GlobalObject.getRunStatus())
				{
					updatPredSec();
				}
			}
		};	
		update_pred_sec.schedule(task_update_pred_sec, 300,30000);	
		
		
		Timer timerNU = new Timer();
		TimerTask startPredSuc = new TimerTask() {
		@Override
			public void run()
			{				
			if(GlobalObject.getRunStatus())
			{	
				Map<String,String> TempMap= new TreeMap<String,String>();
				
				Set<String> keys = RTManager.Routing_Table.keySet();
				for(String key: keys)
				{
					if(!TempRouting_Table.containsKey(key))
						TempMap.put(key, RTManager.Routing_Table.get(key));
				}
				
				String qwe = RTManager.Routing_Table.toString();
				String HashRT=SHA1.getSha1(qwe);
				
				File ipTableReply = null;
				String MyIP = PresentIP.MyPresentIP();
				
				for(String key : keys)
				{
					try
					{
						ipTableReply = OverlayManagementUtilityMethods.convert_hashmap_toxml( TempMap, key,HashRT , myNodeId, MyIP, "2222");
						String ipadd = RTManager.Routing_Table.get(key);
						OverlayManagementUtilityMethods.sendFileDirect(ipadd, ipTableReply);
					} 
					catch (FileNotFoundException | TransformerException | ParserConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				TempMap.clear();
				TempRouting_Table.clear();
				TempRouting_Table.putAll(RTManager.Routing_Table);
								
			}	
			}
							
								
		};
						
		timerNU.schedule(startPredSuc, 30000,90000);

			
}

	public static void updatPredSec()
	{
		preRequisitForPredSucc();
		
		
		OverlayManagementUtilityMethods.updateMyPredecessors();
		OverlayManagementUtilityMethods.updateMySuccessors();
	
			
		String[] myPred = PredecessorSuccessor.getMyPredecessors(nodeId,myNodeId);
		String[] mysucc = PredecessorSuccessor.getMySuccessors(nodeId,myNodeId);
		
		SysOutCtrl.SysoutSet("my self node id is " +myNodeId);
		
		
		SysOutCtrl.SysoutSet("list of my predecessors ");
		for(int i=0; i<myPred.length;i++)
		{
		SysOutCtrl.SysoutSet("value of i for predecessorsuccessor is:"+i);
		SysOutCtrl.SysoutSet(myPred[i]+" ");
		}
	
		SysOutCtrl.SysoutSet("list of my successors ");
		for(int j=0;j<mysucc.length;j++)
		{	
			SysOutCtrl.SysoutSet("value of j for predecessorsuccessor is:"+j);
			SysOutCtrl.SysoutSet(mysucc[j]+" ");
		}
	}

	public static void updateMyNodeId(String nodeId)
	{
		OverlayManagement.myNodeId=nodeId;
	}
	
	public static void nodeStartUp()
	{
	try {
		SysOutCtrl.SysoutSet("trying for nodeid.txt");
		BufferedReader br = new BufferedReader(new FileReader("NodeID.txt"));
		
		myNodeId=br.readLine();
				
		updateFromTimer=false;
		updateFromTag31=false;
		if(myNodeId.isEmpty())
		{
			NodeIDExists = false;
			//e1.printStackTrace();
			SysOutCtrl.SysoutSet("node id does not exists", 1);
			newNodeId=Generate_newnode.generateNewNodeId(40);
			myNodeId=		newNodeId;
			OverlayManagement.updateByTimer(1000);
			while(!UpdateIP.Connected)
			{
				SysOutCtrl.SysoutSet("Aquiring IP for OM for sending tag21");
			}
			String MyIP = PresentIP.MyPresentIP();
			File NewNodeXml = null;
			try {
				SysOutCtrl.SysoutSet("newNodeId"+newNodeId,2);
				SysOutCtrl.SysoutSet("MyIP"+MyIP,2);
				NewNodeXml = Generate_newnode.createNewNodeSearchQuery(newNodeId, "yyyyy", newNodeId, MyIP, "2222");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SysOutCtrl.SysoutSet("bootstrap ip"+BootstrapIP,2);
			//	SysOutCtrl.SysoutSet("new node xml"+NewNodeXml.toString(),2);
			OverlayManagementUtilityMethods.sendFileDirect( BootstrapIP,  NewNodeXml);
			SysOutCtrl.SysoutSet("updateFromTimer"+updateFromTimer,2);
			SysOutCtrl.SysoutSet("updateFromTag31"+updateFromTag31,2);
			while(updateFromTimer||updateFromTag31)
			{
				
				SysOutCtrl.SysoutSet("Node id updated now sending tag22 to bootstrap");
				OverlayManagementUtilityMethods.sendTag22ToBootStrap();
				updateFromTimer=false;
				updateFromTag31=false;
			}
		}
			
			
		SysOutCtrl.SysoutSet("Node-id exists in text file :"+myNodeId, 2);
		
		
		NodeIDExists = true;
		sendRTtoList();
		}
	
	
	 catch (IOException|NullPointerException e1) 
	{
		// TODO Auto-generated catch block
		NodeIDExists = false;
		//e1.printStackTrace();
		SysOutCtrl.SysoutSet("node id does not exists", 1);
		newNodeId=Generate_newnode.generateNewNodeId(40);
		myNodeId=		newNodeId;
		OverlayManagement.updateByTimer(1000);
		while(!UpdateIP.Connected)
		{
			SysOutCtrl.SysoutSet("Aquiring IP for OM for sending tag21");
		}
		String MyIP = PresentIP.MyPresentIP();
		File NewNodeXml = null;
		try {
			SysOutCtrl.SysoutSet("newNodeId"+newNodeId,2);
			SysOutCtrl.SysoutSet("MyIP"+MyIP,2);
			NewNodeXml = Generate_newnode.createNewNodeSearchQuery(newNodeId, "yyyyy", newNodeId, MyIP, "2222");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SysOutCtrl.SysoutSet("bootstrap ip"+BootstrapIP,2);
		//	SysOutCtrl.SysoutSet("new node xml"+NewNodeXml.toString(),2);
		OverlayManagementUtilityMethods.sendFileDirect( BootstrapIP,  NewNodeXml);
		SysOutCtrl.SysoutSet("updateFromTimer"+updateFromTimer,2);
		SysOutCtrl.SysoutSet("updateFromTag31"+updateFromTag31,2);
		while(updateFromTimer||updateFromTag31)
		{
			
			SysOutCtrl.SysoutSet("Node id updated now sending tag22 to bootstrap");
			OverlayManagementUtilityMethods.sendTag22ToBootStrap();
			updateFromTimer=false;
			updateFromTag31=false;
		}
	}
	//catch finishes
	SysOutCtrl.SysoutSet("Node-id exists in text file :"+myNodeId, 2);
	
	
	NodeIDExists = true;
	SysOutCtrl.SysoutSet("Node id updated now sending tag22 to bootstrap");
	OverlayManagementUtilityMethods.sendTag22ToBootStrap();
	Set<String> keys = RTManager.Routing_Table.keySet();
	File ipTableReplyReq=null;
	String myip=PresentIP.MyPresentIP();
	for(String key: keys)
	{
		String Readelem= key;
		if(Readelem==null)
		{}
		else {
			try {
			
				ipTableReplyReq = OverlayManagementUtilityMethods.getRTTableRequest(Readelem, Readelem,myNodeId,myip,"2222" );
				//SysOutCtrl.SysoutSet("reply for tag 22 has been generated");
			} catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) 
			{
				// TODO Auto-generated catch block 
				e.printStackTrace();
			} catch (ParserConfigurationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String ipadd = CommunicationManager.myIpTable.get(Readelem);
			String ipadd = RTManager.Routing_Table.get(key);
			OverlayManagementUtilityMethods.sendFileDirect(ipadd, ipTableReplyReq);
			SysOutCtrl.SysoutSet("routing table has been sent to the"+ Readelem+ " node");
		}
	}
	try {
		sendRTtoList();
	} catch (IOException e) {
		e.printStackTrace();
	}
}
	
	
	
	
	public static void updateByTimer(int number)	
	
	{
		SysOutCtrl.SysoutSet("timer started for newNode",2);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			   public void run() {
				
				
				SysOutCtrl.SysoutSet("timer completed for newNode",2);
				if(updateFromTag31 == false) {
					//if no reply comes from xml
					updateMyNodeId(newNodeId) ;
					SysOutCtrl.SysoutSet("myNodeId udated by timer action " +myNodeId);
					iAmNewlyJoinedNode = true;
					updateFromTimer = true;
					cancel();
				}
				
			}
			
		};
		
		timer.schedule(task, number);
				
		
		//if no reply comes from xml
	}


	
	public static void preRequisitForPredSucc()
	{
		SysOutCtrl.SysoutSet("my iptable is: "+CommunicationManager.myIpTable);
		Map<String, String>  myIpTable1 = new ConcurrentHashMap<String, String>();
	//	myIpTable1 = CommunicationManager.myIpTable;
		myIpTable1.putAll(CommunicationManager.myIpTable);
		myIpTable1.put(myNodeId, PresentIP.MyPresentIP());
		SysOutCtrl.SysoutSet("temp iptable after adding self node"+myIpTable1);
		Collection<String> NodeIDList=myIpTable1.keySet();
		Object[] NodeID_array = NodeIDList.toArray();
		SysOutCtrl.SysoutSet("hello", 3);
		nodeId = Arrays.asList(NodeID_array).toArray(new String[NodeID_array.length]);//should not calculate this node id list from routing table because there are chances that we may
		//leave few node ids closer to us
		SysOutCtrl.SysoutSet("node id after adding my node "+nodeId);
	}
	public static void sendRTtoList() throws IOException
	{
		while(!UpdateIP.Connected)
		{
			SysOutCtrl.SysoutSet("Aquiring IP for OM for sending tag22");
		}
		String MyIP = PresentIP.MyPresentIP();
		File ipTableReply = null;
	
		
		String qwe = RTManager.Routing_Table.toString();
		String HashRT=SHA1.getSha1(qwe);
		
		Set<String> keys = RTManager.Routing_Table.keySet();
			
		for(String key: keys)
		{
			String Readelem= key;
			if(Readelem==null)
			{}
			else {
			try {
				ipTableReply = OverlayManagementUtilityMethods.convert_hashmap_toxml( RTManager.Routing_Table, Readelem,HashRT , myNodeId, MyIP, "2222");
				//SysOutCtrl.SysoutSet("reply for tag 22 has been generated");
			} catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) 
			{
				// TODO Auto-generated catch block 
				e.printStackTrace();
			} catch (ParserConfigurationException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String ipadd = CommunicationManager.myIpTable.get(Readelem);
			String ipadd = RTManager.Routing_Table.get(key);
			OverlayManagementUtilityMethods.sendFileDirect(ipadd, ipTableReply);
			SysOutCtrl.SysoutSet("routing table has been sent to the"+ Readelem+ " node");
		}
		
		}
		TempRouting_Table.putAll(RTManager.Routing_Table);
			
		
		SysOutCtrl.SysoutSet("send Routing table method started PPPPPPPPPPPPPPPPPPPPPPPPPPPPPP ");
	}
	}	
		
		
	
