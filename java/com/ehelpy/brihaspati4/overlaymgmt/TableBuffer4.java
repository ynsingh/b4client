
package com.ehelpy.brihaspati4.overlaymgmt;
import java.io.File;
import java.util.*;
import java.util.Arrays;
import java.net.InetAddress;
//import java.util.Random;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Exception;
import java.lang.Integer;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class TableBuffer4{

	
	//Create ReceivingBuffer; Storing the files 
		ArrayList<File> ReceivingBuffer = new ArrayList<File>();
		
	//Create TransmittingBuffer;
		ArrayList<File> TransmittingBuffer = new ArrayList<File>();

	//Create PingMsgReceivingBuffer;
		ArrayList<File> PingMsgReceivingBuffer = new ArrayList<File>();

	//Create DHTRoutingtableBuffer;
		ArrayList<File> DHTRoutingTableBuffer  = new ArrayList<File>();
		
	//Create a generic File array called FileBuffer[]
		ArrayList<File> FileBuffer = new ArrayList<File>();
	
	//Create Output File Array( result of CheckTag function/ method)
	//File[] OutputFileArray = new File[ReceivingBuffer.size()];
		ArrayList<File> OutputFileArray  = new ArrayList<File>();
	
	
	
	// Class constructor
	public TableBuffer4(){   
	
	//initializing ReceivingBuffer(temporary for simulation only).
	//Files have to be filled in ReceivingBuffer in this Node as and when received from other Nodes
			
	//File f = new File("D:\OverlayMgmtTest\RouteTable1.xml");
		/*
	File f = new File("RouteTable1.xml");
	//System.out.println("File Name   : " + f.getPath());	
	ReceivingBuffer.add(ReceivingBuffer.size(), f);  
					OR
	ReceivingBuffer.add ("RouteTable2.xml");
	ReceivingBuffer.add ("RouteTable3.xml");
	ReceivingBuffer.add ("RouteTable4.xml");
	ReceivingBuffer.add ("RouteTable5.xml");
	ReceivingBuffer.add ("RouteTable6.xml");
	ReceivingBuffer.add ("RouteTable7.xml");
	ReceivingBuffer.add ("RouteTable8.xml");
	ReceivingBuffer.add ("RouteTable9.xml");
	ReceivingBuffer.add ("RouteTable10.xml");*/
		
		
		File f = null;
	      String[] strs = {"RouteTable1.xml","RouteTable2.xml", "RouteTable3.xml", "RouteTable4.xml", "RouteTable5.xml", "RouteTable6.xml", "RouteTable7.xml", "RouteTable8.xml", "RouteTable9.xml", "RouteTable10.xml"};
		  //"};
	      //, "RouteTable2.xml", "RouteTable3.xml", "RouteTable4.xml"
		  //, "RouteTable5.xml"
	      try {
	         // for each string in string array 
	         for(String s:strs ) {
	            // create new file
	            f = new File(s);
	            
	        	ReceivingBuffer.add(ReceivingBuffer.size(), f);
	        	//ReceivingBuffer.add(f)---> another way.
	        	
	         }
	      }
	      catch (Exception e) {
			e.printStackTrace();
		  }
	System.out.println("Receiving Buffer size is : " + ReceivingBuffer.size());
	System.out.println("");
	//for(int k =0; k < ReceivingBuffer.size(); k++){

		
	//ArrayList<File> ReceivingBuffer = new ArrayList<File>(
	//	    Arrays.asList("RouteTable1.xml", "RouteTable.xml", "RouteTable3.xml"));	
	
	
	      
	}

	public ArrayList<File> CheckTag(String CheckXMLTag) {
		//OutputFileArray = null;
		System.out.println("Segregating XML files in Receiving Buffer with Tag = 0002" );
		System.out.println("");
		int j=0;
			//while((ReceivingBuffer.size() != 0) && (j < ReceivingBuffer.size())){
				try{
					for(int i=0; i < ReceivingBuffer.size(); i++){
						
						String path = (ReceivingBuffer.get(i)).getPath();
						//System.out.println(path);
						File inputFile = new File(path);
						DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(inputFile);
						doc.getDocumentElement().normalize();
						String XMLTag = doc.getDocumentElement().getAttribute("Tag");
						//System.out.println(XMLTag);
						if(CheckXMLTag.equals(XMLTag)){
							//System.out.println("Tag check");
							OutputFileArray.add(j, inputFile);
							
							j++;
						}
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				FileBuffer = null;
				DHTRoutingTableBuffer = null;
				ReceivingBuffer = null;
			//}
			System.out.println("No.of XML files segregated : " + OutputFileArray.size()+" ( Srl No. 0 to "+(OutputFileArray.size()-1)+" )");
			System.out.println("");
			System.out.println("List of segregated XML files");
			System.out.println("");
			System.out.println(OutputFileArray);
			System.out.println("");
			
			//System.out.println("OutputFileArray   : " + OutputFileArray);
			return OutputFileArray;
		}
	
	
	public void AddMessage() {

		//receive files from other peers and save them in a queue of ReceivingBuffer
		File element = null;   //new File(../.../.../pathname) of latest received file from other peers.

		ReceivingBuffer.add(ReceivingBuffer.size(), element);//element needs to be implemented as in above comment.


	}
		//System.gc();

		//**************** File Array[] implementation*******************************************
		/*File[] ReceivingBuffer = new File[];
	File[] TransmittingBuffer;
	File[] PingMsgReceivingBuffer;
	File[] OutputFileArray;*/

		/*public File[] CheckTag(String CheckXMLTag) {
		int j=0;
		while((ReceivingBuffer.length != 0)&&(j < ReceivingBuffer.length)){
			try{
				for(int i=0; i < ReceivingBuffer.length; i++){
						File inputFile = new File("ReceivingBuffer[i].getPath()");
				         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				         Document doc = dBuilder.parse(inputFile);
				         doc.getDocumentElement().normalize();
				         String XMLTag = doc.getDocumentElement().getAttribute("Tag");
				         if(XMLTag == CheckXMLTag){
				        	 OutputFileArray[j] = inputFile;
				        	 j++;
				         }
				}
			}
			catch (Exception e) {
		         e.printStackTrace();
		    }
		}
		return OutputFileArray;
	}*/

	}
