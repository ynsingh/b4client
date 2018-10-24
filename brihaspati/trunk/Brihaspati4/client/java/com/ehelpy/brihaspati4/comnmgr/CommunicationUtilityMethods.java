package com.ehelpy.brihaspati4.comnmgr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class CommunicationUtilityMethods extends CommunicationManager {

	public static void addQueryToReceiveBuffer(File inFile) // receive messages from other peers and save them in a
															// queue of
	// receivingBuffer
	{

		String str = inFile.getName();		// System.out.println("reply from addQueryFile method, Flename="+str);

		ReceivingBuffer.add(ReceivingBuffer.size(), inFile); // this statement will append the msg ie
		// filename(Queryi.xml) in the receiving buffer
		SysOutCtrl.SysoutSet("file added to receving buffer"+str);
		SysOutCtrl.SysoutSet("Receiving Bufer: "+ReceivingBuffer);
	}

	public File getQueryFile() // get a message from the front of the queue of receiving buffer // returns an
	// xml file
	{

		return ReceivingBuffer.get(0);
	}

	public static void fileSender(String toNodeIp, String s) {
		SysOutCtrl.SysoutSet("you are in fileSender method",2);
SysOutCtrl.SysoutSet("Transmitting buffer status: "+TransmittingBuffer);
		int port = 2222;
		Socket sock = null;
		try {
			sock = new Socket(toNodeIp, port);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			System.out.println("1111");
			//e1.printStackTrace();
			SysOutCtrl.SysoutSet("check connection");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			SysOutCtrl.SysoutSet("Connection timed out: Please check your connectivity with"+ toNodeIp,1);
//			FileWriter F=null;
//			try {
//				F = new FileWriter("ReAdd.xml");
//			} catch (IOException e11) {
//				// TODO Auto-generated catch block
//				e11.printStackTrace();
//			} 
//			try {
//				F.write(s);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				F.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			File ReTx = new File("ReAdd.xml"); 
//		    CommunicationManager.TransmittingBuffer.add(ReTx);
		    CommunicationUtilityMethods.sendXmlToNextNodeId();
		    //fileSender(toNodeIp, s);
			//e1.printStackTrace();
			
		}

//		if (!sock.isConnected())
//			SysOutCtrl.SysoutSet("Socket Connection Not established",2);
//		else
//			SysOutCtrl.SysoutSet("Socket Connection established : " + sock.getInetAddress(),1);

		byte[] mybytearray = s.getBytes();
		//SysOutCtrl.SysoutSet("file lenght before sending of" + s.toString() + ":" + s.length());
		BufferedInputStream bis = null;

		OutputStream os = null;
		try {
			// bis.read(mybytearray, 0, mybytearray.length);
			//SysOutCtrl.SysoutSet(s.toString() + "bytes sent" + mybytearray.length);
			os = sock.getOutputStream();
			os.write(mybytearray, 0, mybytearray.length);
			os.flush();
			// bis.close();
			sock.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			SysOutCtrl.SysoutSet("CHECK CONNECTION");
		}catch (NullPointerException e1) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			SysOutCtrl.SysoutSet("CHECK CONNECTION-NPE");
		}
	}

	public static synchronized void fileReceiver() throws IOException {

		SysOutCtrl.SysoutSet("You are in File receiver method",2);

		ServerSocket servsock = new ServerSocket(2222);

		int fileReceiveIndex = 0;

		if (!servsock.isBound())
			SysOutCtrl.SysoutSet("Sever Socket not Bounded...",2);
		else
			SysOutCtrl.SysoutSet("Server Socket bounded to Port : " + servsock.getLocalPort(),1);

		// File myFile = new File(file_to_pass);
		while (true) {
			Socket sock = servsock.accept();

			if (!sock.isConnected())
				SysOutCtrl.SysoutSet("Client Socket not Connected...",2);
			else {
				SysOutCtrl.SysoutSet("Client Socket Connected : " + sock,1);
				fileReceiveIndex++;
			}

			int index = 0;
			byte[] mybytearray = new byte[4096];
			InputStream is = sock.getInputStream();
			String fileName=getFileName();
			FileOutputStream fos = new FileOutputStream(fileName);

			BufferedOutputStream bos = new BufferedOutputStream(fos);
			int bytesRead = is.read(mybytearray, 0, mybytearray.length);
			int current = bytesRead;

			do {
				bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
				if (bytesRead >= 0)
					current += bytesRead;
			} while (bytesRead > -1);

			bos.write(mybytearray, 0, current);
			bos.flush();

			bos.close();
			fos.flush();
			fos.close();
			sock.close();
			for (int i = 0; i < mybytearray.length; i++) {
				System.out.print(mybytearray[i] + " ");
			}
			SysOutCtrl.SysoutSet("File  downloaded (" + current + " bytes read)");
			File inFile = new File(fileName);

			SysOutCtrl.SysoutSet("File length after receiving" + inFile.length());

			SysOutCtrl.SysoutSet("Receiving file index"+fileReceiveIndex);
			SysOutCtrl.SysoutSet("inFile" + inFile.toString() + inFile.length() + "bytes");
			String[] xmlParsed =ParseXmlFile.ParseXml(inFile) ;
			if(xmlParsed[0].equals("0031"))
			{
				CommunicationManager.RxBufferOM.add(inFile);
			}
			else
			addQueryToReceiveBuffer(inFile);

		}

	}

	public static void sendXmlToNextNodeId() {

		SysOutCtrl.SysoutSet("you are in sendXmlToNextNodeId",2);

		while (!TransmittingBuffer.isEmpty())// if OpBufferIM is not empty this if block will be executed
		{
			//File file = new File("Query_0.xml");
			File file = TransmittingBuffer.remove();
			// String filepath=file.getAbsolutePath();
			BufferedReader r = null;
			try {
				r = new BufferedReader(new FileReader(file.toString()));
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
			// System.out.print(s);

		//	System.out.print("reading a file before sending: " + s);

			String[] xmlParsed;
			// xmlParsed = ParseXmlFile.ParseXml(new File(filepath));
			SysOutCtrl.SysoutSet("Xml file parsed:" + file,2);
			xmlParsed = ParseXmlFile.ParseXml(file);

			
			String tohashId="";
			//if(xmlParsed[0].equals("0002"))
			{ tohashId = xmlParsed[1];}
			// getting hashId from current query
			//String tonodeId = GiveNextHop(tohashId); // calling function to get the root node id
			
			
			String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(tohashId);
			if(tonodeId.equals(OverlayManagement.myNodeId))
			{
				tonodeId=PredecessorSuccessor.mySuccessors[0];
			}
			SysOutCtrl.SysoutSet("NextHop  from RoutingManagement for " + file + ":" + tonodeId,2);
			String toIp = getIpFromMyIpTable(tonodeId); // calling function to get the ip address of the root node id
			
			try
			{if(toIp.isEmpty())
			//	toIp="127.0.0.1";
			toIp=getIpFromMyIpTable(PredecessorSuccessor.mySuccessors[0]);
			SysOutCtrl.SysoutSet("toIp of successor[0] selected:" + toIp,2);
			
			
			//	tonodeId=OverlayManagement.mySuccessors[0];
				}catch(NullPointerException e1){
				
					//tonodeId=OverlayManagement.mySuccessors[0];
					//toIp="127.0.0.1";
					toIp=getIpFromMyIpTable(PredecessorSuccessor.mySuccessors[0]);
					SysOutCtrl.SysoutSet("toIp of successor[0] selected:" + toIp,2);
					
				}
			
{
				
			//	SysOutCtrl.SysoutSet("to_node_id after getting null entry "+tonodeId);
}
			SysOutCtrl.SysoutSet("Destination Ip Add for " + file + ":" + toIp,2);
			SysOutCtrl.SysoutSet("Calling File Sender Method " ,2);
	//		if(!toIp.equals("xxxx"))
		//	{fileSender("localhost", s);}	
			if(!toIp.equals("127.0.0.1"))
			fileSender(toIp, s);
			else
		//	SysOutCtrl.SysoutSet("in if block");}
			{ SysOutCtrl.SysoutSet("Next hop is not reachable",2);
//SysOutCtrl.SysoutSet("Putting file in TransmittingBuffer to Reattempt transmission",2);
//			
//			FileWriter F=null;
//			try {
//				F = new FileWriter("ReTx.xml");
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			} 
//			try {
//				F.write(s);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			try {
//				F.flush();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			File ReTx = new File("ReTx.xml"); 
//		    CommunicationManager.TransmittingBuffer.add(ReTx);
//		    
//		    SysOutCtrl.SysoutSet("Failed File put for retransmission succesfully");
			}}
		
		SysOutCtrl.SysoutSet("Thread t3 reply-Transmitting Buffer found empty",2);
	}
