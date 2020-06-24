package com.ehelpy.brihaspati4.screencast;

//import core.port.UdpMaster;

public class TestMain {
	public static void main(String[] args) {
		try {
			Gui.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}




	
	/*
	this function is currently hardcoded,  in future it should be routing API
	to get the ip address associated with email address .
	at the moment routing module only queries the routing table in the local machine , finding the IP address
	of the email which is not my immediate neighbour (hence not in my routing table ) needs to be tested.
	 */




    public static String getIP(String email){
        String ip = null ;
        switch (email) {
        case "bishtsarthak@outlook.com":
        	ip="192.168.0.104";
            break;
        case "jiteshps@iitk.ac.in":
        	ip="192.168.0.103";
            break;
        case "nksingh@iitk.ac.in":
        	ip="172.186.24.132";
            break;
        case "sidharth@iitk.ac.in":
        	ip="172.186.24.132";
            break;
        case "umeshengg11@gmail.com":
          ip="192.168.0.103";
          break;
        }
        return ip;
    }
    
    public static int getPort(String email) {
    	return UdpMaster.getDefaultPort();
    }
}
