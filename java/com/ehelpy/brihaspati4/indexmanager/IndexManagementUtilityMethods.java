package com.ehelpy.brihaspati4.indexmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.PurgeEntry;
import com.ehelpy.brihaspati4.routingmgmt.RTUpdate9;
import com.ehelpy.brihaspati4.routingmgmt.Save_Retrieve_RT;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;


//Sqn ldr Deepak Sharma Dated 15 Apr 2018 ; 1255 Hrs
// this class contains all the important functions used by index manager module

public class IndexManagementUtilityMethods extends IndexManagement
{
    static int interval= 10;
    static Timer timer;

    public static void addIndexRequest(String key, String value)
    {   // this method add index entry
    	
    	String encr_cert = get_certificate();
    	
    	if(key.equals(OverlayManagement.myNodeId))
        {
    		if(!myindex.containsKey(key))
    		{	
    			myindex.put(key, value);
        		myIndexTable();
        		NodeId_ip_of_myindex.put(value, PresentIP.MyPresentIP());
        		
        		EmailHashId_certificates.put(key, encr_cert);
        		
        		SysOutCtrl.SysoutSet(" HashId stored at self node being responsible node");
        		IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
        	}	
        }

    	else if(PredecessorSuccessor.myPredecessors[4].equals(OverlayManagement.myNodeId)&&PredecessorSuccessor.mySuccessors[0].equals(OverlayManagement.myNodeId))
    	{
    		 myindex.put(key, value);
    		 myIndexTable();
             NodeId_ip_of_myindex.put(value, PresentIP.MyPresentIP());
             
             EmailHashId_certificates.put(key, encr_cert);
     		
             SysOutCtrl.SysoutSet(" HashId stored at self node being responsible node");
    	}

        else if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,key))
        {

            SysOutCtrl.SysoutSet("my node id is smaller than pred");
            
            if(!myindex.containsKey(key))
            {	
            	myindex.put(key, value);
            	myIndexTable();
            	NodeId_ip_of_myindex.put(value, PresentIP.MyPresentIP());
            	
            	EmailHashId_certificates.put(key, encr_cert);
        		
            	SysOutCtrl.SysoutSet(" HashId stored at self node being responsible node");
            	IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
            }
        }

        else
        {
            File file = IndexManagementUtilityMethods.createXmlAddIndexQuery(key, value, encr_cert );
        //    CommunicationManager.TransmittingBuffer.add(file);
            com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(file);
        }
    }

    private static  String own_encryptcert = null;
    public static String get_certificate()
    {
    	X509Certificate cert;
		try {
			cert = ReadVerifyCert.returnClientCert();
			byte[] certbyte = cert.getEncoded();
	    	own_encryptcert = new String(Base64.getEncoder().encode(certbyte));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return own_encryptcert;
    }
    
    public static void Change_In_IP_Check()
    {
        while (CheckIP==true)
        {
            IPAdd = com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
            if(!MyIP.equals(IPAdd))
            {
                CheckIP=false;
            }
        }
        
        System.out.println("MY old IP was : "+MyIP);

        MyIP = IPAdd;
        
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

        String email_hash = selfHashId;
        String myNodeId = OverlayManagement.myNodeId;

        IndexManagementUtilityMethods.addIndexRequest(email_hash, myNodeId);

        Collection<String> Node_id_extracted = CommunicationManager.myIpTable.keySet();
        Object[] Nodeid_array = Node_id_extracted.toArray();

        for(int i=0; i<Nodeid_array.length; i++)
        {
            String Node_id= (String) Nodeid_array[i];
            System.out.println(" "+Node_id);
            File IPUpdate = IndexManagementUtilityMethods.createXmlUpdate_IP(Node_id);
        
            com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(IPUpdate);
            SysOutCtrl.SysoutSet("Updated IP file sent to TransmittingBuffer :"+Node_id, 2);
            SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
        }
        
        Collection<String> Node_id_extracted1 = RTUpdate9.Routing_Table.keySet();
        Object[] Nodeid_array1 = Node_id_extracted1.toArray();

        for(int i=0; i<Nodeid_array1.length; i++)
        {
            String Node_id= (String) Nodeid_array[i];
            System.out.println(" "+Node_id);
            File IPUpdate = IndexManagementUtilityMethods.createXmlUpdate_IP(Node_id);
        
            com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(IPUpdate);
            SysOutCtrl.SysoutSet("Updated IP file sent to TransmittingBuffer :"+Node_id, 2);
            SysOutCtrl.SysoutSet("Tx Buffer state vis at UpdateIP :"+CommunicationManager.TransmittingBuffer, 2);
        }

        System.out.println("Updating my new Ip at Resposible Node and nodes present in my nodelist.");
        System.out.println("My New IP is : "+MyIP);

        CheckIP=true;

        Change_In_IP_Check();
    }

    public static void timer(String Cached_Entry)
	{
    	Timer time_set = new Timer();
		TimerTask task_timer = new TimerTask()
		{
            @Override
            public void run()
            {
            	if(cached_index.containsKey(Cached_Entry))
            	{
            		String nodeid_of_CacheIndex = cached_index.get(Cached_Entry);
            		cached_index.remove(Cached_Entry, nodeid_of_CacheIndex);
            		
            		if(cached_NodeId_ip_index.containsKey(nodeid_of_CacheIndex))
                	{
            			String Ip_of_CachedIndexNodeId = cached_NodeId_ip_index.get(nodeid_of_CacheIndex);
            			cached_NodeId_ip_index.remove(nodeid_of_CacheIndex, Ip_of_CachedIndexNodeId); 
                	}
            	}
            }
          };

          time_set.schedule(task_timer, 10800000);
	}
    
    public static void Add_in_Index(String[] xml_info) // xml_info[1]=hashid from readxmlfile
    {       
        if(!myindex.containsKey(xml_info[1]))
        {	
        	myindex.put(xml_info[1], xml_info[2]);
        	myIndexTable();
        	NodeId_ip_of_myindex.put(xml_info[2], xml_info[4]);
        	
        	EmailHashId_certificates.put(xml_info[1], xml_info[6]);
        	
        	SysOutCtrl.SysoutSet("myIndex is "+myindex,2);
        	IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
        }
    }

    public static String Search_in_Index(String hashIdFromxml) // This method search an entry in index table, hashid from readxmlfile
    {
        String self_node_id = OverlayManagement.myNodeId; // here calling a function myNodeId from the routing
        // table who will return node's node_id.
        SysOutCtrl.SysoutSet("myNodeId is : " + self_node_id,3);
        String value = "";
        String hash_id = hashIdFromxml;
        System.out.println("query received : "+hash_id);
        
       	if (hash_id.equals(self_node_id)) // it returns boolean value// Here, if it is a exact match i.e. queried
        {
            value = self_node_id;
        }
    	
       	// new code ///////////////////////////////
	/*	else
		{
		    boolean flag = true;
			System.out.println("in else block");
			
			Set<String> keys = myindex.keySet();
    		for(String key: keys)
			{
				System.out.println("in for block");
								
				if(key.equals(hash_id))
				{
					System.out.println("in if block");
					flag = false;
					break;
				}
				
			}
			
			if(flag)
				value ="NoEntryInIndexTable";
				
				
			else
			{
				value = myindex.get(hash_id);
			}
			
				
		}*/
       	//////////////////////
	       
      	else if (myindex.containsKey(hash_id)) // Returns true if this map contains a mapping for the specified key.
        {
           value = myindex.get(hash_id);
           SysOutCtrl.SysoutSet("value of hashID from index table of search_in_index :"+value);
        }

        else if(!myindex.containsKey(hash_id))
        {
           value="SORRY! Called Person Is Not Active At The Moment.";
        }

        SysOutCtrl.SysoutSet("value of hashID from index table of search_in_index b4 returning :"+value);

        return value;
    }

    public static void myIndexTable() // saving my index table
    {
    //    SysOutCtrl.SysoutSet("index table before entry");
     //   SysOutCtrl.SysoutSet("myIndex"+myindex,2);
        
    //    myindex.put(hash_id, node_id); // here appending xml details i.e. hash_id and node_id in index
    	
    	SysOutCtrl.SysoutSet("myIndex"+myindex,2);
        
        String A =myindex.keySet().toString();
        FileWriter F=null;
        try
        {
            F = new FileWriter("MyIndexTable.txt");
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            F.write(A);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            F.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        printMap(myindex);
    }
    
	public static void Ip_txt_empty()
	{
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter("IpTable.txt");
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		
		writer.println("");
		writer.flush();
	}
    
    public static <K, V> void printMap(Map<K, V> map) 
	{
    	System.out.println("MY index: ");
    	System.out.println("");
		for (Map.Entry<K, V> entry : map.entrySet()) 
	    	System.out.println( entry.getKey() +" : "+entry.getValue());
	}

    public static void getIndexing() throws Exception
    {   // creating xml to get indexing from immdt successor
        // String[] mysuccessor = rout.getmysuccessors(); // asking successor details
        // from routing table to whom this request will be sent
        //String mysuccessor[] = OverlayManagement.getMyPredecessors();

        // now i hv to create a request message to send it to my successor that i need
        // those entries from its index table to whom now i am repsonsible, bcz i
        // recently joined the overlay.
    	
   // 	if(PredecessorSuccessor.mySuccessors[0].equals(OverlayManagement.myNodeId))
    //	{}

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try
        {
            db = f.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            e.printStackTrace();
        }

        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0004"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);
        String to_node_id = PredecessorSuccessor.mySuccessors[0];

        // SysOutCtrl.SysoutSet(to_node_id+"my succ 0");
        Text t1 = doc.createTextNode(to_node_id);
        // String nextHopNodeId=rout.getNextHop(");// next hop will be given by routing
        // module
        Text t2 = doc.createTextNode(selfHashId);// here instead of node id we are putting hash id so that successor can transfer my entry from his index table
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222";
        Text t5 = doc.createTextNode(selfPortNo);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try
        {
            transformer = transformerFactory.newTransformer();
        }
        catch (TransformerConfigurationException e)
        {
            e.printStackTrace();
        }

        DOMSource source = new DOMSource(doc);
        StreamResult result = null;

        try
        {
            result = new StreamResult(new FileOutputStream("getIndexingQuery.xml"));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        try
        {
            transformer.transform(source, result);
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
        }


        File file1 = new File("getIndexingQuery.xml");

        SysOutCtrl.SysoutSet("requestIndexingQuery.xml file Generated.",2);

       // CommunicationManager.TransmittingBuffer.add(file1); // passing the info to addmessage method of tablebuffer
        // class
        com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(file1);
        SysOutCtrl.SysoutSet("Get indexing method sending request to immediate successor " + PredecessorSuccessor.mySuccessors[0],2);

    }

    static void UpdateIndexing()
    {   // when a node receive index table copy from predecessor, it will check its position wrt previous and update accordingly

    	String[] my_pred = PredecessorSuccessor.getMyPredecessors(OverlayManagement.nodeId, OverlayManagement.myNodeId);
    	
    	int change = 0;
    	
    	String ip_mypred_1 = get_pred_ip(my_pred[4]);
    	String ip_mypred_2 = get_pred_ip(my_pred[3]);
    	String ip_mypred_3 = get_pred_ip(my_pred[2]);
    	String ip_mypred_4 = get_pred_ip(my_pred[1]);
    	String ip_mypred_5 = get_pred_ip(my_pred[0]);
    	
    	boolean check_1 = true;
    	boolean check_2 = true;
    	boolean check_3 = true;
    	boolean check_4 = true;
    	boolean check_5 = true;
    	
    	if(ip_mypred_1 == "null")
    		check_1 =false;
    	else
    	{	
    		check_1 = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip_mypred_1);
    		if(!check_1)
    		{
    			if(CommunicationManager.myIpTable.containsKey(my_pred[4]))
    			{	
    				CommunicationManager.myIpTable.remove(my_pred[4], ip_mypred_1);
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
    			if(RTUpdate9.Routing_Table.containsKey(my_pred[4]))
    			{
    				RTUpdate9.Routing_Table.remove(my_pred[4], ip_mypred_1);
    				PurgeEntry.purge(my_pred[4]);
    				
    				Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
    				save.Save_RTNow(); 
    			}
    		}
    	}	
    	
    	if(ip_mypred_2 == "null")
    		check_2 =false;
    	else
    	{	
    		check_2 = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip_mypred_2);
    		if(!check_2)
    		{
    			if(CommunicationManager.myIpTable.containsKey(my_pred[3]))
    			{	
    				CommunicationManager.myIpTable.remove(my_pred[3], ip_mypred_2);
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
    			if(RTUpdate9.Routing_Table.containsKey(my_pred[3]))
    			{
    				RTUpdate9.Routing_Table.remove(my_pred[3], ip_mypred_2);
    				PurgeEntry.purge(my_pred[3]);
				
    				Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
    				save.Save_RTNow(); 
    			}
    		}
    	}	
    	
    	if(ip_mypred_3 == "null")
    		check_3 =false;
    	else
    	{	
    		check_3 = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip_mypred_3);
    		if(!check_3)
    		{
    			if(CommunicationManager.myIpTable.containsKey(my_pred[2]))
    			{	
    				CommunicationManager.myIpTable.remove(my_pred[2], ip_mypred_3);
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
    			if(RTUpdate9.Routing_Table.containsKey(my_pred[2]))
    			{
    				RTUpdate9.Routing_Table.remove(my_pred[4], ip_mypred_3);
    				PurgeEntry.purge(my_pred[2]);
    				
    				Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
    				save.Save_RTNow(); 
    			}
    		}
    	}
    		
    	if(ip_mypred_4 == "null")
    		check_4 =false;
    	else
    	{
    		check_4 = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip_mypred_4);
    		if(!check_4)
    		{
    			if(CommunicationManager.myIpTable.containsKey(my_pred[1]))
    			{	
    				CommunicationManager.myIpTable.remove(my_pred[1], ip_mypred_4);
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
    			if(RTUpdate9.Routing_Table.containsKey(my_pred[1]))
    			{
    				RTUpdate9.Routing_Table.remove(my_pred[1], ip_mypred_4);
    				PurgeEntry.purge(my_pred[4]);
    				
    				Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
    				save.Save_RTNow(); 
    			}
    		}
    	}	
    		
    	if(ip_mypred_5 == "null")
    		check_5 =false;
    	else
    	{	
    		check_5 = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(ip_mypred_5);
    		if(!check_5)
    		{
    			if(CommunicationManager.myIpTable.containsKey(my_pred[0]))
    			{	
    				CommunicationManager.myIpTable.remove(my_pred[0], ip_mypred_5);
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
    			if(RTUpdate9.Routing_Table.containsKey(my_pred[0]))
    			{
    				RTUpdate9.Routing_Table.remove(my_pred[0], ip_mypred_5);
    				PurgeEntry.purge(my_pred[4]);
    				
    				Save_Retrieve_RT.Save_RT save = new Save_Retrieve_RT.Save_RT();
    				save.Save_RTNow(); 
    			}
    		}
    	}	
    		
    	if(!check_1)
    		change =1;
    	
    	if(!check_1&&!check_2)
    		change =2;
    	
    	if(!check_1&&!check_2&&!check_3)
    		change =3;
    	
    	if(!check_1&&!check_2&&!check_3&&!check_4)
    		change =4;
    	
    	if(!check_1&&!check_2&&!check_3&&!check_4&&!check_5)
    		change =5;
        
		switch (change)
		{
			case 0:
				System.out.println("No change in pred");
				break;
			
			case 1:
				System.out.println("Imm Pred have left ");
				
				Set<String> keys1 = myindex5.keySet();
		        for(String key : keys1)
		           	myindex.put(key, myindex5.get(key));
		        		       		        
		    	Set<String> ip1 = nodeid_ip_myindex5.keySet();
	            for(String key : ip1)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex5.get(key));
	  		
	            myIndexTable();
	            IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
				break;
				
			case 2:
				System.out.println("Two Imm Pred have left");
				
				Set<String> keys2 = myindex5.keySet();
	            for(String key : keys2)
	            	myindex.put(key, myindex5.get(key));
		        
		    	Set<String> keys3 = myindex4.keySet();
	            for(String key : keys3)
	            	myindex.put(key, myindex4.get(key));
	            
	            Set<String> ip2 = nodeid_ip_myindex5.keySet();
	            for(String key : ip2)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex5.get(key));
		        
		        Set<String> ip3 = nodeid_ip_myindex4.keySet();
	            for(String key : ip3)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex4.get(key));
	    		
	            myIndexTable();
	            IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
				break;
				
			case 3:
				System.out.println("Three Imm Pred have left");
				
				Set<String> keys4 = myindex5.keySet();
	            for(String key : keys4)
	            	myindex.put(key, myindex5.get(key));
		        
		    	Set<String> keys5 = myindex4.keySet();
	            for(String key : keys5)
	            	myindex.put(key, myindex4.get(key));
		    			
	            Set<String> keys6 = myindex3.keySet();
	            for(String key : keys6)
	            	myindex.put(key, myindex3.get(key));
	            
	            Set<String> ip4 = nodeid_ip_myindex5.keySet();
	            for(String key : ip4)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex5.get(key));
		        
		        Set<String> ip5 = nodeid_ip_myindex4.keySet();
	            for(String key : ip5)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex4.get(key));
	            
		        Set<String> ip6 = nodeid_ip_myindex3.keySet();
	            for(String key : ip6)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex3.get(key));	
	  
	            myIndexTable();
	            IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
				break;
				
			case 4:
				System.out.println("Four Imm Pred have left");
				
				Set<String> keys7 = myindex5.keySet();
	            for(String key : keys7)
	            	myindex.put(key, myindex5.get(key));
		        
		    	Set<String> keys8 = myindex4.keySet();
	            for(String key : keys8)
	            	myindex.put(key, myindex4.get(key));
		    			
	            Set<String> keys9 = myindex3.keySet();
	            for(String key : keys9)
	            	myindex.put(key, myindex3.get(key));
	            
	            Set<String> keys10 = myindex2.keySet();
	            for(String key : keys10)
		        	myindex.put(key, myindex2.get(key));
	            
	            Set<String> ip7 = nodeid_ip_myindex5.keySet();
	            for(String key : ip7)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex5.get(key));
		        
		        Set<String> ip8 = nodeid_ip_myindex4.keySet();
	            for(String key : ip8)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex4.get(key));
	            
		        Set<String> ip9 = nodeid_ip_myindex3.keySet();
	            for(String key : ip9)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex3.get(key));
		        
		        Set<String> ip10 = nodeid_ip_myindex2.keySet();
		        for(String key : ip10)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex2.get(key));
		
		        myIndexTable();	         
		       	IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
				break;	
				
			case 5:
				System.out.println("All Five Imm Pred have left");
				
				Set<String> keys11 = myindex5.keySet();
	            for(String key : keys11)
		        	myindex.put(key, myindex5.get(key));
		        
		    	Set<String> keys12 = myindex4.keySet();
	            for(String key : keys12)
		        	myindex.put(key, myindex4.get(key));
		    			
	            Set<String> keys13 = myindex3.keySet();
	            for(String key : keys13)
		        	myindex.put(key, myindex3.get(key));
	            
	            Set<String> keys14 = myindex2.keySet();
	            for(String key : keys14)
		        	myindex.put(key, myindex2.get(key));
				
	            Set<String> keys15 = myindex1.keySet();
	            for(String key : keys15)
		        	myindex.put(key, myindex1.get(key));
	            
	            Set<String> ip11 = nodeid_ip_myindex5.keySet();
	            for(String key : ip11)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex5.get(key));
		        
		        Set<String> ip12 = nodeid_ip_myindex4.keySet();
	            for(String key : ip12)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex4.get(key));
	            
		        Set<String> ip13 = nodeid_ip_myindex3.keySet();
	            for(String key : ip13)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex3.get(key));
		        
		        Set<String> ip14 = nodeid_ip_myindex2.keySet();
		        for(String key : ip14)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex2.get(key));
		        
		        Set<String> ip15 = nodeid_ip_myindex1.keySet();
		        for(String key : ip15)
		        	NodeId_ip_of_myindex.put(key, nodeid_ip_myindex1.get(key));
		        
		        myIndexTable();
		        IndexManagementUtilityMethods.TransmitMyIndexXmlFileToSuccessors();
	            break;
		}		
        
    
   }
      
   public static String get_pred_ip(String nodeId) {
    
        String ip = "null";
        
        if(CommunicationManager.myIpTable.containsKey(nodeId))
        	ip = CommunicationManager.myIpTable.get(nodeId);
        
        else 
        	ip = "null";
        	
        SysOutCtrl.SysoutSet("ip address from myIpTable"+ip,2);
        return ip;
	}

	public static void TransmitMyIndexXmlFileToSuccessors()
    {   // create index table xml file to be forwarded to five successors

        String selfNodeId = OverlayManagement.myNodeId;
        String selfIp = getMyIp();
        String self_port_no = "2222";
        String toNodeId = "CopyOfIndexTable";
        
        List<String> Successors = new ArrayList<String>();

        for (int i = 0; i < 5; i++)
        {
        	SysOutCtrl.SysoutSet("my successor "+i+" "+PredecessorSuccessor.mySuccessors[i]);
        	String to_hash_id = PredecessorSuccessor.mySuccessors[i];
        	
        	if(!Successors.contains(to_hash_id) && !to_hash_id.equals(OverlayManagement.myNodeId) )
        	{	
        		Successors.add(to_hash_id);
        		SysOutCtrl.SysoutSet("successor[i]"+to_hash_id);
        	
        		File myIndexInXml = null;
        		if(!to_hash_id.isEmpty())
        		{
        			try {
        				myIndexInXml = convert_hashmap_of_indexTable_transmitted_toxml(myindex,  "0010",to_hash_id, selfNodeId, toNodeId, selfIp, self_port_no, "NoAction");
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
        			//     CommunicationManager.TransmittingBuffer.add(myIndexInXml);
        			com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(myIndexInXml);
        		}	
        	}	
       }
    }

    // converting index table to xml file
  public static File convert_hashmap_of_indexTable_transmitted_toxml(Map<String, String> myindex1, String tag,String to_hash_id, String selfNodeId,
            String toNodeId, String selfIp, String self_port_no, String action)
    throws FileNotFoundException, TransformerException, ParserConfigurationException
    {
        SysOutCtrl.SysoutSet("You are in convert HashMap to XML method " + myindex1,2);
        SysOutCtrl.SysoutSet("Index table " + myindex1,3);
        
        Collection<String> hash_id_extracted = myindex1.keySet(); /// code to extract hash_id from array by first
        /// converting it into collection then to an array
        Object[] hashid_array = hash_id_extracted.toArray();

        Collection<String> node_id_extracted = myindex1.values();/// code to extract node_id from array by first
        /// converting it into collection then to an array
        Object[] nodeid_array = node_id_extracted.toArray();
        
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("XML");
        doc.appendChild(rootele);
        Element tagidele = doc.createElement("Query_");


        String tagvalue = tag;
        SysOutCtrl.SysoutSet("Tagvalue for indexingQuery: " + tagvalue,2); // prints out the value at the randomly selected
        // index

        ((Element) tagidele).setAttribute("tag", tagvalue);
        rootele.appendChild(tagidele);
        int i = 0;

        for (int j = 0; j < hashid_array.length; j++)
        {
            Element codeele = doc.createElement("indexentries");
            tagidele.appendChild(codeele);
            ((Element) codeele).setAttribute("record_no", Integer.toString(i)); // here using i as tagvalue
            i++;

            Element hashidele = doc.createElement("hash_id");
            hashidele.appendChild(doc.createTextNode((String) hashid_array[j]));
            codeele.appendChild(hashidele);

            Element nodeidele = doc.createElement("node_id");
            nodeidele.appendChild(doc.createTextNode((String) nodeid_array[j]));
            codeele.appendChild(nodeidele);
            
            String ip_of_email_hash_nodeid;
            
            if(NodeId_ip_of_myindex.containsKey(nodeid_array[j]))
            {
            	ip_of_email_hash_nodeid = NodeId_ip_of_myindex.get(nodeid_array[j]);
        		
        		Element ipele_emailhash_nodeid = doc.createElement("self_port_no");
        		ipele_emailhash_nodeid.appendChild(doc.createTextNode((String) ip_of_email_hash_nodeid));
                codeele.appendChild(ipele_emailhash_nodeid);
                
                if(action == "DeleteNodeIdFromIndexIpTable")
                	NodeId_ip_of_myindex.remove(nodeid_array[j], ip_of_email_hash_nodeid);
            }
            
            String encr_cert;
            
            if(EmailHashId_certificates.containsKey(hashid_array[j]))
            {
            	encr_cert = EmailHashId_certificates.get(hashid_array[j]);
        		
        		Element encr_nodeid = doc.createElement("inter_ip");
        		encr_nodeid.appendChild(doc.createTextNode((String) encr_cert));
                codeele.appendChild(encr_nodeid);
                
                if(action == "DeleteNodeIdFromIndexIpTable")
                	EmailHashId_certificates.remove(hashid_array[j], encr_cert);
            }
            
        }
        
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element ipele_emailhash_nodeid = doc.createElement("self_port_no");
        Element encr_nodeid  = doc.createElement("inter_ip");

        Text t1 = doc.createTextNode(to_hash_id);
        Text t2 = doc.createTextNode(toNodeId);
        Text t3 = doc.createTextNode(selfNodeId);
        Text t4 = doc.createTextNode(selfIp);
        Text t5 = doc.createTextNode(self_port_no);
        Text t6 = doc.createTextNode("inter_nodeid_ip");
   
        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        ipele_emailhash_nodeid.appendChild(t5);
        encr_nodeid.appendChild(t6);

        tagidele.appendChild(hashidele);
        tagidele.appendChild(tonodeidele);
        tagidele.appendChild(selfnodeidele);
        tagidele.appendChild(ipele);
        tagidele.appendChild(ipele_emailhash_nodeid);
        tagidele.appendChild(encr_nodeid);
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new FileOutputStream("XML.xml"));
        transformer.transform(source, result);
        SysOutCtrl.SysoutSet("HashMap converted to xml file");

        return (new File("XML.xml"));
    }

    public static File convert_hashmap_of_index_toxml(Map<String, String> myindex1, String tag,String to_hash_id, String selfNodeId,
            String toNodeId, String selfIp, String self_port_no)
    throws FileNotFoundException, TransformerException, ParserConfigurationException
    {
        SysOutCtrl.SysoutSet("You are in convert HashMap to XML method " + myindex1,2);
        SysOutCtrl.SysoutSet("Index table " + myindex1,3);
        Collection<String> hash_id_extracted = myindex1.keySet(); /// code to extract hash_id from array by first
        /// converting it into collection then to an array
        Object[] hashid_array = hash_id_extracted.toArray();

        Collection<String> node_id_extracted = myindex1.values();/// code to extract node_id from array by first
        /// converting it into collection then to an array
        Object[] nodeid_array = node_id_extracted.toArray();



        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = f.newDocumentBuilder();
        Document doc = db.newDocument();

        Element rootele = doc.createElement("XML");
        doc.appendChild(rootele);
        Element tagidele = doc.createElement("Query_");


        String tagvalue = tag;
        SysOutCtrl.SysoutSet("Tagvalue for indexingQuery: " + tagvalue,2); // prints out the value at the randomly selected
        // index

        ((Element) tagidele).setAttribute("tag", tagvalue);
        rootele.appendChild(tagidele);
        int i = 0;

        for (int j = 0; j < hashid_array.length; j++)
        {
            Element codeele = doc.createElement("indexentries");
            tagidele.appendChild(codeele);
            ((Element) codeele).setAttribute("record_no", Integer.toString(i)); // here using i as tagvalue
            i++;

            Element hashidele = doc.createElement("hash_id");
            hashidele.appendChild(doc.createTextNode((String) hashid_array[j]));
            codeele.appendChild(hashidele);

            Element nodeidele = doc.createElement("node_id");
            nodeidele.appendChild(doc.createTextNode((String) nodeid_array[j]));
            codeele.appendChild(nodeidele);

        }

        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        Text t1 = doc.createTextNode(to_hash_id);
        Text t2 = doc.createTextNode(toNodeId);
        Text t3 = doc.createTextNode(selfNodeId);
        Text t4 = doc.createTextNode(selfIp);

        Text t5 = doc.createTextNode(self_port_no);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);

        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        tagidele.appendChild(hashidele);
        tagidele.appendChild(tonodeidele);
        tagidele.appendChild(selfnodeidele);
        tagidele.appendChild(ipele);
        tagidele.appendChild(portele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(new FileOutputStream("XML.xml"));
        transformer.transform(source, result);
        SysOutCtrl.SysoutSet("HashMap converted to xml file");

        return (new File("XML.xml"));
    }
    
    public static Map<String, String> convertXmlToIndexTable(File inFile)
    {   // converting xml to index table ie hashmap

        Map<String, String> tempIndexTable = new LinkedHashMap<String, String>();
        // this method should convert the incoming xml file to myindex9hashmap)

        SysOutCtrl.SysoutSet("you are in convertXmlToIndexTable method",2);

        try {


            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(inFile);


            NodeList nlist = doc.getElementsByTagName("indexentries");
            System.out.println("nlist lenght"+nlist.getLength());

            for (int i = 0; i < nlist.getLength(); i++)
            {
                System.out.println("for loop i "+i);
                Node nNode = nlist.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode; // System.out.println(eElement.getAttribute("id"));

                    String record_no = eElement.getAttribute("record_no");

                    NodeList nodeList = eElement.getChildNodes();

                    for (int x = 0; x < 2; x++) // to get tag value from each xml file.
                    {
                        Node n = nodeList.item(x);
                        if (n.getNodeType() == Node.ELEMENT_NODE)
                        {
                            Element name = (Element) n;
                            SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
                            String key = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
                            String value = eElement.getElementsByTagName("node_id").item(0).getTextContent();
                            tempIndexTable.put(key, value);
                            SysOutCtrl.SysoutSet("record_no: " + record_no + "   hashId: " + key + "  nodeId: " + value,2);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempIndexTable;
    }

    public static Map<String, String> convert_xml_to_Node_ip_table(File inFile)
	{   // converting xml to index table ie hashmap
	
	    Map<String, String> tempIndexTable = new LinkedHashMap<String, String>();
	    // this method should convert the incoming xml file to myindex9hashmap)
	
	    SysOutCtrl.SysoutSet("you are in convertXmlToIndexTable method",2);
	
	    try {
	
	
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(inFile);
	
	
	        NodeList nlist = doc.getElementsByTagName("indexentries");
	        System.out.println("nlist lenght"+nlist.getLength());
	
	        for (int i = 0; i < nlist.getLength(); i++)
	        {
	            System.out.println("for loop i "+i);
	            Node nNode = nlist.item(i);
	
	            if (nNode.getNodeType() == Node.ELEMENT_NODE)
	            {
	                Element eElement = (Element) nNode; // System.out.println(eElement.getAttribute("id"));
	
	                String record_no = eElement.getAttribute("record_no");
	
	                NodeList nodeList = eElement.getChildNodes();
	
	                for (int x = 0; x < 2; x++) // to get tag value from each xml file.
	                {
	                    Node n = nodeList.item(x);
	                    if (n.getNodeType() == Node.ELEMENT_NODE)
	                    {
	                        Element name = (Element) n;
	                        SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
	                        String key = eElement.getElementsByTagName("node_id").item(0).getTextContent();
	                        String value = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();
	                        tempIndexTable.put(key, value);
	                        SysOutCtrl.SysoutSet("record_no: " + record_no + "   NodeId: " + key + "  Ip: " + value,2);
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return tempIndexTable;
	}

	public static Map<String, String> convert_xml_to_Node_encrCert_table(File inFile) 
	{
		Map<String, String> node_cert = new LinkedHashMap<String, String>();
	    // this method should convert the incoming xml file to myindex9hashmap)
			
	    SysOutCtrl.SysoutSet("you are in convertXmlToIndexTable method",2);
	
	    try {
	
	
	        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	        DocumentBuilder db = dbf.newDocumentBuilder();
	        Document doc = db.parse(inFile);
	
	
	        NodeList nlist = doc.getElementsByTagName("indexentries");
	        System.out.println("nlist lenght"+nlist.getLength());
	
	        for (int i = 0; i < nlist.getLength(); i++)
	        {
	            System.out.println("for loop i "+i);
	            Node nNode = nlist.item(i);
	
	            if (nNode.getNodeType() == Node.ELEMENT_NODE)
	            {
	                Element eElement = (Element) nNode; // System.out.println(eElement.getAttribute("id"));
	
	                String record_no = eElement.getAttribute("record_no");
	
	                NodeList nodeList = eElement.getChildNodes();
	
	                for (int x = 0; x < 2; x++) // to get tag value from each xml file.
	                {
	                    Node n = nodeList.item(x);
	                    if (n.getNodeType() == Node.ELEMENT_NODE)
	                    {
	                        Element name = (Element) n;
	                        SysOutCtrl.SysoutSet("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
	                        String key = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
	                        String value = eElement.getElementsByTagName("inter_ip").item(0).getTextContent();
	                        node_cert.put(key, value);
	                        SysOutCtrl.SysoutSet("record_no: " + record_no + "   Hash_id: " + key + "  cert: " + value,2);
	                    }
	                }
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	   
	    return node_cert;
		
	}

	public static File createXmlSearchQuery(String key, String inter_nodeid_ip, String selfNodeId, String selfIp)
	{   // creating serach query xml file from other node's index table
	
	    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db = null;
	    try {
	        db = f.newDocumentBuilder();
	    } catch (ParserConfigurationException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	    Document doc = db.newDocument();
	
	    Element rootele = doc.createElement("Query");
	    Element codeele = doc.createElement("Query_");
	    Element hashidele = doc.createElement("to_hash_id");
	    Element tonodeidele = doc.createElement("to_node_id");
	    Element selfnodeidele = doc.createElement("self_node_id");
	    Element ipele = doc.createElement("self_ip_address");
	    Element portele = doc.createElement("self_port_no");
	    Element intermediate_ipele = doc.createElement("inter_ip");
	
	    /// randomly chooses the string index from the string array
	
	    String tagvalue = "0003"; // use random index to store corresponding string value in another string
	
	    SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index
	
	    ((Element) codeele).setAttribute("tag", tagvalue);
	
	    Text t1 = doc.createTextNode(key);
	    Text t2 = doc.createTextNode("SearchQuery");
	   	
	    Text t3 = doc.createTextNode(selfNodeId);
	    Text t4 = doc.createTextNode(selfIp);
	    String selfPortNo = "2222";
	    Text t5 = doc.createTextNode(selfPortNo);
	    Text t6 = doc.createTextNode(inter_nodeid_ip);
	
	    hashidele.appendChild(t1);
	    tonodeidele.appendChild(t2);
	    selfnodeidele.appendChild(t3);
	    ipele.appendChild(t4);
	    portele.appendChild(t5);
	    intermediate_ipele.appendChild(t6);
	
	    codeele.appendChild(hashidele);
	    codeele.appendChild(tonodeidele);
	    codeele.appendChild(selfnodeidele);
	    codeele.appendChild(ipele);
	    codeele.appendChild(portele);
	    codeele.appendChild(intermediate_ipele);
	
	    rootele.appendChild(codeele);
	    doc.appendChild(rootele);
	
	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    Transformer transformer = null;
	    try {
	        transformer = transformerFactory.newTransformer();
	    } catch (TransformerConfigurationException e) {
	        e.printStackTrace();
	    }
	    DOMSource source = new DOMSource(doc);
	    StreamResult result = null;
	    try {
	        result = new StreamResult(new FileOutputStream("SearchQuery.xml"));
	    } catch (FileNotFoundException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    try {
	        transformer.transform(source, result);
	    } catch (TransformerException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	
	
	    File file1 = new File("SearchQuery.xml");
	
	    SysOutCtrl.SysoutSet("SearchQuery.xml file Generated.",2);
	
	    return file1;
	
	}

	public static Map<String, String> generateIndexingForNewlyJoinedNode(String[] info_from_xml)
    {   // generating index table form immdt newly joined predecessor

        SysOutCtrl.SysoutSet("you are in generate Indexing for newly joined node ",2);

        // now the newly joined node have its immediate successors and predecessors
        // now a series of activities will happen
        // (a) this node will check its index table to find out those key value pairs
        // for which the newly joined node is responsible
        // --- these will be those key value pairs for which the newly joined node is
        // the root node and
        // --- those key value pairs which lies between newly joined node and its
        // immediate predecessor
        // (b) these key value pairs need to be identified, placed in new index table
        // and to be handed over to newly joined node
        // (c) at same time these identified key value pairs need to be purged from the
        // existing index table.
        // (d) the index table after purging needs to be updated at successors
        // (e) the newly joined node has to send its index table to its 5 successors
        // (f) the newly joined node has to take over the responsibility as successor of
        // its immediate predecessor thus it has to take its data and maintain it

        // System.out.println("--------------------- Generate Indexing Method Starts
        // ------------------------------ ");

        // (a) this node will check its index table to find out those key value pairs
        // for which the newly joined node is responsible
        // --- these will be those key value pairs for which the newly joined node is
        // the root node and

        String newlyJoinedNodeId = info_from_xml[3];
        String newlyJoinedHashId= info_from_xml[2];// here hashid of newly joined node comes
        SysOutCtrl.SysoutSet("getIndexing request received from : " + newlyJoinedNodeId,2);

        // here when a node joins an overlay it has nothing, therefore it will first ask
        // from successor his predecessor list
        // String[] mypredecessors = olay.getmypredecessor();
        // now it will decide its immediate predecessor

        String my_immediate_predecessor = PredecessorSuccessor.myPredecessors[4];

        // and it will make the successor its immediate successor

        SysOutCtrl.SysoutSet("My predecessor was: " + my_immediate_predecessor,2);
        SysOutCtrl.SysoutSet("myindex.containsKey(newlyJoinedNodeId)"+myindex.containsKey(newlyJoinedNodeId),2);
        Map<String, String> fresh_index = new LinkedHashMap<String, String>();
        SysOutCtrl.SysoutSet("my index table size"+myindex.size());

        
        if (myindex.containsKey(newlyJoinedNodeId)) // Returns true if this map contains a mapping for the specified key.
        {   // this is for a case when the newly joined node is root node for those entries
            // which were in its successor index table due to its absence

            SysOutCtrl.SysoutSet("fresh index table before entry is " + fresh_index,2);


            fresh_index.put(newlyJoinedNodeId, myindex.get(newlyJoinedNodeId)); // second argument will extract value iro this key
            // i.e. self_node_id

            SysOutCtrl.SysoutSet(" index table for newly joined predecessor/node " + fresh_index,2);


            // (c) at same time these identified key value pairs need to be purged from the
            // existing index table.

            SysOutCtrl.SysoutSet("Previous my index table was " + myindex,2);
            myindex.remove(newlyJoinedNodeId); // remove the identified entries from original index table
            myIndexTable();
            System.out.println("My index remove 1");
            SysOutCtrl.SysoutSet("Now my index table is " + myindex,2);

            Collection<String> key_extracted = myindex.keySet(); /// code to extract hash_id from array by first
            /// converting it into collection then to an array
            Object[] key_array = key_extracted.toArray();

            for (int i = 0; i < key_array.length; i++)
            {
                // System.out.println(key_array[i]);
                // now we will do the comparison
                SysOutCtrl.SysoutSet(" "+((String) key_array[i]).compareToIgnoreCase(newlyJoinedNodeId));
               	
                if(CommunicationUtilityMethods.responsibleNode(OverlayManagement.myNodeId,newlyJoinedNodeId,(String) key_array[i]))
                {
                    fresh_index.put((String) key_array[i], myindex.get(key_array[i])); // second argument will extract
                    // value iro this key i.e.
                    // self_node_id

                    // (c) at same time these identified key value pairs need to be purged from the
                    // existing index table.
                    System.out.println("my pred : "+my_immediate_predecessor);
                    myindex.remove(key_array[i]); // remove the entries from original index table
                    myIndexTable();
                    System.out.println("My index remove 2");
                }
                
            }
        }
        else
        {
            SysOutCtrl.SysoutSet("new node hash id entry does not exist in my table",2);
            SysOutCtrl.SysoutSet("extracting entries from my index table for his responsiblity",2);

            // (b) these key value pairs need to be identified, placed in new index table
            // and to be handed over to newly joined node
            // --- those key value pairs which lies between newly joined node and its
            // immediate predecessor

            // now is the turn for those entries in the original index table which lies
            // between newly joined node and its predecessor (earlier this was predecessor
            // to its successor)
            // -- for this we need to identify those hash (key) which are lesser than newly
            // joined node but greater than predecessor
            // -- for this we will extract keyset and convert it to array

            Collection<String> key_extracted = myindex.keySet(); /// code to extract hash_id from array by first
            /// converting it into collection then to an array
            Object[] key_array = key_extracted.toArray();

            for (int i = 0; i < key_array.length; i++)
            {
                // System.out.println(key_array[i]);
                // now we will do the comparison
                SysOutCtrl.SysoutSet(" "+((String) key_array[i]).compareToIgnoreCase(newlyJoinedNodeId));
                if(CommunicationUtilityMethods.responsibleNode(OverlayManagement.myNodeId,newlyJoinedNodeId,(String) key_array[i]))
                {
                    fresh_index.put((String) key_array[i], myindex.get(key_array[i])); // second argument will extract
                    // value iro this key i.e.
                    // self_node_id

                    // (c) at same time these identified key value pairs need to be purged from the
                    // existing index table.
                    System.out.println("index condition returned : "+CommunicationUtilityMethods.responsibleNode(OverlayManagement.myNodeId,newlyJoinedNodeId,(String) key_array[i])+", for "+newlyJoinedNodeId+" and iam giving him index entry : "+(String) key_array[i]);
                    System.out.println("my pred : "+my_immediate_predecessor);
                    myindex.remove(key_array[i]); // remove the entries from original index table
                    myIndexTable();
                    System.out.println("My index remove 2");
                }
            }
            SysOutCtrl.SysoutSet("generated index table for newly joined node "+newlyJoinedNodeId+"is" + fresh_index,2);
            SysOutCtrl.SysoutSet("my index table size"+myindex.size());
            SysOutCtrl.SysoutSet("My index table changed to " + myindex);

        }

        return fresh_index;

        // also it will tell its successor to update its predecessor list by purging the
        // first entry i.e. predecessor[0] = predecessor[1] and so on.
        // by calling update predecessor method.
        // also it will tell its predecessor to update its successor list
        // SysOutCtrl.SysoutSet("--------------------- Generate Indexing Method Close
        // ------------------------------ ");

    }

    //generating xml search query reply
    public static void createXmlSearchQueryReply(String caller_Ip,String querried_ip, String caller_node_id, String selfNodeId, String selfIp,
            String querried_emailid_hash)
    {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = null;
        try {
            documentbuilder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = documentbuilder.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0012"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(caller_node_id);

        // next hop will be given by routing module
        Text t2 = doc.createTextNode(querried_ip);

        // String selfNodeId1="aaaaa";

        Text t3 = doc.createTextNode(selfNodeId);

        Text t4 = doc.createTextNode(selfIp);

        Text t5 = doc.createTextNode(querried_emailid_hash);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);
        // System.out.println("1");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();

            // System.out.println("2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = null;

            try {
                result = new StreamResult(new FileOutputStream("SearchQueryReply.xml"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println("3");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            // System.out.println("4");

            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            // System.out.println("6");
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("SearchQueryReply.xml file Generated ",2);
        OverlayManagementUtilityMethods.sendFileDirect(caller_Ip, new File("SearchQueryReply.xml"));
 //       CommunicationManager.TransmittingBuffer.add( new File("SearchQueryReply.xml"));
 //       com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("SearchQueryReply.xml"));
        SysOutCtrl.SysoutSet("search query reply xml created and added to tx buffer",2);
        // return (new File("SearchQueryReply.xml"));

    }
    
    

    public static File createXmlAddIndexQuery(String key, String value, String encr_cert) {// creating add index xml query

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");
        Element intermediate_ipele = doc.createElement("inter_ip");

        /// randomly chooses the string index from the string array

        String tagvalue = "0002"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode(value);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222";
        Text t5 = doc.createTextNode(selfPortNo);
        Text t6 = doc.createTextNode(encr_cert);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);
        intermediate_ipele.appendChild(t6);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);
        codeele.appendChild(intermediate_ipele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileOutputStream("NewAddIndexQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet(" NewAddIndexQuery.xml file Generated.",2);
        System.out.println("index querry");

        return (new File("NewAddIndexQuery.xml"));

    }

    public static void informNodeIdToCaller(String caller, String value, String key, boolean flag, String caller_ip) {// at responsible node informing action to caller node
        // This method will inform the caller node the nodeId of the to_hash_id field in
        // his search query.
        // Now here caller's nodeId will be put as to_hash_id to route the query till
        // him and to_Node_id field
        // will have the desired node id
        // String nodeId=value;

        String querried_ip="yyyy";
        if(flag == true)
        {
        
        	if(value.equals(OverlayManagement.myNodeId))
        	{
        		querried_ip= getMyIp();
        	}
        
        	else
        	{ 
        		querried_ip = IndexManagement.NodeId_ip_of_myindex.get(value);// here i am putting directly ip address of the called
        	}
        	SysOutCtrl.SysoutSet("ip"+querried_ip,2 );													// node.
        	// String nodeId = "IP_NOT_FOUND_AT RESP_NODE"; new
        	//	String str= null;
        	//    Optional<String> str2 = Optional.ofNullable(ip); new

        	//    if (str2.isPresent()) new
        	//	  {new
        	//         nodeId = ip; new
        	//       }new
       
        	//       SysOutCtrl.SysoutSet("destination ip not avaliable with comm mgr table", 3);
        	
        	String caller_node_id = caller;
        	// String selfNodeId=OverlayManagement.myNodeId;
        	
        	String selfNodeId = OverlayManagement.myNodeId;// putting searched hash in self node id. this needs to be put in separate filed
        	// later on.
        	String selfIp = getMyIp();
        	String querried_emailid_hash= key;
        	SysOutCtrl.SysoutSet("self ip add " + selfIp,2);
        	String caller_Ip= caller_ip;
        	createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
        	// CommunicationManager.TransmittingBuffer.add(searchQueryReply);
        }
        
        else
        {
        	querried_ip = value;
        	String caller_node_id = caller;
        	String selfNodeId = OverlayManagement.myNodeId;
        	String selfIp = getMyIp();
        	String querried_emailid_hash= key;
        	SysOutCtrl.SysoutSet("self ip add " + selfIp,2);
        	String caller_Ip=caller_ip;
        	createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
        }	
    }
    
    public static void createXmlForCacheEntry(String caller_Ip,String cache_ip, String caller_node_id, String selfNodeId, String selfIp,
            String cache_emailid_hash, String cache_nodeId_for_emailIdHash)
    {
    	String cache_ip_emailHash = cache_emailid_hash+cache_ip;
    	
    	DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = null;
        try {
            documentbuilder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Document doc = documentbuilder.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0333"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(caller_node_id);

        // next hop will be given by routing module
        Text t2 = doc.createTextNode(cache_ip_emailHash);

        // String selfNodeId1="aaaaa";

        Text t3 = doc.createTextNode(selfNodeId);

        Text t4 = doc.createTextNode(selfIp);

        Text t5 = doc.createTextNode(cache_nodeId_for_emailIdHash);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);
        // System.out.println("1");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();

            // System.out.println("2");
            DOMSource source = new DOMSource(doc);
            StreamResult result = null;

            try {
                result = new StreamResult(new FileOutputStream("CacheEntry.xml"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // System.out.println("3");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            // System.out.println("4");

            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            // System.out.println("6");
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("CacheEntry.xml file Generated ",2);
        OverlayManagementUtilityMethods.sendFileDirect(caller_Ip, new File("CacheEntry.xml"));
  //      CommunicationManager.TransmittingBuffer.add( new File("CacheEntry.xml"));
 //       com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("CacheEntry.xml"));
        SysOutCtrl.SysoutSet("search query reply xml created and added to tx buffer",2);
    }
    
    public static String getMyIp() {// getting my ip address		// TODO Auto-generated method stub
        while(!UpdateIP.Connected)
        {
            SysOutCtrl.SysoutSet("AQUIRING IP in index mgmt");
        }
        String ip = PresentIP.MyPresentIP();


        return ip;
    }

    public static void removeIndexing(String to_hash_id)
    {// remove index procedure..
 
                myindex.remove(to_hash_id);
                myIndexTable();
                System.out.println("My index remove 4");
                SysOutCtrl.SysoutSet("index entry removed for hash id "+to_hash_id);

    }

    public static void removeIndexRequest(String key, String value) {// creating remove index query for responsible node
        File file = IndexManagementUtilityMethods.createXmlRemoveIndexQuery(key, value);
      //  CommunicationManager.TransmittingBuffer.add(file);
        com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(file);
    }

    private static File createXmlRemoveIndexQuery(String key, String value) {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "1111"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode(value);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222";
        Text t5 = doc.createTextNode(selfPortNo);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileOutputStream("RemoveIndexQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("RemoveIndexQuery.xml file Generated.",2);

        return (new File("RemoveIndexQuery.xml"));

    }

    private static String destIp = null;
    public static String searchEmailId(String email) throws InterruptedException
    {   // search email request from auth module voip
    	destIp = null;
        SysOutCtrl.SysoutSet("seached email "+email,2);
        String sha1 = SHA1.getSha1(email);
        SysOutCtrl.SysoutSet("hash of email "+email+" is: "+sha1,2);
            	
        if(cached_index.containsKey(sha1))
        {
           	String destNodeId = cached_index.get(sha1);
			SysOutCtrl.SysoutSet("destination node id is."+destNodeId,2);
			destIp = cached_NodeId_ip_index.get(destNodeId);

			SysOutCtrl.SysoutSet("dest ip from my ip table "+destIp,1);
			
	/*		JFrame frame1 = new JFrame("Message");
    		//show a joptionpane dialog using showMessageDialog
    		JOptionPane.showMessageDialog(frame1, "Searched IP address is "+destIp);*/
			
		}
        else if(myindex.containsKey(sha1))
        {
        	String destNodeId = myindex.get(sha1);
			SysOutCtrl.SysoutSet("destination node id is."+destNodeId,2);
			destIp = NodeId_ip_of_myindex.get(destNodeId);

			SysOutCtrl.SysoutSet("dest ip from my ip table "+destIp,1);
			
    	/*	JFrame frame1 = new JFrame("Message");
    		//show a joptionpane dialog using showMessageDialog
    		JOptionPane.showMessageDialog(frame1, "Searched IP address is "+destIp);*/
        }
        else
        {
        	if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,sha1))
        	{
        		SysOutCtrl.SysoutSet("I am responsible node for this email.",2);
        		SysOutCtrl.SysoutSet("my index table"+IndexManagement.myindex,2);
        		if(myindex.containsKey(sha1))
        		{

        			SysOutCtrl.SysoutSet("Indexting found in my index.",2);
        			//    String destNodeId=Search_in_Index(sha1);
        			String destNodeId = myindex.get(sha1);
        			SysOutCtrl.SysoutSet("destination node id is."+destNodeId,2);
        			destIp=IndexManagement.NodeId_ip_of_myindex.get(destNodeId);

        			SysOutCtrl.SysoutSet("dest ip from my ip table "+destIp,1);

        			// to display a user with a messege box
        		}
        		else
        		{
        			destIp="SORRY! Called Person Is Not Active At The Moment.";
        			SysOutCtrl.SysoutSet("Indexting does not found in my index.",2);
        			
        			JFrame frame1 = new JFrame("Message");
            		//show a joptionpane dialog using showMessageDialog
            		JOptionPane.showMessageDialog(frame1, " "+destIp);
        		}

       /* 		JFrame frame1 = new JFrame("Message");
        		//show a joptionpane dialog using showMessageDialog
        		JOptionPane.showMessageDialog(frame1, " "+destIp);*/

        	}
        
        	else
        	{
        		SysOutCtrl.SysoutSet("I am not a responsible node for this email.",2);
        		SysOutCtrl.SysoutSet("creating search query.",2); 
        		emailSha1.put(sha1, email);
        		searchReply.put(sha1,"null");
        		
        		
        		System.out.println("find resposible node");
        		String inter_ip = "null";
        		String my_node_id = OverlayManagement.myNodeId;
        		String my_ip = PresentIP.MyPresentIP();
        		File searchQuery=createXmlSearchQuery(sha1, inter_ip, my_node_id, my_ip);
            
        		String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(sha1);
        		String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
         
        		System.out.println("Node Id returned by routing mgmt : "+tonodeId);
                   
        		OverlayManagementUtilityMethods.sendFileDirect(toIp,searchQuery);
            
        		//  CommunicationManager.TransmittingBuffer.add(searchQuery);
        	//	    com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(searchQuery);
   

        		Timer timer = new Timer();
        		TimerTask startIM = new TimerTask()
        		{
        			@Override
        			public void run()
        			{
        				//check_status(sha1);
                	
        				SysOutCtrl.SysoutSet(""+setInterval());

        				SysOutCtrl.SysoutSet("searchReplyReceived"+IndexManagement.searchReplyReceived);

        				if(IndexManagement.searchReplyReceived==false)
        				{
        					SysOutCtrl.SysoutSet("please wait");
        					destIp = "TimedOut";
        					JFrame frame1 = new JFrame("Message");
        					//show a joptionpane dialog using showMessageDialog
        					JOptionPane.showMessageDialog(frame1, "Searched IP Not Found In Given Time ");
        				}
                    
        				else
        				{
        					SysOutCtrl.SysoutSet("searchReplyReceived after  while"+IndexManagement.searchReplyReceived);
        					SysOutCtrl.SysoutSet("after while");
                       
        					IndexManagement.searchReplyReceived=false;
        					destIp=IndexManagement.searchReply.get(sha1);
                       

        					SysOutCtrl.SysoutSet("ip from search query reply "+destIp,1);
        					
        					if(destIp.equals("SORRY! Called Person Is Not Active At The Moment."))
        					{	
        						JFrame frame1 = new JFrame("Message");
        						//show a joptionpane dialog using showMessageDialog
        						JOptionPane.showMessageDialog(frame1, " "+destIp);
        					}	
        				}
        			}
        		};

        		timer.schedule(startIM, 19000);
        		try {
        			Thread.sleep(20000);
        		} catch (InterruptedException e) {
        			e.printStackTrace();
        		}
           
        		timer.cancel();
           
        		System.out.println("Destination IP is : "+destIp);
      
        	}
        }
		return destIp; 
    }

    private static final int setInterval()
    {
        if (interval == 1)
        {
            timer.cancel();
            IndexManagement.searchReplyReceived=true;
        }
        System.out.println("in setInterval");
        System.out.println(interval);
        return --interval;
    }

    public static String getSelfEmailId()
    {   // this method is to directly enter the email id from indexing.
        // create a jframe
        JLabel frame = new JLabel("EmailID");
        // prompt the user to enter their email id as common name.
        //email id is extracted later at server and used to send OTP for verification.
        String commonname = JOptionPane.showInputDialog(frame, "Please enter self Email-Id(for publishing youself at responsible node");
        return commonname;
    }

    public static void storeHashId()
    {

        selfEmailid = emailid.getemaild();//method from authentication will come here


        selfHashId=SHA1.getSha1(selfEmailid);
        String nodeId=OverlayManagement.myNodeId;

        SysOutCtrl.SysoutSet("myIpTable is"+CommunicationManager.myIpTable, 2);


    }

    public static void newNodeCheckingReply(File inFile)
    {

        String[] xmlDetails=ParseXmlFile.ParseXml(inFile);
        String value=Search_in_Index( xmlDetails[1]);
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentbuilder = null;
        try {
            documentbuilder = f.newDocumentBuilder();
        } catch (ParserConfigurationException e) {

            e.printStackTrace();
        }
        Document doc = documentbuilder.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0031"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(xmlDetails[3]);

        // next hop will be given by routing module
        Text t2 = doc.createTextNode("xxxxx");

        Text t3 = doc.createTextNode(OverlayManagement.myNodeId);

        Text t4 = doc.createTextNode(getMyIp());

        Text t5 = doc.createTextNode("2222");

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();


            DOMSource source = new DOMSource(doc);
            StreamResult result = null;

            result = new StreamResult(new FileOutputStream("checkNewNodeReply.xml"));

            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();

        } catch (TransformerException e) {
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("checkNewNodeReply.xml file Generated.",2);
        File checkNewNodeReply=new File("checkNewNodeReply.xml");

        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader(checkNewNodeReply.toString()));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s = "", line = null;
        try {
            while ((line = r.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        CommunicationUtilityMethods.fileSender(xmlDetails[4], s);
        SysOutCtrl.SysoutSet("Reply for the checkNewNode Query sent back to new node",2);
    }

    public static File createXmlUpdate_IP(String key) {

        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = f.newDocumentBuilder();
        } catch (ParserConfigurationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Document doc = db.newDocument();

        Element rootele = doc.createElement("Query");
        Element codeele = doc.createElement("Query_");
        Element hashidele = doc.createElement("to_hash_id");
        Element tonodeidele = doc.createElement("to_node_id");
        Element selfnodeidele = doc.createElement("self_node_id");
        Element ipele = doc.createElement("self_ip_address");
        Element portele = doc.createElement("self_port_no");

        /// randomly chooses the string index from the string array

        String tagvalue = "0020"; // use random index to store corresponding string value in another string

        SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(key);

        Text t2 = doc.createTextNode("Update IP Query");
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = getMyIp();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222";
        Text t5 = doc.createTextNode(selfPortNo);

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);

        rootele.appendChild(codeele);
        doc.appendChild(rootele);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DOMSource source = new DOMSource(doc);
        StreamResult result = null;
        try {
            result = new StreamResult(new FileOutputStream("Update_IPQuery.xml"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SysOutCtrl.SysoutSet("Update IP xml query Generated.",2);

        return (new File("Update_IPQuery.xml"));

    }

    public static void storeIp(String hashId)
    {

        String searched_ip=searchReply.get(hashId);



        String emailId=emailSha1.get(hashId);
        callingTable.put(emailId, searched_ip);

        if(searched_ip=="xxxx")
            SysOutCtrl.SysoutSet("Searched  Ip details not available for eamilid:"+emailId,1 );

        SysOutCtrl.SysoutSet("Searched  emailid and Ip :"+emailId+"  "+searched_ip,1);

    }

    public static void fillRxBufferIM()
    {
        int i;
        for (i = 0; i < 1; i++)
        {

            File fetchNextXml = new File("Query1" + i + ".xml");

            CommunicationManager.RxBufferIM.add(CommunicationManager.RxBufferIM.size(), fetchNextXml); // this statement
            // will append
            // the msg ie
        }
        SysOutCtrl.SysoutSet("RxBufferIM filled with " + i + " query xml's",2);
    }



    public static void fillMyIndexTable()
    {

        myindex.put("A8055D147FA3CEA159468FDDF7147C003CCF5BB2" ,"C633336666666CEA159468FDDF7147C003CCF5BB2"  );
        myindex.put("88055D147FA3CEA159468FDDF7147C003CCF5BB2" ,"C666886666666CEA159468FDDF7147C003CCF5BB2"  );


        SysOutCtrl.SysoutSet("filled myIndex table "+myindex,2);
    }
}
