package com.ehelpy.brihaspati4.comnmgr;

import com.ehelpy.brihaspati4.XMLhandler2.XMLReader;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class Receiver {
    public static void start() throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(3333);
            System.out.println("Server started");
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;
        InputStream in = null;
        OutputStream out = null;
        while(true){
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                System.out.println("Can't accept client connection. ");
            }
            if(socket.isConnected()){
                try {
                    in = socket.getInputStream();
                } catch (IOException ex) {
                    System.out.println("Can't get socket input stream. ");
                }
                String fileName = getName();
                try {
                    out = new FileOutputStream(fileName);
                } catch (FileNotFoundException ex) {
                    System.out.println("File not found. ");
                }

                byte[] bytes = new byte[2*1024];
                int count;
                while ((count = in.read(bytes)) > 0) {
                    out.write(bytes, 0, count);
                }
                out.close();
                in.close();
                XMLReader.reader(fileName);
            }
            else
                break;
        }
        //socket.close();
        //serverSocket.close();
    }
    public static String getName(){
        Path fileName = Paths.get(UUID.randomUUID() + ".xml");
        return String.valueOf(fileName);
    }
}
