package com.ehelpy.brihaspati4.ReplicaMgt;

import java.io.IOException;
import java.net.*;

public class Monitor {
    public static boolean statusCheck(String hostname)
    {
        boolean liveStatus = false;
        int port = 8880;
        // check hostname has a value
        if(!hostname.equals(null))
        {
            // create socket address and bind it with the hostname and port
            SocketAddress sock_address = new InetSocketAddress(hostname, port);
            Socket sock = new Socket();
            int timeout = 5000;
            try {
                // connect to the socket address after every timeout
                sock.connect(sock_address, timeout);
                // after successfull connection close socket
                sock.close();
                // change the status flag
                liveStatus = true;
            } catch (SocketTimeoutException exception) {
                System.out.println("socket timeout exception");
            }catch(IOException exception) {
                System.out.println("IO Exception11");
            }
        }
        return liveStatus;
    }

    public static void statusReport() throws IOException//checks for the application to be running on the reciever
    {
        // open a server socket
        ServerSocket servsock = null;
            try {
                servsock = new ServerSocket(8888);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Socket sock = null;
            //loop to keep the socket open
            while(true) {
            try {
                // accept the connection request on the server socket and move it to a socket
                sock = servsock.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
