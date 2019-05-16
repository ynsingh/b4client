package com.ehelpy.brihaspati4.comnmgr;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;


public class Applivation_alive_response extends Thread {
	
	private ServerSocket serverSocket;
    private static int port = 4444;
    private boolean running = false;

	public static void main(String[] args) 
	{
		System.out.println( "alive response on port: " + port );

		Applivation_alive_response server = new Applivation_alive_response( port );
        server.startServer_for_Response();

	}
	
	public Applivation_alive_response( int port )
    {
		Applivation_alive_response.port = port;
    }
	
	 public void startServer_for_Response()
	 {
		 try
	        {
	            serverSocket = new ServerSocket( port );
	            this.start();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	 }

	 @Override
	 public void run()
	 {
		 running = true;
		 while( running )
		 {
			 try
			 {
				 System.out.println( "Listening for a connection" );

				 // Call accept() to receive the next connection
				 Socket socket = serverSocket.accept();

				 // Pass the socket to the RequestHandler thread for processing
				 RequestHandler_for_Response requestHandler = new RequestHandler_for_Response( socket );
				 requestHandler.start();
			 }
			 catch (IOException e)
			 {
				 e.printStackTrace();
			 }
		 }
	 }
}

class RequestHandler_for_Response extends Thread
{
	private Socket socket;
	RequestHandler_for_Response( Socket socket )
    {
        this.socket = socket;
    }
    
    @Override
    public void run()
    {
    	try
        {
    		System.out.println( "Alive Response Given" );
    		
    		socket.close();
        }	
    	catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
