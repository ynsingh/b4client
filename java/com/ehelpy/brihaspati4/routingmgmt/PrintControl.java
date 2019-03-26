package com.ehelpy.brihaspati4.routingmgmt;

public class PrintControl  
{

	public static int PrintCtrl=0; 
	// this is set to 4 for deployment mode with Vital printouts
	// set to 3 for basic flow tracking
	// set to 2 for basic debugging along with Debug set to "yes"
	// set to 1 for deep debug along with DeepDebug set to "yes"
	
	
	public static String Debug = ""; // set to yes for basic debug
	public static String DeepDebug = "";// set to yes for deep debug
	public static String Print ="";
	public static String PrintforDebug ="";
	public static String PrintforDeepDebug ="";
	
	
	public void ChooseWhatToPrint ()
	{
		
		if(PrintCtrl==3) 
			{
			//String s="this works";
			System.out.println(Print);
			}
		
		else if(PrintCtrl<=2 && Debug=="yes")
			{
			System.out.println(PrintforDebug);
			
				if(PrintCtrl==1 && DeepDebug=="yes")
				{	
					System.out.println(PrintforDeepDebug);	
					System.out.println(PrintforDebug);
					System.out.println(Print);
					
				}
			
//				else if(PrintCtrl==1 && DeepDebug!="yes")
//				{
//				System.out.println("Please set DeepDebug to yes for PrintforDeepDebug");
//				
//				}
			}
		
//		else if(PrintCtrl<=2 && Debug!="yes")
//		{
//			System.out.println("Please set Debug to yes for PrintforDebug");
//		}

		
	}
}