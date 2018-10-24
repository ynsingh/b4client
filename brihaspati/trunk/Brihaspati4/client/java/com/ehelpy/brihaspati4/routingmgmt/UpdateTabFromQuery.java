package com.ehelpy.brihaspati4.routingmgmt;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

// Major Niladri Roy 3 may 2018
public class UpdateTabFromQuery extends RTUpdate9
{
	
//	this class updates the table from queries received, and pred and succ tables.
	
	public synchronized void NextEntry(String a)
	{ 
    String queryid = a; // this String is taken from CM Node_id_list which is generated
    					// from the queries as recd by it. We take the from node ids as they 
    					// are comfirmed to be existing and active
    
    String Existingid = null;

    // this converts the char to int for comparisons

	char A=10;char B=11;char C=12;char D=13;char E=14;char F=15;// this converts the char to int
	int HexA = Character.getNumericValue(A);
    int HexB = Character.getNumericValue(B);
    int HexC = Character.getNumericValue(C);
    int HexD = Character.getNumericValue(D);
    int HexE = Character.getNumericValue(E);
    int HexF = Character.getNumericValue(F);

    for(int n = 0; n < OverlayManagement.myNodeId.length();n=n+1)
    {
    	char NodeNib = nodeid.charAt(n);
        char QueryNib = queryid.charAt(n);
        char ExistingNib;
        char HalfwayNib;
        
        int HexNodeNib = Character.getNumericValue(NodeNib);
        int HexQueryNib = Character.getNumericValue(QueryNib);
        int HexExistingNib;        
        double HalfwayNibHex;
        
        int PredRg = HexNodeNib-4;
        int SuccRg = HexNodeNib+4;

        if(HexNodeNib == HexQueryNib)
        {
        	if(n==(nodeid.length())-1)
        	{
        		SysOutCtrl.SysoutSet("The query is for Self Node",3);
        	}
        	else
        	{
        	SysOutCtrl.SysoutSet(n+" Both Nibbles are equal, moving to next Nib",3);
        	n=n++;
        	}
        }

//===========================================================================================
//1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	1	11	1	11		
//===========================================================================================	        

        else if(PredRg <= 0)
	    {   
        	SysOutCtrl.SysoutSet("we are in -ve loop",3);

	    	SysOutCtrl.SysoutSet("HexNodeNib is :"+HexNodeNib,3);
	    	SysOutCtrl.SysoutSet("HexQueryNib is :"+HexQueryNib,3);
	    	
	    	SysOutCtrl.SysoutSet("PredRg is :"+PredRg,3);
	        SysOutCtrl.SysoutSet("SuccRg is :"+SuccRg,3);
	        
	        if(HexQueryNib<4)
	        {
	        	if (HexQueryNib >= PredRg)// && HexQueryNib >= 16+PredRg)
	        	{
	    		SysOutCtrl.SysoutSet("we are in -ve loop 1",3);
	    		int UpdPredRg = 16+PredRg;
	  	    	SysOutCtrl.SysoutSet("Updated Pred Rg is :"+UpdPredRg,3);
	  	    	
	  	    		if(Pred[n]==null)
	  	    		{
        			Pred[n] = queryid;
        			break;
	  	    		}
        		
	  	    		else
	  	    		{
        			Existingid = Pred[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("existing node  :"+HexExistingNib,3);
        			
        				if(HexQueryNib==HexExistingNib)
        				{
        				//n++;
        				}
        				else 
        				{	
        					if(HexExistingNib>4)
        					{
        						Pred[n] = queryid;
    							break;
    					
        					}
        					else if(HexExistingNib<HexQueryNib)
        					{
        						Pred[n] = queryid;
    							break;
    					
        					}
        				break;	
        				}
	  	    		}
	  	    		break;
        			}
	        	}
	    	
	        	else if(HexQueryNib >= PredRg && HexQueryNib >= 16+PredRg)
	        	{
	        		SysOutCtrl.SysoutSet("we are in -ve loop 11",3);
		    		int UpdPredRg = 16+PredRg;
		    		SysOutCtrl.SysoutSet("Updated Pred Rg is :"+UpdPredRg,3);
		  	    	
		  	    		if(Pred[n]==null)
		  	    		{
	        			Pred[n] = queryid;
	        			break;
		  	    		}
	        		
		  	    		else
		  	    		{
	        			Existingid = Pred[n];
	        			ExistingNib = Existingid.charAt(n);
	        			HexExistingNib = Character.getNumericValue(ExistingNib);
	        			SysOutCtrl.SysoutSet("existing node  :"+HexExistingNib,3);
	        			
	        				if(HexQueryNib==HexExistingNib)
	        				{
	        				//n++;
	        				}
	        				else 
	        				{
	        					if(HexQueryNib>HexExistingNib)
	        					{
	        					Pred[n] = queryid;
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
	        
	        
	        
	        
	        
	        else if (HexQueryNib <= SuccRg && HexQueryNib > HexNodeNib)
	    	{
	        	SysOutCtrl.SysoutSet("we are in Normal loop 3",3);
        		
        		if(Succ[n]==null)
        		{
        		Succ[n] = queryid;
        		break;
        		}
        		
        		else
        		{
        			Existingid = Succ[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{
        				//n++;
        			}
        			
        			else if(HexQueryNib<HexExistingNib)
        			{
        				Succ[n] = queryid;
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
        		SysOutCtrl.SysoutSet("mid",3);
        		
        		if(Mid[n]==null)
        		{
        		
        		SysOutCtrl.SysoutSet("1",3);
                	
        		Mid[n] = queryid;
        		break;
        		}
        		
        		else
        		{
            	SysOutCtrl.SysoutSet("2",3);
            		
        		Existingid = Mid[n];
        		ExistingNib = Existingid.charAt(n);
        		HexExistingNib = Character.getNumericValue(ExistingNib);
        		HalfwayNibHex = HexNodeNib+8;
    			
        		SysOutCtrl.SysoutSet("Existing Nib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{

                		SysOutCtrl.SysoutSet("21",3);
        				
        			}

        			else
        			{

                		SysOutCtrl.SysoutSet("HexQueryNib is "+HexQueryNib,3);
        		
                		if(HexQueryNib<HalfwayNibHex && HexQueryNib<HexExistingNib)
        				{
    	        			Mid[n] = queryid;
                    		break;	
            			}
        				
                		else
                		{
        						
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
        	SysOutCtrl.SysoutSet("we are in Normal loop",3);

        	SysOutCtrl.SysoutSet("HexNodeNib is :"+HexNodeNib,3);
	        SysOutCtrl.SysoutSet("HexQueryNib is :"+HexQueryNib,3);
	        
	        SysOutCtrl.SysoutSet("PredRg is :"+PredRg,3);
	        SysOutCtrl.SysoutSet("SuccRg is :"+SuccRg,3);
	        

        	if (HexNodeNib > HexQueryNib && HexQueryNib >= PredRg)
        	{
        		SysOutCtrl.SysoutSet("we are in Normal loop 1",3);
        		
        		if(Pred[n]==null)
        		{
        			Pred[n] = queryid;
        			break;
        		}
        		
        		else
        		{
        			Existingid = Pred[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{
        				//n++;
        			}
        			else if(HexQueryNib>HexExistingNib)
        			{
        				Pred[n] = queryid;
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
        		SysOutCtrl.SysoutSet("mid",3);
        		
        		if(Mid[n]==null)
        		{
        		
        		SysOutCtrl.SysoutSet("1",3);
                	
        		Mid[n] = queryid;
        		break;
        		}
        		
        		else
        		{
            	SysOutCtrl.SysoutSet("2",3);
            		
        		Existingid = Mid[n];
        		ExistingNib = Existingid.charAt(n);
        		HexExistingNib = Character.getNumericValue(ExistingNib);
        		double DblHexNodeNib = HexNodeNib;
        		HalfwayNibHex = DblHexNodeNib/2;
    			double Ceiling = Math.ceil(HalfwayNibHex);
    			
        		SysOutCtrl.SysoutSet("Existing Nib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{

                		SysOutCtrl.SysoutSet("21",3);
        				
        			}

        			else
        			{

                		SysOutCtrl.SysoutSet("22",3);
        		
                		if(HexQueryNib>Ceiling)
        				{
    	        			Mid[n] = queryid;
                    		break;	
            			}
        				
                		else
                		{
        						
        		       	}
        			}
        		break;
        		}
        	}
        	

        	else 
        	{
        		SysOutCtrl.SysoutSet("we are in Normal loop 3",3);
        		
        		if(Succ[n]==null)
        		{
        		Succ[n] = queryid;
        		break;
        		}
        		
        		else
        		{
        			Existingid = Succ[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("HexExisting Nib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{
        				//n++;
        			}
        			
        			else if(HexQueryNib<HexExistingNib)
        			{
        				Succ[n] = queryid;
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

        	SysOutCtrl.SysoutSet("We are in >15 loop",3);
        	
        	SysOutCtrl.SysoutSet("HexNodeNib is :"+HexNodeNib,3);
	        SysOutCtrl.SysoutSet("HexQueryNib is :"+HexQueryNib,3);
        	
	        SysOutCtrl.SysoutSet("PredRg is :"+PredRg,3);
	        SysOutCtrl.SysoutSet("SuccRg is :"+SuccRg,3);
	        
        	if(HexQueryNib-16 <= SuccRg-16 && HexQueryNib > HexNodeNib)
        	{
        		if(Succ[n]==null)
        		{
        		Succ[n] = queryid;
        		break;
        		}
        		
        		else
        		{
        			Existingid = Succ[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{
        				//n++;
        			}
        			
        			else if(HexQueryNib<HexExistingNib)
        			{
        				Succ[n] = queryid;
                		break;	
        			}
        			else
        			{
        				
        			}
        		break;
        		}
        	}
        	
        	else if (HexQueryNib > PredRg)
        	{
        		if(Pred[n]==null)
        		{
        		Pred[n] = queryid;
        		break;
        		}
        		
        		else
        		{
        			Existingid = Pred[n];
        			ExistingNib = Existingid.charAt(n);
        			HexExistingNib = Character.getNumericValue(ExistingNib);
        			SysOutCtrl.SysoutSet("HexExistingNib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{
        				//n++;
        			}
        			
        			else if(HexQueryNib>HexExistingNib)
        			{
        				Pred[n] = queryid;
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
        		SysOutCtrl.SysoutSet("mid",3);
        		
        		if(Mid[n]==null)
        		{
        		
        		SysOutCtrl.SysoutSet("1",3);
                	
        		Mid[n] = queryid;
        		break;
        		}
        		
        		else
        		{
            	SysOutCtrl.SysoutSet("2",3);
            		
        		Existingid = Mid[n];
        		ExistingNib = Existingid.charAt(n);
        		HexExistingNib = Character.getNumericValue(ExistingNib);
        		double DblHexNodeNib = HexNodeNib;
        		HalfwayNibHex = DblHexNodeNib/2;
    			double Ceiling = Math.ceil(HalfwayNibHex);

    			
        		SysOutCtrl.SysoutSet("Existing Nib :"+HexExistingNib,3);
        			
        			if(HexQueryNib==HexExistingNib)
        			{

                		SysOutCtrl.SysoutSet("21",3);
        				
        			}
        			
        			else
        			{

                		SysOutCtrl.SysoutSet("22",3);
        		
                		if(HexQueryNib>Ceiling)
        				{

        	        		SysOutCtrl.SysoutSet("31",3);
        	        		
        					if(HexQueryNib>HexExistingNib && HexQueryNib<HexNodeNib)
                        	{
        						SysOutCtrl.SysoutSet("41",3);
                				Mid[n] = queryid;
                        		break;	
                			}
        					else if(HexQueryNib<HexExistingNib && HexQueryNib>HexNodeNib)
            				{
        						
        		        		SysOutCtrl.SysoutSet("42",3);
        		        		Mid[n] = queryid;
                        		break;
            				}
        					else
        					{
        						
        					}
        				}
        				
        				else
        				{
        	        		SysOutCtrl.SysoutSet("32",3);
        					break;	
                    		
        				}
        			break;
        			}
        				
        		}
        		
        	}
        		
        }
    }
    PrintRT9 PrintRT = new PrintRT9();
	PrintRT.PrintMatrix();
	
    
	}
}

class PrintRT9 extends UpdateTabFromQuery
{
	public void PrintMatrix()
	{
		SysOutCtrl.SysoutSet("The Node Id of self node   : "+nodeid,2);
		SysOutCtrl.SysoutSet("",2);
for (int i = 0; i < Pred.length; i++)
	if(Pred[i]!=null)
		SysOutCtrl.SysoutSet("Pred Rg Element at index " + i +" : "+ Pred[i],2);          
SysOutCtrl.SysoutSet("",2);
for (int i = 0; i < Succ.length; i++)
	if(Succ[i]!=null) 
		SysOutCtrl.SysoutSet("Succ Rg Element at index " + i +" : "+ Succ[i],2);          
SysOutCtrl.SysoutSet("",2);
for (int i = 0; i < Mid.length; i++)
	if(Mid[i]!=null) 
		SysOutCtrl.SysoutSet("Mid Rg Element at index " + i +" : "+ Mid[i],2);          

	}

}

