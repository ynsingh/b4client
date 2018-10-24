package com.ehelpy.brihaspati4.comnmgr;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class Main {
	public static void main(String[] args)
	{
//		InetAddress IPNow = null;
//		try {
//			IPNow = InetAddress.getLocalHost();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String StrIPnow = IPNow.toString();//"192.168.2.8";
//		SysOutCtrl.SysoutSet("IP now is :" +StrIPnow,3);
//		int Match = StrIPnow.indexOf("/");
//		int CutAt = Match + 1;
//
//		String ip = StrIPnow.substring(CutAt, StrIPnow.length());
//		if(!ip.equals("127.0.0.1"))
//			{SysOutCtrl.SysoutSet("Your IP captured is"+ip, 1);
//			SysOutCtrl.SysoutSet("Please check your connectivity", 1);}
//	}
		Enumeration<NetworkInterface> nics=null;
		try {
			 nics = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(nics);
	}
	}
