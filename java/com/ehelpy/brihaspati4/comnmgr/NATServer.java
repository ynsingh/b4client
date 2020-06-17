package com.ehelpy.brihaspati4.comnmgr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.overlaymgmt.XML_RTConversion;

public class NATServer extends Thread
{
    private static NATServer natservr;
    // Vector to store active clients 
    static Vector<ClientHandler> ar = new Vector<>();

    // counter for clients 
    static int i = 0;

    public static NATServer getNatserver()
    {
        // If the singleton object does not exist, create one.
        if (natservr == null) natservr = new NATServer();
        // return the object reference of NATServer Singleton.
        return natservr;
    }

    //public static void startServer() throws IOException
    public  void run() 
    {
	    Thread ns0= new Thread
        (
            new Runnable()
        {
            @Override
            public void run()
            {

        // server is listening on port 1234 
        //ServerSocket ss = new ServerSocket(9876);
	ServerSocket ss=null;
	try{
        	ss = new ServerSocket(properties_access.read_debuglevel("client.properties","NATport"));
	}
	catch(IOException ex){
		System.out.println("error in reading nat port from client properties file in nat server class"+ex);
	}

        Socket s=null;
	DataInputStream dis=null;
	DataOutputStream dos=null;

        // running infinite loop for getting 
        // client request 
        while (true)
        {
		try{
	            System.out.println("WAITING FOR NEW REQUEST ");
        	    // Accept the incoming request 
	            s = ss.accept();

        	    System.out.println("New client request received : " + s);

	            // obtain input and output streams 
        	    dis = new DataInputStream(s.getInputStream());
	            dos = new DataOutputStream(s.getOutputStream());

        	    System.out.println("Creating a new handler for this client...");
		}
		catch(IOException ex){
                	System.out.println("error in reading input /output stream client properties file in nat server class inside while"+ex);
        	}

	        // Create a new handler object for handling this request. 
        	ClientHandler mtch = new ClientHandler(s,"client " + i, dis, dos);

            	// Create a new Thread with this object. 
            	Thread t = new Thread(mtch);

            	System.out.println("Adding this client to active client list");

            	// add this client to active clients list 
            	ar.add(mtch);

            	// start the thread. 
            	t.start();

            	// increment i for new client. 
            	// i is used for naming only, and can be replaced 
            	// by any naming scheme 
            	i++;
		try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
        }//while
/*	try {
                        dis.close();
                        dos.close();
                        s.close();
                        ss.close();
        } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
        }
*/
	}
	});
	    ns0.start();
    }//publicrun

   /* 	public void closeConnections()
	{
    		try {
		        dis.close();
		        dos.close();
		        s.close();
		        ss.close();
		} catch (IOException e) {	
		        // TODO Auto-generated catch block
		        e.printStackTrace();
    		}

	}
	*/
}//class

// ClientHandler class 
class ClientHandler implements Runnable
{
  //  Scanner scn = new Scanner(System.in);
    private String name;
    String destlocalip;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor 
    public ClientHandler(Socket s, String name,
                         DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.name = name;
        this.s = s;
        this.isloggedin=true;
    }

