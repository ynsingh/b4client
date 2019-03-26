package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 15 Apr 2018 1215h
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RTRdr extends RTUpdate9
{
	public String[][] PickRT() //throws FileNotFoundException
	{
		CheckRTExists CheckRT = new CheckRTExists();
		boolean RTExists = CheckRT.ImportRT();
		
		if(RTExists = true)
		{	
			try {
				
//				This is the file which will be loaded as your source RT for populating the RT.
//				If not found null will be initiated by 
				
				BR = new BufferedReader(new FileReader("RTP2P.txt"));
//				BR = new BufferedReader(new FileReader("D:\\roy\\aaaaaaroy\\IITK\\Java practice\\Wk space prac\\Trial\\RTP2P.txt"));
//				Place simulated RT here for testing purpose, use Random Node genr for same				
				
				try 
				{
					StringBuffer stringBuffer = new StringBuffer();
					String line;
					
					for(int i=0;i<4;i++)
					{
						
						if(i==0)
						{
							line=BR.readLine();
						}				
						if(i==1)
							{
								
							line=BR.readLine();
							SysOutCtrl.SysoutSet("2nd line :"+line,3);
							int readtill = line.length();
							SysOutCtrl.SysoutSet("readtill "+readtill+"in first row",3);
							
							for(int j=0,l=0;l<40;j=j+40,l++)
								{
								if(j+40<=readtill)
								{    
								String Node = line.substring(j,j+40);
								RoutingInptBuff[l][0] = Node;
								SysOutCtrl.SysoutSet("RoutingInptBuff[0] :"+RoutingInptBuff[0],3);
								}
								
								else
								{
									break;
								}
								}
							}
							
						
					
						if(i==2)
						{
							
							line=BR.readLine();
							int readtill = line.length();
							SysOutCtrl.SysoutSet("readtill "+readtill+"in second row",3);
							
							for(int l=0,j=0;l<40;j=j+40,l++)
							{
						
								if(j+40<=readtill)
								{    
								String Node = line.substring(j,j+40);
//								String Node = line.substring(j,j+40);
								RoutingInptBuff[l+40][0] = Node;
								SysOutCtrl.SysoutSet("RoutingInptBuff[l+40] :"+RoutingInptBuff[l+40],3);
								}
								
								else
								{
									break;
								}
							}
						}
							
					
						if(i==3)
						{
							
							line=BR.readLine();
							int readtill = line.length();
							SysOutCtrl.SysoutSet("readtill "+readtill+"in second row",3);
							
							for(int l=0,j=0;l<40;j=j+40,l++)
							{
									if(j+40<=readtill)
									{    
									String Node = line.substring(j,j+40);
//									String Node = line.substring(j,j+40);
									RoutingInptBuff[l+80][0] = Node;
									SysOutCtrl.SysoutSet("RoutingInptBuff[l+80]"+RoutingInptBuff[l+80],3);
									}
									
									else
									{
										break;
									}		
							}
						}
						
							stringBuffer.append("\n");
					}
						
					BR.close();
					SysOutCtrl.SysoutSet("Contents of file: ",2);
					
				}
				 catch (IOException e) 
				{
					 SysOutCtrl.SysoutSet("File cannot be found",3);
					e.printStackTrace();
				}
				
				SysOutCtrl.SysoutSet("RoutingInptBuff is as follows :",3);
				
				for(int m=0;m<120;m++)
			    {
					if(RoutingInptBuff[m]!=null)
						SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m],3);
			    }
			
			
			
			
			
			} 
			catch (FileNotFoundException e1) 
				{
				// TODO Auto-generated catch block
//				e1.printStackTrace();
				SysOutCtrl.SysoutSet("Initialising Null RT... ",1);
				
				for(int m=0;m<120;m++)
			    {
					RoutingInptBuff[m]=null;
			    	SysOutCtrl.SysoutSet(m+" "+RoutingInptBuff[m],3);
			    }
				}
		}	
	
	return RoutingInptBuff;
	
	}

}
