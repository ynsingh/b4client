package com.ehelpy.brihaspati4.sms;

import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;
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
import org.w3c.dom.Text;

import com.ehelpy.brihaspati4.authenticate.ConvertStringCertToX509;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.indexmanager.IndexManagement;

public class sms_send_rec_management 
{
	public static Map<String, String>  filename_key = new ConcurrentHashMap<String, String>();
	public static boolean sending_message = false;
	
	public static void create_msg_request_query() 
	{
		String self_email_id = emailid.getemaild();
		String self_email_hash = SHA1.getSha1(self_email_id);
		
		String tonodeId = com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(self_email_hash);
	    
	    if(tonodeId.equals(OverlayManagement.myNodeId))
	    {	
	    	tonodeId = PredecessorSuccessor.mySuccessors[0];
	    	self_email_hash = tonodeId;
	    }
	    
	    String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
		
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
	    
	    String tagvalue = "1012";
	    
	    ((Element) codeele).setAttribute("tag", tagvalue);
	
	    Text t1 = doc.createTextNode(self_email_hash);
	
	    Text t2 = doc.createTextNode(self_email_id);
	    String selfNodeId = OverlayManagement.myNodeId;
	
	    Text t3 = doc.createTextNode(selfNodeId);
	    String selfIp = PresentIP.MyPresentIP();
	
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
	        result = new StreamResult(new FileOutputStream("sms_request.xml"));
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
	    
	    OverlayManagementUtilityMethods.sendFileDirect(toIp,new File("sms_request.xml"));
	    
	    String toNodeId_Succ = PredecessorSuccessor.mySuccessors[0];
	    if(!toNodeId_Succ.equals(tonodeId))
	    {
	    	String toIp_succ = CommunicationUtilityMethods.getIpFromMyIpTable(toNodeId_Succ);
	    	OverlayManagementUtilityMethods.sendFileDirect(toIp_succ,new File("sms_request.xml"));
	    }
	    
	}

