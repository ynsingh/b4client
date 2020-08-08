package com.ehelpy.brihaspati4.routingmgmt;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class SystemIPAddress
{
    public static void main(String[] args) {
        String ipAddress = getSystemIP();
        System.out.println("System IP : "+ipAddress);
    }

    public static String getSystemIP() {
        try {
            String sysIP="";
            String OSName=  System.getProperty("os.name");
            if(OSName.contains("Windows")) {
                sysIP   =InetAddress.getLocalHost().getHostAddress();

            }
            else
            {
                sysIP =com.ehelpy.brihaspati4.routingmgmt.GetPublicHostname.tellMyIP();

//		    	sysIP=getSystemIP4Linux("eth0");
//		    	if(sysIP==null){
//                    sysIP=getSystemIP4Linux("eth1");
//		    if(sysIP==null){
//		  	sysIP=getSystemIP4Linux("eth2");
//                        if(sysIP==null){
//                            sysIP=getSystemIP4Linux("usb0");
            }

            return sysIP;
        }
        catch(Exception E) {
            SysOutCtrl.SysoutSet("Please Check connection ");
            return null;
        }
    }

//	    private static String getSystemIP4Linux(String name){
//	        try{
//	            String ip="";
//	            NetworkInterface networkInterface = NetworkInterface.getByName(name);
//	            Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
//	            InetAddress currentAddress = inetAddress.nextElement();
//	            while(inetAddress.hasMoreElements()){
//	                currentAddress = inetAddress.nextElement();
//	                if(currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress()){
//	                    ip = currentAddress.toString();
//	                    break;
//	                }
//	            }
//	            if(ip.startsWith("/")){
//	                ip=ip.substring(1);
//	            }
//	            return ip;
//	        }
//	        catch (Exception E) {
//	            System.err.println("System Linux IP Exp : "+E.getMessage());
//	            return null;
//	        }
//	    }

}



