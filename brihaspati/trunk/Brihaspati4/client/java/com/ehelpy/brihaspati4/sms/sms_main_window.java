package com.ehelpy.brihaspati4.sms;

import com.ehelpy.brihaspati4.sms.sms_methods;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class sms_main_window {

	public JFrame frame;
	private static JTextField emailId;
	public static String emailId_received; 

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sms_main_window window = new sms_main_window();
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
	public sms_main_window() {
		initialize();
	}
	
	public static void get_email_id(String email)
	{
		emailId_received = email;
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setBounds(10, 11, 23, 14);
		frame.getContentPane().add(lblTo);
		
		emailId = new JTextField();
		emailId.setBounds(45, 8, 176, 20);
		frame.getContentPane().add(emailId);
		emailId.setColumns(10);
		
		emailId.setText(emailId_received);
		emailId.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 414, 182);
		frame.getContentPane().add(scrollPane);
		
		JTextArea msg_content = new JTextArea();
		scrollPane.setViewportView(msg_content);
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String new_file_loc = sms_methods.create_folder_sent(emailId_received);
				String text_file_loc = sms_methods.create_textFIle_loc_foler( new_file_loc);
									
				int lines = msg_content.getLineCount();
				try
		        {
		            for(int i = 0; i < lines; i ++)
		            {
		                int start = msg_content.getLineStartOffset(i);
		                int end = msg_content.getLineEndOffset(i);
		                
		                sms_methods.creat_text_file(msg_content.getText(start, end-start),  text_file_loc);
		            }
		        }
		        catch(BadLocationException e){
		            System.out.println("not able to write");
		        }
				
				String text = sms_send_rec_management.readFileAsString(text_file_loc);
				sms_send_rec_management.obtain_responsible_node_id( emailId_received , text);
				
				frame.dispose();
			}
		});
		btnSend.setBounds(176, 227, 73, 23);
		frame.getContentPane().add(btnSend);
		
		
	}
}
