package com.ehelpy.brihaspati4.indexmanager;

//import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.authenticate.Gui;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.overlaymgmt.HeartbeatMonitoring;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Exception;



public class IndexManagement extends Thread {
	
//	
	static CommunicationManager cm= new CommunicationManager();
//	static CommunicationUtilityMethods cum= new CommunicationUtilityMethods();
//	
 public static Map<String, String> myindex = new LinkedHashMap<String, String>();
	static Map<String, String>  myindex1 = new LinkedHashMap<String, String>();
	 static Map<String, String> myindex2 = new LinkedHashMap<String, String>();
	 static Map<String, String> myindex3 = new LinkedHashMap<String, String>();
	 static Map<String, String> myindex4 = new LinkedHashMap<String, String>();
	static Map<String, String> myindex5 = new LinkedHashMap<String, String>();
	static Map<String, String> fresh_index = new LinkedHashMap<String, String>();
	static Map<String, String>  searchReply = new LinkedHashMap<String, String>();
	static Map<String, String>  emailSha1 = new LinkedHashMap<String, String>();
	public static Map<String, String>  callingTable = new LinkedHashMap<String, String>();
	public static String[] myPredecessorsCopy={"","","","",""};
	public static String[] mySuccessorsCopy={"","","","",""};
	static String selfEmailid ;
	static String selfHashId="";
	public static boolean changeFlagIntimationForIndexTableTransmit=false;
	public static boolean changeFlagIntimationForIndexTableUpdate=false;
	volatile static boolean searchReplyReceived=false;
	static boolean storeHashId=true;
	public static int countPublishing=0;
	//public  static void main(String[] args) {
	public void run() {
		SysOutCtrl.SysoutSet("my index table status in Index Management");
		SysOutCtrl.SysoutSet(""+myindex);
		SysOutCtrl.SysoutSet("PredecessorSuccessor visibility from Index Mgmt");
		SysOutCtrl.SysoutSet("IP Table visibility from IndexMgmt"+CommunicationManager.myIpTable, 2);
		//IndexManagementUtilityMethods.fillMyIndexTable();
		for(int i=0; i<5;i++)
		{
			
			SysOutCtrl.SysoutSet("successor"+i +PredecessorSuccessor.mySuccessors[i]);		}
		
		
		
		SysOutCtrl.SysoutSet(" OverlayManagement.iAmNewlyJoinedNode"+OverlayManagement.iAmNewlyJoinedNode,2);
	
						// this could be done in two ways either it could be event based or periodic event
		SysOutCtrl.SysoutSet("boolean storeHashId is "+storeHashId,2);
		while(storeHashId==true)
		{
			IndexManagementUtilityMethods.storeHashId();
			SysOutCtrl.SysoutSet("my hashId stored",2);
			storeHashId=false;
		}
		SysOutCtrl.SysoutSet("boolean flagMyPredecessorsUpdatedForIndexManager is "+OverlayManagement.flagMyPredecessorsUpdatedForIndexManager,2);
		
//		while(OverlayManagement.iAmNewlyJoinedNode==true)
//		{
		
			SysOutCtrl.SysoutSet(" Since i am a new node joining the overlay",2);
		
		String myNodeId = OverlayManagement.myNodeId;
		SysOutCtrl.SysoutSet(" i " + myNodeId + " will  publish myself (my hash id and node id) at responsible node  ",2);
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			   public void run() {
				
				SysOutCtrl.SysoutSet("Re-indexing myself at responsbible node", 2);
				IndexManagementUtilityMethods.addIndexRequest(selfHashId, myNodeId);
				countPublishing++;
				SysOutCtrl.SysoutSet("countPublishing"+countPublishing);
				//timerAction();	
					
				}
				
			
			
		};
		
				timer.schedule(task, 3000,30000);
		
