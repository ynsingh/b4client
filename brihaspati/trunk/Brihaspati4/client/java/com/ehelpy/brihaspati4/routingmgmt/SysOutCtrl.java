package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 29 Apr 2018

public class SysOutCtrl extends RMThreadPrimary
{
//	This is used by the query managing modules for controlling console outputs
//	basic thread view for flow analysis 1
//	Debug 2
//	Deep debug 3
	
	public static void SysoutSet(String Print,int OutCtrl)
	{
		String PrintThis = Print;

		String Dply =null;
		String Imp =null;
		String BasicDBug =null;
		String DeepDbug =null;

		
		int ctrlThis = OutCtrl;
		
		switch (com.ehelpy.brihaspati4.authenticate.ClientMain.CtrlConsoleOut)
		{
		
		case 0 :
			
		if(ctrlThis<1)// Deployment Mode, set CtrlConsoleOut = 0 
		{
			Dply=PrintThis;
			System.out.println(Dply);
			
		}
		break;
	
		case 1 :
		if(ctrlThis<=1)// Basic Debug, set CtrlConsoleOut = 1
		{
			Imp=PrintThis;
			System.out.println(Imp);
			
		}
		break;

		
		case 2 :
		if(ctrlThis<=2)// Advanced Debug, set CtrlConsoleOut = 2
		{
			BasicDBug=PrintThis;
			System.out.println(BasicDBug);
			
		}
		break;
		
		case 3 :
		if(ctrlThis<=3)// Deep Debug, set CtrlConsoleOut = 3
		{
			DeepDbug =PrintThis;
			System.out.println(DeepDbug);
			
		}
		break;
	
		}
	}
	
	
	//added for single argument
	// outctrl=3
	public static void SysoutSet(String Print)
	{
		String PrintThis = Print;

		String Dply =null;
		String Imp =null;
		String BasicDBug =null;
		String DeepDbug =null;

		
		int ctrlThis = 3;
		
		switch (com.ehelpy.brihaspati4.authenticate.ClientMain.CtrlConsoleOut)
		{
		
		case 0 :
			
		if(ctrlThis<1)// Deployment Mode, set CtrlConsoleOut = 0 
		{
			Dply=PrintThis;
			System.out.println(Dply);
			
		}
		break;
	
		case 1 :
		if(ctrlThis<=1)// Basic Debug, set CtrlConsoleOut = 1
		{
			Imp=PrintThis;
			System.out.println(Imp);
			
		}
		break;

		
		case 2 :
		if(ctrlThis<=2)// Advanced Debug, set CtrlConsoleOut = 2
		{
			BasicDBug=PrintThis;
			System.out.println(BasicDBug);
			
		}
		break;
		
		case 3 :
		if(ctrlThis<=3)// Deep Debug, set CtrlConsoleOut = 3
		{
			DeepDbug =PrintThis;
			System.out.println(DeepDbug);
			
		}
		break;
	
		}
	}
}
