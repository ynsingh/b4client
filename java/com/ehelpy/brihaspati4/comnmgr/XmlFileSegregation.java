package com.ehelpy.brihaspati4.comnmgr;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.RTUpdate9;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateTabFromQuery;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;

public class XmlFileSegregation extends CommunicationManager
{

	private static Map<String, String> convertXmlToIndexTable(File inFile)
	{
		Map<String, String> tempIndexTable = new LinkedHashMap<String, String>();
		
		// this method should convert the incoming xml file to myindex9hashmap)
		SysOutCtrl.SysoutSet("you are in convertXmlToIndexTable method", 2);
		
		try 
		{

			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inFile);
			
			NodeList nlist = doc.getElementsByTagName("indexentries");
	
			int nlistlength = nlist.getLength();
			SysOutCtrl.SysoutSet("nlist lenght" + nlistlength, 2);
			for (int i = 0; i < nlist.getLength(); i++)

			{
				
				Node nNode = nlist.item(i);

				SysOutCtrl.SysoutSet("current element " + nNode.getNodeName(), 2);
				SysOutCtrl.SysoutSet("nodetype " + nNode.getNodeType(), 2);
				SysOutCtrl.SysoutSet("ELEMENT NODE " + Node.ELEMENT_NODE, 2);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String record_no = eElement.getAttribute("record_no");
					SysOutCtrl.SysoutSet("");
					NodeList nodeList = eElement.getChildNodes();
					for (int x = 0; x < 2; x++) // to get tag value from each xml file.
					{
						
						Node n = nodeList.item(x);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							Element name = (Element) n;
							
							String key = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
							String value = eElement.getElementsByTagName("node_id").item(0).getTextContent();
						
							tempIndexTable.put(key, value);
							SysOutCtrl.SysoutSet("hashId'''':" + key + "nodeId:" + value, 2);
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tempIndexTable;
	}

	public static synchronized void xmlFileSegregator()
	{
		
			File file = ReceivingBuffer.removeFirst();
        
						
			SysOutCtrl.SysoutSet("fetching XML file for segregation :" + file, 2);
			SysOutCtrl.SysoutSet("File Length:" + file.length());
			String[] xmlParsedFields = ParseXmlFile.ParseXml(file);

			String nodeId = xmlParsedFields[3];
			String ipAdd = xmlParsedFields[4];
		
			if(!fromNodeIdList.contains(nodeId)) 
			{
				fromNodeIdList.add(nodeId);// for RT to check and remove and update
			}
		
			if(!RTUpdate9.Routing_Table.containsKey(xmlParsedFields[3]))
				UpdateTabFromQuery.NextEntry(xmlParsedFields[3], xmlParsedFields[4]);
			
			if(!CommunicationUtilityMethods.myIpTable.containsKey(nodeId))
				update_my_IpTable(nodeId, ipAdd);// ip table for getting ip details
			
				
		//	SysOutCtrl.SysoutSet("myIpTable:" + myIpTable, 2);
		//	SysOutCtrl.SysoutSet("fromNodeIdList" + fromNodeIdList);
			SysOutCtrl.SysoutSet("index table status in comn mgr" + IndexManagement.myindex, 2);

			String selfNodeId = OverlayManagement.myNodeId;
			
			boolean responsible_node = false;
			try
			{

				SysOutCtrl.SysoutSet("xmlParsedFields[1].compareToIgnoreCase(selfNodeId)"	+ xmlParsedFields[1].compareToIgnoreCase(selfNodeId));
				SysOutCtrl.SysoutSet(xmlParsedFields[1]+"compareToIgnoreCase"+PredecessorSuccessor.myPredecessors[4]+"  "	+ xmlParsedFields[1]+"compareToIgnoreCase"+PredecessorSuccessor.myPredecessors[4]);
				if (xmlParsedFields[1].equals(selfNodeId))
				{
				
					if (xmlParsedFields[0].equals("0022") || xmlParsedFields[0].equals("0031")	|| xmlParsedFields[0].equals("0032") || xmlParsedFields[0].equals("0205")|| xmlParsedFields[0].equals("0305")|| xmlParsedFields[0].equals("0021")
						|| xmlParsedFields[0].equals("0024") || xmlParsedFields[0].equals("0098"))
					{
						SysOutCtrl.SysoutSet("Tag matched for OM", 2);
						RxBufferOM.add(RxBufferOM.size(), file);
						SysOutCtrl.SysoutSet("FileAded to OM Buffer " + RxBufferOM.getLast(), 2);
						SysOutCtrl.SysoutSet("");
					}

					else if (xmlParsedFields[0].equals("0001"))
					{
						SysOutCtrl.SysoutSet("Tag matched for RT", 2);
						RxBufferRT.add(RxBufferRT.size(), file);
						SysOutCtrl.SysoutSet("FileAded to RT Buffer " + RxBufferRT.getLast(), 2);
				
					}

					else if (xmlParsedFields[0].equals("0002") || xmlParsedFields[0].equals("0010")
							|| xmlParsedFields[0].equals("0003") || xmlParsedFields[0].equals("0012")
							|| xmlParsedFields[0].equals("1111") || xmlParsedFields[0].equals("0004")
							|| xmlParsedFields[0].equals("0019") || xmlParsedFields[0].equals("0020")
							|| xmlParsedFields[0].equals("0333"))
					{
						SysOutCtrl.SysoutSet("Tag matched for IM", 2);
						synchronized(CommunicationManager.lock_RxBufferIM)	
						{
							RxBufferIM.add(RxBufferIM.size(), file);
						}
					}
				}
	
				
				else if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,xmlParsedFields[1])) 
				{
				
					responsible_node = true;
					if(xmlParsedFields[0].equals("0205"))
					{
						SysOutCtrl.SysoutSet("searched newNode should have between me and my immediate predecessor", 2);
			
						RxBufferOM.add(RxBufferOM.size(), file);
					}
					else
					{
						SysOutCtrl.SysoutSet("Destination lies between me and my immediate predecessor", 2);
						SysOutCtrl.SysoutSet("I am responsible node. ", 2);
						
						synchronized(CommunicationManager.lock_RxBufferIM)	
						{
							RxBufferIM.add(RxBufferIM.size(), file);
						}
					}
				}
			
				else if(!responsible_node&&xmlParsedFields[0].equals("0003"))
				{
					String[] xmlParsedFields_0003 = ParseXmlFile.ParseXml_0003(file); 
					
					if(IndexManagement.cached_index.containsKey(xmlParsedFields_0003[1]))
					{
						System.out.println("Not Resposible but querried hash is in cache");
						
						String Node_id_of_querried_hash = IndexManagement.cached_index.get(xmlParsedFields_0003[1]);
						String querried_ip = IndexManagement.cached_NodeId_ip_index.get(Node_id_of_querried_hash);
						String caller_node_id= xmlParsedFields_0003[3];
			 			String caller_Ip=xmlParsedFields_0003[4];
						String selfIp = IndexManagementUtilityMethods.getMyIp();
						String querried_emailid_hash= xmlParsedFields_0003[1];
						
						if(xmlParsedFields_0003[6].equals("null"))
						{
							System.out.println("Not Resposible but querried hash is in cache and intermediate ip is null");
							
							IndexManagementUtilityMethods.createXmlForCacheEntry(caller_Ip,querried_ip,caller_node_id,selfNodeId,selfIp,xmlParsedFields_0003[1],Node_id_of_querried_hash);
																					
							IndexManagementUtilityMethods.createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
						}	
						
						else
						{
							System.out.println("Not Resposible but querried hash is in cache and intermediate ip is : "+xmlParsedFields_0003[6]);
							
							IndexManagementUtilityMethods.createXmlForCacheEntry(caller_Ip,querried_ip,caller_node_id,selfNodeId,selfIp,xmlParsedFields_0003[1],Node_id_of_querried_hash);
																					
							IndexManagementUtilityMethods.createXmlSearchQueryReply(caller_Ip, querried_ip, caller_node_id, selfNodeId, selfIp, querried_emailid_hash);
							
							String Inter_nodeid_ip = xmlParsedFields_0003[6];
							
							int number =0;
    						String Inter_nodeid = null;
    						String Inter_ip = null;
    					
    						number = Inter_nodeid_ip.length();
    						Inter_nodeid = Inter_nodeid_ip.substring(0,40);
    						Inter_ip = Inter_nodeid_ip.substring(40,number);
    						
    						if(!CommunicationUtilityMethods.myIpTable.containsKey(Inter_nodeid))
    						{	
    							CommunicationManager.myIpTable.put(Inter_nodeid, Inter_ip);
    							OverlayManagement.updatPredSec();
    						
    							Set<String> nodeId_extracted1 = myIpTable.keySet(); /// code to extract hash_id from array by first
    							/// converting it into collection then to an array
    							Collection<String> ip_extracted1 = myIpTable.values();
    					
    							
    							String[] nodeIdArr1 = nodeId_extracted1.toArray(new String[nodeId_extracted1.size()]);
    							String[] ipAddArr1 = ip_extracted1.toArray(new String[ip_extracted1.size()]);
    					
    								
    							lock.lock();
    							try
    							{
    								ipTableWriter(nodeIdArr1,ipAddArr1);
    							}
    							finally
    							{
    								lock.unlock();
    							}
    						}
    						
    						IndexManagementUtilityMethods.createXmlForCacheEntry(Inter_ip, querried_ip, Inter_nodeid, selfNodeId, selfIp, xmlParsedFields_0003[1], Node_id_of_querried_hash);
    							
    					}
						
						
					}
					else
					{
						System.out.println("Not Resposible and querried hash is not in cache");
						
						String key = xmlParsedFields_0003[1];
						String inter_ip = IndexManagementUtilityMethods.getMyIp();
						String caller_node_id = xmlParsedFields_0003[3];
						String caller_ip = xmlParsedFields_0003[4];
						
						String Inter_nodeid_ip = OverlayManagement.myNodeId+inter_ip;
						
						File remade_searchQuery = IndexManagementUtilityMethods.createXmlSearchQuery(key, Inter_nodeid_ip, caller_node_id, caller_ip);
						
						if(!xmlParsedFields[3].equals(OverlayManagement.myNodeId))
						{
							com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(remade_searchQuery);
							SysOutCtrl.SysoutSet("FileAded to Tx Buffer " + TransmittingBuffer.getLast(), 2);
						}	
					}
				}
				
				
				else
				{
					//TransmittingBuffer.add(TransmittingBuffer.size(), file);
					if(!xmlParsedFields[3].equals(OverlayManagement.myNodeId))
					{	
						com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(file);
						SysOutCtrl.SysoutSet("FileAded to Tx Buffer " + TransmittingBuffer.getLast(), 2);
					}	
				}
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				SysOutCtrl.SysoutSet("Receving Buffer after segregation" + ReceivingBuffer, 2);
				SysOutCtrl.SysoutSet("RT Buffer after segregation" + RxBufferRT, 2);
				SysOutCtrl.SysoutSet("OM Buffer after segregation" + RxBufferOM, 2);
				SysOutCtrl.SysoutSet("IM Buffer after segregation" + RxBufferIM, 2);
				SysOutCtrl.SysoutSet("Tx Buffer after segregation" + TransmittingBuffer, 2);
			}
	}

	static Map<String, String> tempIndexTable = new LinkedHashMap<String, String>();

	public static void main(String[] args) 
	{
		File file = new File("XML.xml");
		tempIndexTable = convertXmlToIndexTable(file);
		SysOutCtrl.SysoutSet("Conveted File:" + tempIndexTable);
	}
	
	
	public static void ipTableWriter(String[] nodeId, String[] ipAdd)
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
			write = new FileWriter("IpTable.txt");
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
	
		SysOutCtrl.SysoutSet("ip table written to file");
		wr.flush();
	
	}
}