	public static void send_msgs_of_node(String folder_name, String destination_node_id)
	{
		if(!destination_node_id.equals(OverlayManagement.myNodeId))
		{	
			String loc1_message_rec_folder = "null";
		
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			try {
				loc1_message_rec_folder = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			String fileSeparator = System.getProperty("file.separator");
		
		
			/////////////////////////////////////////////////////this is for messages the destination node is reponsible
			File file_rec = new File(loc1_message_rec_folder);
			String[] files_rec = file_rec.list();
		
			String self_email_id = emailid.getemaild();
			for(String Rec_email_foder : files_rec)
			{
				String email_hash = SHA1.getSha1(Rec_email_foder);
			
				if(!self_email_id.equals(Rec_email_foder))
				{	
					if(CommunicationUtilityMethods.responsibleNode(OverlayManagement.myNodeId,destination_node_id,email_hash))
					{	
						String loc2_message_folder_name_of_rec= "null";
		
						System.out.println("folder to be sent : "+Rec_email_foder);
					
						loc2_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + Rec_email_foder;
						Path loc_path1 = Paths.get(loc2_message_folder_name_of_rec);
		
						File file = new File(loc2_message_folder_name_of_rec);
						String[] files = file.list();
			
						int i =0;
						for(String sender_email : files)
						{
							String msg_file = loc2_message_folder_name_of_rec+fileSeparator + sender_email ;
				
							File file1 = new File(msg_file );
							String[] files1 = file1.list();
							for(String file_name_msg : files1)
							{	
								String msg_file_loc = msg_file + fileSeparator + file_name_msg;
								String text = sms_send_rec_management.readFileAsString(msg_file_loc);
							
								System.out.println("file to be sent "+file_name_msg);
											
								String encr_key = filename_key.get(file_name_msg);
								send_to_dest_node_query_encr(destination_node_id, sender_email, Rec_email_foder, text, file_name_msg, encr_key);
							
								i++;
								System.out.println("file "+i+" sent to : "+Rec_email_foder);
					
								Path path_msg_file_loc = Paths.get(msg_file_loc);
								try {
									Files.delete(path_msg_file_loc);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
				
							Path path_msg_file = Paths.get(msg_file);
							try {
								Files.delete(path_msg_file);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			
						try {
							Files.delete(loc_path1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}	
			}
		
			///////////////////////////////////////////////////////this is for messages pertaining to destination node
			if(!self_email_id.equals(folder_name))
			{	
		
				String loc3_message_folder_name_of_rec= "null";
		
				loc3_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + folder_name;
				Path loc_path3 = Paths.get(loc3_message_folder_name_of_rec);
		
				boolean check_dir1;
				check_dir1 = Files.exists(loc_path3);
		
				if(check_dir1)
				{
					File file = new File(loc3_message_folder_name_of_rec);
					String[] files = file.list();
		
					int i =0;
					for(String sender_email : files)
					{
						String msg_file = loc3_message_folder_name_of_rec+fileSeparator + sender_email ;
			
						File file1 = new File(msg_file );
						String[] files1 = file1.list();
						for(String file_name_msg : files1)
						{	
							String msg_file_loc = msg_file + fileSeparator + file_name_msg;
							String text = sms_send_rec_management.readFileAsString(msg_file_loc);
					
							String encr_key = filename_key.get(file_name_msg);
							send_to_dest_node_query_encr(destination_node_id, sender_email, folder_name, text, file_name_msg, encr_key);
							i++;
							System.out.println("file "+i+" sent to : "+folder_name);
				
							Path path_msg_file_loc = Paths.get(msg_file_loc);
							try {
								Files.delete(path_msg_file_loc);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			
						Path path_msg_file = Paths.get(msg_file);
						try {
							Files.delete(path_msg_file);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
		
					try {
					Files.delete(loc_path3);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void obtain_responsible_node_id(String email, String text, String file_name_of_msg)
	{
		String self_email_id = emailid.getemaild();
		String self_email_hash = SHA1.getSha1(self_email_id);
		
		String sha1 = SHA1.getSha1(email);
		String destNodeId = null;
		
		if(sha1.equals(self_email_hash))
			send_to_dest_node_query(OverlayManagement.myNodeId, self_email_id, email, text, file_name_of_msg);
		
		else if(IndexManagement.cached_index.containsKey(sha1))
		{	
			destNodeId = IndexManagement.cached_index.get(sha1);
			send_to_dest_node_query(destNodeId, self_email_id, email, text, file_name_of_msg);
		}	
		
		else if(IndexManagement.myindex.containsKey(sha1))
		{
            destNodeId = IndexManagement.myindex.get(sha1);
            send_to_dest_node_query(destNodeId, self_email_id, email, text, file_name_of_msg);
		}   
        	
		else
        {
        	if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,sha1))
        		send_to_dest_node_query(sha1, self_email_id, email, text, file_name_of_msg);
        	
        	else
        		send_to_dest_node_query(sha1, self_email_id, email, text, file_name_of_msg);
        }
		
	}
	
	public static void received_querry(String hash_id, String msg, String sender_email, String rec_email_id, String file_name_of_msg, String encr_key)
	{
		String self_email_id = emailid.getemaild();
		String self_email_hash = SHA1.getSha1(self_email_id);
		
		if(hash_id.equals(OverlayManagement.myNodeId))
			store_message(sender_email, rec_email_id, msg, file_name_of_msg, encr_key);
		
		else if(hash_id.equals(self_email_hash))
			store_message(sender_email, rec_email_id, msg, file_name_of_msg, encr_key);
		
		else
		{
			String destNodeId = null;
			if(IndexManagement.cached_index.containsKey(hash_id))
			{	
				destNodeId = IndexManagement.cached_index.get(hash_id);
				send_to_dest_node_query_encr(destNodeId, sender_email, rec_email_id, msg, file_name_of_msg, encr_key );
			}	
			
			else if(IndexManagement.myindex.containsKey(hash_id))
			{
	            destNodeId = IndexManagement.myindex.get(hash_id);
	            send_to_dest_node_query_encr(destNodeId, sender_email, rec_email_id, msg, file_name_of_msg, encr_key );
			}   
	        	
			else
	        {
	        	if (CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,hash_id))
	        		store_message(sender_email, rec_email_id, msg, file_name_of_msg, encr_key);
	        	
	        	else
	        		send_to_dest_node_query_encr(hash_id, sender_email, rec_email_id, msg, file_name_of_msg, encr_key);
	        }
		}
	}
	
	public static void send_to_dest_node_query(String hash_id, String email, String rec_email, String text, String file_name_of_msg) 
	{
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
        
        String tagvalue = "1011";
        
        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(hash_id);

        Text t2 = doc.createTextNode(text);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = PresentIP.MyPresentIP();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222"+file_name_of_msg+email;
        Text t5 = doc.createTextNode(selfPortNo);
        Text t6 = doc.createTextNode(rec_email);

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
            result = new StreamResult(new FileOutputStream("sms_query.xml"));
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
   
        encrypt_before_sending(new File("sms_query.xml"));

	}
	
	public static void send_to_dest_node_query_encr(String hash_id, String email, String rec_email, String text, String file_name_of_msg, String encr_key) 
	{
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
        Element keyele = doc.createElement("key");
        
        String tagvalue = "1015";
        
        ((Element) codeele).setAttribute("tag", tagvalue);

        Text t1 = doc.createTextNode(hash_id);

        Text t2 = doc.createTextNode(text);
        String selfNodeId = OverlayManagement.myNodeId;

        Text t3 = doc.createTextNode(selfNodeId);
        String selfIp = PresentIP.MyPresentIP();

        Text t4 = doc.createTextNode(selfIp);
        String selfPortNo = "2222"+file_name_of_msg+email;
        Text t5 = doc.createTextNode(selfPortNo);
        Text t6 = doc.createTextNode(rec_email);
        Text t7 = doc.createTextNode(encr_key );

        hashidele.appendChild(t1);
        tonodeidele.appendChild(t2);
        selfnodeidele.appendChild(t3);
        ipele.appendChild(t4);
        portele.appendChild(t5);
        intermediate_ipele.appendChild(t6);
        keyele.appendChild(t7);

        codeele.appendChild(hashidele);
        codeele.appendChild(tonodeidele);
        codeele.appendChild(selfnodeidele);
        codeele.appendChild(ipele);
        codeele.appendChild(portele);
        codeele.appendChild(intermediate_ipele);
        codeele.appendChild(keyele);

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
            result = new StreamResult(new FileOutputStream("sms_query_further.xml"));
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
        
        com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("sms_query_further.xml"));

	}
	
		
	public static void store_message(String sender_email, String rec_email, String msg, String file_name_of_msg , String encr_key)
	{
		String self_email_id = emailid.getemaild();
		
		String loc1_message_rec_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_message_rec_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String loc2_message_folder_name_of_rec= "null";
		
		String fileSeparator = System.getProperty("file.separator");
		
		loc2_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + rec_email;
		Path loc_path1 = Paths.get(loc2_message_folder_name_of_rec);
		
		boolean check_dir1;
		check_dir1 = Files.exists(loc_path1);
		
		if (!check_dir1)
		{
			try {
				Files.createDirectory(loc_path1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String loc2_message_folder_sender_name = loc2_message_folder_name_of_rec + fileSeparator + sender_email;
		Path loc_path2 = Paths.get(loc2_message_folder_sender_name);
		
		boolean check_dir2;
		check_dir2 = Files.exists(loc_path2);
		
		if (!check_dir2)
		{
			try {
				Files.createDirectory(loc_path2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
			
		creat_text_file(msg, loc2_message_folder_sender_name, file_name_of_msg, encr_key, rec_email);
			
		if(self_email_id.equals(rec_email))
		{	
			JFrame frame1 = new JFrame("Message");
			JOptionPane.showMessageDialog(frame1, "You have got a new message from "+sender_email);
		}
		else
			update_succ();
		
		
	}
	
	public static void creat_text_file(String encr_msg, String file_loc, String file_name_of_msg, String encr_key, String rec_email)
	{
		String self_email_id = emailid.getemaild();
				
		if(self_email_id.equals(rec_email))
		{
			try 
			{
				FileWriter write = null;
			
				String fileSeparator = System.getProperty("file.separator");
		
				String file_name = null;
						
				String decrypted_msg= null;
							
				file_name = file_loc + fileSeparator + file_name_of_msg ;
							
				byte[] encr_key1_bytes = Base64.getDecoder().decode(encr_key);
				byte[] encr_msg_bytes = Base64.getDecoder().decode(encr_msg);
		
				////////////////////////////////////////////////////////////////////////////////
				////Extracting session key using own private key
				
				PrivateKey own_priv_key = ReadVerifyCert.getKeyPair();
				
				Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    	    cipher1.init(Cipher.PRIVATE_KEY, own_priv_key);
	    	    byte[] decryptedKey = cipher1.doFinal(encr_key1_bytes);
	    	    
	  //////////////////////////////////////////////////////////////////////////////////////// 	    	    
		 	
			    if (decryptedKey.length != 16) {
			      throw new IllegalArgumentException("Invalid key size.");
			    }
			    SecretKeySpec skeySpec = new SecretKeySpec(decryptedKey, "AES");

			    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			    cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
			    byte[] original = cipher.doFinal(encr_msg_bytes);

			    decrypted_msg = new String(original, Charset.forName("UTF-8"));
			    
			    
			   	//////////////////////////////////////////////////////////////////////////////////	    
				
				Path loc_path = Paths.get(file_name);
		    
				boolean check_dir;
				check_dir = Files.exists(loc_path);
			
				if(!check_dir)		
				{	
					try {
						Files.createFile(loc_path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    
					System.out.println("file name : "+file_name);
		    
					try
					{
						write = new FileWriter(file_name);
					}
					catch (IOException e1) 
					{
		
						e1.printStackTrace();
					}
			
					PrintWriter wr = new PrintWriter(write);
		
					wr.write(decrypted_msg);
					wr.println("");
		
					wr.flush();
				}
			} 
			catch (NoSuchAlgorithmException | NoSuchPaddingException  
					| InvalidKeyException  |IllegalBlockSizeException | BadPaddingException |
					InvalidAlgorithmParameterException e2) 
			{
				e2.printStackTrace();
			}
			
		}
		
		else
		{	
			FileWriter write = null;
		
			String fileSeparator = System.getProperty("file.separator");
		
			String file_name = null;
			
			file_name = file_loc + fileSeparator + file_name_of_msg;
				
			filename_key.put(file_name_of_msg, encr_key);
			
	    	Path loc_path = Paths.get(file_name);
	    
			boolean check_dir;
			check_dir = Files.exists(loc_path);
		
			if(!check_dir)		
			{	
				try {
					Files.createFile(loc_path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.out.println("file name : "+file_name);
	    
				try
				{
					write = new FileWriter(file_name);
				}
				catch (IOException e1) 
				{
	
					e1.printStackTrace();
				}
		
				PrintWriter wr = new PrintWriter(write);
				
				wr.write(encr_msg);
				wr.println("");
	
				wr.flush();
			}
		}	
	}
	
	public static void update_succ() 
	{
		String self_email_id = emailid.getemaild();
		
		String loc1_message_rec_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_message_rec_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fileSeparator = System.getProperty("file.separator");
		
		File folders = new File(loc1_message_rec_folder);
		String[] folder_files = folders.list();
		
		for(String rec_folder_name : folder_files)
		{	
			if(!self_email_id.equals(rec_folder_name))
			{	
				String loc2_message_folder_name_of_rec= "null";
		
				loc2_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + rec_folder_name;
						
				File file = new File(loc2_message_folder_name_of_rec);
				String[] files = file.list();
							
				for(String sender_email_folder : files)
				{
					String msg_file = loc2_message_folder_name_of_rec+fileSeparator + sender_email_folder ;
			
					File file1 = new File(msg_file );
					String[] files1 = file1.list();
					for(String file_name_msg : files1)
					{	
						String msg_file_loc = msg_file + fileSeparator + file_name_msg;
						String text = sms_send_rec_management.readFileAsString(msg_file_loc);
						String encr_key = filename_key.get(file_name_msg);
						send_to_succ_query(sender_email_folder, rec_folder_name, text, file_name_msg, encr_key );
					}
				}
			}
		}	
	}

	public static void send_to_succ_query(String sender_email, String rec_email, String text, String file_name_msg, String encr_key) 
	{
		List<String> Successors = new ArrayList<String>();
		
		for (int i = 0; i < 5; i++)
		{
			String hash_id = PredecessorSuccessor.mySuccessors[i];
			
			if(!Successors.contains(hash_id) && !hash_id.equals(OverlayManagement.myNodeId))
			{	
				Successors.add(hash_id);
				
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
				Element keyele = doc.createElement("key");
        
				String tagvalue = "1013";
        
				((Element) codeele).setAttribute("tag", tagvalue);

				Text t1 = doc.createTextNode(hash_id);

				Text t2 = doc.createTextNode(text);
				String selfNodeId = OverlayManagement.myNodeId;

				Text t3 = doc.createTextNode(selfNodeId);
				String selfIp = PresentIP.MyPresentIP();

				Text t4 = doc.createTextNode(selfIp);
				String selfPortNo = "2222"+file_name_msg+sender_email;
				Text t5 = doc.createTextNode(selfPortNo);
				Text t6 = doc.createTextNode(rec_email);
				Text t7 = doc.createTextNode(encr_key );

				hashidele.appendChild(t1);
				tonodeidele.appendChild(t2);
				selfnodeidele.appendChild(t3);
				ipele.appendChild(t4);
				portele.appendChild(t5);
				intermediate_ipele.appendChild(t6);
				keyele.appendChild(t7);

				codeele.appendChild(hashidele);
				codeele.appendChild(tonodeidele);
				codeele.appendChild(selfnodeidele);
				codeele.appendChild(ipele);
				codeele.appendChild(portele);
				codeele.appendChild(intermediate_ipele);
				codeele.appendChild(keyele);

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
					result = new StreamResult(new FileOutputStream("sms_caching_query.xml"));
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
        
				//	String tonodeId = com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_id);
				//	String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
               
				//	OverlayManagementUtilityMethods.sendFileDirect(toIp,new File("sms_caching_query.xml"));
				com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("sms_caching_query.xml"));
			}
		}	
	}
	
	public static void send_msgs_for_cache(String destination_node_id)
	{
		if(!destination_node_id.equals(OverlayManagement.myNodeId))
		{	
			String self_email_id = emailid.getemaild();
		
			String loc1_message_rec_folder = "null";
		
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			try {
				loc1_message_rec_folder = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			String fileSeparator = System.getProperty("file.separator");
		
			File folders = new File(loc1_message_rec_folder);
			String[] folder_files = folders.list();
		
			for(String rec_folder_name : folder_files)
			{	
				if(!self_email_id.equals(rec_folder_name))
				{	
					String loc2_message_folder_name_of_rec= "null";
		
					loc2_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + rec_folder_name;
						
					File file = new File(loc2_message_folder_name_of_rec);
					String[] files = file.list();
							
					for(String sender_email_folder : files)
					{
						String msg_file = loc2_message_folder_name_of_rec+fileSeparator + sender_email_folder ;
			
						File file1 = new File(msg_file );
						String[] files1 = file1.list();
						for(String file_name_msg : files1)
						{	
							String msg_file_loc = msg_file + fileSeparator + file_name_msg;
							String text = sms_send_rec_management.readFileAsString(msg_file_loc);
							String encr_key = filename_key.get(file_name_msg);
							send_to_particular_succ_query(sender_email_folder, rec_folder_name, text, file_name_msg, destination_node_id, encr_key );
						}
					}
				}
			}
		}	
	}

	public static void send_to_particular_succ_query(String sender_email, String rec_email, String text, String file_name_msg, String destination_node_id, String encr_key) 
	{
		String hash_id = destination_node_id;
		
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
		Element keyele = doc.createElement("key");
    
		String tagvalue = "1013";
    
		((Element) codeele).setAttribute("tag", tagvalue);

		Text t1 = doc.createTextNode(hash_id);

		Text t2 = doc.createTextNode(text);
		String selfNodeId = OverlayManagement.myNodeId;

		Text t3 = doc.createTextNode(selfNodeId);
		String selfIp = PresentIP.MyPresentIP();

		Text t4 = doc.createTextNode(selfIp);
		String selfPortNo = "2222"+file_name_msg+sender_email;
		Text t5 = doc.createTextNode(selfPortNo);
		Text t6 = doc.createTextNode(rec_email);
		Text t7 = doc.createTextNode(encr_key );
		
		hashidele.appendChild(t1);
		tonodeidele.appendChild(t2);
		selfnodeidele.appendChild(t3);
		ipele.appendChild(t4);
		portele.appendChild(t5);
		intermediate_ipele.appendChild(t6);
		keyele.appendChild(t7);

		codeele.appendChild(hashidele);
		codeele.appendChild(tonodeidele);
		codeele.appendChild(selfnodeidele);
		codeele.appendChild(ipele);
		codeele.appendChild(portele);
		codeele.appendChild(intermediate_ipele);
		codeele.appendChild(keyele);

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
			result = new StreamResult(new FileOutputStream("sms_caching_query.xml"));
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
    
	//	String tonodeId = com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_id);
	//	String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
           
	//	OverlayManagementUtilityMethods.sendFileDirect(toIp,new File("sms_caching_query.xml"));
		com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("sms_caching_query.xml"));
		
	}

	public static void store_messages_for_cache(String node_id, String sender_email, String rec_email, String msg, String file_name_of_msg, String encr_key)
	{
		String rec_email_hash = SHA1.getSha1(rec_email);
		boolean responsible = CommunicationUtilityMethods.responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.myNodeId,rec_email_hash);
		
		if(!OverlayManagement.myNodeId.equals(node_id) && !responsible )
		{	
			String self_email_id = emailid.getemaild();
			
			String loc1_cache_folder = "null";
		
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader("Msg_cache_folder_path.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
			try {
				loc1_cache_folder = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			String fileSeparator = System.getProperty("file.separator");
		
			String node_id_cache = null;
			node_id_cache = loc1_cache_folder + fileSeparator + node_id;
			Path loc_path = Paths.get(node_id_cache);
		
			boolean check_dir;
			check_dir = Files.exists(loc_path);
		
			if (!check_dir)
			{
				try {
				Files.createDirectory(loc_path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(!self_email_id.equals(rec_email))
			{	
		
				String loc2_message_folder_name_of_rec= "null";
		
				loc2_message_folder_name_of_rec = node_id_cache + fileSeparator + rec_email;
				Path loc_path1 = Paths.get(loc2_message_folder_name_of_rec);
		
				boolean check_dir1;
				check_dir1 = Files.exists(loc_path1);
		
				if (!check_dir1)
				{
					try {
						Files.createDirectory(loc_path1);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
				String loc2_message_folder_sender_name = loc2_message_folder_name_of_rec + fileSeparator + sender_email;
				Path loc_path2 = Paths.get(loc2_message_folder_sender_name);
		
				boolean check_dir2;
				check_dir2 = Files.exists(loc_path2);
		
				if (!check_dir2)
				{
					try {
						Files.createDirectory(loc_path2);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				creat_text_file(msg, loc2_message_folder_sender_name, file_name_of_msg , encr_key, rec_email);
			}
		}	
	}
	
	public static void check_pred_alive()
	{
		String loc1_cache_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_cache_folder_path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_cache_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path loc_path = Paths.get(loc1_cache_folder);
		
		boolean check_dir;
		check_dir = Files.exists(loc_path);
		
		if (check_dir)
		{
			File file = new File(loc1_cache_folder);
			String[] files = file.list();
			
			for(String node_id : files)
			{
				boolean is_nodeId_alive = true;
				String Ip = null;
				if(sms_retrival_thread.nodeId_Ip.containsKey(node_id))
				{	
					Ip =  sms_retrival_thread.nodeId_Ip.get(node_id);
					is_nodeId_alive = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(Ip);
				
					if(!is_nodeId_alive)
						move_from_cahe_to_rec_folder(node_id);
				}
				if(!is_nodeId_alive)
					sms_retrival_thread.nodeId_Ip.remove(node_id, Ip);
			}
		}
		
		boolean is_PredNodeId_alive = true;
		Set<String> NodeId = sms_retrival_thread.PredNodeId_Ip.keySet();
        for(String key : NodeId)
        {	
        	String Ip =  sms_retrival_thread.PredNodeId_Ip.get(key);
        	is_PredNodeId_alive = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(Ip);
        	 
        	if(!is_PredNodeId_alive) 
        	{
        		TreeMap<String, String> Temp_EmailHashId_certificates = sms_retrival_thread.PredNodeId_EmailHashCertMap.get(key);
                IndexManagement.EmailHashId_certificates.putAll(Temp_EmailHashId_certificates);
        	}
        }
	}
	
	public static void move_from_cahe_to_rec_folder(String node_id)
	{
		String loc1_cache_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_cache_folder_path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_cache_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fileSeparator = System.getProperty("file.separator");
		
		String node_id_cache = null;
		node_id_cache = loc1_cache_folder + fileSeparator + node_id;
		
		Path loc_path = Paths.get(node_id_cache);
		
		boolean check_dir;
		check_dir = Files.exists(loc_path);
		
		if(check_dir)
		{
			File file = new File(node_id_cache);
			String[] files = file.list();
			
			for(String rec_email : files)
			{
				String rec_email_folder = node_id_cache + fileSeparator + rec_email;
				
				File file1 = new File(rec_email_folder );
				String[] files1 = file1.list();
				
				for(String sender_email : files1)
				{
					String sender_email_folder = rec_email_folder + fileSeparator + sender_email;
					
					File file2 = new File(sender_email_folder );
					String[] files2 = file2.list();
					
					for(String msg_file : files2)
					{
						String msg_file_loc = sender_email_folder + fileSeparator + msg_file;
						String text = sms_send_rec_management.readFileAsString(msg_file_loc);
						String encr_key = filename_key.get(msg_file);
						
						store_message(sender_email, rec_email, text, msg_file, encr_key);
						
						Path path_msg_file_loc = Paths.get(msg_file_loc);
						try {
							Files.delete(path_msg_file_loc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Path path_sender_email_folder = Paths.get(sender_email_folder);
					try {
						Files.delete(path_sender_email_folder);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Path path_rec_email = Paths.get(rec_email_folder);
				try {
					Files.delete(path_rec_email);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			Path path_node_id_cache = Paths.get(node_id_cache);
			try {
				Files.delete(path_node_id_cache);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void ask_pred_for_cache()
	{
		List<String> Predecessors = new ArrayList<String>();
		
		for (int i = 0; i < 5; i++)
		{
			String hash_id = PredecessorSuccessor.myPredecessors[i];
			
			if(!Predecessors.contains(hash_id) && !hash_id.equals(OverlayManagement.myNodeId))
			{	
				Predecessors.add(hash_id);
				
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
		        
				String tagvalue = "1014";
        
				((Element) codeele).setAttribute("tag", tagvalue);

				Text t1 = doc.createTextNode(hash_id);

				Text t2 = doc.createTextNode("request_for_cache");
				String selfNodeId = OverlayManagement.myNodeId;

				Text t3 = doc.createTextNode(selfNodeId);
				String selfIp = PresentIP.MyPresentIP();

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
					result = new StreamResult(new FileOutputStream("sms_caching_request_query.xml"));
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
        
				String tonodeId = com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_id);
				String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
               
				OverlayManagementUtilityMethods.sendFileDirect(toIp,new File("sms_caching_request_query.xml"));
			}
		}	
	}
	
	public static String rec_email = null;
	public static String sender_email_id = null;
	public static String file_name_of_msg = null;
	public static String port_email = null;
	public static String msg = null;
	public static String hash_email = null;
	public static int number = 0;
	
	public static void encrypt_before_sending(File file)
	{
		String[] xmlParsed;
        
		SysOutCtrl.SysoutSet("Xml file parsed:" + file,2);
     
  	  	xmlParsed = ParseXmlFile.ParseXml(file);
  	    	  	
  	  	if(xmlParsed[0].equals("1011"))
  	  	{
  	  		String[] xmlParsed_0003 = ParseXmlFile.ParseXml_0003(file);;
		  	
  	  		sender_email_id = emailid.getemaild();
  	  		msg = xmlParsed_0003[2];
  	  		rec_email = xmlParsed_0003[6];
  	  		hash_email =(SHA1.getSha1(rec_email));
  	  			
  	  		port_email = (xmlParsed_0003[5]);
  	  		number = port_email.length();
  	  		file_name_of_msg = (port_email.substring(4,34));
  	
  	  		if(IndexManagement.emailIdHash_cert.containsKey(hash_email))
  	  		{
  	  			String cert_string = IndexManagement.emailIdHash_cert.get(hash_email);
  	  			try 
  	  			{
  	  				X509Certificate[] certificate = ConvertStringCertToX509.convertToX509Certarray(cert_string);
  	  				PublicKey rec_public_key = certificate[0].getPublicKey();
  	  				
  	  				byte[] msg_encr_bytes = encrypt_msg(rec_public_key, msg);
  	  				String encr_msg = Base64.getEncoder().encodeToString(msg_encr_bytes);
  	  				
  	  				System.out.println("File Name : "+file_name_of_msg);
  	  				System.out.println("sender email id : "+sender_email_id);
					
  	  				String encrypted_key = Base64.getEncoder().encodeToString(encryptedKey);
  	  				
  	  				File msg_file = make_msg_file(hash_email, sender_email_id, rec_email, encr_msg, file_name_of_msg, encrypted_key);
  	  				
  	  				boolean is_tonodeId_alive = false;
  	  				String toIp = "null";
  	  				
  	  				do
  	  				{	
  	  					String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_email);
  	  					System.out.println("next hop : "+tonodeId);
					
  	  					toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
  	  					System.out.println("next Ip : "+toIp);
  	  					
  	  					is_tonodeId_alive = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(toIp);
  	  					
  	  					if(is_tonodeId_alive)
	  					{
	  						OverlayManagementUtilityMethods.sendFileDirect(toIp, msg_file);
	  						
	  						sending_message = false;
	  						JFrame frame1 = new JFrame("Message");
	  						JOptionPane.showMessageDialog(frame1, "MESSAGE SENT TO "+rec_email);
	  					}
  	  				
  	  				}while(!is_tonodeId_alive);	
    		  	}	 
  	  			catch (IOException e) {
  	  				// TODO Auto-generated catch block
  	  				e.printStackTrace();
  	  			}
		  		
  	  		}
  	  		
  	  		else if (IndexManagement.EmailHashId_certificates.containsKey(hash_email))
  	  		{
  	  			String cert_string = IndexManagement.EmailHashId_certificates.get(hash_email);
	  			try 
	  			{
	  				X509Certificate[] certificate = ConvertStringCertToX509.convertToX509Certarray(cert_string);
	  				PublicKey rec_public_key = certificate[0].getPublicKey();
	  				
	  				byte[] msg_encr_bytes = encrypt_msg(rec_public_key, msg);
	  				String encr_msg = Base64.getEncoder().encodeToString(msg_encr_bytes);
	  				
	  				System.out.println("File Name : "+file_name_of_msg);
	  				System.out.println("sender email id : "+sender_email_id);
				
	  				String encrypted_key = Base64.getEncoder().encodeToString(encryptedKey);
	  				
	  				File msg_file = make_msg_file(hash_email, sender_email_id, rec_email, encr_msg, file_name_of_msg, encrypted_key);
	  				
	  				boolean is_tonodeId_alive = false;
	  				String toIp = "null";
	  				
	  				do
	  				{	
	  					String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_email);
	  					System.out.println("next hop : "+tonodeId);
				
	  					toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
	  					System.out.println("next Ip : "+toIp);
	  					
	  					is_tonodeId_alive = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(toIp);
	  					
	  					if(is_tonodeId_alive)
  					{
  						OverlayManagementUtilityMethods.sendFileDirect(toIp, msg_file);
  						
  						sending_message = false;
  						JFrame frame1 = new JFrame("Message");
  						JOptionPane.showMessageDialog(frame1, "MESSAGE SENT TO "+rec_email);
  					}
	  				
	  				}while(!is_tonodeId_alive);	
		  		}	 
	  			catch (IOException e) {
	  				// TODO Auto-generated catch block
	  				e.printStackTrace();
	  			}
  	  		}
		  	
  	  		else
  	  		{
  	  			String inter_ip = "SendCert";
  	  			
  	  			IndexManagement.searchReply_cert.put(hash_email,"null");
		  	
  	  			String my_node_id = OverlayManagement.myNodeId;
  	  			String my_ip = PresentIP.MyPresentIP();
  			          			          			
  	  			File searchQuery = createXmlSearchQuery(hash_email, inter_ip, my_node_id, my_ip);
      
  	  			String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_email);
				System.out.println("next hop : "+tonodeId);
			
				String toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
				System.out.println("next Ip : "+toIp);
												             
  	  			OverlayManagementUtilityMethods.sendFileDirect(toIp,searchQuery);
		  		
  	  			System.out.println("search query sent");
  	  			
  	  			timer(hash_email, sender_email_id, rec_email, msg, file_name_of_msg);
  	  		}
  	  	}
	}
	
	public static File createXmlSearchQuery(String key, String string, String selfNodeId, String selfIp) 
	{
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
		
		    String tagvalue = "1016"; // use random index to store corresponding string value in another string
		
		    SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index
		
		    ((Element) codeele).setAttribute("tag", tagvalue);
		
		    Text t1 = doc.createTextNode(key);
		    Text t2 = doc.createTextNode("SearchQuery");
		   	
		    Text t3 = doc.createTextNode(selfNodeId);
		    Text t4 = doc.createTextNode(selfIp);
		    String selfPortNo = "2222";
		    Text t5 = doc.createTextNode(selfPortNo);
		    Text t6 = doc.createTextNode(string);
		
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

	public static void timer(String hash_rec_email, String email, String rec_email, String text, String file_name_of_msg1)
	 {
	   	Timer time_set = new Timer();
		TimerTask task_timer = new TimerTask()
		{
			@Override
	        public void run()
	        {
				System.out.println("checking search reply");
					
				if(IndexManagement.searchReply_cert.get(hash_rec_email).equals("null"))
				{
					SysOutCtrl.SysoutSet("please wait");
					System.out.println("searchReply: "+IndexManagement.searchReply_cert);
					
					JFrame frame1 = new JFrame("Message");
					JOptionPane.showMessageDialog(frame1, "Could Not Send Message to "+rec_email+". Please Try Again!");
				}
						
				else if(IndexManagement.searchReply_cert.get(hash_rec_email).equals("NotFound"))
				{
					JFrame frame1 = new JFrame("Message");
					JOptionPane.showMessageDialog(frame1, rec_email+" is not part of Brahspati Network. We Can Not Send this Message. ");
				}
  				
				else
				{
					String cert_string = IndexManagement.emailIdHash_cert.get(hash_rec_email);
					try 
					{
						System.out.println("cert : "+cert_string);
						X509Certificate[] certificate = ConvertStringCertToX509.convertToX509Certarray(cert_string);
						PublicKey rec_public_key = certificate[0].getPublicKey();
							
						byte[] msg_encr_bytes = encrypt_msg(rec_public_key, text);
						String encr_msg = Base64.getEncoder().encodeToString(msg_encr_bytes);
	    						
						System.out.println("File Name : "+file_name_of_msg1);
						System.out.println("sender email id : "+email);
  	  					
						String encrypted_key = Base64.getEncoder().encodeToString(encryptedKey);
					
						File msg_file = make_msg_file(hash_rec_email, email, rec_email, encr_msg, file_name_of_msg1, encrypted_key);
					
						boolean is_tonodeId_alive = false;
	  	  				String toIp = "null";
	  	  				
	  	  				do
	  	  				{	
	  	  					String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(hash_rec_email);
	  	  					System.out.println("next hop : "+tonodeId);
						
	  	  					toIp = CommunicationUtilityMethods.getIpFromMyIpTable(tonodeId);
	  	  					System.out.println("next Ip : "+toIp);
	  	  					
	  	  					is_tonodeId_alive = CommunicationUtilityMethods.IsApplicationAlive_AtReceiver(toIp);
	  	  					
	  	  					if(is_tonodeId_alive)
	  	  					{
	  	  						OverlayManagementUtilityMethods.sendFileDirect(toIp, msg_file);
	  	  						
	  	  						sending_message = false;
	  	  					
	  	  						JFrame frame1 = new JFrame("Message");
	  	  						JOptionPane.showMessageDialog(frame1, "MESSAGE SENT TO "+rec_email);
	  	  					}
	  	  				
	  	  				}while(!is_tonodeId_alive);
    		  		
					/*	OverlayManagementUtilityMethods.sendFileDirect(toIp, msg_file);
  	  					
						JFrame frame1 = new JFrame("Message");
						JOptionPane.showMessageDialog(frame1, "MESSAGE SENT TO "+rec_email);*/
					} 
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	        }
	     };

	     time_set.schedule(task_timer, 30000);
	}
	
	public static byte[] byteCipherText = null;
	public static byte[] encryptedKey = null;
	        
	public static byte[] encrypt_msg(PublicKey publicKey, String msg)
	{
		try 
		{   		
			// chose a Character random from this String 
			String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
					+ "0123456789"
					+ "abcdefghijklmnopqrstuvxyz"; 
	      
			// create StringBuffer size of AlphaNumericString 
			StringBuilder sb = new StringBuilder(16); 
	      
			for (int i = 0; i <16; i++)
			{ 
	      
				// generate a random number between 
				// 0 to AlphaNumericString variable length 
				int index = (int)(AlphaNumericString.length()* Math.random()); 
				
				// add Character one by one in end of sb 
				sb.append(AlphaNumericString.charAt(index)); 
	         } 
	      
			String key = sb.toString(); 
	    		
			byte[] raw = key.getBytes(Charset.forName("UTF-8"));
			
			if (raw.length != 16)
			{
				throw new IllegalArgumentException("Invalid key size.");
			}

			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
			byteCipherText = cipher.doFinal(msg.getBytes(Charset.forName("UTF-8")));
	    	    
			Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher1.init(Cipher.PUBLIC_KEY, publicKey);
			encryptedKey = cipher1.doFinal(raw);
	 
	    }
		catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException e5) {
			// TODO Auto-generated catch block
			e5.printStackTrace();
		} catch (InvalidKeyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return byteCipherText;
	}
	    
	public static File make_msg_file(String hash_id, String email, String rec_email, String text, String file_name_of_msg, String encr_key )
	{
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
		Element keyele = doc.createElement("key");
	        
		String tagvalue = "1011";
	        
		((Element) codeele).setAttribute("tag", tagvalue);

		Text t1 = doc.createTextNode(hash_id);
		
		Text t2 = doc.createTextNode(text);
		String selfNodeId = OverlayManagement.myNodeId;
		
		Text t3 = doc.createTextNode(selfNodeId);
		String selfIp = PresentIP.MyPresentIP();
		
		Text t4 = doc.createTextNode(selfIp);
		String selfPortNo = "2222"+file_name_of_msg+email;
		Text t5 = doc.createTextNode(selfPortNo);
		Text t6 = doc.createTextNode(rec_email);
		Text t7 = doc.createTextNode(encr_key );
		
		hashidele.appendChild(t1);
		tonodeidele.appendChild(t2);
		selfnodeidele.appendChild(t3);
		ipele.appendChild(t4);
		portele.appendChild(t5);
		intermediate_ipele.appendChild(t6);
		keyele.appendChild(t7);
		
		codeele.appendChild(hashidele);
		codeele.appendChild(tonodeidele);
		codeele.appendChild(selfnodeidele);
		codeele.appendChild(ipele);
		codeele.appendChild(portele);
		codeele.appendChild(intermediate_ipele);
		codeele.appendChild(keyele);

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
			result = new StreamResult(new FileOutputStream("sms_query.xml"));
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
	               
	    return (new File("sms_query.xml"));
	}    
	
	public static void search_cert_replyXml(String hash_id, String Ip, String cert, String selfNodeId, String selfIp, String email_hash)
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

         String tagvalue = "1017"; // use random index to store corresponding string value in another string

         SysOutCtrl.SysoutSet("tag selected: " + tagvalue,2); // prints out the value at the randomly selected index

         ((Element) codeele).setAttribute("tag", tagvalue);

         Text t1 = doc.createTextNode(hash_id);

         // next hop will be given by routing module
         
         Text t2 = doc.createTextNode(cert);

         // String selfNodeId1="aaaaa";

         Text t3 = doc.createTextNode(selfNodeId);

         Text t4 = doc.createTextNode(selfIp);

         Text t5 = doc.createTextNode(email_hash);

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
                 result = new StreamResult(new FileOutputStream("SearchQueryReplyCert.xml"));
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
         OverlayManagementUtilityMethods.sendFileDirect(Ip, new File("SearchQueryReplyCert.xml"));
  //       CommunicationManager.TransmittingBuffer.add( new File("SearchQueryReply.xml"));
  //       com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods.addQueryTransmittingBuffer(new File("SearchQueryReply.xml"));
         SysOutCtrl.SysoutSet("search query reply xml created and added to tx buffer",2);
         // return (new File("SearchQueryReply.xml"));
    }
	
	public static String readFileAsString(String filePath)
	{
	/*	String text = ""; 
		
		try 
		{ text = new String(Files.readAllBytes(Paths.get(filePath)));
		}
		catch (IOException e) 
		{
			e.printStackTrace(); 
		}
	
		return text;*/
		
		StringBuilder contentBuilder = new StringBuilder();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
	    {
	 
	        String sCurrentLine;
	        while ((sCurrentLine = br.readLine()) != null)
	        {
	            contentBuilder.append(sCurrentLine).append("\n");
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	    return contentBuilder.toString();
	}
	
	public static void empty_cache_folder()
	{
		String loc1_cache_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_cache_folder_path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_cache_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path loc_path = Paths.get(loc1_cache_folder);
		
		boolean check_dir;
		check_dir = Files.exists(loc_path);
		
		if (check_dir)
		{
			String fileSeparator = System.getProperty("file.separator");
			
			File file_n = new File(loc1_cache_folder);
			String[] files_n = file_n.list();
			
			for(String node_id : files_n)
			{
				String nodeid_folder_loc = loc1_cache_folder + fileSeparator + node_id;
						
				File file = new File(nodeid_folder_loc);
				String[] files = file.list();
				
				for(String rec_email : files)
				{
					String rec_email_folder = nodeid_folder_loc + fileSeparator + rec_email;
					
					File file1 = new File(rec_email_folder );
					String[] files1 = file1.list();
					
					for(String sender_email : files1)
					{
						String sender_email_folder = rec_email_folder + fileSeparator + sender_email;
						
						File file2 = new File(sender_email_folder );
						String[] files2 = file2.list();
						
						for(String msg_file : files2)
						{
							String msg_file_loc = sender_email_folder + fileSeparator + msg_file;
									
							Path path_msg_file_loc = Paths.get(msg_file_loc);
							try {
								System.out.println("delete this file");
								Files.delete(path_msg_file_loc);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						Path path_sender_email_folder = Paths.get(sender_email_folder);
						try {
							System.out.println("delete this file");
							Files.delete(path_sender_email_folder);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Path path_rec_email = Paths.get(rec_email_folder);
					try {
						System.out.println("delete this file");
						Files.delete(path_rec_email);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Path path_nodeid_folder_loc = Paths.get(nodeid_folder_loc);
				try {
					System.out.println("delete this file");
					Files.delete(path_nodeid_folder_loc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void empty_rec_folder()
	{
		String self_email_id = emailid.getemaild();
		String loc1_rec_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_rec_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Path loc_path = Paths.get(loc1_rec_folder);
		
		boolean check_dir;
		check_dir = Files.exists(loc_path);
		
		if (check_dir)
		{
			String fileSeparator = System.getProperty("file.separator");
			
			File file_n = new File(loc1_rec_folder);
			String[] files_n = file_n.list();
			
			for(String rec_folder : files_n)
			{
				if(!self_email_id.equals(rec_folder))
				{	
					String rec_folder_loc = loc1_rec_folder + fileSeparator + rec_folder;
						
					File file = new File(rec_folder_loc);
					String[] files = file.list();
				
					for(String sender_email : files)
					{
						String sender_email_folder = rec_folder_loc + fileSeparator + sender_email;
					
						File file1 = new File(sender_email_folder );
						String[] files1 = file1.list();
					
						for(String msg_file : files1)
						{
							String msg_file_loc = sender_email_folder + fileSeparator + msg_file ;
																	
							Path path_msg_file_loc = Paths.get(msg_file_loc);
							try {
								System.out.println("delete this file");
								Files.delete(path_msg_file_loc);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						Path path_sender_email_folder = Paths.get(sender_email_folder);
						try {
							System.out.println("delete this file");
							Files.delete(path_sender_email_folder);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					Path path_rec_folder_loc = Paths.get(rec_folder_loc);
					try {
						System.out.println("delete this file");
						Files.delete(path_rec_folder_loc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}	
			}
		}
	}
}
