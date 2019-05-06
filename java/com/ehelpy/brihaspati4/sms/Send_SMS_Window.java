package com.ehelpy.brihaspati4.sms;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;

import com.ehelpy.brihaspati4.Address_Book.Display_Window_After_Login;
import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.voip.B4services;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.awt.event.ActionEvent;

public class Send_SMS_Window extends JFrame {

	public JPanel contentPane;
	public static String emailId_received;
	public static String Calling_Window;
	private JTextField emailId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Send_SMS_Window frame = new Send_SMS_Window();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void get_email_id(String email, String Call_From)
	{
		Calling_Window = Call_From;
		emailId_received = email;
	}

	/**
	 * Create the frame.
	 */
	public Send_SMS_Window() {
		
		B4services.sms_send_window = true;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				if(Calling_Window.equals("AddressBook"))
				{	
					Display_Window_After_Login obj=new Display_Window_After_Login();
					obj.setVisible(true);
					
					B4services.sms_send_window = false;
					
					dispose();
					
				}
				
				else if(Calling_Window.equals("SMSWindow"))
				{
					SMS_Window obj = new SMS_Window();
					obj.setVisible(true);
					
					B4services.sms_send_window = false;
					
					dispose();
				}
			}
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 574, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		emailId = new JTextField();
		emailId.setBounds(34, 11, 181, 20);
		contentPane.add(emailId);
		emailId.setColumns(10);
		
		JLabel lblTo = new JLabel("TO");
		lblTo.setFont(new Font("Times New Roman", Font.BOLD, 12));
		lblTo.setBounds(10, 14, 34, 17);
		contentPane.add(lblTo);
		
		emailId.setText(emailId_received);
		emailId.setEditable(false);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 38, 538, 254);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String new_file_loc = sms_methods.create_folder_sent(emailId_received);
				String file_loc = sms_methods.create_textFIle_loc_foler( new_file_loc);
									
				int lines = textArea.getLineCount();
				try
		        {
		            for(int i = 0; i < lines; i ++)
		            {
		                int start = textArea.getLineStartOffset(i);
		                int end = textArea.getLineEndOffset(i);
		                
		                sms_methods.creat_text_file(textArea.getText(start, end-start),  file_loc);
		            }
		            
		            String blank_line1 = "";
		    	    sms_methods.creat_text_file(blank_line1,  file_loc);
		    	    String blank_line2 = "";
		    	    sms_methods.creat_text_file(blank_line2,  file_loc);
		    	    
		    	    DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
		    		
		    		Calendar calobj = Calendar.getInstance();
		    	    String date_time = "sent on : "+df.format(calobj.getTime());
		    	    
		    	    JFrame frame1 = new JFrame("Message");
					JOptionPane.showMessageDialog(frame1, "Your Message will be ENCRYPTED and CONFIRMATION will be given After sending the MESSAGE."
							+ " Please Press O.K.  ");
		    	    
		    	    sms_methods.creat_text_file(date_time, file_loc);
		    	    
		        }
		        catch(BadLocationException e){
		            System.out.println("not able to write");
		        }
				
				String text = sms_send_rec_management.readFileAsString(file_loc);
				
				DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
	    		
	    		Calendar calobj = Calendar.getInstance();
	    	    String date_time = df.format(calobj.getTime());
				
	    	    String file_name = date_time+"(new_message).txt";
				sms_send_rec_management.obtain_responsible_node_id( emailId_received , text, file_name);
				
				B4services.sms_send_window = false;
				
				import_database.main(null);
				
				if(Calling_Window.equals("AddressBook"))
				{	
					Display_Window_After_Login obj=new Display_Window_After_Login();
					obj.setVisible(true);
					dispose();
				}
				
				else if(Calling_Window.equals("SMSWindow"))
				{
					SMS_Window obj = new SMS_Window();
					obj.setVisible(true);
					
					dispose();
				}
			}
		});
		btnSend.setFont(new Font("Times New Roman", Font.BOLD, 13));
		btnSend.setBounds(231, 301, 89, 23);
		contentPane.add(btnSend);
	}
}
