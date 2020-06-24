package com.ehelpy.brihaspati4.screencast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

public class UdpMaster implements Runnable {
	private final static int     NULL_PORT  = Global1.NULL_PORT.val();
	private final static int     MIN_PORT   = Global1.MIN_PORT.val();
	private final static int     MAX_PORT   = Global1.MAX_PORT.val();
	// newObj restricts creation of new instances
	private       static boolean newObj     = false;
	private       static int     defPort    = NULL_PORT;
	private              boolean loop       = true;
	private              int     pktCount   = 0;

	private static ThreadGroup udpThreads
			= new ThreadGroup("udpThreads");
	private static HashMap<Integer, UdpMaster> hashMap = new HashMap<>();
	private DatagramSocket socket = null;

	/**
	 * private constructor to restrict instantiation by external code
	 * @throws AssertionError ensures that constructor is NOT invoked
	 * accidentally (by sub-class, reflection etc)
	 */
    private UdpMaster() {if (!newObj) throw new AssertionError();}

	private static UdpMaster newInstance(int port) throws SocketException {
		newObj = true;
		UdpMaster udp = new UdpMaster();
		newObj = false;
		udp.loop = true;
		udp.pktCount = 0;
		if (port != NULL_PORT) udp.socket = new DatagramSocket(port);
		else udp.socket = new DatagramSocket();
		port = udp.socket.getLocalPort();
		if (defPort == NULL_PORT) defPort = port;
		Thread thread = new Thread(udpThreads, udp, Integer.toString(port));
		hashMap.put(port, udp);
		thread.start();
		System.out.println("UDP Master listening on port " + port);
		return udp;
	}
	public static UdpMaster newInstance()
			throws SocketException {return newInstance(NULL_PORT);}

    public static UdpMaster getInstance(int port) throws SocketException {
    	if (port >= MIN_PORT && port <= MAX_PORT) {
    		UdpMaster udp = hashMap.get(port);
    		if (udp == null) udp = newInstance(port);
        	return udp;
    	} else throw new IllegalArgumentException();
    }

	public static UdpMaster defInstance()
			throws SocketException {
		UdpMaster udp = hashMap.get(defPort);
		if (udp == null) udp = newInstance(NULL_PORT);
		UdpMaster.defPort = udp.socket.getLocalPort();
		return udp;
	}

    public static void send(DatagramPacket packet) {
		try {
			UdpMaster udp = UdpMaster.defInstance();
			udp.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void send(DatagramPacket packet, int port) {
    	try {
			UdpMaster udp = UdpMaster.getInstance(port);
			udp.socket.send(packet);
		} catch (IllegalArgumentException e) {
			send(packet);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {loop = false;}
	public static void close(int port) {
		UdpMaster udp = hashMap.get(port);
		if (udp != null) udp.loop = false;
	}

	public static int getDefaultPort() {return UdpMaster.defPort;}
	public int getPort() {return socket.getLocalPort();}

	public void run() {
		int bufSize = 100; // max size of UDP datagram
    	DatagramPacket packet = null;
		while (loop) {
			try {
				packet = new DatagramPacket(new byte[bufSize], bufSize);
				socket.receive(packet);
				Datagram.inbound(packet);
				pktCount++;
			} catch (IOException e) {
				System.out.println(pktCount
						+ ") IOException at UDP port "
						+ socket.getLocalPort());
				e.printStackTrace();
			}
		}
		socket.close();
		hashMap.remove(socket.getLocalPort());
	}
}
