package com.ehelpy.brihaspati4.Address_Book;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import javax.swing.JTextField;

public class waiting_window_addressbook extends JFrame {

	private JPanel contentPane;
	static String Email_Id;
	private JTextField txtCalling;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					waiting_window_addressbook frame = new waiting_window_addressbook();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public static void Receive(String get)
	{
		Email_Id = get;
	
	}
	
	public waiting_window_addressbook() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtCalling = new JTextField();
		txtCalling.setText("calling......");
		txtCalling.setBounds(177, 130, 86, 20);
		contentPane.add(txtCalling);
		txtCalling.setColumns(10);
		

	}
}
