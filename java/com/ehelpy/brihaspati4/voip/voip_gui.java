package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.ehelpy.brihaspati4.authenticate.properties_access;

import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class voip_gui {

    private JFrame VOIP;
    static boolean status = false;
    static long key = 0;
    static int token = 0;
    static String IPaddr_called = null;
    static String IPaddr_caller = null;
    /**
     * Launch the application.
     */
    public static void callstart(long sym_key, String IPaddr, int passedtoken) {
        key = sym_key;
        token = passedtoken;
        if(token == 0)
            IPaddr_called = IPaddr;
        else
            IPaddr_caller = IPaddr ;
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    voip_gui window = new voip_gui();
                    window.VOIP.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public voip_gui() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        VOIP = new JFrame();
        VOIP.setIconImage(Toolkit.getDefaultToolkit().getImage("D:\\study\\studymtrl\\codes\\client\\imgs\\title.jpg"));
        VOIP.setTitle("VOIP_B4Services");
        VOIP.setBounds(100, 100, 487, 367);
        VOIP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        VOIP.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon("D:\\study\\studymtrl\\codes\\client\\imgs\\Phone_calling.gif"));
        lblNewLabel.setBounds(140, 41, 208, 150);
        VOIP.getContentPane().add(lblNewLabel);

        JButton btnStop = new JButton("Stop");
        btnStop.setFont(new Font("Arial", Font.BOLD, 18));
        btnStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        btnStop.setBounds(271, 216, 121, 42);
        VOIP.getContentPane().add(btnStop);

        JButton btnStart = new JButton("Start");
        btnStart.setFont(new Font("Arial", Font.BOLD, 18));
        btnStart.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent arg0) {
                switch(token) {
                // in case of the recieving client
                case 0: {
                    btnStart.setEnabled(true);
                    status = voip.rxcall(IPaddr_caller, key);
                    System.out.println("status ="  +  status);
                }
                // in case of the calling client
                case 1:  {
                    btnStart.setEnabled(false);
                    status = voip.start_call(IPaddr_called, key);
                    System.out.println("status ="  +  status);
                }

                }
            }
        });
        btnStart.setBounds(49, 216, 121, 42);
        VOIP.getContentPane().add(btnStart);
    }
}
