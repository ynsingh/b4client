package com.ehelpy.brihaspati4.authenticate;

import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
//Lt Col Raja Vijit Dated 22 May 2018 ; 1230 Hrs
//this code is for GUI for B4 Services
// This code will pop out the main GUI for the Brihaspati4 P2P network, it will enable the user to select appropriate service
// as per requirement
public class b4server_services {

	public static JFrame frmBserverServices;
	private static int token = 0; //the token can be initiated and value can be assigned for start of any particular service
	public static ServerSocketChannel ss = null;
	
	/**
	 * Launch the application.
	 */
	public static void service() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					b4server_services window = new b4server_services();
					window.frmBserverServices.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public b4server_services() {
		initialize();
		
		try {
			ss = ServerSocketChannel.open();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		  try {
			ss.socket().bind(new InetSocketAddress(Integer.valueOf(properties_access.read_property("client.properties","Rxsocch"))));
			//ss.socket().bind(new InetSocketAddress(9999));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			ss.configureBlocking(false);
			ss.setOption(StandardSocketOptions.SO_REUSEADDR, true);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  //System.out.println("flag value is = " + flagset);
		  //Thread t = new voip_rxcall(ss, 9999);
		  Thread t = new voip_rxcall(ss, Integer.valueOf(properties_access.read_property("client.properties","Rxsocch")));
		  voip_rxcall.flag = true;
		  t.start();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBserverServices = new JFrame();
		frmBserverServices.setTitle("B4Server Services");
		frmBserverServices.setBounds(100, 100, 450, 300);
		frmBserverServices.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBserverServices.getContentPane().setLayout(null);
		JRadioButton rdbtnNewRadioButton = new JRadioButton("VOIP");
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				token = 1;// for VOIP token can be  initialized to be 1
				}
			
		});
		rdbtnNewRadioButton.setBackground(Color.YELLOW);
		rdbtnNewRadioButton.setForeground(Color.BLUE);
		rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		rdbtnNewRadioButton.setBounds(8, 47, 101, 25);
		frmBserverServices.getContentPane().add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Storage");
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				token=2;
			}
		});
		rdbtnNewRadioButton_1.setBounds(8, 90, 127, 25);
		frmBserverServices.getContentPane().add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Search");
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				token = 2;
			}
		});
		rdbtnNewRadioButton_2.setBounds(8, 143, 127, 25);
		frmBserverServices.getContentPane().add(rdbtnNewRadioButton_2);
		ButtonGroup bG = new ButtonGroup();
	    bG.add(rdbtnNewRadioButton);
	    bG.add(rdbtnNewRadioButton_1);
	    bG.add(rdbtnNewRadioButton_2);
	    
	    JButton btnOk = new JButton("OK");
	    btnOk.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		e.getActionCommand();
	    		if (token == 1 ) {
	    			frmBserverServices.setVisible(false);
					frmBserverServices.dispose();
					voip_rxcall.flag = false;
				/*	try {
						ss.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					 voip_call.callstart(); // call the function voip
					token = 0;
					//serverSocket.close();//voip_rxcall.start_rx_call(serverSocket);
				}
				//rdbtnNewRadioButton.setSelected(true);
	    		
	    	}
	    });
	    btnOk.setFont(new Font("Calibri", Font.BOLD, 18));
	    btnOk.setBounds(105, 204, 74, 25);
	    frmBserverServices.getContentPane().add(btnOk);
	    
	    JButton btnCancel = new JButton("CANCEL");
	    btnCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	System.exit(0);
	    	}
	    });
	    btnCancel.setFont(new Font("Calibri", Font.BOLD, 18));
	    btnCancel.setBounds(274, 204, 113, 25);
	    frmBserverServices.getContentPane().add(btnCancel);
	    
	    JLabel lblNewLabel = new JLabel("WELCOME TO B4SERVICE MANAGER");
	    lblNewLabel.setForeground(new Color(128, 0, 0));
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setBounds(23, 13, 385, 25);
	    frmBserverServices.getContentPane().add(lblNewLabel);
	}
}
