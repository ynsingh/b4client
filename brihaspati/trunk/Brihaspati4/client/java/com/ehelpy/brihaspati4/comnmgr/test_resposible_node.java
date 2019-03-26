package com.ehelpy.brihaspati4.comnmgr;

import java.util.LinkedList;
import java.util.List;

import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;

public class test_resposible_node {

	public static void main(String[] args) 
	{
		boolean Node = CommunicationUtilityMethods.responsibleNode("D8CAA318B87842D2027FD417CC19D8D537E077","A5EFFFFFFFCCCC5555555666666666666222BBBB","A5EFFFFFFFCCCC5555555666666666666222BBBB");
		
		System.out.println("Responsible Node  : "+Node);
	}

}
