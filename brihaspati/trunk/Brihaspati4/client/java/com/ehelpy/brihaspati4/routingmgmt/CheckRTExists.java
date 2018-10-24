package com.ehelpy.brihaspati4.routingmgmt;
// Major Niladri Roy 24 Apr, 2018 1400h
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class CheckRTExists extends Save_Retrieve_RT
{
	public boolean ImportRT() 
// this checks whether there is a saved RT from last logout, if not
// then initiates null routing table	
	{
boolean RTExists = false;
		
		try {
			BR = new BufferedReader(new FileReader("RTP2P.xml"));
			SysOutCtrl.SysoutSet("RT found, initialising RT...",1);
			SysOutCtrl.SysoutSet("",1);
			RTExists = true;
			return RTExists;
		
			} 
		catch (FileNotFoundException e1) {
			
			SysOutCtrl.SysoutSet("RT DOES NOT EXIST",1);
			RTExists = false;
		}
		return RTExists;
		
	}
}
