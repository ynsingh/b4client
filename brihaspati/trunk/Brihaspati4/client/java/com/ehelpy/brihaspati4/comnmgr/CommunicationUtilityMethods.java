package com.ehelpy.brihaspati4.comnmgr;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ehelpy.brihaspati4.indexmanager.IndexManagement;
import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.RTUpdate9;
import com.ehelpy.brihaspati4.routingmgmt.Save_Retrieve_RT;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class CommunicationUtilityMethods extends CommunicationManager {

    public static void addQueryToReceiveBuffer(File inFile) // receive messages from other peers and save them in a
    // queue of
    // receivingBuffer
    {

        String str = inFile.getName();		// System.out.println("reply from addQueryFile method, Flename="+str);

       synchronized(lock_RecBuff_Main)
       {
        ReceivingBuffer.add(ReceivingBuffer.size(), inFile); // this statement will append the msg ie
        
        
        	// filename(Queryi.xml) in the receiving buffer
       	SysOutCtrl.SysoutSet("file added to receving buffer"+str);
       	SysOutCtrl.SysoutSet("Receiving Bufer: "+ReceivingBuffer);
       }
       	
    }
    public static void addQueryTransmittingBuffer(File inFile) 
    // TransmittingBuffer
    {
    	synchronized(lock_TransBuff_Main)
    	{
    		System.out.println("adding to transmit buff");
        	TransmittingBuffer.add(TransmittingBuffer.size(), inFile); 
    	}
       	
   }


    public File getQueryFile() // get a message from the front of the queue of receiving buffer // returns an
    // xml file
    {
    	synchronized(lock_RecBuff_Main)
    	{
        	return ReceivingBuffer.get(0);
    	}
   }

    public static void fileSender(String toNodeIp, String s) 
    {
        SysOutCtrl.SysoutSet("you are in fileSender method",2);
        SysOutCtrl.SysoutSet("Transmitting buffer status: "+TransmittingBuffer);
        
        boolean is_receiver_active = false;
        
        is_receiver_active = IsApplicationAlive_AtReceiver(toNodeIp);
        
        System.out.println("client alive status : "+is_receiver_active);
        
        if(is_receiver_active)
        {
            
        	int port = 2222;
                
        	System.out.println("IP SENT TO FILE SENDER : "+toNodeIp);
        
        	try 
        	{
        		Socket	sock = new Socket(toNodeIp, port);
        		
        		if (sock.isConnected())
        		{	
                    SysOutCtrl.SysoutSet("Client Socket is Connected...",2);
                    
                    byte[] mybytearray = s.getBytes();
                                
                    OutputStream os = sock.getOutputStream();
                    os.write(mybytearray, 0, mybytearray.length);
                	   	
                	os.flush();
                	sock.close();
        		}
        		else
        			sock.close();
        		
        	} 
        	catch (IOException e)
        	{
        		SysOutCtrl.SysoutSet("check connection");
        		SysOutCtrl.SysoutSet("Connection timed out: Please check your connectivity with"+ toNodeIp,1);
        	}
        	catch (NullPointerException e)
        	{
        		System.out.println("NullPointerException");
        	}
       }
    }
    
    public static void Application_Alive_Response_ByReciever()
    {
    	
    	ServerSocket servsock = null;
		
		try {
			servsock = new ServerSocket(4444);
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		
	    Socket sock = null;
		try {
			sock = servsock.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}

        if (!sock.isConnected())
          	System.out.println("Client Socket not Connected...");
        else {
            System.out.println("Client Socket Connected : " + sock);
        }	
              
        try {
			servsock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static boolean IsApplicationAlive_AtReceiver(String hostname)
    {
    	boolean IsAlive = false;
    	int port = 4444;
        	
    	if(!hostname.equals(null)||!hostname.isEmpty()||!hostname.equals("null")||!hostname.equals("")||!hostname.equals(" "))
    	{	
    		SocketAddress sock_address = new InetSocketAddress(hostname, port);
    		Socket sock = new Socket();
    		int timeout = 5000;
    	
    		try {
    			sock.connect(sock_address, timeout);
    			sock.close();
    			IsAlive = true;
    		} catch (SocketTimeoutException exception) {
    			// TODO Auto-generated catch block
    			System.out.println("socket timeout exception");
    		}catch(IOException exception) {
    			System.out.println("IO Exception");
    		}
    	}	
    	return IsAlive;
    	
    }
    
    public static synchronized void fileReceiver() throws IOException {

    	
        SysOutCtrl.SysoutSet("You are in File receiver method",2);

        ServerSocket servsock = new ServerSocket(2222);
		

        int fileReceiveIndex = 0;

        if (!servsock.isBound())
            SysOutCtrl.SysoutSet("Sever Socket not Bounded...",2);
        else
            SysOutCtrl.SysoutSet("Server Socket bounded to Port : " + servsock.getLocalPort(),1);
        	
        Socket sock = servsock.accept();
     	
        if (!sock.isConnected())
        {	
           SysOutCtrl.SysoutSet("Client Socket not Connected...",2);
           sock.close();
        }  
        else 
        {
           SysOutCtrl.SysoutSet("Client Socket Connected : " + sock,1);
           
           byte[] mybytearray = new byte[4096];
           InputStream is = sock.getInputStream();
           String fileName=getFileName();
           FileOutputStream fos = new FileOutputStream(fileName);

           BufferedOutputStream bos = new BufferedOutputStream(fos);
           int bytesRead = is.read(mybytearray, 0, mybytearray.length);
           int current = bytesRead;

           do {
             bytesRead = is.read(mybytearray, current, (mybytearray.length - current));
             if (bytesRead >= 0)
           	  current += bytesRead;
           } while (bytesRead > -1);

           bos.write(mybytearray, 0, current);
           bos.flush();

           bos.close();
           fos.flush();
           fos.close();
           sock.close();
               
           for (int i = 0; i < mybytearray.length; i++) {
              System.out.print(mybytearray[i] + " ");
           }
           SysOutCtrl.SysoutSet("File  downloaded (" + current + " bytes read)");
           File inFile = new File(fileName);

           SysOutCtrl.SysoutSet("File length after receiving" + inFile.length());
           SysOutCtrl.SysoutSet("Receiving file index"+fileReceiveIndex);
           SysOutCtrl.SysoutSet("inFile" + inFile.toString() + inFile.length() + "bytes");

           String[] xmlParsed =ParseXmlFile.ParseXml(inFile) ;
           System.out.println("file received is : "+xmlParsed[0]);
           if(xmlParsed[0].equals("0031"))
           {
           	CommunicationManager.RxBufferOM.add(inFile);
           }
           else
               addQueryToReceiveBuffer(inFile);
           
           fileReceiveIndex++;
        }
              
        servsock.close();

    }
    
    public static void sendXmlToNextNodeId()
    {

        SysOutCtrl.SysoutSet("you are in sendXmlToNextNodeId",2);
        
           	while (!TransmittingBuffer.isEmpty())// if OpBufferIM is not empty this if block will be executed
        	{
         	  File file = TransmittingBuffer.removeFirst();
           
        	  BufferedReader r = null;
        	  try 
        	  {
                r = new BufferedReader(new FileReader(file.toString()));
        	  }
        	  catch (FileNotFoundException e)
        	  {
                // TODO Auto-generated catch block
                e.printStackTrace();
        	  }
            
        	  String s = "";
        	  String line = null;
        	  try 
        	  {
                while ((line = r.readLine()) != null)
                {
                    s += line;
                }
        	  } 
        	  catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
        	  }
            
        	  String[] xmlParsed;
                      
        	  SysOutCtrl.SysoutSet("Xml file parsed:" + file,2);
           
        	  xmlParsed = ParseXmlFile.ParseXml(file);


        	  String tohashId="";
        	  
        	  tohashId = xmlParsed[1];
            
              String tonodeId=com.ehelpy.brihaspati4.routingmgmt.GiveNextHop.NextHop(tohashId);
            
              SysOutCtrl.SysoutSet("NextHop  from RoutingManagement for " + file + ":" + tonodeId,2);
              System.out.println("Querry hash : "+tohashId);
              System.out.println("Next Hop : "+tonodeId);
              
              String toIp = getIpFromMyIpTable(tonodeId); // calling function to get the ip address of the root node id

              System.out.println("Ip from Ip Table"+toIp);
  
              SysOutCtrl.SysoutSet("Destination Ip Add for " + file + ":" + toIp,2);
              SysOutCtrl.SysoutSet("Calling File Sender Method " ,2);
         
              fileSender(toIp, s);
          
          }
      SysOutCtrl.SysoutSet("Thread t2 reply-Transmitting Buffer found empty",2);
    }


   
    public static String getIpFromMyIpTable(String nodeId) {
        
        SysOutCtrl.SysoutSet("My IP Table:"+myIpTable);
        String ip = null;
        
        if(RTUpdate9.Routing_Table.containsKey(nodeId))
        	ip = RTUpdate9.Routing_Table.get(nodeId);
        
        else 
        {
        	if(CommunicationManager.myIpTable.containsKey(PredecessorSuccessor.mySuccessors[0]))
        		ip = CommunicationManager.myIpTable.get(PredecessorSuccessor.mySuccessors[0]);
        	
        	else if(CommunicationManager.myIpTable.isEmpty())
        	{
        		System.out.println("My ip table is empty");
        		ip = PresentIP.MyPresentIP();
        	}
        	
        	else
        	{
        		String nodeid = null;
        		
        		Set<String> keys1 =CommunicationManager.myIpTable.keySet();
	            for(String key : keys1)
		        {
		        	nodeid = key;
		        	System.out.println("getting 1st entry from ip table");
		        	ip = CommunicationManager.myIpTable.get(nodeid);
		        	break;
		        }
        	}
        }
        
        SysOutCtrl.SysoutSet("ip address from myIpTable"+ip,2);
        return ip;
    }
    
    public static void fillReceiveBuffer() {
        // TODO Auto-generated method stub
        SysOutCtrl.SysoutSet("you are in FillReceiveBuffer method",2);
        for(int i=0; i<1; i++)

        {   File fetchNextXml = new File("Query1"+i+".xml");
            addQueryToReceiveBuffer(fetchNextXml);


        }


    }
    static int index=10;
    public synchronized static String getFileName()
    {

        String fileName= "Query"+index+".xml";
        index++;
        while(index==20)
        {
            index=10;
        }
        return fileName;
    }
    public static boolean responsibleNode(String Pred,String Self,String key)
    {
        boolean bool=false;
                
        System.out.println("my node id"+OverlayManagement.myNodeId);
    
        List<String> list= new LinkedList<String>();
        list.add(Pred);
        list.add(Self);
        list.add(key);

        SysOutCtrl.SysoutSet("list"+list);
       
        Collections.sort(list);
        
        System.out.println("sorted list"+list);
        System.out.println("index of pred: "+list.indexOf(Pred));
        System.out.println("index of self: "+list.indexOf(Self));
        System.out.println("index of hash: "+list.indexOf(key));
     
        SysOutCtrl.SysoutSet("sorted list"+list);

        if(list.indexOf(Pred)<list.indexOf(Self))
        {
            if(list.indexOf(key)==1)
                bool=true;

        }
        if(list.indexOf(Pred)>list.indexOf(Self))
        {
            SysOutCtrl.SysoutSet("pred>self");
            if(list.indexOf(key)==2||list.indexOf(key)==0)
                bool=true;
        }
          
        return bool;
    }

    
   
}
