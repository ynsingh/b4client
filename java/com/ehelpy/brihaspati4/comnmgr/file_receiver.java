package com.ehelpy.brihaspati4.comnmgr;

import java.net.ServerSocket;
import java.net.Socket;

import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.sms.sms_send_rec_management;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class file_receiver extends Thread {

    private ServerSocket serverSocket;
    private static int port = 2222;
    private boolean running = false;


    public static void main( String[] args )
    {
        System.out.println( "Start server on port: " + port );

        file_receiver server = new file_receiver( port );
        server.startServer();

    }

    public file_receiver( int port )
    {
        file_receiver.port = port;
    }

    public void startServer()
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
        //   running = true;
        while(GlobalObject.getRunStatus()||sms_send_rec_management.sending_message)
        {
            try
            {
                System.out.println( "Listening for a connection" );

                // Call accept() to receive the next connection
                Socket socket = serverSocket.accept();

                // Pass the socket to the RequestHandler thread for processing
                RequestHandler requestHandler = new RequestHandler( socket );
                requestHandler.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

class RequestHandler extends Thread
{
    private Socket socket;
    RequestHandler( Socket socket )
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println( "Received a connection  in file_receiver - commanager" );

            int fileReceiveIndex = 0;
            // Get input streams

            byte[] mybytearray = new byte[4096];

            InputStream is = socket.getInputStream();

            String fileName=CommunicationUtilityMethods.getFileName();
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
            socket.close();

//            for (int i = 0; i < mybytearray.length; i++) {
  //              System.out.print(mybytearray[i] + " ");
    //        }
      //      SysOutCtrl.SysoutSet("File  downloaded (" + current + " bytes read)");
            File inFile = new File(fileName);

        //    SysOutCtrl.SysoutSet("File length after receiving" + inFile.length());
          //  SysOutCtrl.SysoutSet("Receiving file index"+fileReceiveIndex);
         //   SysOutCtrl.SysoutSet("inFile" + inFile.toString() + inFile.length() + "bytes");

            String[] xmlParsed =ParseXmlFile.ParseXml(inFile) ;
          //  System.out.println("file received is : "+xmlParsed[0]);
            if(xmlParsed[0].equals("0031"))
            {
                CommunicationManager.RxBufferOM.add(inFile);
            }
            else
                CommunicationUtilityMethods.addQueryToReceiveBuffer(inFile);

            fileReceiveIndex++;

            System.out.println( "Connection closed in file_receiver - commanager" );
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
    }
}