		//CommunicationManager.TransmittingBuffer.add(AddIndexQuery);
		SysOutCtrl.SysoutSet(" i " + myNodeId + " will also request my immediate successor "+PredecessorSuccessor.mySuccessors[0]+"to pass index entries for which i am responsible node",2);
		try {
			
			
			IndexManagementUtilityMethods.getIndexing();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}									// invoke GetIndexing method which will generate request for indexing message and send it to immediate successor
//			OverlayManagement.iAmNewlyJoinedNode=false;													// this message will be an xml file with tag = "0003" which indicates to the successor that it is a request for indexing and thus needful to be done
//		}
		
		Thread t0= new Thread(new Runnable() {
			@Override
			public void run()
			{
				while(true)// if OpBufferIM is not empty this if block will be executed
				
				{
					SysOutCtrl.SysoutSet("Index Management thread t0 is running",2);
		while(!CommunicationManager.RxBufferIM.isEmpty())// if OpBufferIM is not empty this if block will be executed
		{
		
		SysOutCtrl.SysoutSet("Getting next file "+" from RxBufferIM for parsing and deciding next course of action",3);
		File inFile=CommunicationManager.RxBufferIM.removeFirst();
	
		//	SysOutCtrl.SysoutSet("Passing Xmlfile "+comm.filename.get(j)+" to ParseXml method of ReadXmlFile Class");		// new line 090417
			
			 String[] info_from_xml = ParseXmlFile.ParseXml(inFile);		//	SysOutCtrl.SysoutSet("Parsing returning hash id : " +xmldetail);// calling parsexml method of readxmlfile and storing its return value in a string array
			
			if(info_from_xml[0].equals( "0002"))	
			{
				try {
					IndexManagementUtilityMethods.Add_in_Index(info_from_xml);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					SysOutCtrl.SysoutSet("----------------------------------------------------------------------------------------------------------------------------------------------------");
		}
			
			else if(info_from_xml[0].equals( "0003"))	
			{
			String hashIdFromxml=info_from_xml[1];
				String value = null;
				try {
					value = IndexManagementUtilityMethods.Search_in_Index(hashIdFromxml);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String caller= info_from_xml[3];
				SysOutCtrl.SysoutSet("value after checking index table :"+value);
				//SysOutCtrl.SysoutSet(caller);
				if(value.equals("xxxxx"))
				{
					SysOutCtrl.SysoutSet("no match found in this index table sending 'xxxxx' to caller",2);
					IndexManagementUtilityMethods.informNodeIdToCaller(caller,value,hashIdFromxml);
				}
				SysOutCtrl.SysoutSet(" match found in this index table informing caller",2);

				IndexManagementUtilityMethods.informNodeIdToCaller(caller,value, hashIdFromxml);
			// inform the sender about this value ie node id by setting up a socket.
		
			
		
			}
			else if(info_from_xml[0].equals( "0004"))	
			{
			
				Map<String, String> generatedIdexing=IndexManagementUtilityMethods.generateIndexingForNewlyJoinedNode(info_from_xml);
			//	informNodeIdToCaller(info_from_xml[],value);
				File generatedIndexingXml = null;
				try {
					generatedIndexingXml = IndexManagementUtilityMethods.convert_hashmap_toxml(generatedIdexing,"0019", info_from_xml[3], OverlayManagement.myNodeId, "IndexingReply", IndexManagementUtilityMethods.getMyIp(), "2222");
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
				// inform the sender about this value ie node id by setting up a socket.
				CommunicationManager.TransmittingBuffer.add(generatedIndexingXml);
				SysOutCtrl.SysoutSet("Indexing Generated and sent for newly joined node"+generatedIndexingXml.toPath());
			}
			else if(info_from_xml[0].equals( "1111"))	
			{
			
				IndexManagementUtilityMethods.removeIndexing(info_from_xml[3]);
			//	informNodeIdToCaller(info_from_xml[],value);
			// inform the sender about this value ie node id by setting up a socket.
		
			}
			else if(info_from_xml[0].equals( "0012"))	// reply of search query
			{
			String nodeId=info_from_xml[2];
			searchReply.put(info_from_xml[5], nodeId);// storing hashid(5) and ip(2)  of called number in searchreply
			IndexManagementUtilityMethods.storeIp( info_from_xml[5]);
			searchReplyReceived=true;
			//searchReplyReceived=true;
			SysOutCtrl.SysoutSet("Search query reply: nodeId "+nodeId,2);}
			// inform the sender about this value ie node id by setting up a socket.
			
			else if(info_from_xml[0].equals( "0019"))	// generate indexing query
			{
				SysOutCtrl.SysoutSet("index table before"+myindex,2);
				myindex.putAll( IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
				SysOutCtrl.SysoutSet("index table received from my immdediate successor",2);
				SysOutCtrl.SysoutSet("index table after"+myindex,2);
			}
			else if(info_from_xml[0].equals( "0021"))	// reply of new node checking search query
			{
				IndexManagementUtilityMethods.newNodeCheckingReply(inFile);
				SysOutCtrl.SysoutSet("New Node has sent request for checking the existance of node id",2);
			}
			else if(info_from_xml[0].equals( "0010"))// here checking if received xmlindex file is from any of my predecessors and updating the same
			{	
				SysOutCtrl.SysoutSet("index table copy received from one of the my predecessors",2);
				String from_node_id=info_from_xml[3];
					
					if(from_node_id.equals(PredecessorSuccessor.myPredecessors[0]))
					{						
						SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor1 "+PredecessorSuccessor.myPredecessors[0],2);
						SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex1",2);
						myindex1= IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
						SysOutCtrl.SysoutSet("myIndex1(myPredecessor1) index table saved successfully",2);
					}
					else if(from_node_id.equals(PredecessorSuccessor.myPredecessors[1]))
					{
						SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor2 "+PredecessorSuccessor.myPredecessors[1],2);
						SysOutCtrl.SysoutSet(" Now converting this xml to index table and saving in myIndex2",2);
						myindex2= IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
						SysOutCtrl.SysoutSet("myIndex2(myPredecessor2) index table saved successfully",2);
					}
					else if(from_node_id.equals(PredecessorSuccessor.myPredecessors[2]))
					{
						SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor3 "+PredecessorSuccessor.myPredecessors[2],2);
						SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex3",2);
						myindex3= IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
						SysOutCtrl.SysoutSet("myIndex3(myPredecessor3) index table saved successfully",2);
					}
					else if(from_node_id.equals(PredecessorSuccessor.myPredecessors[3]))
					{
					
						SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor4 "+PredecessorSuccessor.myPredecessors[3],2);
						SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex4",2);
						myindex4= IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
						SysOutCtrl.SysoutSet("myIndex4(myPredecessor4) index table saved successfully",2);
					}
					else if(from_node_id.equals(PredecessorSuccessor.myPredecessors[4]))
					{
						SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor5 "+PredecessorSuccessor.myPredecessors[4],2);
						SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex5",2);
						myindex5= IndexManagementUtilityMethods.convertXmlToIndexTable(inFile);
						SysOutCtrl.SysoutSet("myIndex5(myPredecessor5) index table saved successfully",2);
					}
				}
				
			}
		
		
	//	updateTable =true;
	//	 SysOutCtrl.SysoutSet("updateTable in CM"+updateTable);
	
				SysOutCtrl.SysoutSet("Thread t0 reply- Rx Buffer IM is empty");
				try {
		
			
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
				}}});
t0.start();

SysOutCtrl.SysoutSet("boolean flagMySuccessorsUpdatedForIndexManager is "+OverlayManagement.flagMySuccessorsUpdatedForIndexManager,2);				







Thread t3= new Thread(new Runnable() {
	@Override
	public void run()
	{
		while(true)// if OpBufferIM is not empty this if block will be executed
		
		{
			SysOutCtrl.SysoutSet(" thread t3 index manager is waiting for predecessor update",2);
			
			SysOutCtrl.SysoutSet("boolean flagMyPredecessorsUpdatedForIndexManager is "+OverlayManagement.flagMyPredecessorsUpdatedForIndexManager,2);	
			while(OverlayManagement.flagMyPredecessorsUpdatedForIndexManager==true)
			{
				
//					myPredecessorsCopy= OverlayManagement.getMyPredecessors();
				SysOutCtrl.SysoutSet("updated myPredecessor copied in IndexManager ");		
				myPredecessorsCopy=PredecessorSuccessor.getMyPredecessors(OverlayManagement.nodeId,OverlayManagement.myNodeId);
//					String[] mySuccessorsUpdated= OverlayManagement.getMySuccessors();
//					for (int i = 0; i<myPredecessorsUpdated.length;i++)
//						if(!myPredecessorsUpdated[i].equals(myPredecessor[i]))
//						{
//							myPredecessor[i]=myPredecessorsUpdated[i];
							OverlayManagement.flagMyPredecessorsUpdatedForIndexManager=false;
							changeFlagIntimationForIndexTableUpdate = true;
							// update is noticed and following actions are to be initieade
							//updating the index table
							//transmitting to the successors
							//updateMyIndexTable();
//						}
//					for (int j = 0; j<myPredecessorsUpdated.length;j++)
//					if(!mySuccessorsUpdated[j].equals(mySuccessor[j]))
//					{
//						mySuccessor[j]=mySuccessorsUpdated[j];
			}
				SysOutCtrl.SysoutSet("Thread t3 reply- no change detected in myPredcessors");
				try {
		
			
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}});
t3.start();













Thread t4= new Thread(new Runnable() {
	@Override
	public void run()
	{
		while(true)// if OpBufferIM is not empty this if block will be executed
		
		{
			SysOutCtrl.SysoutSet(" thread t4 index manager is waiting for successor update",2);
			
			while(OverlayManagement.flagMySuccessorsUpdatedForIndexManager==true)
			{
SysOutCtrl.SysoutSet("updated mySuccecessor copied in IndexManager ");		
//			mySuccessorsCopy = OverlayManagement.getMySuccessors();
			mySuccessorsCopy=PredecessorSuccessor.getMySuccessors(OverlayManagement.nodeId,OverlayManagement.myNodeId);
			OverlayManagement.flagMySuccessorsUpdatedForIndexManager=false;
			changeFlagIntimationForIndexTableTransmit = true;
		// update is noticed and following actions are to be initieade
		//updating the index table
		//transmitting to the successors
		//updateMyIndexTable();
//	}
}	
				SysOutCtrl.SysoutSet("Thread t4 reply- no change detected in mySuccessors");
				try {
		
			
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}});
t4.start();














SysOutCtrl.SysoutSet("boolean changeFlagIntimationForIndexTableUpdate is "+changeFlagIntimationForIndexTableUpdate,2);				
while (changeFlagIntimationForIndexTableUpdate == true)
{
	IndexManagementUtilityMethods.UpdateIndexing();
}
//SysOutCtrl.SysoutSet(" OverlayManagement.iAmNewlyJoinedNode"+OverlayManagement.iAmNewlyJoinedNode,2);
SysOutCtrl.SysoutSet("boolean changeFlagIntimationForIndexTableTransmit is "+changeFlagIntimationForIndexTableTransmit,2);				

while(changeFlagIntimationForIndexTableTransmit==true )
{	
SysOutCtrl.SysoutSet("my index table "+myindex,2);
SysOutCtrl.SysoutSet("Now, transferring the index table to successors",2);
SysOutCtrl.SysoutSet("----------------------------------------------------------------------------------------------------------------------------------------------------------------");
IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();			// here we are converting myindex map to xml and putting in tx_buffer in tableBuffer.	
changeFlagIntimationForIndexTableTransmit=false;
}	
		
		

		
	}
	
	public static void timerAction()
	{
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			   public void run() {
				SysOutCtrl.SysoutSet("resetting iAmNewlyJoinedNode ");
				OverlayManagement.iAmNewlyJoinedNode=true;
				
									
				}
				
			
			
		};
		
				timer.schedule(task, 3000,30000);
	}
}