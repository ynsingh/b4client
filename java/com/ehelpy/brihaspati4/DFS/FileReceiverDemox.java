package com.ehelpy.brihaspati4.DFS;

import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.authenticate.emailid;

public class FileReceiverDemox {

	   
	     
	public static void fileReceiver_Fetch () throws IOException {     
	     
		
	    int bytesRead;  
	    int current = 0;  
		System.out.println("S: You are in File receiver method for Fettching");
	    ServerSocket serverSocket = null;  
	    serverSocket = new ServerSocket(13267);  
	         
	    while(true) {  
	        Socket clientSocket = null;  
	        clientSocket = serverSocket.accept();  
	        System.out.println("S: Client Socket Connected for Fetching: " + clientSocket);  
	        InputStream in = clientSocket.getInputStream();  
	           
	        DataInputStream clientData = new DataInputStream(in);   
	           
	        String fileName = clientData.readUTF();  
	        //String fileName =filename.replaceFirst(emailid.getemaild(),"");
	        String pathName = fileName.replace('&', ':');
	        	        
	        long size = clientData.readLong();     
	        byte[] buffer = new byte[1024]; 
	       // Path path = Paths.get(pathName.substring(0,fileName.lastIndexOf('\\')));
	       // Files.createDirectories(path);
	        
	        OutputStream output = new FileOutputStream(pathName);  
	        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
	        {     
	            output.write(buffer, 0, bytesRead);     
	            size -= bytesRead;     
	        }  
	           
	        // Closing the FileOutputStream handle
	        in.close();
	        clientData.close();
	        output.close();  
	        System.out.println("S: File length after receiving the Fetched data" + fileName.length());
	        
	        
	        System.out.println("S: trying to merge the chunks after they have been Received");
	        Timer Action_Receiver = new Timer();
			TimerTask merge_chunks = new TimerTask() {
				@Override
				public void run() 
				{	
					String File_Name=pathName.substring(0,pathName.indexOf("Chunk"));
					int No_Of_Chunk=DistFileSys.nodefilemap.get(File_Name);
					try {
						DistFileSysUtilityMethods.mergeFiles(File_Name, No_Of_Chunk);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
			Action_Receiver.schedule(merge_chunks,5000);
	    }  
	  }
	  
	  public static void fileReceiver_Store(String pathName) throws IOException 
		 {     
		     //here Pathname is the place where the user want to STORE all DHT files
				
			    int bytesRead;   
				System.out.println("S: You are in File receiver method for Storing the File in DHT");
			    ServerSocket serverSocket = null;  
			    serverSocket = new ServerSocket(13269);  //new port for storing of data chunk in DHT
			      
			    while(true) 
			    {  
			        Socket clientSocket = null;  
			        clientSocket = serverSocket.accept();  
			        System.out.println("S: Client Socket Connected : " + clientSocket);  
			        InputStream in = clientSocket.getInputStream();  
			           
			        DataInputStream clientData = new DataInputStream(in);   
			           
			        String fileName = clientData.readUTF();  //this read the data chunk "path" of the local file sysytem
			      		        
			        
			        long size = clientData.readLong();  // this read the size of the data chunk in local file system   
			        byte[] buffer = new byte[1024]; 
			        // we make the path in the root node by adding the pathName(place decided by root node)+ file Name(the data path name ))
			        Path path = Paths.get(pathName,"\\",fileName.substring(0,fileName.lastIndexOf('\\')));
			        Files.createDirectories(path);
			        String name= path.toString();
			        System.out.println("in receiver_strore method  "+name);
			        // data is saved at (pathName+fileName)
			        FileOutputStream output = new FileOutputStream(pathName+"\\"+fileName);  
			        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1)     
			        {     
			            output.write(buffer, 0, bytesRead);     
			            size -= bytesRead;     
			        }  
			           
			        // Closing the FileOutputStream handle
			        in.close();
			        clientData.close();
			        output.close();  
			        //serverSocket.close();
			        System.out.println("S: File length after receiving" + fileName.length());
			       
			    }
			    
			  } 
}
