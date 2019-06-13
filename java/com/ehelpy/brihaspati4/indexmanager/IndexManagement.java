package com.ehelpy.brihaspati4.indexmanager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.RTUpdate9;
import com.ehelpy.brihaspati4.routingmgmt.Save_Retrieve_RT;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.sms.sms_retrival_thread;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Exception;
import java.security.cert.X509Certificate;



public class IndexManagement extends Thread
{


    static CommunicationManager cm= new CommunicationManager();
    public static Map<String, String> myindex = new ConcurrentHashMap<String, String>();
    public static Map<String, String>  NodeId_ip_of_myindex = new ConcurrentHashMap<String, String>();
    static Map<String, String>  myindex1 = new ConcurrentHashMap<String, String>();
    static Map<String, String> nodeid_ip_myindex1 = new ConcurrentHashMap<String, String>();
    static Map<String, String> myindex2 = new ConcurrentHashMap<String, String>();
    static Map<String, String> nodeid_ip_myindex2 = new ConcurrentHashMap<String, String>();
    static Map<String, String> myindex3 = new ConcurrentHashMap<String, String>();
    static Map<String, String> nodeid_ip_myindex3 = new ConcurrentHashMap<String, String>();
    static Map<String, String> myindex4 = new ConcurrentHashMap<String, String>();
    static Map<String, String> nodeid_ip_myindex4 = new ConcurrentHashMap<String, String>();
    public static Map<String, String> myindex5 = new ConcurrentHashMap<String, String>();
    public static Map<String, String> nodeid_ip_myindex5 = new ConcurrentHashMap<String, String>();
    public static Map<String, String> fresh_index = new ConcurrentHashMap<String, String>();
    static Map<String, String>  searchReply = new ConcurrentHashMap<String, String>();
    public static Map<String, String>  searchReply_cert = new ConcurrentHashMap<String, String>();
    static Map<String, String>  emailSha1 = new ConcurrentHashMap<String, String>();
    public static Map<String, String>  cached_index = new ConcurrentHashMap<String, String>();
    public static Map<String, String>  cached_NodeId_ip_index = new ConcurrentHashMap<String, String>();
    public static Map<String, String>  callingTable = new ConcurrentHashMap<String, String>();
    static String selfEmailid ;
    static String selfHashId="";
    public static boolean changeFlagIntimationForIndexTableTransmit=false;
    public static boolean changeFlagIntimationForIndexTableUpdate=false;
    volatile static boolean searchReplyReceived=false;
    static boolean storeHashId=true;
    public static int countPublishing=0;
    public static String MyIP = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
    public static String IPAdd;
    public static boolean CheckIP=true;
    public static boolean timer_flag = false;
    public static TreeMap<String, String>  EmailHashId_certificates= new TreeMap<String, String>();
    public static TreeMap<String, String>  emailIdHash_cert= new TreeMap<String, String>();
           
    public void run()
    {
        SysOutCtrl.SysoutSet("my index table status in Index Management");
        SysOutCtrl.SysoutSet(""+myindex);
        SysOutCtrl.SysoutSet("PredecessorSuccessor visibility from Index Mgmt");
        SysOutCtrl.SysoutSet("IP Table visibility from IndexMgmt"+CommunicationManager.myIpTable, 2);

        for(int i=0; i<5; i++)
        {

            SysOutCtrl.SysoutSet("successor"+i +PredecessorSuccessor.mySuccessors[i]);
        }



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

        SysOutCtrl.SysoutSet(" Since i am a new node joining the overlay",2);

        String myNodeId = OverlayManagement.myNodeId;
        SysOutCtrl.SysoutSet(" i " + myNodeId + " will  publish myself (my hash id and node id) at responsible node  ",2);
       	
        SysOutCtrl.SysoutSet("Re-indexing myself at responsbible node", 2);
       	
        Timer publish = new Timer();
        TimerTask task_publish = new TimerTask()
        {
        	@Override
        	public void run()
        	{
       			if(GlobalObject.getRunStatus())
       			{
       				IndexManagementUtilityMethods.addIndexRequest(selfHashId, myNodeId);//Publishing self hash id of email  at responsible node
       			}
        	}
        };
        publish.schedule(task_publish, 2000, 60000);
        
        Timer check_own_index = new Timer();
        TimerTask task_check_own_index = new TimerTask()
        {
        	@Override
        	public void run()
        	{
        		if(GlobalObject.getRunStatus())
       			{
        			Set<String> keys_myindex = myindex.keySet();
                
        			for(String index_entry: keys_myindex)
        			{
        				if(index_entry.equals(OverlayManagement.myNodeId))
        				{
        					System.out.println("No Action");
        				}
        				else if(PredecessorSuccessor.myPredecessors[4].equals(OverlayManagement.myNodeId)&&PredecessorSuccessor.mySuccessors[0].equals(OverlayManagement.myNodeId))
        				{
        					System.out.println("No Action");
        				}
        				else if(!CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,index_entry))
        				{
        					String value = myindex.get(index_entry);
        					myindex.remove(index_entry, value);
                		
        					String value2 = NodeId_ip_of_myindex.get(value);
        					NodeId_ip_of_myindex.remove(value, value2);
                		
        					String value3 = EmailHashId_certificates.get(index_entry);
        					EmailHashId_certificates.remove(index_entry, value3);
                		
        					IndexManagementUtilityMethods.myIndexTable();
        				}
        			}
       			}	
        	}
        
        };
                
