package com.ehelpy.brihaspati4.sms;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

public class sms_retrival_thread extends Thread
{
	public static Map<String, String> nodeId_Ip= new ConcurrentHashMap<String, String>();
	public static Map<String, String> PredNodeId_Ip= new ConcurrentHashMap<String, String>();
	public static TreeMap<String, TreeMap<String, String>> PredNodeId_EmailHashCertMap = new TreeMap<String, TreeMap<String, String>>();
	
	public void run()
    {
		sms_send_rec_management.ask_pred_for_cache();
		
		Thread t1= new Thread
		(
			new Runnable()
			{
				@Override
				public void run()
				{
					while(true)	
					{
						System.out.println("Thread for asking msgs from succ.");
						sms_send_rec_management.create_msg_request_query();
						try
						{
							Thread.sleep(120000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}    
					}
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
					while(true)	
					{
						System.out.println("Thread for updating succ cache for sms ");
						sms_send_rec_management.update_succ();
						try
						{
							Thread.sleep(200000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}    
					}
				}
			}
		);
		t2.start();
		
		Thread t3= new Thread
		(
			new Runnable()
			{
				@Override
				public void run()
				{
					while(true)
					{
						System.out.println("SMS_management thread");
		            			
		            	while(!CommunicationManager.RxBufferSMS.isEmpty())
		            	{
		            		File file=CommunicationManager.RxBufferSMS.removeFirst();
        					
        					String[] xmlParsedFields = ParseXmlFile.ParseXml(file);
        					
        					if (xmlParsedFields[0].equals("1011"))
        					{
        						String[] xmlParsedFields_1011 = ParseXmlFile.ParseXml_1011(file);
        						
        						String destination_node_id = xmlParsedFields_1011[3];
        						String Ip = xmlParsedFields_1011[4];
        						nodeId_Ip.put(destination_node_id, Ip);
        						
        						String hash_id = xmlParsedFields_1011[1];
        					       						
        						String encr_key = xmlParsedFields_1011[7];
        						
        						String msg = xmlParsedFields_1011[2];
        						
        						String port_email = xmlParsedFields_1011[5];
        												
        						String sender_email_id = null;
        						String file_name_of_msg = null;
        						int number = 0;
        						
        						number = port_email.length();
        						
        						file_name_of_msg = port_email.substring(4,34);
        						sender_email_id = port_email.substring(34,number);
        						
        						String rec_email_id = xmlParsedFields_1011[6];
        																				
        						sms_send_rec_management.received_querry(hash_id, msg, sender_email_id, rec_email_id, file_name_of_msg, encr_key );
        					}
        					
        					else if (xmlParsedFields[0].equals("1012"))
        					{
        						String folder_name = xmlParsedFields[2];
        						String destination_node_id = xmlParsedFields[3];
        						String Ip = xmlParsedFields[4];
        						nodeId_Ip.put(destination_node_id, Ip);
        						sms_send_rec_management.send_msgs_of_node(folder_name, destination_node_id);
        					}
        					
        					else if(xmlParsedFields[0].equals("1013"))
        					{
        						String[] xmlParsedFields_1011 = ParseXmlFile.ParseXml_1011(file);
        						
        						String destination_node_id = xmlParsedFields_1011[3];
        						String Ip = xmlParsedFields_1011[4];
        						nodeId_Ip.put(destination_node_id, Ip);
        						
        						String msg = xmlParsedFields_1011[2];
        						
        						String port_email = xmlParsedFields_1011[5];
        												
        						String sender_email_id = null;
        						String file_name_of_msg = null;
        						int number = 0;
        						
        						number = port_email.length();
        						
        						file_name_of_msg = port_email.substring(4,34);
        						sender_email_id = port_email.substring(34,number);
        						
        						String rec_email_id = xmlParsedFields_1011[6];
        						String node_Id = xmlParsedFields_1011[3];
        						
        						String encr_key = xmlParsedFields_1011[7];
        																				
        						sms_send_rec_management.store_messages_for_cache(node_Id, sender_email_id, rec_email_id, msg, file_name_of_msg, encr_key );
        					}
        					
        					else if (xmlParsedFields[0].equals("1014"))
        					{
        						String destination_node_id = xmlParsedFields[3];
        						String Ip = xmlParsedFields[4];
        						nodeId_Ip.put(destination_node_id, Ip);
        						sms_send_rec_management.send_msgs_for_cache(destination_node_id);
        					}
        					
        					else if (xmlParsedFields[0].equals("1015"))
        					{
        						String[] xmlParsedFields_1011 = ParseXmlFile.ParseXml_1011(file);
        						
        						String destination_node_id = xmlParsedFields_1011[3];
        						
        						String Ip = xmlParsedFields_1011[4];
        						nodeId_Ip.put(destination_node_id, Ip);
        						
        						String hash_id = xmlParsedFields_1011[1];
        					       						
        						String encr_key = xmlParsedFields_1011[7];
        						
        						String msg = xmlParsedFields_1011[2];
        						
        						String port_email = xmlParsedFields_1011[5];
        												
        						String sender_email_id = null;
        						String file_name_of_msg = null;
        						int number = 0;
        						
        						number = port_email.length();
        						
        						file_name_of_msg = port_email.substring(4,34);
        						sender_email_id = port_email.substring(34,number);
        						
        						String rec_email_id = xmlParsedFields_1011[6];
        																				
        						sms_send_rec_management.received_querry(hash_id, msg, sender_email_id, rec_email_id, file_name_of_msg, encr_key );
        					}
        					
        					else if (xmlParsedFields[0].equals("1016"))
        					{
        						String[] info_from_xml_0003 = ParseXmlFile.ParseXml_0003(file);
        						
        						String hashIdFromxml=info_from_xml_0003[1];
                                
    							String selfIp = IndexManagementUtilityMethods.getMyIp();
    							String selfNodeId = OverlayManagement.myNodeId;
    							
    							String cert = null;
    							    							
    							if(IndexManagement.EmailHashId_certificates.containsKey(hashIdFromxml))
    							{		
    								cert = IndexManagement.EmailHashId_certificates.get(hashIdFromxml);
    								sms_send_rec_management.search_cert_replyXml(info_from_xml_0003[3],info_from_xml_0003[4], cert, selfNodeId, selfIp, hashIdFromxml);
    							}
    							
    							else
    								sms_send_rec_management.search_cert_replyXml(info_from_xml_0003[3],info_from_xml_0003[4], "NotFound", selfNodeId, selfIp, hashIdFromxml);
        						
        					}
        					
        					else if(xmlParsedFields[0].equals( "1017"))
        					{
        						String cert = xmlParsedFields[2];
        						if(cert.equals("NotFound"))
        						{		
        							IndexManagement.searchReply_cert.put(xmlParsedFields[5], cert);
        						}		
        						
        						else	
        						{	
        							IndexManagement.searchReply_cert.put(xmlParsedFields[5], cert);
        							IndexManagement.emailIdHash_cert.put(xmlParsedFields[5], cert);
        						}
        					}
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
		            		
		Thread t4= new Thread
		(
			new Runnable()
			{
				@Override
				public void run()
				{
					while(true)	
					{
						System.out.println("Thread for checking pred alive status for movig sms from cache to rec folder ");
						sms_send_rec_management.check_pred_alive();
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
		t4.start();
	}	
}
