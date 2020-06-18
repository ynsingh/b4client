package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.Address_Book.Display_Window_After_Login;
import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.screencast.Gui;
import com.ehelpy.brihaspati4.sms.SMS_Window;
import com.ehelpy.brihaspati4.sms.Send_SMS_Window;
import com.ehelpy.brihaspati4.sms.sms_send_rec_management;
import com.ehelpy.brihaspati4.authenticate.GlobalObject;
import com.ehelpy.brihaspati4.authenticate.emailid;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import com.ehelpy.brihaspati4.DFS.DFSUI;

import java.awt.Color;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;

import com.ehelpy.brihaspati4.DFS.DFS_gui;
import com.ehelpy.brihaspati4.DFS.DistFileSys;

//Maj Saurabh Vij Dated 15 Mar 2019 ; 1230 Hrs
//this code is for GUI for B4 Services and gives main GUI for the Brihaspati4 P2P network
//it will enable the user to select appropriate service as per requirement
public class B4services {

	public static JFrame BServices;
	private static int token = 0; //the token can be initiated and value can be assigned for start of any particular service
	public static ServerSocketChannel ss = null;
	public static boolean b4services_window_open = false;
	public static boolean display_window_open = false;
	public static boolean voip_gui_window = false;
	public static boolean ss_gui_window = false;
	public static boolean address_book_new_entry_window = false;
	public static boolean address_book_show_details_window = false;
	public static boolean address_book_search_window = false;
	public static boolean address_book_delete_window = false;
	public static boolean address_book_multiple_entries_window = false;
	public static boolean sms_send_window = false;
	public static boolean sms_window = false;
	public static boolean sms_reader_window = false;
	public static boolean sms_sent_messages_window = false;
	public static boolean DFS_gui_window = false;		
	/**
	 * Launch the application.
	 */
	public static void service() {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					B4services window = new B4services();
					window.BServices.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public B4services() {
		initialize();
		
		b4services_window_open = true;
		
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
		  
		  Thread t = new voip_rxcall(ss, Integer.valueOf(properties_access.read_property("client.properties","Rxsocch")));
		  voip_rxcall.flag = true;
		  t.start();
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
	
		BServices = new JFrame();
		BServices.setTitle("B4Server Services");
		BServices.setBounds(100, 100, 450, 450);


		BServices.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				GlobalObject.setRunStatus(false);	
												
				BServices.dispose();
				
				try {
					voip_rxcall.ss.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if(sms_send_rec_management.sending_message||!CommunicationManager.ReceivingBuffer.isEmpty())
				{
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
		    	
				System.out.println("ALL THREADS, TIMERS AND SOCKETS ARE CLOSED");
		    	System.exit(0);
			}
		});
		
		BServices.getContentPane().setLayout(null);
		
		JRadioButton NewRadioButton = new JRadioButton("ADDRESS BOOK");
		NewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				token = 1;
				
				}
			
		});
		NewRadioButton.setBackground(Color.YELLOW);
		NewRadioButton.setForeground(Color.BLUE);
		NewRadioButton.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton.setBounds(8, 45, 200, 25);
		BServices.getContentPane().add(NewRadioButton);
		
		JRadioButton NewRadioButton_1 = new JRadioButton("VOIP");
		NewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				token=2;
			}
		});
		NewRadioButton_1.setBackground(Color.YELLOW);
		NewRadioButton_1.setForeground(Color.RED);
		NewRadioButton_1.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_1.setBounds(8, 90, 127, 25);
     	BServices.getContentPane().add(NewRadioButton_1);
		
		JRadioButton NewRadioButton_2 = new JRadioButton("SMS");
		NewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				token = 3;
			}
		});
		NewRadioButton_2.setBackground(Color.YELLOW);
		NewRadioButton_2.setForeground(Color.GREEN);
		NewRadioButton_2.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_2.setBounds(8, 135, 127, 25);
		BServices.getContentPane().add(NewRadioButton_2);
		
		JRadioButton NewRadioButton_3 = new JRadioButton("EMAIL");
		NewRadioButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				token = 4;
			}
		});
		NewRadioButton_3.setBackground(Color.YELLOW);
		NewRadioButton_3.setForeground(Color.ORANGE);
		NewRadioButton_3.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_3.setBounds(8, 180, 127, 25);
		BServices.getContentPane().add(NewRadioButton_3);
	
		///////////////////////////////////////////////////////////////////////////////////
		JRadioButton NewRadioButton_4 = new JRadioButton("SCREEN SHARE");
		NewRadioButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				token = 5;
			}
		});
		NewRadioButton_4.setBackground(Color.YELLOW);
		NewRadioButton_4.setForeground(Color.blue);
		NewRadioButton_4.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_4.setBounds(8, 225, 207, 25);
		NewRadioButton_4.setBounds(8, 225, 207, 25);
		BServices.getContentPane().add(NewRadioButton_4);

		////////////////////////////////////////////////////////////////////////////////////////////
		JRadioButton NewRadioButton_5 = new JRadioButton("DFS");
		NewRadioButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				token = 6;
				
				}
			
		});
		NewRadioButton_5.setBackground(Color.YELLOW);
		NewRadioButton_5.setForeground(Color.BLUE);
		NewRadioButton_5.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_5.setBounds(8, 325, 127, 25);
		NewRadioButton_5.setBounds(8, 325, 127, 25);
		BServices.getContentPane().add(NewRadioButton_5);
		//////////////////////////////////////////////////////////////////////////////////
		JRadioButton NewRadioButton_6 = new JRadioButton("Search");
		NewRadioButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				token = 7;

			}

		});
		NewRadioButton_6.setBackground(Color.YELLOW);
		NewRadioButton_6.setForeground(Color.BLUE);
		NewRadioButton_6.setFont(new Font("Tahoma", Font.BOLD, 20));
		NewRadioButton_6.setBounds(8, 320, 127, 25);
		NewRadioButton_6.setBounds(8, 270, 127, 25);
		BServices.getContentPane().add(NewRadioButton_6);

		ButtonGroup bG = new ButtonGroup();
	    bG.add(NewRadioButton);
	    bG.add(NewRadioButton_1);
	    bG.add(NewRadioButton_2);
	    bG.add(NewRadioButton_3);
	    bG.add(NewRadioButton_4);
	    bG.add(NewRadioButton_5);
		bG.add(NewRadioButton_6);
	    
	    JButton btnOk = new JButton("OK");
	    btnOk.addActionListener(new ActionListener() 
	      {
	    	public void actionPerformed(ActionEvent e) 
	    	 {
	    		e.getActionCommand();
	    		
	    		if (token == 1 ) 
    		    {
	    			b4services_window_open = false;
    			BServices.setVisible(false);
				BServices.dispose();
				
				Display_Window_After_Login.main();
				
				}				
				
				
	    		if (token == 2 ) 
	    		    {
	    			b4services_window_open = false;
	    			BServices.setVisible(false);
					BServices.dispose();
					
					voip_gui.main();
					
					}
				 if(token == 7)
				 {
					 b4services_window_open = false;
					 BServices.setVisible(false);
					 BServices.dispose();

					 try
					 {
						 com.ehelpy.brihaspati4.Distributed_Search.SearchClient_v4.main( );
					 }
					 catch (IOException e1)
					 {
						 // TODO Auto-generated catch block
						 e1.printStackTrace();
					 }

				 }
					
	    		if (token == 3)
	    		{
	    			b4services_window_open = false;
	    			BServices.setVisible(false);
					BServices.dispose();
					
					SMS_Window obj = new SMS_Window();
					obj.setVisible(true);
	    			
	    		}
			if (token == 5)
	    		{ System.out.println("token selected for screen share++++++++++++++++++++++++++++++++++++++++");
	    			b4services_window_open = false;
	    			BServices.setVisible(false);
					BServices.dispose();
					System.out.println("token selected for screen share++++++++++++++++++++++++++++++++++++++++");
					try {
						Gui.start();

					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}

			if(token == 6)
    			{
    			    b4services_window_open = false;
    			    BServices.setVisible(false);
				    BServices.dispose();
				    DFSUI.main();
    			}
	    				
			}
	    			
	      });
	    
	    
	    btnOk.setFont(new Font("Calibri", Font.BOLD, 18));
	    btnOk.setBounds(105, 374, 74, 25);
	    BServices.getContentPane().add(btnOk);
	    
	    JButton btnCancel = new JButton("CANCEL");
	    btnCancel.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	
	    	GlobalObject.setRunStatus(false);	
	    		    		    	
	    	BServices.dispose();
	    	
	    	try {
				voip_rxcall.ss.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(sms_send_rec_management.sending_message||!CommunicationManager.ReceivingBuffer.isEmpty()||!CommunicationManager.RxBufferSMS.isEmpty())
			{
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
	    	System.out.println("ALL THREADS, TIMERS AND SOCKETS ARE CLOSED AND BUFFERS CLEARED");	    	
	    	System.exit(0);
	    	}
	    });
	    btnCancel.setFont(new Font("Calibri", Font.BOLD, 18));
	    btnCancel.setBounds(274, 374, 113, 25);
	    BServices.getContentPane().add(btnCancel);
	    
	    String email_id=emailid.getemaild();
	    JLabel lblNewLabel = new JLabel("WELCOME "+email_id);
	    lblNewLabel.setForeground(new Color(128, 0, 0));
	    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setBounds(23, 13, 385, 25);
	    BServices.getContentPane().add(lblNewLabel);
	}
}
