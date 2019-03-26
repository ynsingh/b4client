package com.ehelpy.brihaspati4.voip;


import java.net.InetAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import javax.crypto.SecretKey;
import com.ehelpy.brihaspati4.authenticate.debug_level;

public class voip_rxcall extends Thread 

{
		public static ServerSocketChannel ss;
		final int port;
		public static SocketChannel s;
		static SecretKey secr_key = null;
		@SuppressWarnings("unused")
		private static long sym_key = 0;
		static int token = 0 ; 
		public static String[] received_ip = null;
		public static boolean flag = true;
	// public static void rxcall() throws IOException 
	 
	@SuppressWarnings("static-access")
	public voip_rxcall(ServerSocketChannel ss, int port) 
	    {
	        this.ss = ss;
	        this.port = port;
	        
	    }
	 @SuppressWarnings("static-access")
	
	 public void run() 
	    {
	        
	        
	        // running infinite loop for getting 
	        // client request
	        while(flag) 
	         {
	            
	            try
	            {
	                // socket object to receive incoming client requests
	                s = ss.accept();
	                // keep on checking the inputstream every 1 millisecond for any calling request
	                if(s==null) 
	                   {
	                	Thread.sleep(1000);
	                	debug_level.debug(0,"No input recd");	              
	                   }
	                else
	                  {
	               	   debug_level.debug(3,"A new client is connected : " + s);
	                   String received = socketch_read.ch_read(s); 
	                   // obtaining i/o streams
	                   received_ip = new String[2];
	                   received_ip = received.split("this is the seperator");
   	                   received_ip[0]= received_ip[0].replaceAll("this is the seperator", "");
	                   String rec_ip = new String(received_ip[1]);
   	                   InetAddress far_end_IP = InetAddress.getByName(rec_ip);
	                   debug_level.debug(0,"Assigning new thread for this client" + received_ip[0] + far_end_IP);
	                   s = s.open(); //socket channel opened
	                   //SocketAddress socketAddr = new InetSocketAddress(far_end_IP, 9999);
	                   //SocketAddress socketAddr = new InetSocketAddress(far_end_IP, Integer.valueOf(properties_access.read_property("client.properties","Rxsocch")));
	                   voip_connect_dialog.connect(s, received_ip[1].trim(),token);// dialogue box for approval of the client
	                   
	                  }
	            
	            }
	            
	          catch (Exception e)
	               {
	                	e.printStackTrace();
	                	
	                					   
	               }
	            
	        }
	  
	      }
	 
	  }
	
