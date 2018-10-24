package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.authenticate.b4server_services;
import com.ehelpy.brihaspati4.authenticate.debug_level;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.awt.event.ActionEvent;

public class voip_connect_dialog {

	private JFrame frame;
	private final JLabel lblVoip = new JLabel("VOIP");
	static boolean status = false;
	static long sym_key = 0;

	/**
	 * Launch the application.
	 */
	public static void connect(SocketChannel Sc, String received, int token) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					System.out.println("d2");
					voip_connect_dialog window = new voip_connect_dialog(Sc,received, token);
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
	public voip_connect_dialog(SocketChannel Sc, String received, int token) {
		initialize(Sc, received,token);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(SocketChannel Sc,String received, int token) {
		lblVoip.setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame = new JFrame();
		frame.getContentPane().setFont(new Font("Times New Roman", Font.BOLD, 18));
		frame.setBounds(100, 100, 429, 237);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				voip_rxcall.flag = false;
				b4server_services.frmBserverServices.dispose();
				debug_level.debug(0, "I have reached connect dialogue box");
				//sym_key = socketclient.client_socket(received, 25000, "Yes would like to connect");
				sym_key = socketclient.client_socket(received, Integer.valueOf(properties_access.read_property("client.properties","Txsocch")), "Yes would like to connect");
				debug_level.debug(1,"IP addr received of the caller is  =  " + received);
				voip_receive.receive(received, sym_key);
				try {
					Sc.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					b4server_services.ss.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setVisible(false);
				frame.dispose();
				status = true;
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton.setBounds(31, 112, 104, 34);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//socketclient.disconnect(received, 25000, "No would like to disconnect");
				socketclient.disconnect(received, Integer.valueOf(properties_access.read_property("client.properties","Txsocch")), "No would like to disconnect");
				frame.setVisible(false);
				frame.dispose();
			}
		});
		btnDisconnect.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnDisconnect.setBounds(226, 112, 125, 34);
		frame.getContentPane().add(btnDisconnect);
		
		JLabel lblNewLabel = new JLabel("Do you want to connect to  " + received);
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
		lblNewLabel.setBounds(12, 24, 387, 46);
		frame.getContentPane().add(lblNewLabel);
		
		
	}
}
