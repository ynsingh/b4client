package com.ehelpy.brihaspati4.screencast;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for functions detailing host screen capturing

//import core.port.TcpClient;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class HostScreen implements Runnable {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private boolean loop;

    private static ThreadGroup threadGroup =
            new ThreadGroup("HostScreen");
    private static HashMap<String, HostScreen> hashMap = new HashMap<>();
    private Rectangle rectangle = null;
    private Robot robot = null;
    private TcpClient tcp;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private HostScreen() {if (!newObj) throw new AssertionError();}

    private static HostScreen newInstance(
            TcpClient tcpClient, Robot robot, Rectangle rectangle) {
        newObj = true;
        HostScreen host = new HostScreen();
        newObj = false;
        host.loop = true;
        host.rectangle = rectangle;
        host.robot = robot;
        host.tcp = tcpClient;
        String ipPort = host.tcp.getIpPort();
        Thread thread = new Thread(threadGroup, host, ipPort);
        hashMap.put(ipPort, host);
        thread.start();
        return host;
    }

    public static HostScreen getInstance(
            TcpClient tcpClient, Robot robot, Rectangle rectangle) {
        HostScreen host = hashMap.get(tcpClient.getIpPort());
        if (host == null) host = newInstance(tcpClient, robot, rectangle);
        else {
            host.rectangle = rectangle;
            host.robot = robot;
        }
        return host;
    }

    public void close() {loop = false;}
    public static void close(TcpClient tcpClient) {
        HostScreen host = hashMap.get(tcpClient.getIpPort());
        if (host != null) host.loop = false;
    }

    public void run() {
        while(loop) {
            BufferedImage image = robot.createScreenCapture(rectangle);
            try {tcp.writeImage(image, "jpeg");
            } catch (IOException e) {e.printStackTrace();}
            try {Thread.sleep(10);
            } catch (InterruptedException e) {e.printStackTrace();}
        }
        hashMap.remove(tcp.getIpPort());
    }
}
