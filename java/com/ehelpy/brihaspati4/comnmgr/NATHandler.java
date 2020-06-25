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
import java.net.InetSocketAddress;
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
import com.ehelpy.brihaspati4.authenticate.Gui;

public class NATHandler 
{
    	//final static int ServerPort = 9876; 
    	final static int ServerPort = properties_access.read_debuglevel("client.properties","NATport"); 
        static List<String> addr=new ArrayList<String>();
        //final static String ID="B";
	String ID="";
        //static String localip="";
         String localip="";
        static String localport="0";
        static String destinationlocalip="";
//	public static void main(String args[]) throws UnknownHostException, IOException 
	public void callNATHandler()throws UnknownHostException, IOException 
	{ 
		try{
			BufferedReader br = new BufferedReader(new FileReader("NodeID.txt"));
			ID=br.readLine();
			br.close();
		}
		catch(FileNotFoundException fnfex){
//			Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, fnfex);
               	 	System.out.println(fnfex);
		}
		catch(IOException ioex){
			Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, ioex);
	                System.out.println(ioex);
		}
		Scanner scn = new Scanner(System.in); 
		// getting boot strap ip
		String BootstrapIP=properties_access.read_property("client.properties","BotstrpIP");
		System.out.println("Ip address in NAT Handler"+BootstrapIP);
		// getting localhost ip 
		//InetAddress ip = InetAddress.getByName("34.201.66.83"); 
		InetAddress ip = InetAddress.getByName(BootstrapIP); 
		System.out.println("Ip address (Inet address ) in NAT Handler "+ip.toString());
		
