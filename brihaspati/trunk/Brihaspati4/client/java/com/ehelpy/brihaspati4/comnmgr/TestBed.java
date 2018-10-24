package com.ehelpy.brihaspati4.comnmgr;


import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

import com.ehelpy.brihaspati4.indexmanager.CreateXmlFile;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class TestBed {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	//	CreateXmlFile.createXmlQueryFiles();
		CommunicationUtilityMethods.fillReceiveBuffer();
		OverlayManagement.myNodeId="3333333333333333333333333333333333333333";
		PredecessorSuccessor.myPredecessors[4]="6666666666666666666666666666666666666666";
		System.out.println(OverlayManagement.myNodeId);
	//	System.out.println(OverlayManagement.getMyNodeId());
		System.out.println(PredecessorSuccessor.myPredecessors[4]);
	//	SysOutCtrl.SysoutSet(""+OverlayManagement.getMyNodeId().compareToIgnoreCase(PredecessorSuccessor.myPredecessors[4]));
		CommunicationManager cm= new CommunicationManager();
		cm.start();
		//String[] nodeId= {"33333","66666","99999","BBBBB"};
		//String[] ret=PredecessorSuccessor.getMyPredecessors(nodeId, "33333");
		//for(int i=0; i<5; i++)
		//{
		//	System.out.println("my pred"+i+PredecessorSuccessor.myPredecessors[i]);
		//	System.out.println("my pred ret"+i+ret[i]);
		//}
		
	//	boolean isResponsibleNode=responsibleNode(PredecessorSuccessor.myPredecessors[4],OverlayManagement.getMyNodeId(),"48B69A3DB89592C998C05A7E605291B9430F693F");
	//System.out.println(isResponsibleNode);
	
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
		SysOutCtrl.SysoutSet("pred>succ");
		if(list.indexOf(key)==2)
			bool=true;
	}
	return bool;
	}
}
