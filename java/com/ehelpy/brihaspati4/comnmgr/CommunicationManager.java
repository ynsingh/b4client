package com.ehelpy.brihaspati4.comnmgr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
//import java.util.Random;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Exception;
import java.lang.Integer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.w3c.dom.Document;

//import com.ehelpy.brihaspati4.indexmanager.*;

public class CommunicationManager extends Thread{

	// Create Receiving Buffer for putting all the received xml files from previous Comm Manager
	public static LinkedList<File> ReceivingBuffer = new LinkedList<File>();

	// Create TransmittingBuffer for putting all the received xml files for next hop;
	public static LinkedList<File> TransmittingBuffer = new LinkedList<File>();

	// Creating buffers for OM/RT/IM. Xml files will be put based on tag in respective buffer
	public static LinkedList<File> RxBufferOM = new LinkedList<File>();
	public static LinkedList<File> RxBufferRT = new LinkedList<File>();
	public static LinkedList<File> RxBufferIM = new LinkedList<File>();
	public static Map<String, String> myIpTable = new LinkedHashMap<>();	
	public static LinkedList<String> fromNodeIdList = new LinkedList<>();
	public static boolean updateTable =false;
	public void  run()
	//public static void main(String[] args)
	
	{
	
	//CommunicationUtilityMethods.fillReceiveBuffer();
	//FillOutputBuffer.fillOutputBuffer();
//	
	Thread t0= new Thread(new Runnable() {
		@Override
		public void run()
		{
			while(true)// if OpBufferIM is not empty this if block will be executed
			
			{
				SysOutCtrl.SysoutSet("xmlfilesegregation thread t0 is running",2);
				
					while(!ReceivingBuffer.isEmpty())// if OpBufferIM is not empty this if block will be executed
				{
						
			SysOutCtrl.SysoutSet(CommunicationManager.fromNodeIdList.size()+"size with comm mgr");			
			XmlFileSegregation.xmlFileSegregator();
			 updateTable =true;
			 SysOutCtrl.SysoutSet("updateTable in CM"+updateTable);
		}
					SysOutCtrl.SysoutSet("Thread t0 reply- Receiving Buffer is empty");
					try {
			
				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}});
	t0.start();

		Thread t1 = new Thread(new Runnable() {				// here implementing threads to start http server, because server will send this file to the first successor
		@Override
			public void run() 
		{
			while(true) 
			{
				SysOutCtrl.SysoutSet("FileReceiver Thread t1 is running",1);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					try { CommunicationUtilityMethods.fileReceiver();} 
					catch (IOException e) { e.printStackTrace();}
		
	     } 
			}
		});
		t1.start();

		Thread t2= new Thread(new Runnable() {
			@Override
			public void run()
			{
				while(true)
				{
				SysOutCtrl.SysoutSet( "Thread-t2 (File Transmitter) is running",1);
				
				CommunicationUtilityMethods.sendXmlToNextNodeId();
				//Sendfile method will be used for sending files to next hop.
				try {
					
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					SysOutCtrl.SysoutSet("thread t2 exception");
				}
			}
				}
		});
		t2.start();
		
		SysOutCtrl.SysoutSet("my predecessor 4....................."+PredecessorSuccessor.myPredecessors[4]);

	//	CommunicationUtilityMethods.sendXmlToNextNodeId();
	}
	

	
}




