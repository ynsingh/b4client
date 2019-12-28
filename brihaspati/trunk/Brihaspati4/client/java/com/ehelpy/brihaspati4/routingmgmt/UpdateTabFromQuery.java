package com.ehelpy.brihaspati4.routingmgmt;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

// Major Niladri Roy 3 may 2018
public class UpdateTabFromQuery extends RTManager
{
	
//	this class updates the table from queries received, and pred and succ tables.
	public synchronized static void NextEntry(String a,String b)
	{ 
		//System.out.println	(OverlayManagement.myNodeId);
	    String queryid = a; 
	    String ipad=b;
	    String Existingid = null;
	    System.out.println("nextentry");
	    // this converts the char to int for comparisons

		char A=10;char B=11;char C=12;char D=13;char E=14;char F=15;// this converts the char to int
		

	    for(int n = 0; n < OverlayManagement.myNodeId.length();n=n+1)
	    { 
	    	char NodeNib = OverlayManagement.myNodeId.charAt(n);
	        char QueryNib = queryid.charAt(n);
	        char ExistingNib;
	        char HalfwayNib;
	        
	        int HexNodeNib = Character.getNumericValue(NodeNib);
	        int HexQueryNib = Character.getNumericValue(QueryNib);
	        System.out.println("HexQueryNib is 1234 :"+HexQueryNib);
	        int HexExistingNib;        
	        double HalfwayNibHex;
	        
	        int PredRg = HexNodeNib-4;
	        int SuccRg = HexNodeNib+4;

	        if(HexNodeNib == HexQueryNib)
	        {
	        	if(n==(OverlayManagement.myNodeId.length())-1)
	        	{
	        		System.out.println("The query is for Self Node");
	        	}
	        	else
	        	{
	        		System.out.println(n+" Both Nibbles are equal, moving to next Nib");
	        		n=n++;
	        	}
	        }

	//===========================================================================================
	//1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	11	1	11		
	//===========================================================================================	        

	        else if(PredRg <= 0)
	        {   
	        	System.out.println("we are in -ve loop");

	        	System.out.println("HexNodeNib is :"+HexNodeNib);
	        	System.out.println("HexQueryNib is 123 :"+HexQueryNib);
		    	
	        	System.out.println("PredRg is :"+PredRg);
	        	System.out.println("SuccRg is :"+SuccRg);
		        
		        if(HexQueryNib<HexNodeNib || HexQueryNib>=PredRg+16)
		        {
		        	System.out.println("we are in -ve loop 1");
		        	int UpdPredRg = 16+PredRg;
		        	System.out.println("Updated Pred Rg is :"+UpdPredRg);
		        	
		  	    	if(HexNodeNib>HexQueryNib)
		  	    	{
		  	    		if(Pred[n][0]==null)
		        		{
		        			Pred[n][0] = queryid;
		        			Pred[n][1] = ipad;
		        			RTManager.Routing_Table.put(a,b);
		        			break;
		  	    		}
	        		
		        		else
		        		{
		        			Existingid = Pred[n][0];
		        			ExistingNib = Existingid.charAt(n);
		        			HexExistingNib = Character.getNumericValue(ExistingNib);
		        			System.out.println("existing Nib  :"+HexExistingNib);
		        			System.out.println("HexQueryNib" +HexQueryNib);
	        			
		        			if(HexQueryNib==HexExistingNib)
	        				{
		        				if(queryid.compareTo( Existingid)>0)
		        				{
		        					Pred[n][0] = queryid;
	        						Pred[n][1] = ipad;
	        						RTManager.Routing_Table.put(a,b);
	    							break;
		        				}
	        				}
	        				else 
	        				{	
	        					int HexExistingNibval = HexExistingNib;
	        					int HexQueryNibval = HexQueryNib;
	        					if(HexExistingNib<=4)
	        					{
	        						HexExistingNibval=HexExistingNib+16;
	        					}
	        					if (HexQueryNib<=4)
	        					{
	        						HexQueryNibval=HexQueryNib+16;
	        					}
	        					if(HexExistingNibval<HexQueryNibval)
	        					{
	        						Pred[n][0] = queryid;
	        						Pred[n][1] = ipad;
	        						System.out.println("cond 1");
	        						RTManager.Routing_Table.put(a,b);
	    							break;
	    					
	        					}
	        				break;	
	        				}
		  	    		}
		  	    		break;
		  	    	}
		        	
		    	
		        	else if( HexQueryNib >= 16+PredRg)
		        	{
		        		System.out.println("we are in -ve loop 11");
			    		UpdPredRg = 16+PredRg;
			    		System.out.println("Updated Pred Rg is :"+UpdPredRg);
			  	    	
			  	    		if(Pred[n][0]==null)
			  	    		{
			  	    			Pred[n][0] = queryid;
	    						Pred[n][1] = ipad;
	    						RTManager.Routing_Table.put(a,b);
	    						break;
			  	    		}
		        		
			  	    		else
			  	    		{
			  	    			Existingid = Pred[n][0];
			  	    			ExistingNib = Existingid.charAt(n);
			  	    			HexExistingNib = Character.getNumericValue(ExistingNib);
			  	    			System.out.println("existing node  :"+HexExistingNib);
		        			
			  	    			if(HexQueryNib==HexExistingNib)
		        				{
			  	    				if(queryid.compareTo( Existingid)>0)
			        				{
			        					Pred[n][0] = queryid;
		        						Pred[n][1] = ipad;
		        						RTManager.Routing_Table.put(a,b);
		    							break;
			        				}
		        			
		        				}
		        				else 
		        				{
		        					int HexExistingNibval=HexExistingNib;
		        					if(HexExistingNib<=4)
		        					{
		        						HexExistingNibval=HexExistingNib+16;
		        					}
		        					if(HexQueryNib>HexExistingNibval)
		        					{
		        						Pred[n][0] = queryid;
		        						Pred[n][1] = ipad;
		        						RTManager.Routing_Table.put(a,b);
		        					break;
		        					}
		        				
		        					else 
		        					{
		        					}
		        			
		        				break;	
		        				}
			  	    		}
			  	    		break;
		        	
		        	}
		        }
		     
		        
		        	else if (HexQueryNib <= SuccRg && HexQueryNib > HexNodeNib)
		        	{
		        		System.out.println("we are in Normal loop 3");
		        		
	        		
		        		if(Succ[n][0]==null)
		        		{
		        			Succ[n][0] = queryid;
		        			Succ[n][1] = ipad;
		        			RTManager.Routing_Table.put(a,b);
		        			break;
		        		}
	        		
		        		else
		        		{
		        			Existingid = Succ[n][0];
		        			ExistingNib = Existingid.charAt(n);
		        			HexExistingNib = Character.getNumericValue(ExistingNib);
		        			System.out.println("HexExistingNib :"+HexExistingNib);
		        			System.out.println("existing Nib  :"+HexExistingNib);
		        			System.out.println("HexQueryNib" +HexQueryNib);
	        			
		        			if(HexQueryNib==HexExistingNib)
		        			{
		        				if(queryid.compareTo( Existingid)<0)
		        				{
		        					Succ[n][0] = queryid;
	        						Succ[n][1] = ipad;
	        						RTManager.Routing_Table.put(a,b);
	    							break;
		        				}
		        			}
	        			
		        			else if(HexQueryNib<HexExistingNib)
		        			{
		        				Succ[n][0] = queryid;
		        				Succ[n][1] = ipad;
		        				RTManager.Routing_Table.put(a,b);
		        				break;	
		        			}
		        			else
		        			{
	        				
		        			}
		        			break;
		        		}
	        
		        	}
		    	
		        	else if (HexQueryNib > SuccRg)
		        	{
		        		System.out.println("mid");
	        		
		        		if(Mid[n][0]==null)
		        		{
	        		
		        			System.out.println("1");
	                	
		        			Mid[n][0] = queryid;
		        			Mid[n][1] = ipad;
		        			RTManager.Routing_Table.put(a,b);
		        			break;
		        		}
	        		
		        		else
		        		{
		        			System.out.println("2e11331");
	            		
		        			Existingid = Mid[n][0];
		        			ExistingNib = Existingid.charAt(n);
		        			HexExistingNib = Character.getNumericValue(ExistingNib);
		        			HalfwayNibHex = HexNodeNib+8;
		        			System.out.println("existing Nib  :"+HexExistingNib);
		        			System.out.println("HexQueryNib" +HexQueryNib);
	    			
		        			System.out.println("Existing Nib :"+HexExistingNib);
	        			
		        			if(HexQueryNib==HexExistingNib)
		        			{

		        				if(((queryid.compareTo( Existingid)>0) && (HexQueryNib<HalfwayNibHex))|| ((queryid.compareTo( Existingid)<0 &&(HexQueryNib>=HalfwayNibHex))))
		        				{
		        					Mid[n][0] = queryid;
	        						Mid[n][1] = ipad;
	        						RTManager.Routing_Table.put(a,b);
	    							break;
		        				}
		        			}

		        			else
		        			{

		        				System.out.println("HexQueryNib is "+HexQueryNib);
	        		
		        				if(HexQueryNib<HalfwayNibHex && HexQueryNib>HexExistingNib)//problrmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm
		        				{
		        					Mid[n][0] = queryid;
		        					Mid[n][1] = ipad;
		        					RTManager.Routing_Table.put(a,b);
		        					break;	
		        				}
	        				
		        				else if(HexQueryNib>HalfwayNibHex && HexQueryNib<HexExistingNib)
		        				{
		        					Mid[n][0] = queryid;
		        					Mid[n][1] = ipad;
		        					RTManager.Routing_Table.put(a,b);
		        					break;		
		        				}
		        			}
		        			break;
		        		}
		        		//SysOutCtrl.SysoutSet("we are in -ve loop 3",3);
		        	}
		        break;
	        }

	//===========================================================================================
	//2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	2	
	//===========================================================================================	        
	   	   
	        else if(SuccRg <= 15)
	        {
	        	System.out.println("we are in Normal loop");

	        	System.out.println("HexNodeNib is :"+HexNodeNib);
	        	System.out.println("HexQueryNib is :"+HexQueryNib);
		        
	        	System.out.println("PredRg is :"+PredRg);
	        	System.out.println("SuccRg is :"+SuccRg);
		        

	        	if (HexNodeNib > HexQueryNib && HexQueryNib >= PredRg)
	        	{
	        		SysOutCtrl.SysoutSet("we are in Normal loop 1",3);
	        		
	        		if(Pred[n][0]==null)
	        		{
	        			Pred[n][0] = queryid;
	        			Pred[n][1] = ipad;
	        			RTManager.Routing_Table.put(a,b);
	        			break;
	        		}
	        		
	        		else
	        		{
	        			Existingid = Pred[n][0];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
	        			
	        			if(HexQueryNib==HexExistingNib)
        				{
	        				if(queryid.compareTo(Existingid)>0)
	        				{
	        					Pred[n][0] = queryid;
        						Pred[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
        				}
	        			else if(HexQueryNib>HexExistingNib)
	        			{
	        				Pred[n][0] = queryid;
	            			Pred[n][1] = ipad;
	            			RTManager.Routing_Table.put(a,b);
	                		break;	
	        			}
	        			else
	        			{
	        				
	        			}
	        		break;
	        		}
	        		
	        	}
	        		
	        	else if(HexQueryNib < PredRg || HexQueryNib > SuccRg)
	        	{
	        		System.out.println("mid");
	        		
	        		if(Mid[n][0]==null)
	        		{
	        		
	        			System.out.println("2nm,n");
	                	
	        		Mid[n][0] = queryid;
	        		Mid[n][1] = ipad;
	        		RTManager.Routing_Table.put(a,b);
	        		break;
	        		}
	        		
	        		else
	        		{
	        			System.out.println("2");
	            		
	        			Existingid = Mid[n][0];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			HalfwayNibHex= HexNodeNib+8;
	        			
	    			
	        			System.out.println("Existing Nib :"+HexExistingNib);
	        			
	        			if(HexQueryNib==HexExistingNib)
        				{
	        				if(HexQueryNib>=10 && ((queryid.compareTo( Existingid)>0) && (HexQueryNib<HalfwayNibHex))|| ((queryid.compareTo( Existingid)<0 &&(HexQueryNib>=HalfwayNibHex))))
	        				{
	        					Mid[n][0] = queryid;
        						Mid[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}else if(HexQueryNib>=0 && ((queryid.compareTo( Existingid)>0) && (HexQueryNib<(HexNodeNib-8)))|| ((queryid.compareTo( Existingid)<0 &&(HexQueryNib>=(HexNodeNib-8)))))
	        				{
	        					Mid[n][0] = queryid;
        						Mid[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
        				}
	        			else if(0<=HexQueryNib &&  HexQueryNib<=6)//for node B the mid range is (0 to 6 )
	        			{
	        				HexQueryNib=HexQueryNib+16;
	        			}
	        			if(Math.abs(HexQueryNib-HalfwayNibHex)<Math.abs(HexExistingNib-HalfwayNibHex))
	        			{
	        				Mid[n][0] = queryid;
	    	        		Mid[n][1] = ipad;
	    	        		RTManager.Routing_Table.put(a,b);
	    	        		System.out.println("condition 1");
	    	        		break;
	        			}

	        		break;
	        		}
	        	}
	        	

	        	else 
	        	{
	        		SysOutCtrl.SysoutSet("we are in Normal loop 3",3);
	        		
	        		if(Succ[n][0]==null)
	        		{
	        		Succ[n][0] = queryid;
	        		Succ[n][1] = ipad;
	        		RTManager.Routing_Table.put(a,b);
	        		break;
	        		}
	        		
	        		else
	        		{
	        			Existingid = Succ[n][0];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			SysOutCtrl.SysoutSet("HexExisting Nib :"+HexExistingNib,3);
	        			
	        			if(HexQueryNib==HexExistingNib)
	        			{
	        				if(queryid.compareTo(Existingid)<0)
	        				{
	        					Succ[n][0] = queryid;
        						Succ[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
	        			}
	        			
	        			else if(HexQueryNib<HexExistingNib)
	        			{
	        				Succ[n][0] = queryid;
	                		Succ[n][1] = ipad;
	                		RTManager.Routing_Table.put(a,b);
	                		break;	
	        			}
	        			else
	        			{
	        				
	        			}
	        		break;
	        		}
	        	}
	         
	        }		
	//===========================================================================================
	//3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3	3 
	//===========================================================================================	        

	        
	        else if (SuccRg > 15)
	        {

	        	System.out.println("We are in >15 loop");
	        	
	        	System.out.println("HexNodeNib is :"+HexNodeNib);
	        	System.out.println("HexQueryNib is :"+HexQueryNib);
	        	
	        	System.out.println("PredRg is :"+PredRg);
	        	System.out.println("SuccRg is :"+SuccRg);
		        int ValueToCompareQuery=HexQueryNib;;
		        
		        if(HexQueryNib<=3)
		        {
		        	ValueToCompareQuery=16+HexQueryNib;
		        }	
		        if(ValueToCompareQuery > HexNodeNib && (ValueToCompareQuery-HexNodeNib)<=4)
	        	{
	        		if(Succ[n][0]==null)
	        		{
	        		Succ[n][0] = queryid;
	        		Succ[n][1] = ipad;
	        		RTManager.Routing_Table.put(a,b);
	        		break;
	        		}
	        		
	        		else
	        		{
	        			Existingid = Succ[n][0];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			System.out.println("HexExistingNib 456 :"+HexExistingNib);
	        			int ValueToCompareExistingNib=HexExistingNib;
	        			if(HexExistingNib<=3)
	        	        {
	        				ValueToCompareExistingNib=16+HexExistingNib;
	        	        }
	        			
	        			if( HexQueryNib==HexExistingNib)
	        			{
	        				if(queryid.compareTo(Existingid)<0 )
	        				{
	        					Succ[n][0] = queryid;
        						Succ[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
	        			}
	        			
	        			else if(ValueToCompareQuery<ValueToCompareExistingNib)
	        			{
	        				Succ[n][0] = queryid;
	                		Succ[n][1] = ipad;
	                		System.out.println("234");
	                		RTManager.Routing_Table.put(a,b);
	                		break;	
	        			}
	        			else
	        			{
	        				
	        			}
	        		break;
	        		}
	        	}
	        	
	        	else if (HexQueryNib >= PredRg)
	        	{
	        		if(Pred[n][0]==null)
	        		{
	        		Pred[n][0] = queryid;
	        		Pred[n][1] = ipad;
	        		RTManager.Routing_Table.put(a,b);
	        		break;
	        		}
	        		
	        		else
	        		{
	        			Existingid = Pred[n][0];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
	        			
	        			if(HexQueryNib==HexExistingNib)
	        			{
	        				if(queryid.compareTo(Existingid)>0 )
	        				{
	        					Pred[n][0] = queryid;
        						Pred[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
	        			}
	        			
	        			else if(HexQueryNib>HexExistingNib)
	        			{
	        				Pred[n][0] = queryid;
	                		Pred[n][1] = ipad;
	                		RTManager.Routing_Table.put(a,b);
	                		break;	
	        			}
	        			else
	        			{
	        				
	        			}
	        		break;
	        		}
	        		
	        	}
	        	
	        	else
	        	{
	        		System.out.println("mid");
	        		
	        		
	        		if(Mid[n][0]==null)
	        		{
	        		
	        			SysOutCtrl.SysoutSet("1",3);
	                	
	        			Mid[n][0] = queryid;
	        			Mid[n][1] = ipad;
	        			RTManager.Routing_Table.put(a,b);
	        			break;
	        		}
	        		
	        		else
	        		{
	        			Existingid = Mid[n][0];
		        		ExistingNib = Existingid.charAt(n);
		        		HexExistingNib = Character.getNumericValue(ExistingNib);
		        		int CentreOfMid=HexNodeNib-8;
	        			
		        		System.out.println("CentreOfMid :"+CentreOfMid);
	        	       			
	        			if(HexQueryNib==HexExistingNib)
	        			{
	        				System.out.println("check");
	        				if((HexQueryNib<CentreOfMid && queryid.compareTo(Existingid)>0 )||(HexQueryNib>CentreOfMid && queryid.compareTo(Existingid)<0 ))
	        				{
	        					System.out.println("check12");
	        					Mid[n][0] = queryid;
        						Mid[n][1] = ipad;
        						RTManager.Routing_Table.put(a,b);
    							break;
	        				}
	        				
	        			}
	        			
	        			else if(Math.abs(HexQueryNib-CentreOfMid)<Math.abs(HexExistingNib-CentreOfMid))
	        			{
	        				System.out.println("HexQueryNib"+HexQueryNib);
	        				System.out.println("CentreOfMid"+CentreOfMid);
	        				System.out.println("HexExistingNib"+HexExistingNib);
	        				Mid[n][0] = queryid;
	        				Mid[n][1] = ipad;
	        				RTManager.Routing_Table.put(a,b);
	        				System.out.println("condition 1");
	        				break;
	        			}

	          
	        		}
	        				
	        	}
		        
	        }
	        break;
	        		
	    }	
	
	
	
	        			
	   
	   	try
	   	{
	   		boolean succ = false;
	   		for(int k=0;k<40;k++)
	   		{
	   			for(int j=0;j<2;j++)
	   			{
	   				if(Succ[k][j].equals(queryid))
	   				{	
	   					Succ[k][j+1] = ipad;
	   					succ = true;
	   					RTManager.Routing_Table.put(a,b);
	   					break;
	   				}
	   			}	
	   			if(succ)
	   				break;
	   		}
		
	   		boolean mid = false;
	   		for(int k=0;k<40;k++)
	   		{
	   			for(int j=0;j<2;j++)
	   			{
	   				if(Mid[k][j].equals(queryid))
	   				{	
	   					Mid[k][j+1] = ipad;
	   					succ = true;
	   					RTManager.Routing_Table.put(a,b);
	   					break;
	   				}
	   			}	
	   			if(mid)
	   				break;
	   		}
		
	   		boolean pred = false;
	   		for(int k=0;k<40;k++)
	   		{
	   			for(int j=0;j<2;j++)
	   			{
	   				if(Pred[k][j].equals(queryid))
	   				{	
	   					Pred[k][j+1] = ipad;
	   					pred = true;
	   					RTManager.Routing_Table.put(a,b);
	   					break;
	   				}
	   			}	
	   			if(pred)
	   				break;
	   		}
	   	}
	   	catch(NullPointerException e)
	   	{
			
		}
		
	//    PrintRT9 .PrintMatrix();
	   	
	//	RTManager.Routing_Table.put(a,b);
		
		Save_Retrieve_RT.Save_RT save1 = new Save_Retrieve_RT.Save_RT();
		save1.Save_RTNow();
	    
		}
	}

	class PrintRT9 extends RTManager
	{
		public static void PrintMatrix()
		{
		//	System.out.println("The Node Id of self node   : "+OverlayManagement.myNodeId);
			SysOutCtrl.SysoutSet("",2);
			for (int i = 0; i < Pred.length; i++)
			{	
				if(Pred[i][0]!=null)
				{
					System.out.println("Pred Rg Element at index " + i +" : "+ Pred[i][0]);  
					RTManager.Routing_Table.put(Pred[i][0],Pred[i][1]);
				}
			}
			SysOutCtrl.SysoutSet("",2);
			for (int i = 0; i < Succ.length; i++)
			{
				if(Succ[i][0]!=null)
				{
					System.out.println("Succ Rg Element at index " + i +" : "+ Succ[i][0]);
					RTManager.Routing_Table.put(Succ[i][0],Succ[i][1]);
				}
			}
			SysOutCtrl.SysoutSet("",2);
			for (int i = 0; i < Mid.length; i++)
			{
				if(Mid[i][0]!=null)
				{
					System.out.println("Mid Rg Element at index " + i +" : "+ Mid[i][0]);
					RTManager.Routing_Table.put(Mid[i][0],Mid[i][1]);
				}
			}

		}

	}