		// establish the connection 34.201.66.83 103.246.106.195 
		//Socket s = new Socket("34.201.66.83", ServerPort); 
		//Socket s = new Socket(BootstrapIP, ServerPort); 
		boolean flagsp=ipAddressReachable(BootstrapIP, ServerPort, 1000);
		System.out.println(" ping result2 is  "+flagsp);
                if(!flagsp){
                        String msg = "Please check the internet connection because server is not reachable then try again." ;
                        Gui.showMessageDialogBox(msg);
                        System.exit(0);
                }
                else {

		Socket s = new Socket(ip, ServerPort); 
		//Socket s = new Socket("172.20.160.56", ServerPort); 
		//Socket s = new Socket("103.246.106.197", ServerPort); 
		
                localip=s.getLocalAddress().toString();
		// obtaining input and out streams 
		DataInputStream dis = new DataInputStream(s.getInputStream()); 
		DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 

		// sendMessage thread 
		Thread sendMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
                            // write on the output stream 
                            System.out.println("Connecting to NAT Server with ID "+ID);
                            try {
                                dos.writeUTF("CONNECT "+ID+" "+localip);
                            } catch (IOException ex) {
                                Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, ex);
                                System.out.println(ex);
                            }
				while (true) { 
                                    // read the message to deliver. 
                                    //String msg = scn.nextLine(); 
                                    try { 
                                        //System.out.print("Enter Message :");
                                        String msg = scn.nextLine();

                                        dos.writeUTF(msg);
					}catch (IOException e) 
                                        { 
						e.printStackTrace(); 
					} 
				} 
			} 
		}); 
		
		// readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 

				while (true) { 
					try 
                                        { 
                                          // read the message sent to this client 
					  while(dis.available()>0)
                                          {
                                                String msg = dis.readUTF(); 
                                                System.out.println("Message From NAT Server "+msg);
                                                if(msg.contains("ADDRESS"))
                                                {
                                                    try 
                                                    {   
                                                        String[] dat=msg.split(":");
                                                        String publicip="/"+NATHandler.getPublicIp();
                                                        NATHandler.localport=dat[4];
                                                        NATHandler.destinationlocalip=dat[5];
                                                        NATHandler.destinationlocalip=NATHandler.destinationlocalip.substring(1);
                                                        
                                                        if(publicip.equals(dat[2]))
                                                        {
                                                            System.out.println("Same Global IP");
                                                            //scn.close();
                                                            UDP_Hole_Punching(dat[3]);
                                                        }
                                                        
                                                    } catch (Exception ex) {
                                                        Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                }
                                                else if(msg.equals("####"))
                                                {
                                                   if(!addr.contains(msg))
                                                  {
                                                        addr.add(msg);
                                                  }
                                                }                                                
                                          }
					} catch (IOException e) { 

						e.printStackTrace(); 
					} 
				} 
			} 
		}); 

		sendMessage.start(); 
		readMessage.start();
		}// close ping else
        }//main close
        
        public static void UDP_Hole_Punching(final String destination_port)
        {
            System.out.println("--- UDP HOLE PUNCHING ---");
            try 
            { 
                final String destination_ip=NATHandler.getPublicIp();
                System.out.println(NATHandler.getPublicIp()+" "+localport+" "+destination_port+" "+NATHandler.destinationlocalip);
                DatagramSocket clientSocket = new DatagramSocket(Integer.parseInt(localport));
                Scanner scn=new Scanner(System.in);
                    
                Thread sendMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
                        { 
                             System.out.println("UDP HOLE PunCHING sendMessage Run Method :");
                            while (true) 
                            { 
                                try {
                                        String msg = scn.nextLine();
                                        //dos.writeUTF(msg);
                                        //String msg="Hey There";
                                        //System.out.println("Message Entered");
                                        //System.out.println("Client Socket "+clientSocket.getInetAddress()+" "+clientSocket.getLocalAddress()+" "+clientSocket.getLocalPort()+" "+clientSocket.getLocalAddress());
					NATHandler nath=new NATHandler();
					try{
						BufferedReader br = new BufferedReader(new FileReader("NodeID.txt"));
						nath.ID=br.readLine();
						br.close();
					}
					catch(FileNotFoundException fnfex){
						Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, fnfex);
				                System.out.println(fnfex);
					}
					catch(IOException ioex){
						Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, ioex);
				                System.out.println(ioex);
					}

                                        byte[] sendData = ("Datapacket From "+nath.ID+" : "+msg).getBytes();
                                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(NATHandler.destinationlocalip), Integer.parseInt(destination_port));
                                        clientSocket.send(sendPacket);
                                        
					}catch (Exception e) 
                                        { 
						e.printStackTrace(); 
					} 
				} 
			} 
		});
                
                // readMessage thread 
		Thread readMessage = new Thread(new Runnable() 
		{ 
			@Override
			public void run() 
                        { 
                            System.out.println("UDP HOLE PunCHING readMessage Run Method :");
                            while (true) 
                            { 
				try 
                                { 
                                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);  
                                    // read the message sent to this client 
                                    receivePacket.setData(new byte[1024]);
                                    clientSocket.receive(receivePacket);
                                    System.out.println("REC: "+ new String(receivePacket.getData()));
				} 
                                catch (Exception e) 
                                { 
                                    e.printStackTrace(); 
				} 
                            } 
			} 
		});
    
                sendMessage.start(); 
                readMessage.start();
            } 
            catch (Exception ex) 
            {
                Logger.getLogger(NATHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
             

        }
       
	private static boolean ipAddressReachable(String address, int port, int timeout) {
		try {
 
			try (Socket b4Socket = new Socket()) {
				// Connects this socket to the server with a specified timeout value.
				b4Socket.connect(new InetSocketAddress(address, port), timeout);
			}
			// Return true if connection successful
			return true;
		} catch (IOException exception) {
			exception.printStackTrace();
 
			// Return false if connection fails
			return false;
		}
	}

        public static String getPublicIp() throws Exception 
        {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = null;
            try 
            {
                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                String ip = in.readLine();
                return ip;
            } finally 
            {
                if (in != null) 
                {
                    try 
                    {
                        in.close();
                    } catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
}
/*
class NATServer
{

    // Vector to store active clients 
    static Vector<ClientHandler> ar = new Vector<>();

    // counter for clients 
    static int i = 0;

    public static void startServer() throws IOException
    {
        // server is listening on port 1234 
        //ServerSocket ss = new ServerSocket(9876);
        ServerSocket ss = new ServerSocket(properties_access.read_debuglevel("client.properties","NATport"));

        Socket s;

        // running infinite loop for getting 
        // client request 
        while (true)
        {
            System.out.println("WAITING FOR NEW REQUEST ");
            // Accept the incoming request 
            s = ss.accept();

            System.out.println("New client request received : " + s);

            // obtain input and output streams 
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Creating a new handler for this client...");

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

        }
    }
}

// ClientHandler class 
class ClientHandler implements Runnable
{
    Scanner scn = new Scanner(System.in);
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
*/
