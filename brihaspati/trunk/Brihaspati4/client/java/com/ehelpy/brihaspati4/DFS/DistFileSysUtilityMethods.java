package com.ehelpy.brihaspati4.DFS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import java.lang.Integer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
//import com.sun.glass.events.WindowEvent;
import com.ehelpy.brihaspati4.voip.B4services;

//import sun.security.mscapi.KeyStore.MY;

//import sun.security.mscapi.KeyStore.MY;

import com.ehelpy.brihaspati4.comnmgr.*;
import com.ehelpy.brihaspati4.indexmanager.SHA1;

public class DistFileSysUtilityMethods extends DistFileSys {

	static String myIp = PresentIP.MyPresentIP();
	static String myNodeId = OverlayManagement.myNodeId;

	public static void createDirectory(Path path) {

		// Path path = Paths.get("C:\\shared
		// Folder\\abhsingh@iitk.ac.in:\\Pub\\file.jpg");
		// Path path1 = Paths.get("C:\\DirectoryP2P\\Cache");
		// if directory exists?
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				// fail to create directory
				e.printStackTrace();
			}
		}

	}

	public static void replySpaceQueryMessage(String asker, String messageId, int remNodes, String askerIp,String direction)
			throws ParserConfigurationException, FileNotFoundException, TransformerException {

		SysOutCtrl.SysoutSet(" Reply ofSpace query msg for node id " + asker);

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = f.newDocumentBuilder();
		Document doc = db.newDocument();

		Element rootele = doc.createElement("replySpaceQueryMessage");
		doc.appendChild(rootele);
		Element tagidele = doc.createElement("Query_");

		String tagvalue = "0111";
		SysOutCtrl.SysoutSet("Tagvalue for Reply Space Query: " + tagvalue);

		((Element) tagidele).setAttribute("tag", tagvalue);
		rootele.appendChild(tagidele);

		Element hashidele = doc.createElement("to_hash_id");
		Element tonodeidele = doc.createElement("to_node_id");
		Element selfnodeidele = doc.createElement("self_node_id");
		Element ipele = doc.createElement("self_ip_address");
		Element portele = doc.createElement("self_port_no");
		Element interipele= doc.createElement("inter_ip");

		Text t0 = doc.createTextNode(asker);
		Text t1 = doc.createTextNode(messageId);
		Text t2 = doc.createTextNode(myNodeId);
		Text t3 = doc.createTextNode(myIp);
		Text t4 = doc.createTextNode(Integer.toString(remNodes));
		//remNodes can have only three options 2,1,0
		//if 2 then reply is for the originator
		//if 1,0 then reply is for the root node
		Text t5 = doc.createTextNode(direction);

		hashidele.appendChild(t0);
		tonodeidele.appendChild(t1);
		selfnodeidele.appendChild(t2);
		ipele.appendChild(t3);
		portele.appendChild(t4);
		interipele.appendChild(t5);

		tagidele.appendChild(hashidele);
		tagidele.appendChild(tonodeidele);
		tagidele.appendChild(selfnodeidele);
		tagidele.appendChild(ipele);
		tagidele.appendChild(portele);
		tagidele.appendChild(interipele);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new FileOutputStream("replySpaceQueryMessage.xml"));
		try {
			transformer.transform(source, result);
		} catch (NullPointerException e) {
		}
		File file = new File("replySpaceQueryMessage.xml");
		SysOutCtrl.SysoutSet("replySpaceQueryMessage generated");
		OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
		SysOutCtrl.SysoutSet("replySpaceQueryMessage has been sent to the node");

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void forwardSpaceQueryMessage(String toNodeId, String messageId, String asker, String askerIp,
			int remNodes,String direction) throws ParserConfigurationException, FileNotFoundException, TransformerException {

		SysOutCtrl.SysoutSet("Space query msg for Successor node id by " + asker);

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = f.newDocumentBuilder();
		Document doc = db.newDocument();

		Element rootele = doc.createElement("spaceQueryMessage");
		doc.appendChild(rootele);
		Element tagidele = doc.createElement("Query_");

		String tagvalue = "0110";
		SysOutCtrl.SysoutSet("Tagvalue for Space Query: " + tagvalue);

		((Element) tagidele).setAttribute("tag", tagvalue);
		rootele.appendChild(tagidele);

		Element hashidele = doc.createElement("to_hash_id");
		Element tonodeidele = doc.createElement("to_node_id");
		Element selfnodeidele = doc.createElement("self_node_id");
		Element ipele = doc.createElement("self_ip_address");
		Element portele = doc.createElement("self_port_no");
		Element interipele= doc.createElement("inter_ip");

		Text t0 = doc.createTextNode(toNodeId);
		Text t1 = doc.createTextNode(messageId);
		Text t2 = doc.createTextNode(asker);
		Text t3 = doc.createTextNode(askerIp);
		Text t4 = doc.createTextNode(Integer.toString(remNodes));
		Text t5 = doc.createTextNode(direction);
		
		hashidele.appendChild(t0);
		tonodeidele.appendChild(t1);
		selfnodeidele.appendChild(t2);
		ipele.appendChild(t3);
		portele.appendChild(t4);
		interipele.appendChild(t5);

		tagidele.appendChild(hashidele);
		tagidele.appendChild(tonodeidele);
		tagidele.appendChild(selfnodeidele);
		tagidele.appendChild(ipele);
		tagidele.appendChild(portele);
		tagidele.appendChild(interipele);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new FileOutputStream("spaceQueryMessage.xml"));
		try {
			transformer.transform(source, result);
		} catch (NullPointerException e) {
		}
		File file = new File("spaceQueryMessage.xml");
		SysOutCtrl.SysoutSet("spaceQueryMessage generated");
		CommunicationManager.TransmittingBuffer.add(file);
		//OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
		SysOutCtrl.SysoutSet("spaceQueryMessage has been sent to the node");

	}

	/////////////////////////////////////////////////////////////////////////////////////////////
	public static void splitFile(File fileName) {
		long d = fileName.length();
		int noOfChunkBytes = ((int) ((d / 1024) / 512)) + 1;
		System.out.println("total no of bytes  " + d);
		System.out.println(noOfChunkBytes);
		try {
			FileInputStream fis = new FileInputStream(fileName);
			byte data[] = new byte[(int) fileName.length()];
			int off = 0;
			int len;
			Integer i = new Integer(0);
			for (i = 0; i < noOfChunkBytes; i++) {
				if ((fileName.length() - (i * 512 * 1024)) < (512 * 1024)) {
					len = (int) fileName.length() - (i * 512 * 1024);
				} else {
					len = 512 * 1024;
				}
				System.out.println("noOfChunkBytes");
				fis.read(data, off, len);
				FileOutputStream fos = new FileOutputStream(
						new File(((fileName.toString()).concat("Chunk")).concat(i.toString())));
				fos.write(data, off, len);
				off = off + 512;
				fos.flush();
				fos.close();
			}
			fis.close();
		} catch (Exception e) {
			System.out.println("no file found");
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void mergeFiles(String Pathname,int No_Of_Chunks) throws InterruptedException 
	{
		// this method stitch the downloaded chunks 
		// Pathname is the name of file which we want to reconstruct
		// Important this directory may also contain other files ,therefore cannot stitch everything
		File file = new File(Pathname);
		//String name=file.getName();
		File fetchDir = file.getParentFile();
		System.out.println("fetchDir  "+fetchDir);
		System.out.println("name..  "+Pathname);
		FileInputStream fis = null;
		FileOutputStream fos;
		System.out.println("stepdone");
		File fileset[] = fetchDir.listFiles();
		System.out.println("fileset[]   "+ fileset.length);
		

		try {
			Collection<File> fileTobeCleared=new ArrayList<File>();//a list of chunks which are required to be deleted after stitching
			fos = new FileOutputStream(new File(Pathname), true);
			for(Integer i=0;i<No_Of_Chunks;i++)
			{
				for (File list : fileset) 
				{
					
					if(list.toString().equals(Pathname.concat("Chunk").concat(i.toString())))
					{
						System.out.println("file details   "+list);
						fis = new FileInputStream(list);
						byte b[] = new byte[(int) list.length()];
						fis.read(b, 0, (int) list.length());
						fos.write(b);
						fis.close();
						fileTobeCleared.add(list);
						
					}
				}
			}
			fos.flush();			
			for(File list:fileTobeCleared)
			{list.delete();}
			System.out.println("stepdone e e  " );

		} catch (IOException e) {
			System.out.println("hahahaha");
			e.printStackTrace();
		}
		
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////
	public static String getMetaFileName(File fileName) {
		String path = fileName.toString();
		Integer index = path.lastIndexOf('.');
		if (!index.equals(-1)) {
			path = path.substring(0, index);
			return path.concat("metadata.txt");
		} else {
			return path.concat("metadata.txt");
		}

	}

////////////////////////////////////////////////////////////////////////////////////////////////////
	public static File fetchFile(String toNodeId, String chunkName, String selfNodeId, String myIp)
			throws ParserConfigurationException, TransformerException, FileNotFoundException {
		SysOutCtrl.SysoutSet("fetch file msg for fetching the file " + selfNodeId);

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = f.newDocumentBuilder();
		Document doc = db.newDocument();

		Element rootele = doc.createElement("FetchFileMessage");
		doc.appendChild(rootele);
		Element tagidele = doc.createElement("Query_");

		String tagvalue = "0112";
		SysOutCtrl.SysoutSet("Tagvalue for FetchFile Query: " + tagvalue);

		((Element) tagidele).setAttribute("tag", tagvalue);
		rootele.appendChild(tagidele);

		Element hashidele = doc.createElement("to_hash_id");
		Element tonodeidele = doc.createElement("to_node_id");
		Element selfnodeidele = doc.createElement("self_node_id");
		Element ipele = doc.createElement("self_ip_address");
		Element portele = doc.createElement("self_port_no");

		boolean replyRecdOfFetchMsg = false;

		Text t0 = doc.createTextNode(toNodeId);
		Text t1 = doc.createTextNode(chunkName);
		Text t2 = doc.createTextNode(selfNodeId);
		Text t3 = doc.createTextNode(myIp);
		Text t4 = doc.createTextNode("false");

		hashidele.appendChild(t0);
		tonodeidele.appendChild(t1);
		selfnodeidele.appendChild(t2);
		ipele.appendChild(t3);
		portele.appendChild(t4);

		tagidele.appendChild(hashidele);
		tagidele.appendChild(tonodeidele);
		tagidele.appendChild(selfnodeidele);
		tagidele.appendChild(ipele);
		tagidele.appendChild(portele);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new FileOutputStream("fetchFileMessage.xml"));
		try {
			transformer.transform(source, result);
		} catch (NullPointerException e) {
		}
		File file = new File("fetchFileMessage.xml");
		SysOutCtrl.SysoutSet("fetchFileMessage generated");
		CommunicationManager.TransmittingBuffer.add(file);// Doubt regarding a new transmitting buffer to be created
		SysOutCtrl.SysoutSet("fetchFileMessage has been sent to the node");
		return file;

	}

	/////////////////////////////////////////////////////////////////////////////////////////////

	public static void fetchFileReply(String toNodeid, String fileName,String value)
			throws ParserConfigurationException, TransformerException, FileNotFoundException {
		SysOutCtrl.SysoutSet("fetch file reply msg for fetching the file by " + myNodeId);

		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = f.newDocumentBuilder();
		Document doc = db.newDocument();

		Element rootele = doc.createElement("FetchFileReplyMessage");
		doc.appendChild(rootele);
		Element tagidele = doc.createElement("Query_");

		String tagvalue = "0113";
		SysOutCtrl.SysoutSet("Tagvalue for FetchFile Query: " + tagvalue);

		((Element) tagidele).setAttribute("tag", tagvalue);
		rootele.appendChild(tagidele);

		Element hashidele = doc.createElement("to_hash_id");
		Element tonodeidele = doc.createElement("to_node_id");
		Element selfnodeidele = doc.createElement("self_node_id");
		Element ipele = doc.createElement("self_ip_address");
		Element portele = doc.createElement("self_port_no");

		boolean FetchMsgRecd = true;
		// String value="found";//it may be not found

		Text t0 = doc.createTextNode(toNodeid);// determines whether the chunk is present or not
		Text t1 = doc.createTextNode(fileName);// the nodeid from 0112 query
		Text t2 = doc.createTextNode(myNodeId);
		Text t3 = doc.createTextNode(myIp);
		Text t4 = doc.createTextNode(value);// depicts that the reply of fetch msg is being send

		hashidele.appendChild(t0);
		tonodeidele.appendChild(t1);
		selfnodeidele.appendChild(t2);
		ipele.appendChild(t3);
		portele.appendChild(t4);

		tagidele.appendChild(hashidele);
		tagidele.appendChild(tonodeidele);
		tagidele.appendChild(selfnodeidele);
		tagidele.appendChild(ipele);
		tagidele.appendChild(portele);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(new FileOutputStream("fetchFileReplyMessage.xml"));
		try {
			transformer.transform(source, result);
		} catch (NullPointerException e) {
		}
		File file = new File("fetchFileReplyMessage.xml");
		SysOutCtrl.SysoutSet("fetchFileReplyMessage generated");
		CommunicationManager.TransmittingBuffer.add(file);
		SysOutCtrl.SysoutSet("fetchFileReplyMessage has been sent to the node");
		// return file;

	}

	//////////////////////////////////////////////////////////////////////////////////
	public static Object mergeFilesHashLinkedList(File output) {
		Map<String, File> fileMap = new LinkedHashMap<String, File>();

		for (Integer i = 0; i <= 2; i++) {
			File fileName = new File(("F:/New Folder/receipts.pptx".concat("chunk")).concat(i.toString()));
			fileMap.put(("F:/New Folder/receipts.pptx".concat("chunk")).concat(i.toString()), fileName);
			System.out.println(fileMap);
		}

		FileInputStream fis;
		FileOutputStream fos;

		System.out.println("stepdone");
		try {

			fos = new FileOutputStream(output, true);
			for (String list : fileMap.keySet()) {
				fis = new FileInputStream(fileMap.get(list));

				byte b[] = new byte[(int) fileMap.get(list).length()];
				fis.read(b, 0, (int) fileMap.get(list).length());
				System.out.println((int) fileMap.get(list).length());
				fos.write(b);
				fos.flush();
				fis.close();
				int j = 0;
				j++;
				System.out.println("stepdone e e  " + j);

			}

		} catch (IOException e) {
			System.out.println("hahahaha");
			e.printStackTrace();
		}

		return null;
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static long folderSize(String Path) {
		File file=new File(Path);
		long Spacesize=file.getTotalSpace();
		//long Spacesize = FileUtils.sizeOfDirectory(new File(Path));

		System.out.println("Folder Size: " + Spacesize + " bytes");
		return Spacesize;
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Algorithm 4.7 File creation and upload procedure FileCreate(FileName;
// FileData)
	public static void fileUploading_DHT()
			throws ParserConfigurationException, TransformerException, IOException 
	{
		 JFileChooser f = new JFileChooser();
		 f.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		 f.setDialogTitle("CHOOSE FILE TO SAVE IN DFS");
		 f.showSaveDialog(null);
		 System.out.println("Selected File in DFS  "+f.getSelectedFile());
		 File fileName=f.getSelectedFile();
		 System.out.println("why notttttt11111111111111");	
		 B4services.ss.close();
			B4services.service();
		 
		Properties prop = new Properties();

		String UID = null;
		String chunkName = null;
		
		String sha1 = "";
		long d = fileName.length(); // here we r just seeing the size of the file
		// if the given file name is Directory then the length is unspecified which is
		// good as i do not want to make chunks of folder
		System.out.println("total Size of the File in KiloByte   " + (d / 1024));
		int noOfChunkBytes = ((int) (((d-1) / 1024) / 512)) + 1;

		DistFileSysUtilityMethods.splitFile(fileName);//this split the file and saves it in same directory with suffix as chunks0,1,2
		
		DistFileSys.nodefilemap.put(fileName.toString(),noOfChunkBytes);// adding file to Main Linked List
		Save_Retrive_data_Structures.Save_nodefilemap();
		// String chunkHash =null;
		//LinkedHashMap<String, ArrayList<String>> fileFragMap = new LinkedHashMap<String, ArrayList<String>>();
		for (Integer i = 0; i < noOfChunkBytes; i++) {
			String Suffix =keyWord_From_User();// taking the unique suffix from user
			chunkName = ((fileName.toString()).concat("Chunk")).concat(i.toString());
			String CN0=chunkName.concat(Suffix).concat("0");
			String CN1=chunkName.concat(Suffix).concat("1");
			String UID0 = SHA1.getSha1(CN0);
			String UID1 = SHA1.getSha1(CN1);
			int remNodes=3;
			DistFileSysUtilityMethods.forwardSpaceQueryMessage(UID0, chunkName, myNodeId, PresentIP.MyPresentIP(),
					remNodes,"1");
			DistFileSysUtilityMethods.forwardSpaceQueryMessage( UID1,chunkName, myNodeId, PresentIP.MyPresentIP(),
					remNodes,"2");
			String[] IpNode = new String[20];
			
			
			nodeFileChunkMap.put(chunkName, IpNode);
			//Save_Retrive_data_Structures.Save_nodefilemap();
		}
		
		String name=fileName.getName();
		File fetchDir = fileName.getParentFile();
		System.out.println("fetchDir  "+fetchDir);
		System.out.println("stepdone");
		File fileset[] = fetchDir.listFiles();
		

		Collection<File> fileTobeCleared=new ArrayList<File>();//a list of chunks which are required to be deleted after stitching
		//String prefix=DistFileSysUtilityMethods.choose_WhereToSave();
		
		for(Integer i=0;i<noOfChunkBytes;i++)
		{
			for (File list : fileset) 
			{
				
				if(list.toString().equals(name.concat("Chunk").concat(i.toString())))
				{
					System.out.println(list);
					fileTobeCleared.add(list);
					
				}
			}
		}
					
		for(File list:fileTobeCleared)
		{list.deleteOnExit();}
		System.out.println("stepdone e e  " );
		

	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean checkSpace( String messageID,String asker, String askerIp, String remnodes,String direction)
			throws ParserConfigurationException, TransformerException, IOException 
	{
		{
			int remNodes=Integer.parseInt(remnodes);
			//long spaceConsumed = folderSize(choose_WhereToSave());
			File file1 =new File(choose_WhereToSave());
			long spaceAvailable=file1.getUsableSpace();// how much space is available on this platform
			
			System.out.println("spaceAvailable  "+spaceAvailable);
			boolean replySend;
			if ( spaceAvailable>(512*1024)) 
			{
				DistFileSysUtilityMethods.replySpaceQueryMessage(asker,messageID , remNodes, askerIp,direction);
				replySend = true;
				System.out.println("checkspace sucess  ");
				//different 0110 packets have different values at different instance .plz make a diagram to analyze it.
				switch(remNodes)
				{
				case 3:
					remNodes=remNodes-1;//2
					
					direction=asker+askerIp;//changing the field 6 in the further 0110 msg(originatorNodeId and OriginatorIp) 
					DistFileSysUtilityMethods.forwardSpaceQueryMessage(	PredecessorSuccessor.mySuccessors[0],
							messageID,myNodeId,myIp,remNodes,direction);
					break;
				case 2:
					remNodes=remNodes-1;//1
					direction=asker+askerIp;//changing the field 6 in the further 0110 msg(originatorNodeId and OriginatorIp) 					
					DistFileSysUtilityMethods.forwardSpaceQueryMessage(	PredecessorSuccessor.mySuccessors[0],
							messageID, asker, askerIp, remNodes,direction);
					break;
				case 1:
					remNodes=remNodes-1;//now the msg need not go anywhere
					break;
				}
				

			} else 
			{
				replySend = false;
				switch(remNodes)
				{
				case 3:
					DistFileSysUtilityMethods.forwardSpaceQueryMessage(
							PredecessorSuccessor.mySuccessors[0],messageID,
							asker, askerIp,remNodes,direction);
					
					break;
				case 2:
					DistFileSysUtilityMethods.forwardSpaceQueryMessage(
							PredecessorSuccessor.mySuccessors[0],messageID,myNodeId,myIp, remNodes,direction);
					
					direction=asker+askerIp;//changing the field 6 in the further 0110 msg(originatorNodeId and OriginatorIp) 					
					
					break;
				case 1:
					DistFileSysUtilityMethods.forwardSpaceQueryMessage(
							PredecessorSuccessor.mySuccessors[0],messageID, asker, askerIp, remNodes,direction);
					break;
				}
				
				
			}
			return replySend;
		}
	}

//////////////////////////////////////////////////////////////////////////////////////////////////
	static Boolean Search_File = new Boolean(false);

	public static boolean searchFile(String fileName) throws IOException {
		LinkedHashMap<String,String> search_File = new LinkedHashMap<>();
		String prefix=choose_WhereToSave()+"\\";
		File f = new File(prefix + fileName.substring(0, fileName.lastIndexOf('\\')));
		System.out.println(f);

		File matchingFiles[] = f.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) 
			{
				System.out.println("name  " + name);
				System.out.println(dir);
				String lk = fileName.substring(fileName.lastIndexOf('\\') + 1);
				System.out.println("File name  " + lk);
				Boolean Search1_File = new Boolean(false);
				Search1_File = name.startsWith(lk);// && name.endsWith("txt");
				// Search_File= name.startsWith("P");
				System.out.println(Search1_File);
				search_File.put(name, Search1_File.toString());
				return Search1_File; 
			}
		});
		String boolvalue=search_File.get(fileName.substring(fileName.lastIndexOf('\\') + 1));
		if (boolvalue.equals("true"))
		{
			Search_File=true;
			}
		else {Search_File=false;}
		
		return Search_File;
	}

///////////////////////////////////////////////////////////////////////////////////////
	 public static String choose_WhereToSave() throws IOException
	    {
		 BufferedReader BR;
			File fi = new File("Choose_WhereToSave.txt"); 
			fi.createNewFile();
			FileReader fr=new FileReader(fi);
			boolean bool=fr.ready();
			
	    	if(bool)
	    	
	    	{
	    		BR = new BufferedReader(new FileReader("Choose_WhereToSave.txt"));
	        	String prefix=BR.readLine();
	        	BR.close();
	    		return prefix;
	    	}
	    	else
	    	{
	    	
	        JFileChooser f = new JFileChooser();
	        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	        f.setDialogTitle("CHOOSE LOCATION TO SAVE DISTRIBUTED FILES OF SYSTEM");
	        f.showSaveDialog(null);

	        //System.out.println(f.getCurrentDirectory());
	        System.out.println(f.getSelectedFile());
	        File Prefix=f.getSelectedFile();
	        String prefix = Prefix.getPath();
	        System.out.println(prefix);
	        
	        FileWriter write = null;		
			try		
			{
				write = new FileWriter("Choose_WhereToSave.txt");
			}
			catch (IOException e1) 
			{			e1.printStackTrace();		}
			PrintWriter wr = new PrintWriter(write);
			wr.write(prefix);
			System.out.println("Prefix written to Choose_WhereToSave.txt");
			wr.flush();
			return prefix;
	    	}
	    }   
//////////////////////////////////////////////////////////////////////////////////////////////////////
	 public static String keyWord_From_User() throws IOException
	    {
		 	BufferedReader BR;
			File fi = new File("DFS_keyWord_From_User.txt"); 
			fi.createNewFile();
			FileReader fr=new FileReader(fi);
			boolean bool=fr.ready();
			System.out.println("detrmining the the suffix");
			
	    	if(bool)
	    	
	    	{
	    		BR = new BufferedReader(new FileReader("DFS_keyWord_From_User.txt"));
	        	String prefix=BR.readLine();
	        	BR.close();
	    		return prefix;
	    	}
	    	else
	    	{
	    		String input = null;
	    	    JTextField textField = new JTextField();
	    	    textField.setColumns(50);
	    	
	    	    textField.setVisible(true);
	    	
	    	
	    	    JFrame frame = new JFrame("ENTER THE SUFFIX TO ALL FILE EXTENSIONS(UNIQUE)");
	    	    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	    	    frame.setSize(500, 300);
	    	    frame.add(textField);
	    	    frame.setVisible(true);
	    	    frame.requestFocus();
	    	    frame.addWindowListener(null);
	    	
	    	
	    	    textField.addActionListener(new ActionListener()
	    	    {
	               public void actionPerformed(ActionEvent ae)
	               {
	                  String text = textField.getText();

	                  System.out.println(text);
	                  FileWriter write = null;		
	  				try		
	  				{
	  					write = new FileWriter("DFS_keyWord_From_User.txt");
	  				}
	  				catch (IOException e1) 
	  				{			e1.printStackTrace();		}
	  				PrintWriter wr = new PrintWriter(write);
	  				wr.write(text);
	  				System.out.println("Prefix written to keyWord_From_User_DFS.txt");
	  				wr.flush();
	  				frame.dispose();	  				
	               }
	            });
	    	    
	    	    
	    	    BR = new BufferedReader(new FileReader("keyWord_From_User_DFS.txt"));
	        	String prefix=BR.readLine();
	        	BR.close();
	    		return prefix;
	    	}
	    }   
/////////////////////////////////////////////////////////////////////////////////////////////////
	 public static void Application_Alive_Response_ByRecieverSide()
	    {
	    	
	    	ServerSocket servsock = null;
			
			try {
				servsock = new ServerSocket(8888);
			} catch (IOException e1) {
				
				e1.printStackTrace();
			}
			
		    Socket sock = null;
			try {
				sock = servsock.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}

	        if (!sock.isConnected())
	          	System.out.println("Client Socket not Connected...");
	        else {
	            System.out.println("Client Socket Connected : " + sock);
	        }	
	              
	        try {
				servsock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
	    public static boolean IsApplicationAlive_AtReceivingSide(String hostname)
	    {
	    	boolean IsAlive = false;
	    	int port = 4444;
	        	
	    	if(!hostname.equals(null)||!hostname.isEmpty()||!hostname.equals("null")||!hostname.equals("")||!hostname.equals(" "))
	    	{	
	    		SocketAddress sock_address = new InetSocketAddress(hostname, port);
	    		Socket sock = new Socket();
	    		int timeout = 5000;
	    	
	    		try {
	    			sock.connect(sock_address, timeout);
	    			sock.close();
	    			IsAlive = true;
	    		} catch (SocketTimeoutException exception) {
	    			// TODO Auto-generated catch block
	    			System.out.println("socket timeout exception");
	    		}catch(IOException exception) {
	    			System.out.println("IO Exception");
	    		}
	    	}	
	    	return IsAlive;
	    	
	    }
	    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	    
	    public static void check_Alive_Status() throws ParserConfigurationException, FileNotFoundException, TransformerException
	    {
	    	
	    	Set<String> fileNames_root=root_Fileinfo_Map.keySet();
	    	for (String filename :fileNames_root)
	    	{
	    		String[] root_ReplNode_Array = new String[20];
	    		root_ReplNode_Array=root_Fileinfo_Map.get(filename);
	    		String ip1=root_ReplNode_Array[1];
	    		String ip2=root_ReplNode_Array[3];
	    		boolean bool1=IsApplicationAlive_AtReceivingSide(ip1);
	    		boolean bool2=IsApplicationAlive_AtReceivingSide(ip2);
	    		
	    		if (!bool1)//the Node1 in repl set is not alive
	    		{
	    			String rootNodeid=myNodeId;
	    			String rootIp=myIp;
	    			String originatorInfo=root_ReplNode_Array[0]+root_ReplNode_Array[1];//nodeid + ipadd of originator
	    			String otherNode_ofReplSet_Info=root_ReplNode_Array[4]+"node1";//node id of node2
	    			include_NewNode_ReplicationSet(root_ReplNode_Array[2],filename,rootNodeid,
	    					rootIp,originatorInfo,otherNode_ofReplSet_Info);
	    		}
	    		if (!bool2)//the Node2 in repl set is not alive
	    		{
	    			String rootNodeid=myNodeId;
	    			String rootIp=myIp;
	    			String originatorInfo=root_ReplNode_Array[0]+root_ReplNode_Array[1];//nodeid + ipadd of originator
	    			String otherNode_ofReplSet_Info=root_ReplNode_Array[2]+"node2";//node id of node2
	    			include_NewNode_ReplicationSet(root_ReplNode_Array[4],filename,rootNodeid,
	    					rootIp,originatorInfo,otherNode_ofReplSet_Info);
	    		}
	    	}
	    	Set<String> fileNames_Shared=shared_Fileinfo_Map.keySet();
	    	for (String filename :fileNames_Shared)
	    	{
	    		String[] shared_info_Array = new String[20];
	    		shared_info_Array=shared_Fileinfo_Map.get(filename);
	    		
	    		String ip_root=shared_info_Array[3];
	    		
	    		boolean bool1=IsApplicationAlive_AtReceivingSide(ip_root);
	    		
	    		if (!bool1)
	    		{
	    			// then assume that you are the root Node
	    			String rootNodeid=myNodeId;
	    			String rootIp=myIp;
	    			String originatorInfo=shared_info_Array[0]+shared_info_Array[1];//nodeid + ipadd of originator
	    			String otherNode_ofReplSet_Info=myNodeId+"node_all";// this replication set query need to go to both nodes
	    			
	    			include_NewNode_ReplicationSet(PredecessorSuccessor.mySuccessors[0],filename,
	    					rootNodeid,rootIp,originatorInfo, otherNode_ofReplSet_Info);
	    		}
	    		
	    	}
	    } 	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////    	
	    public static void include_NewNode_ReplicationSet(String filename,String toNodeId,String rootNodeid,
				String rootIp,String originatorInfo,String otherNode_ofReplSet_Info) throws ParserConfigurationException, FileNotFoundException, TransformerException
	    {
	    	SysOutCtrl.SysoutSet("Space query msg by root Node for replication set " + rootNodeid);

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = f.newDocumentBuilder();
			Document doc = db.newDocument();

			Element rootele = doc.createElement("ReplicationMsgByRootNode");
			doc.appendChild(rootele);
			Element tagidele = doc.createElement("Query_");

			String tagvalue = "R100";
			SysOutCtrl.SysoutSet("Tagvalue for Space Query: " + tagvalue);

			((Element) tagidele).setAttribute("tag", tagvalue);
			rootele.appendChild(tagidele);

			Element hashidele = doc.createElement("to_hash_id");
			Element tonodeidele = doc.createElement("to_node_id");
			Element selfnodeidele = doc.createElement("self_node_id");
			Element ipele = doc.createElement("self_ip_address");
			Element portele = doc.createElement("self_port_no");
			Element interipele= doc.createElement("inter_ip");

			Text t0 = doc.createTextNode(toNodeId);
			Text t1 = doc.createTextNode(filename);
			Text t2 = doc.createTextNode(rootNodeid);
			Text t3 = doc.createTextNode(rootIp);
			Text t4 = doc.createTextNode(originatorInfo);
			Text t5 = doc.createTextNode(otherNode_ofReplSet_Info);// this feild will have some time nodeid and 
			//also a number to denote(as to no of nodes this msg should go)
			
			
			hashidele.appendChild(t0);
			tonodeidele.appendChild(t1);
			selfnodeidele.appendChild(t2);
			ipele.appendChild(t3);
			portele.appendChild(t4);
			interipele.appendChild(t5);

			tagidele.appendChild(hashidele);
			tagidele.appendChild(tonodeidele);
			tagidele.appendChild(selfnodeidele);
			tagidele.appendChild(ipele);
			tagidele.appendChild(portele);
			tagidele.appendChild(interipele);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new FileOutputStream("Replication_msg_by_Rootode.xml"));
			try {
				transformer.transform(source, result);
			} catch (NullPointerException e) {
			}
			File file = new File("Replication_msg_by_Rootode.xml");
			SysOutCtrl.SysoutSet("Replication_msg_by_Rootode.xml generated");
			CommunicationManager.TransmittingBuffer.add(file);
			//OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
			SysOutCtrl.SysoutSet("Replication_msg_by_Rootode.xml has been sent to the node");

	    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    public static void reply_Replication_msg(String fileName, String rootNodeId,String myNodeId, String myIp,String nodevalue) throws ParserConfigurationException, FileNotFoundException, TransformerException 
		{
	    	SysOutCtrl.SysoutSet("reply of replication is send by " + myNodeId);

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = f.newDocumentBuilder();
			Document doc = db.newDocument();

			Element rootele = doc.createElement("replyReplicationMsgByMyNode");
			doc.appendChild(rootele);
			Element tagidele = doc.createElement("Query_");

			String tagvalue = "R101";
			SysOutCtrl.SysoutSet("Tagvalue for Space Query: " + tagvalue);

			((Element) tagidele).setAttribute("tag", tagvalue);
			rootele.appendChild(tagidele);

			Element hashidele = doc.createElement("to_hash_id");
			Element tonodeidele = doc.createElement("to_node_id");
			Element selfnodeidele = doc.createElement("self_node_id");
			Element ipele = doc.createElement("self_ip_address");
			Element portele = doc.createElement("self_port_no");
			

			Text t0 = doc.createTextNode(fileName);
			Text t1 = doc.createTextNode(rootNodeId);
			Text t2 = doc.createTextNode(myNodeId);
			Text t3 = doc.createTextNode(myIp);
			Text t4 = doc.createTextNode(nodevalue);
		
			
			
			hashidele.appendChild(t0);
			tonodeidele.appendChild(t1);
			selfnodeidele.appendChild(t2);
			ipele.appendChild(t3);
			portele.appendChild(t4);
			

			tagidele.appendChild(hashidele);
			tagidele.appendChild(tonodeidele);
			tagidele.appendChild(selfnodeidele);
			tagidele.appendChild(ipele);
			tagidele.appendChild(portele);
		

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new FileOutputStream("Replication_msg_by_Rootode.xml"));
			try {
				transformer.transform(source, result);
			} catch (NullPointerException e) {
			}
			File file = new File("Replication_msg_by_Rootode.xml");
			SysOutCtrl.SysoutSet("Replication_msg_by_Rootode.xml generated");
			CommunicationManager.TransmittingBuffer.add(file);
			//OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
			SysOutCtrl.SysoutSet("Replication_msg_by_Rootode.xml has been sent to the node");
			
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    public static void file_Deletion_query(String fileName, String toNodeId,String myNodeId, String myIp,String action) throws ParserConfigurationException, FileNotFoundException, TransformerException 
		{
	    	SysOutCtrl.SysoutSet("File chunk deletion Message is sent by  " + myNodeId);

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = f.newDocumentBuilder();
			Document doc = db.newDocument();

			Element rootele = doc.createElement("FileChunkDeletionMsgByMyNode");
			doc.appendChild(rootele);
			Element tagidele = doc.createElement("Query_");

			String tagvalue = "D100";
			SysOutCtrl.SysoutSet("Tagvalue for Space Query: " + tagvalue);

			((Element) tagidele).setAttribute("tag", tagvalue);
			rootele.appendChild(tagidele);

			Element hashidele = doc.createElement("to_hash_id");
			Element tonodeidele = doc.createElement("to_node_id");
			Element selfnodeidele = doc.createElement("self_node_id");
			Element ipele = doc.createElement("self_ip_address");
			Element portele = doc.createElement("self_port_no");
			

			Text t0 = doc.createTextNode(toNodeId);
			Text t1 = doc.createTextNode(fileName);
			Text t2 = doc.createTextNode(myNodeId);
			Text t3 = doc.createTextNode(myIp);
			Text t4 = doc.createTextNode(action);
		
			
			
			hashidele.appendChild(t0);
			tonodeidele.appendChild(t1);
			selfnodeidele.appendChild(t2);
			ipele.appendChild(t3);
			portele.appendChild(t4);
			

			tagidele.appendChild(hashidele);
			tagidele.appendChild(tonodeidele);
			tagidele.appendChild(selfnodeidele);
			tagidele.appendChild(ipele);
			tagidele.appendChild(portele);
		

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new FileOutputStream("File_Deletion_Msg.xml"));
			try {
				transformer.transform(source, result);
			} catch (NullPointerException e) {
			}
			File file = new File("File_Deletion_Msg.xml");
			SysOutCtrl.SysoutSet("File_Deletion_Msg.xml generated");
			CommunicationManager.TransmittingBuffer.add(file);
			//OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
			SysOutCtrl.SysoutSet("File_Deletion_Msg.xml has been sent to the node");
			
		}
///////////////////////////////////////////////////////////////////////////////////////////////////////
	    public static void root_To_Originator(String originator_NodeId,String fileName,String myNodeId,String myIp,String area) throws ParserConfigurationException, FileNotFoundException, TransformerException
	    {
	    	SysOutCtrl.SysoutSet("Root to Originator Message is sent by  " + myNodeId);

			DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = f.newDocumentBuilder();
			Document doc = db.newDocument();

			Element rootele = doc.createElement("RootToOriginatorMsgByMyNode");
			doc.appendChild(rootele);
			Element tagidele = doc.createElement("Query_");

			String tagvalue = "R500";
			SysOutCtrl.SysoutSet("Tagvalue for Space Query: " + tagvalue);

			((Element) tagidele).setAttribute("tag", tagvalue);
			rootele.appendChild(tagidele);

			Element hashidele = doc.createElement("to_hash_id");
			Element tonodeidele = doc.createElement("to_node_id");
			Element selfnodeidele = doc.createElement("self_node_id");
			Element ipele = doc.createElement("self_ip_address");
			Element portele = doc.createElement("self_port_no");
			

			Text t0 = doc.createTextNode(originator_NodeId);
			Text t1 = doc.createTextNode(fileName);
			Text t2 = doc.createTextNode(myNodeId);
			Text t3 = doc.createTextNode(myIp);
			Text t4 = doc.createTextNode(area);
		
			
			
			hashidele.appendChild(t0);
			tonodeidele.appendChild(t1);
			selfnodeidele.appendChild(t2);
			ipele.appendChild(t3);
			portele.appendChild(t4);
			

			tagidele.appendChild(hashidele);
			tagidele.appendChild(tonodeidele);
			tagidele.appendChild(selfnodeidele);
			tagidele.appendChild(ipele);
			tagidele.appendChild(portele);
		

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StreamResult result = new StreamResult(new FileOutputStream("root_To_Originator.xml"));
			try {
				transformer.transform(source, result);
			} catch (NullPointerException e) {
			}
			File file = new File("root_To_Originator.xml");
			SysOutCtrl.SysoutSet("root_To_Originator.xml generated");
			CommunicationManager.TransmittingBuffer.add(file);
			//OverlayManagementUtilityMethods.sendFileDirect(askerIp, file);
			SysOutCtrl.SysoutSet("root_To_Originator.xml has been sent to the node");
	    }

}