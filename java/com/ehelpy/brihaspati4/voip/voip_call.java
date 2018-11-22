package com.ehelpy.brihaspati4.voip;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import com.ehelpy.brihaspati4.authenticate.Gui;
import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.authenticate.ReadVerifyCert;
import com.ehelpy.brihaspati4.authenticate.b4server_services;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress;
import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;

public class voip_call {
    private static SocketChannel socket;
    static boolean flag = false;
    static X509Certificate client_cert = null;
    static X509Certificate server_cert = null;
    static String selfemailid = null;
    static String tosend = null;
    static String received = null;
    static long sym_key = 0;
    static SecretKey secr_key = null;
    static int token = 1;
    public static void callstart() {
        // Request Index manager for node id
        // Request Index manager for farend client IP address and and openin port
        try
        {
            int port = Integer.valueOf(properties_access.read_property("client.properties","Rxsocch"));
            debug_level.debug(0,"The socket channel port is "+ port);
            client_cert = ReadVerifyCert.returnClientCert();// get a copy of own cert for secure handshake

            String called_emailid	=	Gui.getcalleremailid(); //once from emailid index manager can fetch the ip address
            // String NodeIDAddr_Caller = DHT_Querry.nodeid(called_emailid);
            // String IPAddr_Caller = Router_Querry.ipaddr(NodeIDAddr_Caller);
            //Pass emailid to Index Manager to fetch the IP address
            //String IPAddr_Caller =	IndexManagementUtilityMethods.searchEmailId(called_emailid, client_cert);// later on replaced by the ip address fetched from the index manager
            String IPAddr_Caller =	IndexManagementUtilityMethods.searchEmailId(called_emailid );// later on replaced by the ip address fetched from the index manager
            if (IPAddr_Caller.equals("NoEntryInIndexTable") || IPAddr_Caller.equals("TimedOut")) {
                b4server_services.service();
                return ;
            }

            //String IPAddr_Caller =	Gui.getip_address();// later on replaced by the ip address fetched from the index manager
            //int port = 9999 ;
            debug_level.debug(0,"The destination iP is "+ IPAddr_Caller);
            // now it is required to open a socket channel to open communication with the peer
            socket = SocketChannel.open();
            try {
                SocketAddress socketAddr = new InetSocketAddress(IPAddr_Caller, port);
                socket.connect(socketAddr);
            }
            catch(Exception e) {
                socket.close();
                b4server_services.service();
                return;
            }

            //while (true)
            {
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(client_cert.toString());
                while (m.find())
                {
                    selfemailid = m.group().toString();
                }// get own email id
                //InetAddress IP=InetAddress.getLocalHost();
                String IP=SystemIPAddress.getSystemIP();

                //System.out.println("selfmailid =" + selfemailid);
                //tosend = selfemailid + "this is the seperator" + IP.getHostAddress(); // own email id and the ipaddress fwd to farend for approval
                tosend = selfemailid + "this is the seperator" + IP; // own email id and the ipaddress fwd to farend for approval
                socketch_write.ch_write(socket, tosend);
                //socket.close();
                debug_level.debug(0, "The SocketChannel has been closed" + socket.isConnected());
                sym_key = socketserver.server_socket(Integer.valueOf(properties_access.read_property("client.properties","Txsocch")));// get the long random number for genr secretkey
                debug_level.debug(0,"The sym key recd is "+ sym_key);
                if (sym_key!=0)
                {
                    debug_level.debug(5,"The status of tx call is true ");
                    voip_start.start(IPAddr_Caller, sym_key);

                }

                // the gui to control the ongoing call
                else
                {
                    return;

                }

            }

        }
        catch (Exception e)

        {
            e.printStackTrace();
        } finally

        {   try
            {
                b4server_services.ss.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return;
    }
}

