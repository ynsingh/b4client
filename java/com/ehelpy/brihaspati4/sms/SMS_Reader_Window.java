package com.ehelpy.brihaspati4.sms;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.voip.B4services;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;

public class SMS_Reader_Window extends JFrame {

	private JPanel contentPane;
	
	public static String file_name = null;
			
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SMS_Reader_Window frame = new SMS_Reader_Window();
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
	public SMS_Reader_Window() 
	{
		B4services.sms_reader_window = true;
				
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				import_database.main(null);
				SMS_Window obj = new SMS_Window();
				obj.setVisible(true);
				
				B4services.sms_reader_window = false;
				
				dispose();
			}
		});
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 590, 382);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextArea msg_display = new JTextArea();
		msg_display.setBounds(20, 45, 533, 248);
		contentPane.add(msg_display);
		
		String filepath =null;
		String own_msg_folder = null;
		String self_email_id = emailid.getemaild();
						
		BufferedReader rec_folder = null;
		try {
			rec_folder = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			filepath = rec_folder.readLine();
											
			String fileSeparator = System.getProperty("file.separator");
			
			own_msg_folder = filepath + fileSeparator + self_email_id;
								
			Path loc_path1 = Paths.get(own_msg_folder);
			
			boolean check_dir1;
			check_dir1 = Files.exists(loc_path1);
			
			if (!check_dir1)
			{
				try {
					Files.createDirectory(loc_path1);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
				////////////////////////////////////////////////////////////////
		
		File root = new File(own_msg_folder);
		FileSystemView fsv = new SingleRootFileSystemView( root );
		
		JFileChooser file_to_be_read = new JFileChooser(fsv);

		disableButton(file_to_be_read, "FileChooser.newFolderIcon");
					
		int response = file_to_be_read.showOpenDialog(null);
		
		if (response == JFileChooser.APPROVE_OPTION)
	    {
			File f = file_to_be_read.getSelectedFile();
			file_name = f.getAbsolutePath();
				
			try
			{
				FileReader reader = new FileReader(file_name);
				BufferedReader br = new BufferedReader(reader);
			
				msg_display.read(br, null);
				br.close();
				msg_display.requestFocus();
			}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog(null, e2);
			}
		/////////////////////////////////////////////////	code for renaming the file opened from new message to read msg
			String name_of_file = f.getName();
			
			int start = 0; 
			int end = 13; 
			char[] buf = new char[end - start]; 
			name_of_file.getChars(start, end, buf, 0); 
			
			String new_file_name = new String(buf)+".txt";
			
			Path source = Paths.get(file_name);
			try {
				Files.move(source, source.resolveSibling(new_file_name));
			} catch (IOException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}
		//////////////////////////////////////////////////////////////////////////////////////////////
	    }
		else if(response == JFileChooser.CANCEL_OPTION)
	    {
	      System.out.println("No Selection ");
	    }
				
	/////////////////////////////////////////////////////////////////////////////////
		
		JButton open_file = new JButton("SELECT FILE TO BE READ");
		open_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String filepath =null;
				String own_msg_folder = null;
				String self_email_id = emailid.getemaild();
								
				BufferedReader rec_folder = null;
				try {
					rec_folder = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					filepath = rec_folder.readLine();
													
					String fileSeparator = System.getProperty("file.separator");
					
					own_msg_folder = filepath + fileSeparator + self_email_id;
										
					Path loc_path1 = Paths.get(own_msg_folder);
					
					boolean check_dir1;
					check_dir1 = Files.exists(loc_path1);
					
					if (!check_dir1)
					{
						try {
							Files.createDirectory(loc_path1);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
						////////////////////////////////////////////////////////////////
				
				File root = new File(own_msg_folder);
				FileSystemView fsv = new SingleRootFileSystemView( root );
				
				JFileChooser file_to_be_read = new JFileChooser(fsv);
		
				disableButton(file_to_be_read, "FileChooser.newFolderIcon");
							
				int response = file_to_be_read.showOpenDialog(null);
				
				if (response == JFileChooser.APPROVE_OPTION)
			    {
					File f = file_to_be_read.getSelectedFile();
					file_name = f.getAbsolutePath();
				
					try
					{
						FileReader reader = new FileReader(file_name);
						BufferedReader br = new BufferedReader(reader); 
							
						msg_display.read(br, null);
						br.close();
						msg_display.requestFocus();
					}
					catch(Exception e2)
					{
						JOptionPane.showMessageDialog(null, e2);
					}
				/////////////////////////////////////////////////	code for renaming the file opened from new message to read msg
					String name_of_file = f.getName();
					
					int start = 0; 
					int end = 13; 
					char[] buf = new char[end - start]; 
					name_of_file.getChars(start, end, buf, 0); 
					
					String new_file_name = new String(buf)+".txt";
					
					Path source = Paths.get(file_name);
					try {
						Files.move(source, source.resolveSibling(new_file_name));
					} catch (IOException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}
					
				//////////////////////////////////////////////////////////////////////////////////////////////
			    }
				else if(response == JFileChooser.CANCEL_OPTION)
			    {
			      System.out.println("No Selection ");
			    }
						
			/////////////////////////////////////////////////////////////////////////////////
			}
		});
		open_file.setFont(new Font("Times New Roman", Font.BOLD, 12));
		open_file.setBounds(10, 11, 205, 23);
		contentPane.add(open_file);
		
		JButton btnNewButton = new JButton("DELETE");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				String name_of_file = file_name;
				
				System.out.println("name_of_file: "+name_of_file);
				System.out.println("file_name: "+file_name);
				
				name_of_file = name_of_file.replaceAll("\\(","");
				name_of_file = name_of_file.replaceAll("new_message","");
				file_name = name_of_file.replaceAll("\\)","");
				
				System.out.println("file_name: "+file_name);
								
				System.out.println("delete this file");
				Path local_path = Paths.get(file_name);
				
				boolean check_dir;
				check_dir = Files.exists(local_path);
				
				if (check_dir)
				{
					System.out.println("delete this file1");
					try {
						System.out.println("delete this file2");
						Files.delete(local_path);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					JFrame frame1 = new JFrame("Message");
					JOptionPane.showMessageDialog(frame1, " FILE DELETED ");
				}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 12));
		btnNewButton.setBounds(20, 304, 89, 23);
		contentPane.add(btnNewButton);
	}
	
	public void disableButton( Container c, final String iconString ) 
	{
	     int len = c.getComponentCount();
	     
	     for (int i = 0; i < len; i++) 
	     {
	    	 Component comp = c.getComponent(i);
	    	 if (comp instanceof JButton) 
	    	 {
	    		 JButton b = (JButton)comp;
	    		 Icon icon = b.getIcon();
	    		 if (icon != null && icon == UIManager.getIcon(iconString))
	    		 {
	    			 b.setEnabled(false);
	    		 }
	    		
	    	 }
	    	 else if (comp instanceof Container) 
	    	 {
	    		 disableButton((Container)comp, iconString);
	    	 }
	     }
	 }
}
