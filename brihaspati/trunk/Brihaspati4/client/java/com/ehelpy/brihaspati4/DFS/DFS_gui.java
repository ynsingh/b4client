package com.ehelpy.brihaspati4.DFS;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.Address_Book.Display_Window_After_Login;
import com.ehelpy.brihaspati4.Address_Book.ProgressBar;
import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.Address_Book.sqlite_connection;
import com.ehelpy.brihaspati4.Address_Book.upload_csv;
import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.sms.SMS_Window;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_gui;
import com.ehelpy.brihaspati4.voip.voip_rxcall;
import com.sun.java.swing.plaf.windows.resources.windows;

//import javafx.fxml.Initializable;
import net.proteanit.sql.DbUtils;
public class DFS_gui extends JFrame{
	public JFrame DFSservice;
	public static boolean DFSservice_window_open = false;
	public static ServerSocketChannel ss = null;
	private static int token ;

	private JPanel contentPane;
	
	
	Connection con = null; //new
	
//	public static ServerSocketChannel ss = null;
    
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DFS_gui window = new DFS_gui();
					window.DFSservice.setVisible(true);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create frame.
	 */
	

	public DFS_gui(){
		DFSservice_window_open= true;
		
		//B4services.DFS_gui_window = true;
	       
		   
		   DFSservice = new JFrame();
		   DFSservice.setTitle("DFS Services");
		   DFSservice.setBounds(100, 100, 450, 450);
		   DFSservice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   DFSservice.getContentPane().setLayout(null);
	///////////////////////////////////////////////////////////////////////////	   
		   JRadioButton NewRadioButton = new JRadioButton("STORE");
			NewRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					token = 1;
					
					}
				
			});
			NewRadioButton.setBackground(Color.YELLOW);
			NewRadioButton.setForeground(Color.BLUE);
			NewRadioButton.setFont(new Font("Tahoma", Font.BOLD, 20));
			NewRadioButton.setBounds(8, 45, 200, 25);
			DFSservice.getContentPane().add(NewRadioButton);
	//////////////////////////////////////////////////////////////////////////////////		
			JRadioButton NewRadioButton1 = new JRadioButton("FETCH/DELETE");
			NewRadioButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					token = 2;
					
					}
				
			});
			NewRadioButton1.setBackground(Color.YELLOW);
			NewRadioButton1.setForeground(Color.BLUE);
			NewRadioButton1.setFont(new Font("Tahoma", Font.BOLD, 20));
			NewRadioButton1.setBounds(8, 90, 200, 25);
			DFSservice.getContentPane().add(NewRadioButton1);
			
			ButtonGroup bG = new ButtonGroup();
		    bG.add(NewRadioButton);
		    bG.add(NewRadioButton1);
		    
			
			 JButton btnOk = new JButton("OK");
			    btnOk.addActionListener(new ActionListener() 
			      {
			    	public void actionPerformed(ActionEvent e) 
			    	 {
			    		e.getActionCommand();
			    		System.out.println("token  "+token );
			    		
			    		if (token == 1 ) 
		    		    {
			    			DFSservice_window_open = false;
			    			DFSservice.setVisible(false);
			    			DFSservice.dispose();
			    			System.out.println("1 is working");
			    			try {
								DistFileSysUtilityMethods.fileUploading_DHT();
							} catch (ParserConfigurationException | TransformerException | IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							//B4services.service();
		    		    }		
					
						
			    		if (token == 0 ) 
			    		{
			    			DFSservice_window_open = false;
			    			DFSservice.setVisible(false);
			    			DFSservice.dispose();
							System.out.println("2 is working");
							try {
								Fetch_file.createAndShowGUI();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							}						
			    					    		
					}
			    			
			      });
			    btnOk.setFont(new Font("Calibri", Font.BOLD, 18));
			    btnOk.setBounds(105, 304, 74, 25);
			    DFSservice.getContentPane().add(btnOk);
			    
			    JButton btnCancel = new JButton("CANCEL");
			    btnCancel.addActionListener(new ActionListener() {
			    	public void actionPerformed(ActionEvent e) {
			    	System.exit(0);
			    	}
			    });
			    btnCancel.setFont(new Font("Calibri", Font.BOLD, 18));
			    btnCancel.setBounds(274, 304, 113, 25);
			    DFSservice.getContentPane().add(btnCancel);
			    
			    
			    JLabel lblNewLabel = new JLabel("WELCOME ");
			    lblNewLabel.setForeground(new Color(128, 0, 0));
			    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
			    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			    lblNewLabel.setBounds(23, 13, 385, 25);
			    DFSservice.getContentPane().add(lblNewLabel);
			    
			    DFSservice.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						//DFSservice.dispose();
						try {
							System.out.println("why notttttt");
							B4services.ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						System.out.println("why notttttt11111111111111");		
						B4services.service();
					}
				});
			    
	}
	
}