    @Override
    public void run() {

        String received = "";
        while (true)
        {
            try
            {
                // receive the string 
                while(dis.available()>0){
                    received = dis.readUTF();
                    System.out.println(received);
                    if(received.split("\\s+")[0].equals("CONNECT")){
                        this.name=received.split("\\s+")[1];
                        this.destlocalip=received.split("\\s+")[2];
                        System.out.println("CONNECTING TO CLIENT :"+this.name);
                        dos.writeUTF("CONNECTED TO SERVER");
                        System.out.println("The iterator values are: ");
                        int count=0;
                        Vector<ClientHandler> copy = new Vector<>(NATServer.ar);
                        for (ClientHandler s : copy) {
                            if(s.name.equals(this.name))
                            {
                                count++;
                                if(count>1)
                                {
                                    NATServer.ar.remove(s);
                                    count--;
                                }
                            }
                        }
                        Vector<ClientHandler> copy1 = new Vector<>(NATServer.ar);
                        System.out.println(NATServer.ar.size());
                        for (ClientHandler os : copy1) {
                            for (ClientHandler is : copy1) {
                                os.dos.writeUTF("####");
                                System.out.println(is.name);
                                os.dos.writeUTF(is.s.getInetAddress()+" "+is.s.getPort()+" "+is.name);
                                os.dos.writeUTF("###");
                            }
                        }
                        System.out.println(s.getInetAddress()+" "+s.getLocalAddress()+" "+s.getLocalSocketAddress());
                        System.out.println(s.getLocalPort()+" "+s.getRemoteSocketAddress()+" "+s.getPort());
                    }
                }
                if(received.equals("LOGOUT")){
                    System.out.println("DISCONNECTING");
                    this.isloggedin=false;
                    NATServer.ar.remove(this);
                    this.s.close();
                    break;
                }

                String MsgToSend = null;
                String recipient = null;

                if(received.contains("CONNECT#ID"))
                {
                    System.out.println("RECIEVED CONNECTION REQUEST");

                    StringTokenizer st = new StringTokenizer(received,"CONNECT#ID");
                    recipient = st.nextToken();
                    System.out.println("SOURCE :"+this.name+" "+"DESTINATION :"+recipient);

                    String sourceid,destinationid,sourceip = null,sourcelocalip=null,
                            destinationip = null,destinationlocalip=null,sourceport = null,destinationport = null;

                    sourceid=this.name;
                    destinationid=recipient;

                    ClientHandler sourceh = null,destinationh = null;
                    int flag=0;
                    for (ClientHandler mc : NATServer.ar)
                    {
                        if (mc.name.equals(this.name) && mc.isloggedin==true)
                        {
                            sourceip=String.valueOf(mc.s.getInetAddress());
                            //sourcelocalip=String.valueOf(mc.s.getLocalAddress());
                            sourcelocalip=String.valueOf(mc.destlocalip);
                            sourceport=String.valueOf(mc.s.getPort());
                            sourceh=mc;
                            flag++;
                        }
                        else if (mc.name.equals(recipient) && mc.isloggedin==true)
                        {
                            destinationip=String.valueOf(mc.s.getInetAddress());
                            //destinationlocalip=String.valueOf(mc.s.getLocalAddress());
                            destinationlocalip=String.valueOf(mc.destlocalip);
                            destinationport=String.valueOf(mc.s.getPort());
                            flag++;
                            destinationh=mc;
                            //mc.dos.writeUTF(this.name+" : "+MsgToSend); 
                            //break; 
                        }

                        if(flag==2)
                        {
                            sourceh.dos.writeUTF("ADDRESS:"+destinationid+":"+destinationip+":"+destinationport+":"+sourceport+":"+destinationlocalip);
                            destinationh.dos.writeUTF("ADDRESS:"+sourceid+":"+sourceip+":"+sourceport+":"+destinationport+":"+sourcelocalip);
                            break;
                        }
                    }
                    received="";
                }
                else if(received.contains("#ID"))
                {
                    System.out.println("RECIEVED ID");
                    // break the string into message and recipient part
                    StringTokenizer st = new StringTokenizer(received,"#ID");
                    MsgToSend = st.nextToken();
                    recipient = st.nextToken();

                    // search for the recipient in the connected devices list.
                    // ar is the vector storing client of active users
                    for (ClientHandler mc : NATServer.ar)
                    {
                        // if the recipient is found, write on its
                        // output stream
                        if (mc.name.equals(recipient) && mc.isloggedin==true)
                        {
                            mc.dos.writeUTF(this.name+" : "+MsgToSend);
                            break;
                        }
                    }
                    received="";
                }

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
        try
        {
            // closing resources 
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
