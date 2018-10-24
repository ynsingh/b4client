package com.ehelpy.brihaspati4.messagesend.server;

import com.ehelpy.brihaspati4.messagesend.encryption.AESEncryptionDecryptionTest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class Receiver {
    public static Scanner scanner;
    public static void receiveFile(String ipAddress,int portNo,String fileLocation) throws IOException
    {
            int bytesRead=0;
            int current = 0;
            FileOutputStream fileOutputStream = null;
            BufferedOutputStream bufferedOutputStream = null;
            Socket socket = null;               
            try {
                    //creating connection.
                    socket = new Socket(ipAddress,portNo);
                    System.out.println("connected.");
            
                    // receive file
                    byte [] byteArray  = new byte [6022386]; //I have hard coded size of byteArray, you can send file size from socket before creating this.
                    System.out.println("Please wait downloading file");                       
                   
                    //reading file from socket
                    InputStream inputStream = socket.getInputStream();
                    fileOutputStream = new FileOutputStream(fileLocation);
                    bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bytesRead = inputStream.read(byteArray,0,byteArray.length);    //copying file from socket to byteArray

                    current = bytesRead;
            do {
                bytesRead =inputStream.read(byteArray, current, (byteArray.length-current));
                if(bytesRead >= 0) current += bytesRead;
                            } while(bytesRead > -1);
            bufferedOutputStream.write(byteArray, 0 , current); //writing byteArray to file
            bufferedOutputStream.flush();//flushing buffers
            System.out.println("File " + fileLocation  + " downloaded ( size: " + current + " bytes read)");
                } catch (IOException e) {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                  }
          finally {
        	  // the finally statment out here is very important for the sake of running the same receiver code again
                    if (fileOutputStream != null) fileOutputStream.close();
                    if (bufferedOutputStream != null) bufferedOutputStream.close();
                    if (socket != null) socket.close();
          }
    }
           
        }