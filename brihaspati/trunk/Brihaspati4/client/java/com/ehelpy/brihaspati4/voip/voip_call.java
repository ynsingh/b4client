package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import com.ehelpy.brihaspati4.authenticate.Gui;
import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

public class voip_call {
		private static SocketChannel socket;
		static boolean flag = false;
		static X509Certificate client_cert = null;
		static X509Certificate server_cert = null;
		static String selfemailid = null;
		static String tosend = null;
		static String received = null;
		static String args;
		static long sym_key = 0;
		
		static SecretKey secr_key = null;
		static int token = 1;
		//@SuppressWarnings("deprecation")
		

		public static void callstart_waiting(String called_emailid)
		{
			
			try 
			{
				//String IPAddr_Caller = "172.25.75.22";
				 String IPAddr_Caller = IndexManagementUtilityMethods.searchEmailId(called_emailid);
				 if(IPAddr_Caller =="TimedOut"||IPAddr_Caller=="SORRY! Called Person Is Not Active At The Moment.")
				 {
					 System.out.println("in window if block");
					 try {
						 B4services.ss.close();
					 } catch (IOException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 B4services.service();
				 }
				 else if(IPAddr_Caller.equals(PresentIP.MyPresentIP()))
				 {
					 JFrame frame1 = new JFrame("Message");
					 //show a joptionpane dialog using showMessageDialog
					 JOptionPane.showMessageDialog(frame1, "Sorry You Can't Call Yourself ");
					 try {
						 B4services.ss.close();
					 } catch (IOException e) {
						 // TODO Auto-generated catch block
						 e.printStackTrace();
					 }
					 B4services.service();
				 }
				 else
				 {	
					 System.out.println("in window else block");
					 voip_call.callstart(IPAddr_Caller);
				 }
			} catch (InterruptedException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
		public static void callstart(String IPAddr_Caller) 
		 {
		// Request Index manager for node id
		// Request Index manager for farend client IP address and and opening port
		   try
		     { 
		       int port = Integer.valueOf(properties_access.read_property("client.properties","Rxsocch"));
		       debug_level.debug(0,"The socket channel port is "+ port);
		       client_cert = ReadVerifyCert.returnClientCert();// get a copy of own cert for secure handshake
		       //String called_emailid	= email;
		
	        	
		       //Pass emailid to Index Manager to fetch the IP address
		       //String IPAddr_Caller =	IndexManagementUtilityMethods.searchEmailId(called_emailid);// later on replaced by the ip address fetched from the index manager
		      // String IPAddr_Caller = "172.25.76.161";
		       
		       if (IPAddr_Caller == null)
		          {
		    	   // IP address recieved; If Null then goto Welcome window; return to ClientMain
			        B4services.service();
			        return ;
		          }
		
			   debug_level.debug(0,"The destination IP is "+ IPAddr_Caller);
		// now it is required to open a socket channel to open communication with the peer
                socket = SocketChannel.open();
                    try 
                      {          
                        SocketAddress socketAddr = new InetSocketAddress(IPAddr_Caller, port);
                        socket.connect(socketAddr);
                      }
                    catch(Exception e) 
                      {
        	            socket.close();
        	            String msg="CALLED PERSON IS BUSY. PLEASE TRY AFTER SOME TIME";
        	            Gui.showMessageDialogBox(msg);
        	            try 
     	 	           {
     			        B4services.ss.close();
     		           } 
     			     catch (IOException e1) 
     			       {
     			         // TODO Auto-generated catch block
     			         e1.printStackTrace();
     		           }
        	            B4services.service();
        	            return;
        	            
                      }
        
    
      	
        	     Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(client_cert.toString());
        	     while (m.find()) 
        	        {
        		       selfemailid = m.group().toString(); 
        	        }  // get own email id
        	
        	     String IP=SystemIPAddress.getSystemIP(); //get own IP address
        	     debug_level.debug(0, "own IP sent is " + IP);
        	     tosend = selfemailid + "this is the seperator" + IP ; // own email id, ipaddress combined to send to farend for approval"
        	     socketch_write.ch_write(socket, tosend); // fwd to the farend
                 //socket.close();
                 //debug_level.debug(0, "mail & IP sent is " + tosend);
                 sym_key = socketserver.server_socket(Integer.valueOf(properties_access.read_property("client.properties","Txsocch")));// get the long random number for genr secretkey
          
                 debug_level.debug(0,"The sym key recd is "+ sym_key);
                 if (sym_key!=0)
                  {
            	   debug_level.debug(5,"The status of tx call is true ");
            	   //s.stop();
            	   voip_start.start(IPAddr_Caller, sym_key);
				
			      }
            
                  // the gui to control the ongoing call
                 else
                  {
            	   	return;
            	  }
            
		 }
		
	catch (Exception e)
		
		{
			e.printStackTrace();
		}
  
    finally
			
		     {
			     try 
	 	           {
			        B4services.ss.close();
		           } 
			     catch (IOException e1) 
			       {
			         // TODO Auto-generated catch block
			         e1.printStackTrace();
		           }
	         }
    return;
  }

}