// hashid will be used to locate the root node id. this method will be required if new add query has come. RT module will implement this method.
	
//	public static String GiveNextHop(String hashId) {
//		// TODO Auto-generated method stub
//		String nodeId = "cdef0";
//		return nodeId;
//	}

	// from nodeid to getIp will be implemented by OM.
	public static String getIpFromMyIpTable(String nodeId) {
		// String str="192.168.1.104";
		SysOutCtrl.SysoutSet("My IP Table:"+myIpTable);
		String ip = CommunicationManager.myIpTable.get(nodeId);
	//	if(ip.isEmpty())
	//	{ip="localhost";}
		SysOutCtrl.SysoutSet("ip address from myIpTable"+ip,2);
		return ip;
	}
	public static void fillReceiveBuffer() {
		// TODO Auto-generated method stub
		SysOutCtrl.SysoutSet("you are in FillReceiveBuffer method",2);
		for(int i=0;i<1;i++)
			
		{File fetchNextXml = new File("Query1"+i+".xml");
		addQueryToReceiveBuffer(fetchNextXml);
		

				}
		

	}
	static int index=10;
	public synchronized static String getFileName()
	{
		
	String fileName= "Query"+index+".xml";
	index++;
	while(index==20)
	{index=10;}
		return fileName;
	}
	public static boolean responsibleNode(String Pred,String Self,String key)
	{
		boolean bool=false;
	List<String> list= new LinkedList();
	list.add(Pred);
	list.add(Self);
	list.add(key);
	
	SysOutCtrl.SysoutSet("list"+list);
	Collections.sort(list);
	SysOutCtrl.SysoutSet("sorted list"+list);
	if(list.indexOf(Pred)<list.indexOf(Self))
	{
		if(list.indexOf(key)==1)
			 bool=true;
		
	}
	if(list.indexOf(Pred)>list.indexOf(Self))
	{
		SysOutCtrl.SysoutSet("pred>self");
		if(list.indexOf(key)==2||list.indexOf(key)==0)
			bool=true;
	}
	return bool;
	}
}
