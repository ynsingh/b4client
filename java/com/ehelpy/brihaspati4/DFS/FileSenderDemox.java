package com.ehelpy.brihaspati4.DFS;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import com.ehelpy.brihaspati4.authenticate.emailid;

public class FileSenderDemox {
	
	 public static void fileSender_Fetch(String IpAdd,String myfile) throws IOException {  
	    	File myFile= new File(myfile);
	    	System.out.println("C: you are in fileSender DFS method to Fetch the File");
	   
	        Socket sock = new Socket(IpAdd, 13267);  
	        System.out.println("C: trying to est connection to Fetch the data");
	   
	        byte[] mybytearray = new byte[(int) myFile.length()];  
	           
	        FileInputStream fis = new FileInputStream(myFile);  
	        BufferedInputStream bis = new BufferedInputStream(fis);  
	           
	        DataInputStream dis = new DataInputStream(bis);     
	        dis.readFully(mybytearray, 0, mybytearray.length);  
	           
	        OutputStream os = sock.getOutputStream();  
	           
	        //Sending file name and file size to the server  
	        DataOutputStream dos = new DataOutputStream(os); 
	        
	        String abc=myFile.getPath();
	        System.out.println("abc   "+ abc);
	        String prefix=DistFileSysUtilityMethods.choose_WhereToSave()+"\\";
	        System.out.println("prefix  "+ prefix);
	        int len=prefix.length();
	        String lkj=abc.substring(len);
	        String qw=lkj.replace('&',':' );//as :cannot come in file name
	        System.out.println("qw   "+qw);
	        dos.writeUTF(qw); 
	        
	    	
	        dos.writeLong(mybytearray.length);     
	        dos.write(mybytearray, 0, mybytearray.length);     
	        dos.flush();  
	           
	        //Sending file data to the server  
	        //os.write(mybytearray, 0, mybytearray.length);  
	       // os.flush();  
	           
	        //Closing socket
	        os.close();
	        dos.close();  
	        sock.close();  
	        System.out.println("C: socket is closed");
	    } 
public static void fileSender_Store(String IpAdd,File myFile) throws IOException {  
	    	
	    	System.out.println("C: you are in fileSender DFS method");
	   
	        Socket sock = new Socket(IpAdd, 13269);  
	        System.out.println("C: trying to est connection");
	   
	        //Send file  
	       // File myFile = new File("F:\\New Folder\\Picture.png");  
	        byte[] mybytearray = new byte[(int) myFile.length()];  
	           
	        FileInputStream fis = new FileInputStream(myFile);  
	        BufferedInputStream bis = new BufferedInputStream(fis);  
	        //bis.read(mybytearray, 0, mybytearray.length);  
	           
	        DataInputStream dis = new DataInputStream(bis);     
	        dis.readFully(mybytearray, 0, mybytearray.length);  
	           
	        OutputStream os = sock.getOutputStream();  
	           
	        //Sending file name and file size to the server  
	        DataOutputStream dos = new DataOutputStream(os); 
	        String abc=myFile.getPath();
	        String qw=abc.replace(':', '&');//as :cannot come in file name
	      //  String gh=emailid.getemaild()+"\\"+qw;//adding emailid to the file name
	        dos.writeUTF(qw); // we are writing the name of file in socket
	        
	    	
	        dos.writeLong(mybytearray.length);     //we are writing the length of file in socket
	        dos.write(mybytearray, 0, mybytearray.length);   //we are writing the file in socket  
	        dos.flush();  
	           
	        //Sending file data to the server  
	      //  os.write(mybytearray, 0, mybytearray.length);  
	      //  os.flush();  
	           
	        //Closing socket
	      //  os.close();
	        dos.close();  
	        sock.close();  
	        System.out.println("C: socket is closed");
	    } 

}
