package com.ehelpy.brihaspati4.DFS;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
public class Demo2
{
     

	public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException 
    {
		
		fileReceiver_Fetch();
		
    }
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
		        String pathName = fileName.replace('&', ':');
		        	        
		        long size = clientData.readLong();     
		        byte[] buffer = new byte[1024]; 
		        Path path = Paths.get(pathName.substring(0,fileName.lastIndexOf('\\')));
		        Files.createDirectories(path);
		        
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
		    }  
		  } 
}