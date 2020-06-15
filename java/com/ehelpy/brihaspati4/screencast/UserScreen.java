package com.ehelpy.brihaspati4.screencast;

//import core.port.TcpClient;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.HashMap;

public class UserScreen implements Runnable {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private boolean loop;

    private static ThreadGroup threadGroup =
            new ThreadGroup("UserScreen");
    private static HashMap<String, UserScreen> hashMap = new HashMap<>();
    private TcpClient tcp;
    private JPanel panel;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private UserScreen() {if (!newObj) throw new AssertionError();}

    private static UserScreen newInstance(
            TcpClient tcpClient, JPanel jPanel) {
        newObj = true;
        UserScreen user = new UserScreen();
        newObj = false;
        user.loop = true;
        user.tcp = tcpClient;
        user.panel = jPanel;
        String ipPort = user.tcp.getIpPort();
        Thread thread = new Thread(threadGroup, user, ipPort);
        hashMap.put(ipPort, user);
        thread.start();
        return user;
    }

    public static UserScreen getInstance(
            TcpClient tcpClient, JPanel jPanel) {
        UserScreen user = hashMap.get(tcpClient.getIpPort());
        if (user == null) user = newInstance(tcpClient, jPanel);
        else user.panel = jPanel;
        return user;
    }

    public void close() {loop = false;}
    public static void close(TcpClient tcpClient) {
        UserScreen user = hashMap.get(tcpClient.getIpPort());
        if (user != null) user.loop = false;
    }

    public void run() {
        while (loop) {
            try {
                int width = panel.getWidth();
                int height = panel.getHeight();
                Image image = tcp.readImage();
                image = image.getScaledInstance(width, height, Image.SCALE_FAST);
                Graphics graphics = panel.getGraphics();
                graphics.drawImage(image, 0, 0, width, height, panel);
            } catch (IOException e) {e.printStackTrace();}
        }
        hashMap.remove(tcp.getIpPort());
    }
}
