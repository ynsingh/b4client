package com.ehelpy.brihaspati4.routingmgmt;

// Major Niladri Roy 29 Apr 2310h

import java.io.FileWriter;
import java.io.IOException;

public class BootUp 
{

	public static void main(String[] args) 
	{
				
		BootUp_Save_RT InitiateRT = new BootUp_Save_RT();
		
		InitiateRT.WriteBootRT();
	}	
}	

// this class is used for generating a specific Routing Table for any specific debugging.
// node ids entered here will populate the RT on startup
class BootUp_Save_RT 
		{
			String[] BootUp_RT = new String[120];
		
			FileWriter RTWriter = null;
			
// nodes used for populating RT on startup given here
			public void WriteBootRT()
			{
				BootUp_RT[0] ="6666666666666666666666666666666666666666";
				BootUp_RT[1] ="3333333333333333333333333333333333333333";
				BootUp_RT[2] ="9999999999999999999999999999999999999999";
				BootUp_RT[3] ="CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC";
				BootUp_RT[4] ="FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF";
//				BootUp_RT[5] ="";

				try {
					RTWriter = new FileWriter("RTP2P.xml");
//					RTWriter = new FileWriter("RTP2P.txt");
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				for(int i=0; i<BootUp_RT.length;i++)
				{
			    	if(BootUp_RT[i]!=null)
			    	{
			    		SysOutCtrl.SysoutSet(i+" Node ID to be written :"+ BootUp_RT[i],2);
			    		
			    		
			    		if(BootUp_RT[i]!=com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement.myNodeId)
			    		{	
			    			SysOutCtrl.SysoutSet(i+" Node in RT :"+ BootUp_RT[i],2);
			    			try 
			    			{
			    				RTWriter.write(BootUp_RT[i]);
			    			} 
			    			catch (IOException e) 
			    			{
			    				e.printStackTrace();
			    			}
			    		}
			    	}	
			    }
			    
				try 
			    {
					RTWriter.flush();
				} catch (IOException e) 
			    {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}



	}


