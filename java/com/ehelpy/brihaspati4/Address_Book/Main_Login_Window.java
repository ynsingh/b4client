package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import com.ehelpy.brihaspati4.authenticate.properties_access;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_rxcall;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ServerSocketChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class Main_Login_Window {

	public JFrame frmAddressBook;
	private JTextField username_text;
	private JPasswordField passwordField;
	
	private Popup popup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Login_Window window = new Main_Login_Window();
					window.frmAddressBook.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main_Login_Window() {
		initialize();
		 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmAddressBook = new JFrame();
		frmAddressBook.setForeground(Color.RED);
		frmAddressBook.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		frmAddressBook.setTitle("B4 SERVICES");
		frmAddressBook.setBounds(100, 100, 566, 433);
		frmAddressBook.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAddressBook.getContentPane().setLayout(null);
		
		JLabel lblNewJgoodiesLabel = DefaultComponentFactory.getInstance().createLabel("PLEASE LOGIN");
		lblNewJgoodiesLabel.setForeground(new Color(204, 0, 102));
		lblNewJgoodiesLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 22));
		lblNewJgoodiesLabel.setBounds(180, 11, 257, 47);
		frmAddressBook.getContentPane().add(lblNewJgoodiesLabel);
		
		
		JLabel username = new JLabel("USERNAME");
		username.setFont(new Font("Times New Roman", Font.BOLD, 18));
		username.setBounds(99, 102, 105, 14);
		frmAddressBook.getContentPane().add(username);
		
		JLabel password = new JLabel("PASSWORD");
		password.setFont(new Font("Times New Roman", Font.BOLD, 18));
		password.setBounds(99, 184, 105, 14);
		frmAddressBook.getContentPane().add(password);
		
		username_text = new JTextField();
		username_text.setBounds(287, 98, 165, 23);
		frmAddressBook.getContentPane().add(username_text);
		username_text.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(287, 175, 165, 23);
		frmAddressBook.getContentPane().add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {
		        @Override
		        public void keyPressed(KeyEvent e) {
		            if(e.getKeyCode() == KeyEvent.VK_ENTER){
		            	try 
						{
							Connection con = null;//new
							
							con = sqlite_connection.db_connector();//new
							
							Statement stmt = con.createStatement();
							
							String query=  "select * from login where name= ('"+username_text.getText()+"') and password= ('"+passwordField.getText()+"')";
							
							ResultSet rs = stmt.executeQuery(query);
							if(rs.next()){
								
								stmt.close();
								con.close();
								
						//		upload_csv.csv("information2.csv");
												
								Display_Window_After_Login obj=new Display_Window_After_Login();
								obj.setVisible(true);	
								
								frmAddressBook.dispose();
							}
							
							else 
								JOptionPane.showMessageDialog(null, "Wrong Password or Username");
						} 
						catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
		            }
		        }

		    });
		
		JButton submit_button = new JButton("");
		Image img = new ImageIcon(this.getClass().getResource("/submit.png")).getImage();
		submit_button.setIcon(new ImageIcon(img));
		submit_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("SUBMIT");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		submit_button.setBorderPainted(false);
		submit_button.setContentAreaFilled(false);
		submit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try 
				{
					Connection con = null;//new
					
					con = sqlite_connection.db_connector();//new
					
					Statement stmt = con.createStatement();
					
					String query=  "select * from login where name= ('"+username_text.getText()+"') and password= ('"+passwordField.getText()+"')";
					
					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()){
						
						stmt.close();
						con.close();
						
			//			upload_csv.csv("information2.csv");
										
						B4services.service();	
						
						frmAddressBook.dispose();
					}
					
					else 
						JOptionPane.showMessageDialog(null, "Wrong Password or Username");
				} 
				catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		submit_button.setFont(new Font("Tahoma", Font.BOLD, 16));
		submit_button.setBounds(161, 286, 43, 47);
		frmAddressBook.getContentPane().add(submit_button);
		
		JButton change_pass_button = new JButton("Change Password");
		change_pass_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try 
				{
					Connection con = null;//new
					
					con = sqlite_connection.db_connector();//new
					
					Statement stmt = con.createStatement();
					
					String query= "select * from login where name= ('"+username_text.getText()+"') and password= ('"+passwordField.getText()+"')";
					
					ResultSet rs = stmt.executeQuery(query);
					if(rs.next()){
						
						stmt.close();
						con.close();
						Password_Change_window new_pass = new Password_Change_window();
						new_pass.setVisible(true);	
														
					}
					
					else
						JOptionPane.showMessageDialog(null, "Wrong Password or Username");
				} 
				catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		change_pass_button.setBounds(307, 209, 145, 23);
		frmAddressBook.getContentPane().add(change_pass_button);
		
		JButton btnCancel = new JButton("");
		Image img1 = new ImageIcon(this.getClass().getResource("/cancel1.png")).getImage();
		btnCancel .setIcon(new ImageIcon(img1));
		btnCancel.setContentAreaFilled(false);
		btnCancel.setBorderPainted(false);
		btnCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("CANCEL");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frmAddressBook.dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCancel.setBounds(353, 286, 50, 47);
		frmAddressBook.getContentPane().add(btnCancel);
	}
}
