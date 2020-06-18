package com.ehelpy.brihaspati4.screencast;

//import auxx.File;
//import auxx.TestMain;
//import cast.HostSetup;
//import cast.host.InitConnection;
//import com.ehelpy.brihaspati4.isec.RightsMgmt;
//import core.port.Datagram;
//import core.port.UdpMaster;

import com.ehelpy.brihaspati4.voip.B4services;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
// lt col jitesh ps updated this on 14 june 2020 ; 0500 Hrs
//this code is for initiating the GUI for screenshare

public class Gui {
    public static void main() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gui frame = new Gui();
                    frame.setVisible(true);
                    B4services.ss_gui_window = true;

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setVisible(boolean b) {

            }

    private static String fileName = "dat" + System.getProperty("file.separator") + "userInfo.csv";

    public static void start() throws IllegalArgumentException, FileNotFoundException, IOException {
    	com.ehelpy.brihaspati4.screencast.UdpMaster.getInstance(4907); // Initiating UDP listener at default core.port
    	
        TableModel tableModel = com.ehelpy.brihaspati4.screencast.File.array2table(com.ehelpy.brihaspati4.screencast.File.csv2array(fileName)); //loading names from .csv file
        JTable table = new JTable(tableModel);
        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.setFont(font);
        table.setRowHeight(30);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION); //this is for multiple interval selection
        
        JFrame frame = new JFrame();
        JTextField textField = new JTextField();
        frame.setSize(600, 400);
        Color c3 = new Color(35,220,20);
        frame.getContentPane().setBackground(c3);
        frame.add(new JScrollPane(table));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);

        JButton b = new JButton("Ping");
        b.setBounds(650, 150, 95, 30);
        b.setBackground(Color.blue);
        Color c4 = new Color(255,255,0);
        b.setForeground(c4);
        b.setFont(new Font("Times New Roman", Font.BOLD, 20));
        b.addActionListener(e -> {
            textField.setBounds(650, 200, 150, 30);
            textField.setText("Sent UDP to Addressee");
            if (table.getSelectedRows().length > 0) {
                int[] selectedRows = table.getSelectedRows();
                for (int i = 0; i < selectedRows.length; i++) {
                    String email = table.getValueAt(selectedRows[i], 1).toString();
                    // TODO - Remove next two lines after implementing routing
                    String ip = com.ehelpy.brihaspati4.screencast.TestMain.getIP(email);
                    int port = com.ehelpy.brihaspati4.screencast.TestMain.getPort(email);
                    try { com.ehelpy.brihaspati4.screencast.Datagram.outbound(com.ehelpy.brihaspati4.screencast.Datagram.SCREEN_CAST, ip, port);
					} catch (UnknownHostException e1) {
                        System.out.println("Host Not Found");
                        e1.printStackTrace();
                    }
                    JFrame frame1 = new JFrame("Addressee Details");
                    JOptionPane.showMessageDialog(frame1,"Email => " + email 
                    		+ "\n" + "IP:Port => "+ ip + ":" + port);
                } } });
        
        frame.add(b);
        frame.add(textField);
        frame.setSize(800, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }
// connect request function is for host to make a decision whether to connect or not
    public static void connectRequest(String ip, int port) {
        JFrame f1 = new JFrame();
        f1.setSize(500, 300);
        Color c = new Color(155, 100, 200);
        f1.getContentPane().setBackground(c);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        f1.setLocation(dim.width / 2 - f1.getSize().width / 2, dim.height / 2 - f1.getSize().height / 2);
        f1.setVisible(true);
        Color c1 = new Color(255, 255, 0);
        JButton b1 = new JButton("YES");
        b1.setBounds(100, 170, 95, 30);
        Color c5 = new Color(35, 220, 20);
        b1.setBackground(c5);
        b1.setForeground(c1);
        b1.setFont(new Font("Times New Roman", Font.BOLD, 20));
        b1.addActionListener(e -> {
            try {
                com.ehelpy.brihaspati4.screencast.HostSetup host = com.ehelpy.brihaspati4.screencast.HostSetup.newInstance(10);
                host.pingUser(ip, port);
                f1.dispose();
            } catch (NoSuchAlgorithmException | IOException | AWTException ex) {
                ex.printStackTrace();
            }
        });
        JButton b2 = new JButton("NO");
        b2.setBounds(300, 170, 95, 30);
        b2.setBackground(Color.RED);
        Color c2 = new Color(0, 255, 255);
        b2.setForeground(c2);
        b2.setFont(new Font("Times New Roman", Font.BOLD, 20));
        b2.addActionListener(e -> {
            try {
                com.ehelpy.brihaspati4.screencast.Datagram.outbound(com.ehelpy.brihaspati4.screencast.Datagram.CAST_FAIL, ip, port);
            } catch (UnknownHostException e1) {
                System.out.println("HOST NOT FOUND");
                e1.printStackTrace();
            }
            JFrame frame2 = new JFrame("DISCONNECT");
            JOptionPane.showMessageDialog(frame2, "YOU HAVE SELECTED TO DISCONNECT");
        });

        f1.add(b1);
        f1.add(b2);
        f1.setLayout(null);
        f1.setVisible(true);
        f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f1.getContentPane().setLayout(null);
        JLabel lblNewLabel = new JLabel("Screen cast request from " + ip);
        lblNewLabel.setForeground(Color.white);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblNewLabel.setBounds(32, 74, 387, 46);
        f1.getContentPane().add(lblNewLabel);
    }
// these are pop ups on various actions taken
    public static void connectionDenied() {
        JFrame frame3 = new JFrame("DISCONNECT");
        JOptionPane.showMessageDialog(frame3, "FAREND SELECTED TO DISCONNECT");
    }

    public static void connectionEst() {
        JFrame frame4 = new JFrame("CONNECTED");
        JOptionPane.showMessageDialog(frame4, "FAREND SELECTED TO CONNECT");
    }
}



