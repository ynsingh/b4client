package com.ehelpy.brihaspati4.screencast;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for declaring host events
import com.ehelpy.brihaspati4.screencast.TcpClient;

import java.awt.Robot;
import java.util.HashMap;

public class HostEvent implements Runnable {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private boolean loop;

    private static ThreadGroup threadGroup =
            new ThreadGroup("HostEvent");
    private static HashMap<String, HostEvent> hashMap = new HashMap<>();
    private Robot robot = null;
    private TcpClient tcp;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private HostEvent() {if (!newObj) throw new AssertionError();}

    private static HostEvent newInstance(TcpClient tcpClient, Robot robot) {
        newObj = true;
        HostEvent host = new HostEvent();
        newObj = false;
        host.loop = true;
        host.robot = robot;
        host.tcp = tcpClient;
        String ipPort = host.tcp.getIpPort();
        Thread thread = new Thread(threadGroup, host, ipPort);
        hashMap.put(ipPort, host);
        thread.start();
        return host;
    }

    public static HostEvent getInstance(TcpClient tcpClient, Robot robot) {
        HostEvent host = hashMap.get(tcpClient.getIpPort());
        if (host == null) host = newInstance(tcpClient, robot);
        else host.robot = robot;
        return host;
    }

    public void close() {loop = false;}
    public static void close(String ip, int port) {
        HostEvent host = hashMap.get(ip + ":" + port);
        if (host != null) host.loop = false;
    }

    public void run() {
        while(loop) {
            int command;
            if (tcp.hasNextInt()) {
                command = tcp.nextInt();
                switch (command) {
                    case 1: robot.mousePress(tcp.nextInt()); break;
                    case 2: robot.mouseRelease(tcp.nextInt()); break;
                    case 3: robot.keyPress(tcp.nextInt()); break;
                    case 4: robot.keyRelease(tcp.nextInt()); break;
                    case 5: robot.mouseMove(tcp.nextInt(), tcp.nextInt()); break;
                }
            }
        }
        hashMap.remove(tcp.getIpPort());
    }
}