        check_own_index.schedule(task_check_own_index, 30000, 60000);
        
        SysOutCtrl.SysoutSet(" i " + myNodeId + " will also request my immediate successor "+PredecessorSuccessor.mySuccessors[0]+"to pass index entries for which i am responsible node",2);

        Timer get_index = new Timer();
        TimerTask task_get_index= new TimerTask()
        {
        	@Override
        	public void run()
        	{
        		if(GlobalObject.getRunStatus())
        		{
        			try {
        				IndexManagementUtilityMethods.getIndexing();
        			} catch (Exception e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        					
        		}
        	}
        };
        get_index.schedule(task_get_index, 5000,60000);
        
        Timer publish_index_to_succ = new Timer();
        TimerTask task_publish_index_to_succ= new TimerTask()
        {
        	@Override
        	public void run()
        	{
        		if(GlobalObject.getRunStatus())
				{
					IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
				}	
        		
        	}
        
        };
        publish_index_to_succ.schedule(task_publish_index_to_succ, 90000, 60000);
        
        Thread t3= new Thread
        (
            new Runnable()
            {
            	@Override
            	public void run()
            	{
            		while(GlobalObject.getRunStatus()||!CommunicationManager.RxBufferIM.isEmpty())

            		{
            			SysOutCtrl.SysoutSet("Index Management thread t3 is running",2);
            			synchronized(CommunicationManager.lock_RxBufferIM )
            			{
            				while(!CommunicationManager.RxBufferIM.isEmpty())
            				{

            					SysOutCtrl.SysoutSet("Getting next file "+" from RxBufferIM for parsing and deciding next course of action",3);
            					File inFile=CommunicationManager.RxBufferIM.removeFirst();
            					
            					String[] info_from_xml = ParseXmlFile.ParseXml(inFile);		//	SysOutCtrl.SysoutSet("Parsing returning hash id : " +xmldetail);// calling parsexml method of readxmlfile and storing its return value in a string array

            					if(info_from_xml[0].equals( "0002"))
            					{
            						String[] info2_from_xml = ParseXmlFile.ParseXml_0003(inFile);
            						try
            						{
            							System.out.println("Evaluating 0002");
            							IndexManagementUtilityMethods.Add_in_Index(info2_from_xml);
            						}
            						catch (Exception e)
            						{
            							e.printStackTrace();
            						}
            						SysOutCtrl.SysoutSet("----------------------------------------------------------------------------------------------------------------------------------------------------");
            					
            					}

            					else if(info_from_xml[0].equals( "0003"))
            					{
            						String[] info_from_xml_0003 = ParseXmlFile.ParseXml_0003(inFile);
            						            						
            						String hashIdFromxml=info_from_xml_0003[1];
                            
            						String Node_id_of_querried_hash = null;
            						
            						String selfIp = IndexManagementUtilityMethods.getMyIp();
            						String selfNodeId = OverlayManagement.myNodeId;
        							                                                 
            						System.out.println("Received query email_hash : "+hashIdFromxml);
                            
            						Node_id_of_querried_hash= IndexManagementUtilityMethods.Search_in_Index(hashIdFromxml);
            						SysOutCtrl.SysoutSet("value after checking index table :"+Node_id_of_querried_hash);
            						
            						String caller= info_from_xml_0003[3];
            						String caller_ip = info_from_xml_0003[4];
                            
            						boolean flag = true;
            						if(Node_id_of_querried_hash.equals("NoEntryInIndexTable"))
            						{
            							flag = false;
            							SysOutCtrl.SysoutSet("no match found in this index table sending 'NoEntryInIndexTable' to caller",2);
            							IndexManagementUtilityMethods.informNodeIdToCaller(caller,Node_id_of_querried_hash,hashIdFromxml, flag, caller_ip);
            						}
							
            						else
            						{	
            							SysOutCtrl.SysoutSet(" match found in this index table informing caller",2);

            							IndexManagementUtilityMethods.informNodeIdToCaller(caller,Node_id_of_querried_hash, hashIdFromxml, flag, caller_ip);
            							
            							String caheIp = NodeId_ip_of_myindex.get(Node_id_of_querried_hash);

            							if(info_from_xml_0003[6].equals("null"))
            							{
            								IndexManagementUtilityMethods.createXmlForCacheEntry(info_from_xml_0003[4],caheIp,info_from_xml_0003[3],selfNodeId,selfIp,hashIdFromxml,Node_id_of_querried_hash);
            							}	
            							else
            							{
            								IndexManagementUtilityMethods.createXmlForCacheEntry(info_from_xml_0003[4],caheIp,info_from_xml_0003[3],selfNodeId,selfIp,hashIdFromxml,Node_id_of_querried_hash);
            								
            								String Inter_nodeid_ip = info_from_xml[6];
            								
            								int number =0;
            								String Inter_nodeid = null;
            								String Inter_ip = null;
                    					
            								number = Inter_nodeid_ip.length();
            								Inter_nodeid = Inter_nodeid_ip.substring(0,40);
            								Inter_ip = Inter_nodeid_ip.substring(40,number);
                    						
            								IndexManagementUtilityMethods.createXmlForCacheEntry(Inter_ip, caheIp, Inter_nodeid, selfNodeId, selfIp, hashIdFromxml, Node_id_of_querried_hash);
            							}
            						}	
            					}
            					
            					else if(info_from_xml[0].equals( "0333"))
            					{
            						String cache_ip_emailHash = info_from_xml[2];
            						
            						int number = 0;
            						String cache_emailid_hash = null;
            						String cache_ip = null;
            					
            						number = cache_ip_emailHash.length();
            						cache_emailid_hash = cache_ip_emailHash.substring(0,40);
            						cache_ip = cache_ip_emailHash.substring(40,number);
            						
            						String cache_nodeId_for_emailIdHash = info_from_xml[5];
            						
            						cached_index.put(cache_emailid_hash, cache_nodeId_for_emailIdHash);
            						cached_NodeId_ip_index.put(cache_nodeId_for_emailIdHash, cache_ip);
            						            						
            						IndexManagementUtilityMethods.timer(cache_emailid_hash);
            					}

            					else if(info_from_xml[0].equals( "0004"))
            					{
            						if(!OverlayManagement.myNodeId.equals(info_from_xml[3]))
            				        {
            							Map<String, String> generatedIdexing=IndexManagementUtilityMethods.generateIndexingForNewlyJoinedNode(info_from_xml);
            						
            							String Ip_of_node_asking_for_index=info_from_xml[4];
            							File generatedIndexingXml = null;

            							try
            							{
            								generatedIndexingXml = IndexManagementUtilityMethods.convert_hashmap_of_indexTable_transmitted_toxml(generatedIdexing,"0019", info_from_xml[3], OverlayManagement.myNodeId, "IndexingReply", IndexManagementUtilityMethods.getMyIp(), "2222", "DeleteNodeIdFromIndexIpTable");
            							}
            							catch (FileNotFoundException e)
            							{
            								e.printStackTrace();
            							}
            							catch (TransformerException e)
            							{
            								e.printStackTrace();
            							}
            							catch (ParserConfigurationException e)
            							{
            								e.printStackTrace();
            							}	
            							      					
            						
            							OverlayManagementUtilityMethods.sendFileDirect(Ip_of_node_asking_for_index, generatedIndexingXml);
            							//	com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(generatedIndexingXml);
            							SysOutCtrl.SysoutSet("Indexing Generated and sent for newly joined node"+generatedIndexingXml.toPath());
            				        }
            					}

            					else if(info_from_xml[0].equals("1111"))
            					{

            						IndexManagementUtilityMethods.removeIndexing(info_from_xml[3]);

            						// 	inform the sender about this value ie node id by setting up a socket.
            					

            					}
            					else if(info_from_xml[0].equals( "0012"))	// reply of search query
            					{
            						String querried_ip=info_from_xml[2];
            						searchReply.put(info_from_xml[5], querried_ip);// storing querried_emailid_hash(5) and ip(2)  of called number in searchreply
            						IndexManagementUtilityMethods.storeIp( info_from_xml[5]);
            						searchReplyReceived=true;
            						SysOutCtrl.SysoutSet("Search query reply: nodeId "+querried_ip,2);
            					
            					}
            					
            					else if(info_from_xml[0].equals( "0019"))	// generate indexing query
            					{
            						SysOutCtrl.SysoutSet("index table before"+myindex,2);
            						
            						Map<String, String>  temp = new ConcurrentHashMap<String, String>();
            						temp.putAll( IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            						
            						Set<String> emailhash_extracted = temp.keySet(); 
        							Collection<String> nodeid_extracted = temp.values();
        					
        							
        							String[] emailhash = emailhash_extracted.toArray(new String[emailhash_extracted.size()]);
        							String[] nodeid = nodeid_extracted.toArray(new String[nodeid_extracted.size()]);
            						
        							boolean entry_made = false;
        							
        							for (int i=0; i<emailhash.length;i++)
        							{
        								if(!myindex.containsKey(emailhash[i]))
        								{	
        									myindex.put(emailhash[i], nodeid[i]);
        									EmailHashId_certificates.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile));
        									NodeId_ip_of_myindex.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
        									entry_made = true;
        								}	
        							}
        							
        							IndexManagementUtilityMethods.myIndexTable();
            						            						
            						if(entry_made)
        								IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();        						
            						temp.clear();
            						
            						SysOutCtrl.SysoutSet("index table received from my immdediate successor",2);
            						SysOutCtrl.SysoutSet("index table after"+myindex,2);
            					
            					}

            					else if(info_from_xml[0].equals( "0021"))	// reply of new node checking search query
            					{
            						IndexManagementUtilityMethods.newNodeCheckingReply(inFile);
            						SysOutCtrl.SysoutSet("New Node has sent request for checking the existance of node id",2);
            					
            					}

            					else if(info_from_xml[0].equals( "0020"))
            					{
            						String nodeId = info_from_xml[3];
            						String ipAdd = info_from_xml[4];
            						
            						if(RTUpdate9.Routing_Table.containsKey(nodeId))
            						{
            							RTUpdate9.Routing_Table.put(nodeId, ipAdd);
            							
            							Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
            							save.Save_RTNow();
            						}	
            						
            						if(CommunicationUtilityMethods.myIpTable.containsKey(nodeId))
            							CommunicationManager.myIpTable.put(nodeId, ipAdd);
            						
            						if(!CommunicationUtilityMethods.myIpTable.containsKey(nodeId))
            							CommunicationManager.update_my_IpTable(nodeId, ipAdd);//updating Ip Table
            						
            						if(cached_NodeId_ip_index.containsKey(nodeId))
            						{
            							cached_NodeId_ip_index.put(nodeId, ipAdd);
            						}
            						
            					}
            					else if(info_from_xml[0].equals( "0010"))// here checking if received xmlindex file is from any of my predecessors and updating the same
            					{
            						SysOutCtrl.SysoutSet("index table copy received from one of the my predecessors",2);
            						String from_node_id=info_from_xml[3];
            					
            						if(from_node_id.equals(PredecessorSuccessor.myPredecessors[0]))
            						{
            							SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor1 "+PredecessorSuccessor.myPredecessors[0],2);
            							SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex1",2);
            							myindex1.clear();
            							myindex1.putAll(IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            							nodeid_ip_myindex1.clear();
            							nodeid_ip_myindex1.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
            							
            							TreeMap<String,String> TempMap= new TreeMap<String,String>();
            							TempMap = IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile);
            							
            							sms_retrival_thread.PredNodeId_Ip.put(info_from_xml[3], info_from_xml[4]);
            							sms_retrival_thread.PredNodeId_EmailHashCertMap.put(info_from_xml[3], TempMap);
            						            							
            							SysOutCtrl.SysoutSet("myIndex1(myPredecessor1) index table saved successfully",2);
            						
            						}

            						if(from_node_id.equals(PredecessorSuccessor.myPredecessors[1]))
            						{
            							SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor2 "+PredecessorSuccessor.myPredecessors[1],2);
            							SysOutCtrl.SysoutSet(" Now converting this xml to index table and saving in myIndex2",2);
            							myindex2.clear();
            							myindex2.putAll(IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            							nodeid_ip_myindex2.clear();
            							nodeid_ip_myindex2.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
            	            							
            							TreeMap<String,String> TempMap= new TreeMap<String,String>();
            							TempMap = IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile);
            							
            							sms_retrival_thread.PredNodeId_Ip.put(info_from_xml[3], info_from_xml[4]);
            							sms_retrival_thread.PredNodeId_EmailHashCertMap.put(info_from_xml[3], TempMap);
            							
            							SysOutCtrl.SysoutSet("myIndex2(myPredecessor2) index table saved successfully",2);
            						
            						}

            						if(from_node_id.equals(PredecessorSuccessor.myPredecessors[2]))
            						{
            							SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor3 "+PredecessorSuccessor.myPredecessors[2],2);
            							SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex3",2);
            							myindex3.clear();
            							myindex3.putAll(IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            							nodeid_ip_myindex3.clear();
            							nodeid_ip_myindex3.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
            							            							
            							TreeMap<String,String> TempMap= new TreeMap<String,String>();
            							TempMap = IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile);
            							
            							sms_retrival_thread.PredNodeId_Ip.put(info_from_xml[3], info_from_xml[4]);
            							sms_retrival_thread.PredNodeId_EmailHashCertMap.put(info_from_xml[3], TempMap);
            							
            							SysOutCtrl.SysoutSet("myIndex3(myPredecessor3) index table saved successfully",2);
            						
            						}

            						if(from_node_id.equals(PredecessorSuccessor.myPredecessors[3]))
            						{

            							SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor4 "+PredecessorSuccessor.myPredecessors[3],2);
            							SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex4",2);
            							myindex4.clear();
            							myindex4.putAll(IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            							nodeid_ip_myindex4.clear();
            							nodeid_ip_myindex4.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
            							            							
            							TreeMap<String,String> TempMap= new TreeMap<String,String>();
            							TempMap = IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile);
            							
            							sms_retrival_thread.PredNodeId_Ip.put(info_from_xml[3], info_from_xml[4]);
            							sms_retrival_thread.PredNodeId_EmailHashCertMap.put(info_from_xml[3], TempMap);
            				            
            							SysOutCtrl.SysoutSet("myIndex4(myPredecessor4) index table saved successfully",2);
            						}

            						if(from_node_id.equals(PredecessorSuccessor.myPredecessors[4]))
            						{
            							SysOutCtrl.SysoutSet("This file is the index table copy received from myPredecessor5 "+PredecessorSuccessor.myPredecessors[4],2);
            							SysOutCtrl.SysoutSet("Now converting this xml to index table and saving in myIndex5",2);
            							myindex5.clear();
            							myindex5.putAll(IndexManagementUtilityMethods.convertXmlToIndexTable(inFile));
            							nodeid_ip_myindex5.clear();
            							nodeid_ip_myindex5.putAll(IndexManagementUtilityMethods.convert_xml_to_Node_ip_table(inFile));
            							
            							TreeMap<String,String> TempMap= new TreeMap<String,String>();
            							TempMap = IndexManagementUtilityMethods.convert_xml_to_Node_encrCert_table(inFile);
            							
            							sms_retrival_thread.PredNodeId_Ip.put(info_from_xml[3], info_from_xml[4]);
            							sms_retrival_thread.PredNodeId_EmailHashCertMap.put(info_from_xml[3], TempMap);
            				            	
            							SysOutCtrl.SysoutSet("myIndex5(myPredecessor5) index table saved successfully",2);
            						}
            					}
            				}
            				SysOutCtrl.SysoutSet("Thread t3 reply- Rx Buffer IM is empty");
            			}

    					try
    					{	
    						Thread.sleep(10000);
    					}
    					catch (InterruptedException e)
    					{
    						e.printStackTrace();
    					}
    					
    					
            		}
            	}
            }
        );	
        t3.start();

