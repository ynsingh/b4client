package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 12 Apr 1430h
import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;

public class GiveNextHop extends RTManager {
	

	
		public static File[] RMInPtBuff = new File[2048];
		public static String[][] InPtQueryBuff = new String[2048][5];
		public static String[][] OtptQueryBuff = new String[2048][6];
		
		public static void main(String[] args) 
		{
			//RTManager.Pred[6][0]="3333333233333333333333333333333333333333";
			//RTManager.Succ[6][0]="6666666666666666666666666666666666666666";
			//RTManager.Mid[6][0] ="9999999999999999999999999999999999999999";
			RTManager.Pred[0][0]="E2A3333233333333333333333333333333333333";
			
			//RTManager.Pred[0][0]="3333333233333333333333333333333333333333";
			//RTManager.Succ[0][0]="6666666666666666666666666666666666666666";
			//RTManager.Mid[0][0] ="9999999999999999999999999999999999999999";
			
			OverlayManagement.myNodeId="D84F6DE804BE0AD29154A6C15654371E08E3AD72";
			
			String Answer = GiveNextHop.NextHop("DEC09A3DB89592C998C05A7E605291B9430F693F");
			//String Answer = GiveNextHop.NextHop("9999999999999999999999999999999999999999");
			SysOutCtrl.SysoutSet("Next Hop :"+Answer);
			System.out.println("Next Hop :"+Answer);
			
		}
		
//		Returns Next Hop, takes 40 hex as input returns 40 hex as output.
//		query node/hash id in, next hop out
		
	 public static String NextHop(String Queryfornode)
	 {
		 
//		 String nodeid= OverlayManagement.getMyNodeId();// comment this line to add node id by design above.
		 String GoTo =null;
		 
		 SysOutCtrl.SysoutSet("GiveNextHop called", 2);
		 SysOutCtrl.SysoutSet("Destination Node is : " + Queryfornode,2);
		 
		 FindMismatch obj = new FindMismatch();
		 SysOutCtrl.SysoutSet("mynodeid in nexthop"+OverlayManagement.myNodeId,3);
		 int RTColn= obj.MismatchAt(Queryfornode, OverlayManagement.myNodeId);
		 SysOutCtrl.SysoutSet("Mismatch at :"+RTColn,2);
		 
		 if(RTColn == 41) 
		 {
			 GoTo =OverlayManagement.myNodeId;
		 }
		 
		 else 
		 {
			 FindRow obj1 =new FindRow();
		 
			 if(RTColn!=0)
			 {
		 
				 GoTo =obj1.NextHop(Queryfornode, OverlayManagement.myNodeId, RTColn);
				 SysOutCtrl.SysoutSet("Next hop is  :"+GoTo);
				 System.out.println("-------------------"+OverlayManagement.myNodeId);
				 
		 
			 }
		 
			 else 
			 {
			 GoTo =obj1.NextHop(Queryfornode, OverlayManagement.myNodeId, RTColn);
			 System.out.println("kfhhfkhfhjfhj"+OverlayManagement.myNodeId);
			 }
		 }
		 return GoTo;
	 }
	}

