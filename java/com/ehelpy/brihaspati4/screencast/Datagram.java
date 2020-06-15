package com.ehelpy.brihaspati4.screencast;

//import cast.Gui;
//import cast.UserSetup;
//import core.port.UdpMaster;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Datagram {
	private       static boolean newObj       = false;         // restricts creation of new instances
	public  final static byte[]  SCREEN_CAST  = "screencast".getBytes();
	public  final static byte[]  CAST_FAIL    = "cast request denied".getBytes();
	public  final static byte[]  CAST_PASS    = "cast request approved".getBytes();
	
	/**
	 * private constructor to enforce non-instantiability by external code
	 * @throws AssertionError ensures that constructor is NOT invoked accidently (by sub-class, reflection etc)
	 */
    private Datagram() throws AssertionError {if (Datagram.newObj == false) throw new AssertionError();}
	
    public static void sorter(DatagramPacket packet) {
    	
    }
    
    public static void outbound(String data, String host, int destinationPort) 
    		throws UnknownHostException {
    	DatagramPacket packet = new DatagramPacket(data.getBytes(), 
    			data.length(), InetAddress.getByName(host), destinationPort);
    	UdpMaster.send(packet);
    }

	public static void outbound(byte[] data, String host, int destinationPort)
			throws UnknownHostException {
		DatagramPacket packet = new DatagramPacket(data,
				data.length, InetAddress.getByName(host), destinationPort);
		UdpMaster.send(packet);
	}
    
    public static void outbound(String data, String host, int destinationPort, int sourcePort) 
    		throws UnknownHostException {
    	DatagramPacket packet = new DatagramPacket(data.getBytes(), 
    			data.length(), InetAddress.getByName(host), destinationPort);
    	UdpMaster.send(packet, sourcePort);
    }

	public static void outbound(byte[] data, String host, int destinationPort, int sourcePort)
			throws UnknownHostException {
		DatagramPacket packet = new DatagramPacket(data,
				data.length, InetAddress.getByName(host), destinationPort);
		UdpMaster.send(packet, sourcePort);
	}
    
    public static void inbound(DatagramPacket packet) {
    	byte[] data = packet.getData();
    	String ip   = packet.getAddress().getHostAddress();
    	int    port = packet.getPort();

    	byte noVal = 0;
    	int dataLength = 0, index = 0;
    	while(dataLength <= index) {
    		if (data[index] == noVal) {
    			dataLength = index;
    			index = -1;
			} else {index++;}
		}
    	byte[] trimData = Arrays.copyOfRange(data, 0, dataLength);
    	if (Arrays.equals(SCREEN_CAST, trimData)) Gui.connectRequest(ip, port);
    	else if (Arrays.equals(CAST_FAIL, trimData)) Gui.connectionDenied();
    	else {
			byte[] header = Arrays.copyOfRange(data, 0, CAST_PASS.length);
    		byte[] newPort = Arrays.copyOfRange(data, CAST_PASS.length, CAST_PASS.length + Integer.BYTES);
			byte[] password = Arrays.copyOfRange(data, CAST_PASS.length + Integer.BYTES, data.length);
			if (Arrays.equals(CAST_PASS, header)) {
				ByteBuffer byteBuffer = ByteBuffer.wrap(newPort);
				int tcpPort = byteBuffer.getInt();
				try {
					System.out.println(ip + ":" + tcpPort + " => " + new String(password));
					UserSetup.getInstance(ip, tcpPort, password);
				} catch (IOException e) {e.printStackTrace();}
			} else System.out.println("Unknown Datagram => " + new String(data));
		}
    }
}
