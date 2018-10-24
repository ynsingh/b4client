package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class discon {

    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void disconnect() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    discon window = new discon();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public discon() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        btnOk.setFont(new Font("Times New Roman", Font.BOLD, 16));
        btnOk.setBounds(159, 184, 97, 25);
        frame.getContentPane().add(btnOk);

        JLabel lblNewLabel = new JLabel("The farend refused to connect.");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 18));
        lblNewLabel.setBounds(65, 81, 292, 40);
        frame.getContentPane().add(lblNewLabel);
    }
}