class FindMismatch extends GiveNextHop
{
	public int MismatchAt(String Queryfornode, String Nodeid) 
	{ 
		SysOutCtrl.SysoutSet("Give MisMatch called", 3);
		SysOutCtrl.SysoutSet("Destination for mismatching query is : " + Queryfornode,2);
		 
		int MisMatch =0;
		
		String nodeid  = Nodeid;
		
//		String nodeid= OverlayManagement.getMyNodeId();// comment this line to add node id by design above.
		String queryid = Queryfornode;
	    String Existingid = null;
	    
		// this converts the char to int for comparisons

		char A=10;char B=11;char C=12;char D=13;char E=14;char F=15;// this converts the char to int
		int HexA = Character.getNumericValue(A);
	    int HexB = Character.getNumericValue(B);
	    int HexC = Character.getNumericValue(C);
	    int HexD = Character.getNumericValue(D);
	    int HexE = Character.getNumericValue(E);
	    int HexF = Character.getNumericValue(F);

	    for(int n = 0; n < nodeid.length();n=n+1) // reads character at 'n'
	    {
	    	char NodeNib = nodeid.charAt(n);
	        char QueryNib = queryid.charAt(n);
	        char ExistingNib;
	        char HalfwayNib;
	        
	        int HexNodeNib = Character.getNumericValue(NodeNib);// gets numeric value of the char at 'n'
	        int HexQueryNib = Character.getNumericValue(QueryNib);
	        int HexExistingNib;        
	        double HalfwayNibHex;
	        
	        int PredRg = HexNodeNib-4;// calculates the predecessor and succesor ranges for the particular Nib
	        int SuccRg = HexNodeNib+4;

	        if(HexNodeNib == HexQueryNib)
	        {
	        	if(n==(nodeid.length())-1)
	        	{
	        		String NextHop = nodeid;
	        		SysOutCtrl.SysoutSet("The query is for Self Node", 2);
	        		MisMatch=41;
	        	}
	        	else
	        	{
        		
	        		SysOutCtrl.SysoutSet("Both Nibbles are equal, moving to next Nib", 2);
	        		n=n++;
	        	}
	        }

	        else if(HexNodeNib != HexQueryNib) 
	        {
	        MisMatch=n;
	        System.out.println("Mismatch of query and self Node at :"+n);	
	        break;
	        }
		
	}
	    System.out.println("MisMatch:"+MisMatch);
	    return MisMatch;
	    
}

}


class FindRow extends GiveNextHop
{
	
// public int Row(String Queryfornode, String Nodeid, int MismatchAt) 
 public String NextHop(String Queryfornode, String Nodeid, int MismatchAt) 
 {
	 String NextHop=null;
	 
	 int runfor =MismatchAt;
	 SysOutCtrl.SysoutSet("the nib to be compared at :"+runfor);
	 String nodeid  = Nodeid;
		
	List<String> A = new LinkedList();
			
	for(int m =runfor;m<Pred.length;m++)
	{
		if(Pred[m][0]!=null)
		{	
		A.add(Pred[m][0]);
		}
		if(Succ[m][0]!=null)
		{
			A.add(Succ[m][0]);
		}
		if(Mid[m][0]!=null)
		{
			A.add(Mid[m][0]);
		}
		
	}
	// abhishek
	if(!A.contains(Queryfornode))
	{
		A.add(Queryfornode);
		A.add(nodeid);	
	
	
		int size= A.size();
		SysOutCtrl.SysoutSet("Size of Array :"+ size);
	
	
		Collections.sort(A);
	
		SysOutCtrl.SysoutSet("Sorted RT :"+A);
		int index = A.indexOf(Queryfornode);
	
		SysOutCtrl.SysoutSet("index of query :"+index);
		System.out.println("Sorted RT :"+A);
	
		if(index==size-1) 
		{
			List<String> B = new LinkedList();
			for(int m =0;m<Pred.length;m++)
			{
				if(Pred[m][0]!=null)
				{	
					B.add(Pred[m][0]);
				}
				if(Succ[m][0]!=null)
				{
					B.add(Succ[m][0]);
				}
				if(Mid[m][0]!=null)
				{
					B.add(Mid[m][0]);
				}
			}
			B.add(Queryfornode);
			B.add(nodeid);	
			int size1= B.size();
			SysOutCtrl.SysoutSet("Size of Array :"+ size1);
			
			
			Collections.sort(B);
			
			SysOutCtrl.SysoutSet("Sorted RT :"+A);
			int index1 = B.indexOf(Queryfornode);
			
			SysOutCtrl.SysoutSet("index of query :"+index1);
				
			
			if(index1==size1-1) 
			{
				NextHop =B.get(0);
			}
			
			else
			{
				NextHop =B.get(index1+1);
			}

			System.out.println("Sorted RT :"+B);
		}
			
		else
		{
			NextHop =A.get(index+1);
		}
	}
	else
	{
		NextHop=Queryfornode;
	}
		
	return NextHop;
	 
 }
}
