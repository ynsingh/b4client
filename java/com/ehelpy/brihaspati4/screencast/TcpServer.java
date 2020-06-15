package com.ehelpy.brihaspati4.screencast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class TcpServer implements Runnable {
    private final static int     NULL_PORT   = Global1.NULL_PORT.val();
    private final static int     MIN_PORT    = Global1.MIN_PORT.val();
    private final static int     MAX_PORT    = Global1.MAX_PORT.val();
    // newObj restricts creation of new instances
    private       static boolean newObj      = false;
    // getInstance(port) does NOT return reserved TcpServer objects
    private              boolean reserved;
    private              boolean loop;

    private static ThreadGroup tcpThreads
            = new ThreadGroup("tcpThreads");
    private static HashMap<Integer, TcpServer> hashMap = new HashMap<>();
    private ArrayList<Socket> clientele = null;
    private ServerSocket socket = null;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private TcpServer() {if (!newObj) throw new AssertionError();}

    private static TcpServer newInstance(int port) throws IOException {
        newObj = true;
        TcpServer tcp = new TcpServer();
        newObj = false;
        tcp.reserved = false;
        tcp.loop = true;
        if (port != NULL_PORT) tcp.socket = new ServerSocket(port);
        // 0 to use a core.port number that is automatically allocated
        else tcp.socket = new ServerSocket(0);
        tcp.clientele = new ArrayList<Socket>();
        port = tcp.socket.getLocalPort();
        Thread thread = new Thread(tcpThreads, tcp, Integer.toString(port));
        hashMap.put(port, tcp);
        thread.start();
        System.out.println("TCP Server listening on port " + port);
        return tcp;
    }

    public static TcpServer newInstance()
            throws IOException {return newInstance(NULL_PORT);}

    public static TcpServer getInstance(int port)
            throws IOException, IllegalAccessException {
        if (port >= MIN_PORT && port <= MAX_PORT) {
            TcpServer tcp = hashMap.get(port);
            if (tcp == null) tcp = newInstance(port);
            if (tcp.reserved) throw new IllegalArgumentException();
            else return tcp;
        } else throw new IllegalArgumentException();
    }

    public void setReserved(boolean val) {reserved = val;}

    public void close() {loop = false;}
    public static void close(int port) {
        TcpServer tcp = hashMap.get(port);
        if (tcp != null) tcp.loop = false;
    }

    public int getPort() {return socket.getLocalPort();}

    public int getClientCount() {return clientele.size();}

    public TcpClient getClient(int index) throws IOException {
        return TcpClient.getInstance(clientele.get(index));}

    public void run() {
        while (loop) {
            try {
                clientele.add(socket.accept());
                Socket cSocket = clientele.get(clientele.size() - 1);
                System.out.println(cSocket.getInetAddress().getHostAddress() + ":" + cSocket.getPort()
                        + " connected at port " + socket.getLocalPort());
                System.out.println("Clientele size " + clientele.size());
            } catch (IOException e) {
                System.out.println(clientele.size()
                        + ") IOException at TCP Server port "
                        + socket.getLocalPort());
                e.printStackTrace();
            }
        }
        try {socket.close();
        } catch (IOException e) {
            System.out.println(clientele.size()
                    + ") IOException in closing TCP Server port "
                    + socket.getLocalPort());
            e.printStackTrace();
        }
        hashMap.remove(socket.getLocalPort());
    }
}
