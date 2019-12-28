 	
package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.PurgeEntry;
import com.ehelpy.brihaspati4.routingmgmt.RTManager;
import com.ehelpy.brihaspati4.routingmgmt.Save_Retrieve_RT;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class HeartbeatMonitoring extends OverlayManagementUtilityMethods{
	       
	  public static void heartbeatCheck() {
          // TODO Auto-generated method stub
  	SysOutCtrl.SysoutSet("you are in heartbeatcheck method cheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeckkkkkkkkkkkkkkkkkkkkk111111111111111111", 2);
  	
  	/// code for checking  ip table entries
  	
  	boolean bool_iptable = false;
     Set<String> keys_ip = CommunicationManager.myIpTable.keySet();
     for(String key: keys_ip)
     {
   	  String ip = CommunicationManager.myIpTable.get(key);
   	  bool_iptable =	CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip);
   	 
   	  if(!bool_iptable)
   	  {
   		  System.out.println("node to be purged : "+key);
   		  CommunicationManager.myIpTable.remove(key, ip);
   		  OverlayManagement.updatPredSec();
   		 
   		  Set<String> nodeId_extracted = CommunicationManager.myIpTable.keySet();    			
   		  Collection<String> ip_extracted = CommunicationManager.myIpTable.values();
   													
   		  String[] nodeIdArr = nodeId_extracted.toArray(new String[nodeId_extracted.size()]);
   		  String[] ipAddArr = ip_extracted.toArray(new String[ip_extracted.size()]);

   		  CommunicationManager.lock.lock();
   		  try
   		  {
   				XmlFileSegregation.ipTableWriter(nodeIdArr,ipAddArr);
   		  }
   		  finally
   		  {
   				CommunicationManager.lock.unlock();
   		  }
   			
   	  }	  
	 }
    
     //removing entries from suu and pred
 	for(int i = 0; i<CommunicationManager.succ.size(); i++)
	{
		if(!CommunicationManager.myIpTable.containsKey(CommunicationManager.succ.get(i)))
			CommunicationManager.succ.remove(i);
	}
	
	for(int i = 0; i<CommunicationManager.pred.size(); i++)
	{
		if(!CommunicationManager.myIpTable.containsKey(CommunicationManager.pred.get(i)))
			CommunicationManager.pred.remove(i);
	}
      
	
	///code for myindex and cached index entries removal
	
      Collection<String> ipList_myindex = IndexManagement.NodeId_ip_of_myindex.values();
      Collection<String> ipList_cached_index = IndexManagement.cached_NodeId_ip_index.values();
      
      Object[] ip_array_myindex = ipList_myindex.toArray();
      Object[] ip_array_cached_index = ipList_cached_index.toArray();
      			
      
      boolean entry_removed = false;
      
      for(int i=0;i<ip_array_myindex.length;i++) 
      {
      	String ip = (String) ip_array_myindex[i];
      	
      	String ip_check = null;
      	String nodeid_of_ip = null;    
      	
      	Set<String> keys1 = IndexManagement.NodeId_ip_of_myindex.keySet();
          for(String key : keys1)   
          {
          	ip_check = IndexManagement.NodeId_ip_of_myindex.get(key);
          	if(ip_check.equals(ip))
          	{
          		nodeid_of_ip = key;
          	   	break;
          	}
          }
          
            
          String node_id_of_email_hash_check = null;
          String email_hash = null;
      	
          Set<String> keys2 = IndexManagement.myindex.keySet();
          for(String key : keys2)   
          {
          	node_id_of_email_hash_check = IndexManagement.myindex.get(key);
          	if(node_id_of_email_hash_check.equals(nodeid_of_ip))
          	{
          		email_hash = key;
          		break;
          	}
          }
      	
        boolean bool;
      	
      	SysOutCtrl.SysoutSet("Ip table from first method " +ip, 2);
          
                  
      	bool =	CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip);
      	SysOutCtrl.SysoutSet("bool"+bool, 2);
      	
      	if(bool==false)
      	{
      		SysOutCtrl.SysoutSet("DeadAfterPing", 2);  
             	
      		System.out.println("Ip to be removed");
  		
        		IndexManagement.NodeId_ip_of_myindex.remove(nodeid_of_ip, ip);
        		
        		if(!email_hash.equals(null)||!email_hash.equals("null")||!email_hash.equals(" ")||!email_hash.equals("")||!email_hash.isEmpty())
        		{	
        			IndexManagementUtilityMethods.myindex.remove(email_hash, node_id_of_email_hash_check);
        			entry_removed = true;
        		}	
        	
      	}
      }
      
      if(entry_removed)
    	  IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
      
      for(int i=0;i<ip_array_cached_index.length;i++) 
      {
      	String ip = (String) ip_array_cached_index[i];
      	
      	String ip_check = null;
      	String nodeid_of_ip = null;    
      	
      	Set<String> keys1 = IndexManagement.cached_NodeId_ip_index.keySet();
          for(String key : keys1)   
          {
          	ip_check = IndexManagement.cached_NodeId_ip_index.get(key);
          	if(ip_check.equals(ip))
          	{
          		nodeid_of_ip = key;
          	   	break;
          	}
          }
          
            
          String node_id_of_email_hash_check = null;
          String email_hash = null;
      	
          Set<String> keys2 = IndexManagement.cached_index.keySet();
          for(String key : keys2)   
          {
          	node_id_of_email_hash_check = IndexManagement.cached_index.get(key);
          	if(node_id_of_email_hash_check.equals(nodeid_of_ip))
          	{
          		email_hash = key;
          		break;
          	}
          }
      	
        boolean bool;
      	
      	SysOutCtrl.SysoutSet("Ip table from first method " +ip, 2);
          
                  
      	bool =	CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip);
      	SysOutCtrl.SysoutSet("bool"+bool, 2);
      	
      	if(bool==false)
      	{
      		SysOutCtrl.SysoutSet("DeadAfterPing", 2);  
             	
      		System.out.println("Ip to be removed");
  		
        		IndexManagement.cached_NodeId_ip_index.remove(nodeid_of_ip, ip);
        		
        		if(!email_hash.equals(null)||!email_hash.equals("null")||!email_hash.equals(" ")||!email_hash.equals("")||!email_hash.isEmpty())
        			IndexManagementUtilityMethods.cached_index.remove(email_hash, node_id_of_email_hash_check);
        			
      	}
      }
      			
     ///for routing table removal
      
      boolean bool_routingtable = false;
      Set<String> keys_rt = RTManager.Routing_Table.keySet();
      for(String key: keys_rt)
      {
    	  String ip = RTManager.Routing_Table.get(key);
    	  bool_routingtable =	CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip);
    	  if(!bool_routingtable)
    	  {
    		  System.out.println("Entry to be pugedd: "+ key);
				
					  RTManager.Routing_Table.remove(key, ip);
					  PurgeEntry.purge(key);
    	  }
      }
      
      Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
	  save.Save_RTNow();      
      
      
      
  SysOutCtrl.SysoutSet("HM run completed doooooooooooooooooooooooooooooooooneeeeeeeeeeeeeeeeeeeeeeeeeee 1111111111111111111", 2);
	        
  }

        
        
	  public static void heartbeatCheck1() throws IOException {
                // TODO Auto-generated method stub
		  SysOutCtrl.SysoutSet("you are in heartbeatcheck method cheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeckkkkkkkkkkkkkkkkkkkkk22222222222222", 2);
        	
		  Map<String,String>	ActNodeipList= OverlayManagement.AliveNodes;
		  List<String> Act=ActNodeipList.keySet().stream().collect(Collectors.toList());
		  Collections.sort(Act);
		//  com.ehelpy.brihaspati4.routingmgmt.PurgeEntry deleteFromRt = new com.ehelpy.brihaspati4.routingmgmt.PurgeEntry();
            			
		  Set<String> keys = RTManager.Routing_Table.keySet();
		  
		  try
		  {
			  for(String key: keys)
			  {
				  if(Act.contains(key))
				  {
					  System.out.println("the NON purged entries:"+key);
				  }
				  else 
				  {	 
					  try {
						  String purge = key;
						  String ipadd = RTManager.Routing_Table.get(key);
						  if(!purge.equals(null))
						  {
							  System.out.println("Entry to be pugedd: "+ purge);
							  
							  if(CommunicationManager.myIpTable.containsKey(purge))
							  { 
								  CommunicationManager.myIpTable.remove(purge, ipadd);
								  OverlayManagement.updatPredSec();
								  
								  Set<String> nodeId_extracted = CommunicationManager.myIpTable.keySet();    			
						   		  Collection<String> ip_extracted = CommunicationManager.myIpTable.values();
						   													
						   		  String[] nodeIdArr = nodeId_extracted.toArray(new String[nodeId_extracted.size()]);
						   		  String[] ipAddArr = ip_extracted.toArray(new String[ip_extracted.size()]);

						   		  CommunicationManager.lock.lock();
						   		  try
						   		  {
						   				XmlFileSegregation.ipTableWriter(nodeIdArr,ipAddArr);
						   		  }
						   		  finally
						   		  {
						   				CommunicationManager.lock.unlock();
						   		  }
							  }
								  
							  RTManager.Routing_Table.remove(purge, ipadd);
							  PurgeEntry.purge(purge);
						  }
					  }catch(NullPointerException e)
					  {}
            	 }
			  }
		  }
		  catch(ConcurrentModificationException e)
		  {
			  
		  }
		  
		  		  
		  Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
		  save.Save_RTNow();
		  
		  SysOutCtrl.SysoutSet("RT saved after Purging", 2);
      	
		  SysOutCtrl.SysoutSet("HM run completed doooooooooooooooooooooooooooooooooneeeeeeeeeeeeeeeeeeeeeeeeeee22222222222222222", 2);
		  OverlayManagement.AliveNodes.clear(); 
		  System.out.println(AliveNodes);
            			
        }
		
  	}
