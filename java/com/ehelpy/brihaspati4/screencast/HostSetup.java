package com.ehelpy.brihaspati4.screencast;
// lt col jitesh ps updated this on 14 june 2020 ; 0700 Hrs
//this code is for declaring for common functions which can be used for general activities like pinging , get instances
import com.ehelpy.brihaspati4.isec.MsgIntegrity;
import com.ehelpy.brihaspati4.isec.RightsMgmt;
//import core.port.Datagram;
//import core.port.TcpClient;
//import core.port.TcpServer;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class HostSetup implements Runnable {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private boolean loop;
    private byte[] password;
    private int width, height, pwdLength;

    private static ThreadGroup threadGroup =
            new ThreadGroup("HostSetup");
    private static HashMap<String, HostSetup> hashMap = new HashMap<>();
    private String pwdHash;
    private String threadName = "HostSetup";
    private com.ehelpy.brihaspati4.screencast.TcpServer tcpServer = null;
    private GraphicsEnvironment gEnv = null;
    private GraphicsDevice gDev = null;
    private Dimension dim = null;
    private Rectangle rectangle = null;
    private Robot robot = null;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private HostSetup() {if (!newObj) throw new AssertionError();}

    private static HostSetup newInstance(byte[] password)
            throws IOException, AWTException, NoSuchAlgorithmException {
        newObj = true;
        HostSetup host = new HostSetup();
        newObj = false;
        host.tcpServer = com.ehelpy.brihaspati4.screencast.TcpServer.newInstance();
        host.gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        host.gDev = host.gEnv.getDefaultScreenDevice();
        host.dim = Toolkit.getDefaultToolkit().getScreenSize();
        host.rectangle = new Rectangle(host.dim);
        host.width = host.rectangle.width;
        host.height = host.rectangle.height;
        host.robot = new Robot(host.gDev);
        host.loop = true;
        host.pwdHash = new String(MsgIntegrity.getDigest(
                password, MsgIntegrity.DGST_SHA_256));
        host.pwdLength = password.length;
        host.password = password;
        Thread thread = new Thread(threadGroup, host, host.pwdHash);
        hashMap.put(host.pwdHash, host);
        thread.start();
        return host;
    }

    public static HostSetup newInstance(int pwdLength)
            throws NoSuchAlgorithmException, IOException, AWTException {
        byte[] password = RightsMgmt.randomPWD(pwdLength);
        String pwdHash = new String(MsgIntegrity.getDigest(
                password, MsgIntegrity.DGST_SHA_256));
        HostSetup host = hashMap.get(pwdHash);
        if (host == null) host = newInstance(password);
        return host;
    }

    public static HostSetup getInstance(byte[] password)
            throws NoSuchAlgorithmException, IOException, AWTException {
        String pwdHash = new String(MsgIntegrity.getDigest(
                password, MsgIntegrity.DGST_SHA_256));
        HostSetup host = hashMap.get(pwdHash);
        if (host == null) host = newInstance(password);
        return host;
    }

    public void setPassword(byte[] newPassword)
            throws NoSuchAlgorithmException {
        pwdHash = new String(MsgIntegrity.getDigest(
                newPassword, MsgIntegrity.DGST_SHA_256));
        pwdLength = newPassword.length;
        password = newPassword;
    }

    public void pingUser(String ip, int port)
            throws UnknownHostException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES);
        byteBuffer.putInt(tcpServer.getPort());
        byte[] tcpPort = byteBuffer.array();
        byte[] header = com.ehelpy.brihaspati4.screencast.Datagram.CAST_PASS;
        byte[] message = new byte[header.length+ tcpPort.length + password.length];
        System.arraycopy(header, 0, message,
                0, header.length);
        System.arraycopy(tcpPort, 0, message,
                header.length, tcpPort.length);
        System.arraycopy(password, 0, message,
                header.length + tcpPort.length, password.length);
        System.out.println("Pinging " + ip + ":" + port + "with message => " + message);
        com.ehelpy.brihaspati4.screencast.Datagram.outbound(message, ip, port);
    }

    public void close() {loop = false;}
    public static void close(byte[] password)
            throws NoSuchAlgorithmException {
        String pwdHash = new String(MsgIntegrity.getDigest(
                password, MsgIntegrity.DGST_SHA_256));
        HostSetup host = hashMap.get(pwdHash);
        if (host != null) host.loop = false;
    }

    public void run() {
        System.out.println("HostSetup running at port " + tcpServer.getPort());
        String accessGranted = com.ehelpy.brihaspati4.screencast.Global.AUTH_PASS.val();
        String accessDenied  = com.ehelpy.brihaspati4.screencast.Global.AUTH_FAIL.val();
        int printCount = 0;
        int clientCount = 0, prev = -1, total = 0;
        while(loop) {
            total = tcpServer.getClientCount();
            printCount++;
            if (printCount == 10000) {
                System.out.print(" " + total);
                printCount = 0;
            }
            if (clientCount < total) {
                System.out.println("Looking for new client");
                try {
                    com.ehelpy.brihaspati4.screencast.TcpClient tcpClient
                            = tcpServer.getClient(clientCount);
                    System.out.println("Client " + tcpClient.getIpPort() + " connected on port " + tcpServer.getPort());
                    clientCount++;
                    byte[] pwd = new byte[pwdLength];
                    System.out.println("Waiting on " + tcpClient.getIpPort() + " for " + pwd.length + " bytes of password");
                    tcpClient.readFully(pwd);
                    String pwdHash = new String(MsgIntegrity.getDigest(
                            pwd, MsgIntegrity.DGST_SHA_256));
                    if(this.pwdHash.equals(pwdHash)) {
                        System.out.println(tcpClient.getIpPort() + " Authenticated!!");
                        tcpClient.writeBytes(accessGranted);
                        tcpClient.writeInt(width);
                        tcpClient.writeInt(height);
                        com.ehelpy.brihaspati4.screencast.HostScreen.getInstance(tcpClient
                                , robot, rectangle);
                        com.ehelpy.brihaspati4.screencast.HostEvent.getInstance(tcpClient, robot);
                    } else tcpClient.writeUTF(accessDenied);
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } } }
        hashMap.remove(pwdHash);
    }
}
