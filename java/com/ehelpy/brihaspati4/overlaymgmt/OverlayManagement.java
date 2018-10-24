package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.*;
import com.ehelpy.brihaspati4.authenticate.properties_access;

//Major Piyush Tiwari Dated: 
//This class is the main class of the overlay management (OM) in which all the methods
// of OM are called.

/*
OverlayManagement object 
 - It will be singleton object (only single instance can exist in the client). 
 - This object sends the routing (neighbour) table to all of its neighbours periodically.
 - It will retrieve the routing table messages received by communication manager (CommManager should have function for this.)
 - On receipt  

*/

   public class OverlayManagement extends Thread {  
      public static  boolean iAmNewlyJoinedNode = false;
// The flag will be set true later in this object if the node joins for the first time.

      public static boolean flagMyPredecessorsUpdatedForIndexManager = false;
      public static boolean flagMyPredecessorsUpdatedForRoutingManager = false;
      public static boolean flagMySuccessorsUpdatedForIndexManager = false;
		public static boolean flagMySuccessorsUpdatedForRoutingManager = false;
		public static String[] nodeId=null;		
		public static boolean updateFromTag31=false;
		public static boolean updateFromTimer = false;
		static CommunicationManager cm = new CommunicationManager();
		static CommunicationUtilityMethods cum = new CommunicationUtilityMethods();
		
		static IndexManagement indmgt = new IndexManagement();
		static Map<String, String> myindex = new LinkedHashMap<String, String>();
 		
//	public static String BootstrapIP = "192.168.1.10";
// to be updated with actual ip of bootstrap server
//public static String BootstrapNodeId = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
		public static String BootstrapNodeId = properties_access.read_property("client.properties","BotstrpND");
		
 		public static Map<String, String>  myIpTable1 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable2 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable3 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable4 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable5 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable6 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable7 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable8 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable9 = new LinkedHashMap<String, String>();
 		static Map<String, String>  myIpTable10 = new LinkedHashMap<String, String>();
		
 		public static boolean newNodeSearchReplyReceived=false;
 		static Map<String, String>  emailSha1 = new LinkedHashMap<String, String>();
 	
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
		
		public  void run() {
		//public static void main(String[] args) throws Exception {
			
			
			
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
//				OverlayManagementUtilityMethods.fillRoutingTable();
			
			
			
			
				Thread t7= new Thread(new Runnable() {
					@Override
					public void run()
					{
						while(true)// if OpBufferIM is not empty this if block will be executed
						
						{		
							
			while(CommunicationManager.RxBufferOM.size()>0)// if OpBufferIM is not empty this if block will be executed
			{
			
			SysOutCtrl.SysoutSet("Getting next file "+" from RxBufferOM for parsing and deciding next course of action", 2);
			File inFile=CommunicationManager.RxBufferOM.removeFirst();
		
			//	System.out.println("Passing Xmlfile "+comm.filename.get(j)+" to ParseXml method of ReadXmlFile Class");		// new line 090417
				
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
				//	nodeAlreadyExists = true;
				
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
					String nodeid = info_from_xml[1];
					String ipadd = info_from_xml[4];
					while(!UpdateIP.Connected)
					{
						SysOutCtrl.SysoutSet("Aquiring IP for OM for tag22 reply");
					}
					String MyIP = PresentIP.MyPresentIP();
							
					
					File ipTableReply = null;
					try {
						ipTableReply = OverlayManagementUtilityMethods.convert_hashmap_toxml( CommunicationManager.myIpTable, info_from_xml[3],"yyyy" , myNodeId, MyIP, "2222");
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
							CommunicationManager.TransmittingBuffer.add(searchNewNodeReply);
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
							CommunicationManager.TransmittingBuffer.add(searchNewNodeReply);
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
							// genereate xml and address it to bootstrap ie info_from_xml[3]
							//put true in to_node_id
							// else false
						}
				
					
	 
					//{
				// the xml file contains request for iplist.
				// the comn manager will check the node and send the iplist to node by converting it to XML.
				//	indmgt.generateIndexingForNewlyJoinedNode(info_from_xml);
//				//	informNodeIdToCaller(info_from_xml[],value);
//				// inform the sender about this value ie node id by setting up a socket.
//			
//				}
			else if(info_from_xml[0].equals( "0032"))//The Xml file contains the iplist.
			{
			Map<String, String>	ipList = IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
			
			Collection<String> key_extracted = ipList.keySet();			/// code to extract hash_id from array by first converting it into collection then to an array
			Object[] key_array = key_extracted.toArray();
			
			
			
			for(int i=0;i<key_array.length;i++)
			
				
				{
					CommunicationManager.myIpTable.put((String) key_array[i], ipList.get(key_array[i]));   // second argument will extract value iro this key i.e. self_node_id
					if(!CommunicationManager.fromNodeIdList.contains(key_array[i]))
					{ 
					CommunicationManager.fromNodeIdList.add((String) key_array[i]);
					SysOutCtrl.SysoutSet("TAble in OM"+CommunicationManager.fromNodeIdList + "key array entry :"+(String) key_array[i]);
					}	
				}
			iAmNewlyJoinedNode = true;
			Set<String> nodeId_extracted = CommunicationManager.myIpTable.keySet(); /// code to extract hash_id from array by first
			/// converting it into collection then to an array
			Collection<String> ip_extracted = CommunicationManager.myIpTable.values();
			
			int size=CommunicationManager.myIpTable.size();
			String[] nodeIdArr=new String[size];
			String[] ipAddArr=new String[size];
			
			nodeId_extracted.toArray(nodeIdArr);
			ip_extracted.toArray(ipAddArr);
			
			XmlFileSegregation.ipTableWriter(nodeIdArr,ipAddArr);

			}
				// the index file contains the iplist.
				// parse the iplist and call the function to convert it to 2d array.
				// reply of search query				
				
			}
			SysOutCtrl.SysoutSet("Thread t0 reply- Rx BufferOM  is empty");
			
			try {
	
		
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}});
t7.start();

	

//if(CommunicationManager.myIpTable.size()>0)
//{
		
		Timer timer = new Timer();
		TimerTask startHM = new TimerTask() {
			@Override
			public void run() {	
		
			
			
					 
					try {
			
					OverlayManagementUtilityMethods.fillRoutingTable();
					
					SysOutCtrl.SysoutSet("Heartbeat monitoring has started", 1);
					HeartbeatMonitoring.heartbeatCheck();
					}
					catch(Exception e)
					{
						OverlayManagementUtilityMethods.fillRoutingTable();
						
						SysOutCtrl.SysoutSet("Heartbeat monitoring has started", 1);
						HeartbeatMonitoring.heartbeatCheck();
					}
		
	
				
					}
					
					};
					
							timer.schedule(startHM, 300,30000);
		
			
//}


				
						
					
//		}
//		SysOutCtrl.SysoutSet("Thread t0 reply- Receiving Buffer is empty");
//		try {
//
//	
//	
//} 
//});
//t0.start();
					
					Timer timerNU = new Timer();
						TimerTask startPredSuc = new TimerTask() {
							@Override
							   public void run() {
								
//Thread t6= new Thread(new Runnable() {
//	@Override
//	public void run()
//	{
//		while(true)// if OpBufferIM is not empty this if block will be executed
//		
//		{
//			//SysOutCtrl.SysoutSet("xmlfilesegregation thread t0 is running",2);
			
			if(!CommunicationManager.myIpTable.isEmpty())
					
			preRequisitForPredSucc();
			
			
			OverlayManagementUtilityMethods.updateMyPredecessors();
			OverlayManagementUtilityMethods.updateMySuccessors();
			
//			String[] predBeforeUpdate = PredecessorSuccessor.myPredecessors;
//			String[] succBeforeUpdate = PredecessorSuccessor.mySuccessors;
			
			String[] myPred = PredecessorSuccessor.getMyPredecessors(nodeId,myNodeId);
			String[] mysucc = PredecessorSuccessor.getMySuccessors(nodeId,myNodeId);
			
			
			getIpList();
			

			 
			
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

				
		// updateTable =true;
		// SysOutCtrl.SysoutSet("updateTable in CM"+updateTable);
	
				
//				try {
//		
//			
//			Thread.sleep(300000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}});
//t6.start();
								
		}									
							
								
							};
						
								timerNU.schedule(startPredSuc, 300,90000);

			
}
		
		
		
			
			

		



			//blic static String myNodeId() {
			// TODO Auto-generated method stub
			
//				if(myNodeId.isEmpty())	
//			{
//				SysOutCtrl.SysoutSet("my node id is not updated",2);
//			}
//			return myNodeId;
//		}




	private static void updateMyNodeId(String nodeId)
	{
		OverlayManagement.myNodeId=nodeId;
	}
	
	
	
	
	public static void nodeStartUp()
	{
	try {
		SysOutCtrl.SysoutSet("trying for nodeid.txt");
		BufferedReader br = new BufferedReader(new FileReader("NodeID.txt"));
		try {
			myNodeId=br.readLine();
			NodeIDExists = true;
			
		SysOutCtrl.SysoutSet("Node-id exists in text file :"+myNodeId, 2);
		
		
		
		
		
		readIpTable();
		CommunicationManager.myIpTable.put(BootstrapNodeId, BootstrapIP);
	
		
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
//		
		e.printStackTrace();
		}
		} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
			NodeIDExists = false;
		//e1.printStackTrace();
		SysOutCtrl.SysoutSet("node id does not exists", 1);
		newNodeId=Generate_newnode.generateNewNodeId(40);
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
	}
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
		Map<String, String>  myIpTable1 = new LinkedHashMap<String, String>();
		//myIpTable1 = CommunicationManager.myIpTable;
		myIpTable1.putAll(CommunicationManager.myIpTable);
		myIpTable1.put(myNodeId, PresentIP.MyPresentIP());
		SysOutCtrl.SysoutSet("temp iptable after adding self node"+myIpTable1);
		Collection<String> NodeIDList=myIpTable1.keySet();
		Object[] NodeID_array = NodeIDList.toArray();
		SysOutCtrl.SysoutSet("hello", 3);
		nodeId = Arrays.asList(NodeID_array).toArray(new String[NodeID_array.length]);
		SysOutCtrl.SysoutSet("node id after adding my node "+nodeId);
	}
	public static void readIpTable()
	{
		BufferedReader BR2= null;
		//String[] nodeId= new String[1024];
		//String[] ipAdd= new String [1024];
		try 
		{
			BR2 = new BufferedReader(new FileReader("IpTable.txt"));
					
			String Line;
			int number =0;
			String NodeId=null;
			String IPAdd=null;
			try 
				{
				Line = BR2.readLine();
				
				
				if(!BR2.equals(null))
				{
					number=Line.length();
				NodeId=Line.substring(0,40);
				IPAdd=Line.substring(40,number);
				CommunicationManager.myIpTable.put(NodeId, IPAdd);
				SysOutCtrl.SysoutSet("ipTable loaded with.....: "+NodeId+IPAdd);
				}
				
					
				}
			
			catch (IOException e) 
				{
				// TODO Auto-generated catch block
//				e.printStackTrace();
				SysOutCtrl.SysoutSet("IpTable file not found by readIpTable",0);
				}
		} 
		
		
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
//			e.printStackTrace();
			SysOutCtrl.SysoutSet("IpTable file not found by readIpTable",0);

		}
	
	}

	public static void getIpList()
	{	
		while(!UpdateIP.Connected)
		{
			SysOutCtrl.SysoutSet("Aquiring IP for OM for sending tag22");
		}
		String MyIP = PresentIP.MyPresentIP();
		
		SysOutCtrl.SysoutSet("get ip list method started PPPPPPPPPPPPPPPPPPPPPPPPPPPPPP ");
		for(int i=0; i<PredecessorSuccessor.myPredecessors.length;i++) 
		{
			
			String PredList = (String) PredecessorSuccessor.myPredecessors[i];
			
			File IPRequestPred = null;
			try {
				SysOutCtrl.SysoutSet("the value of i for the for loop of getiplist is-------------------: "+i );
				SysOutCtrl.SysoutSet("the node where tag 22 has to be sent is: "+PredList);
				
				IPRequestPred = OverlayManagementUtilityMethods.getIpTableRequest(PredList, PredList, myNodeId, MyIP, "2222");
				CommunicationManager.TransmittingBuffer.add(IPRequestPred);
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
			
			SysOutCtrl.SysoutSet("Ip request sent to Predecessor :"+PredList, 2);
		//	SysOutCtrl.SysoutSet("Tx Buffer state vis end of getIprequest :"+CommunicationManager.TransmittingBuffer, 2);
		
		
		
		
			String SuccList = (String) PredecessorSuccessor.mySuccessors[i];
			File IPRequestSucc = null;
			try {
				IPRequestSucc = OverlayManagementUtilityMethods.getIpTableRequest(SuccList, SuccList, myNodeId, MyIP, "2222");
				CommunicationManager.TransmittingBuffer.add(IPRequestSucc);
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
			
			SysOutCtrl.SysoutSet("Ip request sent to Successors :"+SuccList, 2);
			//SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
		}
	}
	
	}
	
