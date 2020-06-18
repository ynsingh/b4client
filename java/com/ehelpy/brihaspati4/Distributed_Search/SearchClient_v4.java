package com.ehelpy.brihaspati4.Distributed_Search;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
// This class will execute the query and run the thread to establish Peer Connections. This is the main class of SEarch Engine and all classes are called here.
public class SearchClient_v4 
{ 
        final static int PeerPort = 2223;
        public static String ID="";
        public static int PeerServerPort = 2222;
        public static String PeerServerIP = "localhost";
        public static int hop = 1;
        public static List<String> addr=new ArrayList<String>();
        public static String QUERY=null;
        public static String Recieved_QUERY=null;
        public static Socket peer_s;
	    public static ServerSocket peer_ss;
        public static Socket peerServer_s;
        
        public static DataInputStream peer_dis = null;
        public static DataOutputStream peer_dos = null;
        public static DataInputStream peerServer_dis=null;
        public static DataOutputStream peerServer_dos=null;
        public static Thread broadcastPeer=null;
        public boolean lock=false;
        public static Map<String,String> result_map=new HashMap<String,String>();
        public static String activeID=null;
        public static String trackID="";
        
        public static String message="";
        public void getBroadcastPeerThread(String id,int h,String id_track)//Initialisation of Thread
        {
            hop=h;
            trackID=id_track+" "+id;
            
            File file = new File("IIpTable.txt");//Reading IP addresses from IP table.txt
            BufferedReader br = null; 
            String st;
            try {
                	br = new BufferedReader(new FileReader(file));
                	while ((st = br.readLine()) != null)// Establishing Socket Connection
                	{
                    	if(!addr.contains(st))
                    	{
                        	addr.add(st);
                    	}
                	} 
                	for (String i : addr) 
                	{
                		// System.out.println(i);
                	}
                } catch (FileNotFoundException ex) 
                {
                    Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) 
                {
                    Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
           Thread broadcastPeer = new Thread(new Runnable()
		{ 
                    @Override
                    public void run() {
                        try {
                           
                            peer_s = new Socket(PeerServerIP, PeerServerPort);
                            peer_dis = new DataInputStream(peer_s.getInputStream());// For transfer of data between peers
                            peer_dos = new DataOutputStream(peer_s.getOutputStream());
                            peer_dos.writeUTF(activeID+","+hop+","+trackID+"###"+QUERY);//To seperate Peer ID from Query ### is used
                            outerloop:
                            while(true)
                            {// Loop to allow peer to establish a socket connection
                                while(peer_dis.available()>0){
                                    String msg = peer_dis.readUTF(); // To receive data from peer
                                    
                                    message+=msg;
                                    if(msg.equals("DONE"))
                                    {
                                       
                                        peer_dos.writeUTF(activeID+","+hop+","+trackID+"###"+"CLOSE");// Passing of Close indicates termination of connection
                                        
                                        break outerloop;
                                    }
                                    else
                                    {
                                        result_map.put(activeID, msg);
                                    }
                                }
                            }
                            
                           } catch (IOException ex) {
                                            Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                            } 
                        }
                });
                
                
                int k=1;
                int size=addr.size();
                
                for (String i : addr) 
                {
                  
                    String s=i.substring(0,40);
                    	
                    activeID=s;
                    String splitTrackID[]=trackID.split(" ");// Splitting Message/Query from Node ID
                    int flag=0;
                    for(String j: splitTrackID)
                    {
                        if(activeID.equals(j))
                        {
                            flag=1;
                        }
                    }
                    if(flag==1)
                    {
                        continue;
                    }
                    if(!s.equals(id))
                    {
                    //    Connecting to Peer
                        try {
                            
                        	PeerServerPort=2222;
                        	PeerServerIP=i.substring(40);
                        		
                            broadcastPeer.run();
                            broadcastPeer.join();
                               
                        } catch (Exception ex) 
                        {
                            Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                        } 
                    }
                                
                }
                
                System.out.println("FINAL MESSAGE :"+message);
                String result=LuceneTester.main(QUERY);// Query passed to LuceneTester main Class 
                System.out.println("LUCENE TESTER MAIN RESULT ");// This will append the result along with Node ID
                String[] data=result.split("File:");
                String fileContent="";
                int ck1=0;
                for(String d : data)
                {
                   if(ck1==0)
                   {
                       ck1++;
                       continue;
                   }
                   String content = null;
                try 
                {
                    content = new String(Files.readAllBytes(Paths.get(d.trim())));
                    fileContent+=content+":::::::";
                } catch (IOException ex) {
                    Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                }
                }
                message+="-"+ID+"-"+result+":::::::"+fileContent;
                //System.out.println(message);
            try {
                if(message!=null&&peerServer_dos!=null)
                {
                    peerServer_dos.writeUTF(message);
                    
                }
                else
                {
                    String[] displayMessage=message.split("DONE");// Done indicates termination of connection
                    Map<String,String> Resultmap=new HashMap<String,String>();
                    String key=null;
                    String value=null;
                    String resultPane="";
                    for(String i:displayMessage)
                    {
                        
                        
                        key= i.substring(0, 2);
                        value=i.substring(3);
                        
                        
                       
                        resultPane+="<br />";
                        resultPane+=String.valueOf(key.charAt(1));
                        resultPane+="<br />";
                        
                        key=String.valueOf(key.charAt(1));
                        
                      
                        
                        String[] temp=value.split("File:");
                        value="";
                        int ck=0;
                        System.out.println("LENGTH :"+temp.length);
                        for(String j : temp)
                        {
                           
                            if(ck==0)
                            {    
                                ck++;
                                continue;
                            }
                            if(ck==temp.length-1)
                            {
                                String[] fj=j.split(":::::::");
                                
                                j=fj[0]+"  <a href='"+key+"-"+(ck-1)+"'>LINK</a>";// Hyperlink of the file
                                //Dump Content to File
                                int cck=1;
                                for(int iter=1;iter<fj.length;iter++)
                                {
                                    if(cck<=(ck))
                                    {
                                        String path = "dat\\result\\"+key+"_"+(iter-1)+".txt";
                                        Files.write( Paths.get(path.trim()), fj[iter].getBytes());
                                    }
                                    cck++;
                                }
                                resultPane+=j;
                                resultPane+="<br />";
                                value+=j+",";
                            }
                            else
                            {
                                j+="<a href='"+key+"-"+(ck-1)+"'>LINK</a>";
                                resultPane+=j;
                                resultPane+="<br />";
                                value+=j+",";
                            }
                            
                            ck++;
                        }
                        
                        
                        Resultmap.put(key, value);
                    }
                    System.out.println("RESULT PANE ----");
                    System.out.println(resultPane);
                    ProjectWindow.result.setText(resultPane);
                    for (Map.Entry<String, String> entry : Resultmap.entrySet()) 
                    {
                        System.out.println(entry.getKey() + "/" + entry.getValue());
                    }
                }
                
            } catch (Exception ex) {
                Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        public static void main( ) throws UnknownHostException, IOException
	{
	    File file = new File("C:\\Users\\Alok awasthi\\Desktop\\All Folders\\Java Programs\\2.p2p for eclipse\\NodeID.txt");// path to read node ID
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
        {
            ID=st;
        }
                Scanner scn = new Scanner(System.in); // receive input from user
		
		peer_ss = new ServerSocket(PeerPort);
                
                Thread serverPeer = new Thread(new Runnable() 
		{ 
			@Override
			public void run() { 
                            try { 
                                
                                while(true)
                                {
                                    String received = null;
                                    peerServer_s = peer_ss.accept();
                                    new Thread()
                                {
                                    public void run() {
                                        try {
                                           
                                            peerServer_dis = new DataInputStream(peerServer_s.getInputStream()); 
                                            peerServer_dos = new DataOutputStream(peerServer_s.getOutputStream());
                                            outerloop:
                                            while(true)
                                            {
                                                 while(peerServer_dis.available()>0){
                                                 String received = peerServer_dis.readUTF();
                                                 System.out.println(received);
                                                 String[] parts = received.split("###"); 
                                                 received=parts[1];
                                                 Recieved_QUERY=received;
                                                 String[] info=parts[0].split(",");
                                                 String i_d=info[0];
                                                 int h=Integer.parseInt(info[1]);
                                                 String t=info[2];
                                                 if(received.equals("CLOSE")){
                             
                                                     break;
                                                 }
                                                 else{
                                                     QUERY=received;
                                                   if(h>0)
                                                   {
                                                       h--;
                                                       SearchClient_v4 sc=new SearchClient_v4();
                                                       sc.getBroadcastPeerThread(i_d,h,t);
                                                   }
                                                   else if(h==0)
                                                   {
                                                        String result=LuceneTester.main(received);
                                                        System.out.println("LUCENE TESTER MAIN RESULT "+result);
                                                        String[] data=result.split("File:");
                                                        String fileContent="";
                                                        int ck1=0;
                                                        for(String d : data)
                                                        {
                                                            if(ck1==0)
                                                            {
                                                                ck1++;
                                                                continue;
                                                            }
                                                            String content = null;
                                                            try 
                                                            {
                                                                content = new String(Files.readAllBytes(Paths.get(d.trim())));
                                                                fileContent+=content+":::::::";
                                                            } catch (IOException ex) 
                                                            {
                                                                Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                                                            }
                                                        }
                                                        peerServer_dos.writeUTF("-"+i_d+"-"+result+":::::::"+fileContent);
                                                   }
                                                 }
                                                 }
                                                 peerServer_dos.writeUTF("DONE");
                                            }
                                        } catch (IOException ex) {
                                            Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }.start();
                                    
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                            }
				 
			} 
		});
                
		
                serverPeer.start();
                
                ProjectWindow.main(null);
                

	} 
} 
