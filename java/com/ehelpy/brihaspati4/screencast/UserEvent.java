package com.ehelpy.brihaspati4.screencast;

//import core.port.TcpClient;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.HashMap;

public class UserEvent
        implements KeyListener, MouseMotionListener, MouseListener {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private int width, height;

    private final static int MOUSE_PRESS = -1;
    private final static int MOUSE_RELEASE = -2;
    private final static int KEY_PRESS = -3;
    private final static int KEY_RELEASE = -4;
    private final static int MOUSE_MOVE = -5;

    private static HashMap<String, UserEvent> hashMap = new HashMap<>();
    private TcpClient tcp;
    private JPanel panel;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private UserEvent() {if (!newObj) throw new AssertionError();}

    private static UserEvent newInstance(
            TcpClient tcpClient, JPanel jPanel, int width, int height) {
        newObj = true;
        UserEvent user = new UserEvent();
        newObj = false;
        user.width = width;
        user.height = height;
        user.tcp = tcpClient;
        user.panel = jPanel;
        user.panel.addKeyListener(user);
        user.panel.addMouseListener(user);
        user.panel.addMouseMotionListener(user);
        hashMap.put(user.tcp.getIpPort(), user);
        return user;
    }

    public static UserEvent getInstance(
            TcpClient tcpClient, JPanel jPanel, int width, int height) {
        UserEvent user = hashMap.get(tcpClient.getIpPort());
        if (user == null) user = newInstance(
                tcpClient, jPanel, width, height);
        else {
            user.panel = jPanel;
            user.width = width;
            user.height = height;
        }
        return user;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        try {
            tcp.writeInt(KEY_PRESS);
            tcp.writeInt(e.getKeyCode());
            tcp.flush();
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public void keyReleased(KeyEvent e) {
        try {
            tcp.writeInt(KEY_RELEASE);
            tcp.writeInt(e.getKeyCode());
            tcp.flush();
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        try {
            tcp.writeInt(MOUSE_PRESS);
            if (e.getButton() == 3) tcp.writeInt(4);
            else tcp.writeInt(16);
            tcp.flush();
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public void mouseReleased(MouseEvent e) {
        try {
            tcp.writeInt(MOUSE_RELEASE);
            if (e.getButton() == 3) tcp.writeInt(4);
            else tcp.writeInt(16);
            tcp.flush();
        } catch (IOException ex) {ex.printStackTrace();}
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        double xScale = width * 1.0 / panel.getWidth();
        double yScale = height * 1.0 / panel.getHeight();
        try {
            tcp.writeInt(MOUSE_MOVE);
            tcp.writeInt((int) (e.getX() * xScale));
            tcp.writeInt((int) (e.getY() * yScale));
            tcp.flush();
        } catch (IOException ex) {ex.printStackTrace();}
    }
}