        SysOutCtrl.SysoutSet("boolean flagMySuccessorsUpdatedForIndexManager is "+OverlayManagement.flagMySuccessorsUpdatedForIndexManager,2);

        Thread t4= new Thread
        (
            new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    IndexManagementUtilityMethods.Change_In_IP_Check();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        );
        t4.start();

        Thread t5= new Thread
        (
            new Runnable()
        {
            @Override
            public void run()
            {
                while(GlobalObject.getRunStatus())
                {
                    SysOutCtrl.SysoutSet(" thread t5 index manager is waiting for predecessor update",2);
                    
                    IndexManagementUtilityMethods.UpdateIndexing();
                    System.out.println("update indexing from pred");
                    
                    try
                    {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }    
                }
            }
        }
        );
        t5.start();
        
        Timer check = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
            if(GlobalObject.getRunStatus())
            {	
            	System.out.println("Iam resposible for these email hashes : ");
            	System.out.println(myindex);
            	System.out.println("my index node id and ip : "+NodeId_ip_of_myindex);
            	System.out.println("my index of imm pred : "+myindex5);
        		System.out.println("my index of farthest pred : "+myindex1);
        		String my_immediate_predecessor = PredecessorSuccessor.myPredecessors[4];
        		String my_immediate_successor = PredecessorSuccessor.mySuccessors[0];
        		System.out.println("my pred imm : "+my_immediate_predecessor);
        		System.out.println("my suc imm : "+my_immediate_successor);
        		System.out.println("My Ip Table : "+CommunicationManager.myIpTable);
        		System.out.println("My Trans Buf : "+CommunicationManager.TransmittingBuffer);
        		System.out.println("My Rec Buf : "+CommunicationManager.ReceivingBuffer);
        		System.out.println("My Index Rec Buff : "+CommunicationManager.RxBufferIM);
        		System.out.println("My SMS Rec Buff : "+CommunicationManager.RxBufferSMS);
        		System.out.println("Iam resposible for these Email Hash Id's and their cert : ");
        		System.out.println(EmailHashId_certificates);
        		System.out.println("my cached index : "+cached_index);
        		System.out.println("my cached ip table : "+cached_NodeId_ip_index);
        		System.out.println("my routing table : "+RTUpdate9.Routing_Table);
        		Map<String, String> Alive_nodes = OverlayManagement.AliveNodes;
        		System.out.println("my alive nodes : "+Alive_nodes );
        		System.out.println("search reply: "+searchReply_cert);
        		
        		System.out.println("pred, mid, succ range: ");
        		for (int i = 0; i < RTUpdate9.Pred.length; i++)
        			if(RTUpdate9.Pred[i][0]!=null)
        				System.out.println("Pred Rg Element at index " + i +" : "+ RTUpdate9.Pred[i][0]);          
        		SysOutCtrl.SysoutSet("",2);
        		for (int i = 0; i < RTUpdate9.Succ.length; i++)
        			if(RTUpdate9.Succ[i][0]!=null) 
        				System.out.println("Succ Rg Element at index " + i +" : "+ RTUpdate9.Succ[i][0]);         
        		SysOutCtrl.SysoutSet("",2);
        		for (int i = 0; i < RTUpdate9.Mid.length; i++)
        			if(RTUpdate9.Mid[i][0]!=null) 
        				System.out.println("Mid Rg Element at index " + i +" : "+ RTUpdate9.Mid[i][0]);
        		
        		System.out.println("My succ list: "+CommunicationManager.succ);
        		System.out.println("My Pred list: "+CommunicationManager.pred);
            }
        	}

        };

        check.schedule(task, 3000, 3000);
        
            
    }
          
}