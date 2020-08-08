package com.ehelpy.brihaspati4.routingmgmt;

import java.net.*;
import java.util.*;

import java.util.regex.Pattern;

public class GetPublicHostname
{
    public static void main(String[] args) throws Throwable
    {
        System.out.println("The IP : " + tellMyIP());
    }

    public static String tellMyIP()
    {
        NetworkInterface iface = null;
        String ethr;
        String myip = "";
        String regex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +	"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        try
        {
            for(Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();)
            {
                iface = (NetworkInterface)ifaces.nextElement();
                ethr = iface.getDisplayName();
                //	System.out.println("The IF : " + ethr);
                if(!(ethr.equals("lo"))) {
                    if(!(ethr.contains("br") )) {
                        //System.out.println("Interface:" + ethr);
                        InetAddress ia = null;
                        for(Enumeration ips = iface.getInetAddresses(); ips.hasMoreElements();)
                        {
                            ia = (InetAddress)ips.nextElement();
//				System.out.println("The IF2 : " + ia.toString());
//				System.out.println("The IF21 : " + ia.getCanonicalHostName());
                            if (Pattern.matches(regex, ia.getCanonicalHostName()))
                            {
                                myip = ia.getCanonicalHostName();
//				System.out.println("The IF3 : " + myip);
                                return myip;
                            }

                        }
                        if(myip.isEmpty()) {
                            String pip=ia.toString();
                            int abc=pip.indexOf("/");
                            int cutat=abc+1;
                            myip= pip.substring(cutat,pip.length());
//				System.out.println("The IF4 : " + myip);
                            return myip;
                        }
                    }
                }
            }
        }
        catch (SocketException e) {}
        return myip;
    }
}
