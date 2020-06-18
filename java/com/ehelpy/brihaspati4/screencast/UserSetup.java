package com.ehelpy.brihaspati4.screencast;

import com.ehelpy.brihaspati4.isec.MsgIntegrity;
//import core.port.TcpClient;

import com.ehelpy.brihaspati4.screencast.Global;
import com.ehelpy.brihaspati4.screencast.TcpClient;
import com.ehelpy.brihaspati4.screencast.UserEvent;
import com.ehelpy.brihaspati4.screencast.UserScreen;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UserSetup {
    // newObj restricts creation of new instances
    private static boolean newObj = false;
    private        int width, height;

    private static HashMap<String, UserSetup> hashMap = new HashMap<>();
    private TcpClient tcp = null;

    /**
     * private constructor to restrict instantiation by external code
     * @throws AssertionError ensures that constructor is NOT invoked
     * accidentally (by sub-class, reflection etc)
     */
    private UserSetup() {if (!newObj) throw new AssertionError();}

    private static UserSetup newInstance(String ip, int port, byte[] password)
            throws IOException {
        newObj = true;
        UserSetup user = new UserSetup();
        newObj = false;
        user.tcp = TcpClient.getInstance(ip, port);
        user.tcp.write(password, 0, password.length);
        byte[] auth = new byte[Global.AUTH_PASS.val().length()];
        System.out.println("Password sent to " + user.tcp.getIpPort() + " for Authentication");
        user.tcp.readFully(auth);
        String strAuth = new String(auth);
        System.out.println("From " + user.tcp.getIpPort() + "recieved message => " + strAuth);
        if (Global.AUTH_PASS.val().equals(strAuth)) {
            System.out.println("Session authenticated by " + user.tcp.getIpPort());
            user.width = user.tcp.readInt();
            user.height = user.tcp.readInt();
            String ipPort = user.tcp.getIpPort();
            hashMap.put(ipPort, user);
            user.start();
            return user;
        } else throw new IllegalArgumentException();
    }

    public static UserSetup getInstance(String ip, int port, byte[] password)
            throws IOException {
        UserSetup user = hashMap.get(ip + ":" + port);
        if (user == null) user = newInstance(ip, port, password);
        return user;
    }

    public void start() {
        JFrame frame = new JFrame();
        JDesktopPane desktopPane = new JDesktopPane();
        JInternalFrame internalFrame = new JInternalFrame(
                "Server" + tcp.getIpPort(),
                true, true, true );
        JPanel panel = new JPanel();
        frame.add(desktopPane, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        internalFrame.setLayout(new BorderLayout());
        internalFrame.getContentPane().add(panel, BorderLayout.CENTER);
        internalFrame.setSize(100, 100);
        desktopPane.add(internalFrame);
        try {internalFrame.setMaximum(true);
        } catch (PropertyVetoException e) {e.printStackTrace();}
        panel.setFocusable(true);
        internalFrame.setVisible(true);
        UserScreen.getInstance(tcp, panel);
        UserEvent.getInstance(tcp, panel, width, height);
    }
}
