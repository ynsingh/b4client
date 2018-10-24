package com.ehelpy.brihaspati4.indexmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

//import com.ehelpy.brihaspati4.overlaymgmt.Overlay;

public class TestBed {
	
	public static void main(String[] args) 
	{
//		SysOutCtrl.SysoutSet("Starting IndexManagement thread",0);
		IndexManagement indmgt= new IndexManagement();
		indmgt.start();
		indmgt.getState();
		SysOutCtrl.SysoutSet("changing iAmNewlyJoinedNode varialbe in Overlay to true",2);
		OverlayManagement.iAmNewlyJoinedNode=true;
//	
//		SysOutCtrl.SysoutSet("filling RxBufferIM in Communiation Manger with dummy query files",3);
	//	IndexManagementUtilityMethods.fillRxBufferIM();
		//SysOutCtrl.SysoutSet("filling MyIndexTable with dummy entries",3);
		IndexManagementUtilityMethods.fillMyIndexTable();
		
		
		OverlayManagement.myNodeId="3333333333333333333333333333333333333333";
		PredecessorSuccessor.myPredecessors[4]="AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	//	PredecessorSuccessor.myPredecessors[4]="BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
//		if(OverlayManagement.getMyNodeId().compareToIgnoreCase(PredecessorSuccessor.myPredecessors[4])>0)
//			System.out.println("my node id is larger than pred");
//		System.out.println("my node id is smaller than pred");
		//String sha1 = SHA1.getSha1("test");
		//IndexManagementUtilityMethods.addIndexRequest(sha1, "test");
		System.out.println(IndexManagement.myindex);
		//CommunicationManager.myIpTable.put("test", "192.168.1.3");
		//SysOutCtrl.SysoutSet("creating search query for nodeId 'cdef1' ",2);
		//String key="cdef1";
		//String ip=IndexManagementUtilityMethods.searchEmailId("test");
		//System.out.println("ip is "+ip);
		
		//CommunicationManager.TransmittingBuffer.add(searchQueryFile);
		
//		SysOutCtrl.SysoutSet("creating publishing query for nodeId 'cdef1' ",2);
//	
//		String email="deepaksharma808@yahoo.co.in";
//		String ip1=IndexManagementUtilityMethods.searchEmailId(email);
//		System.out.println(ip1);
//		
		
	}}




