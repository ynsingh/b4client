package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;

import javax.swing.JPasswordField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class Password_Change_window extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	public static String username;
	private Popup popup;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Password_Change_window frame = new Password_Change_window();
					frame.setVisible(true);
					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Password_Change_window() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 619, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel current_pass = new JLabel("CURRENT PASSWORD");
		current_pass.setFont(new Font("Times New Roman", Font.BOLD, 16));
		current_pass.setBounds(110, 101, 198, 14);
		contentPane.add(current_pass);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(339, 100, 168, 20);
		contentPane.add(passwordField);
		
		JLabel new_pass = new JLabel("NEW PASSWORD");
		new_pass.setFont(new Font("Times New Roman", Font.BOLD, 16));
		new_pass.setBounds(110, 172, 198, 20);
		contentPane.add(new_pass);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(339, 172, 168, 22);
		contentPane.add(passwordField_1);
		
		JLabel lblConfirmPassword = new JLabel("CONFIRM PASSWORD");
		lblConfirmPassword.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblConfirmPassword.setBounds(110, 241, 198, 20);
		contentPane.add(lblConfirmPassword);
		
		passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(339, 241, 168, 22);
		contentPane.add(passwordField_2);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		Image img = new ImageIcon(this.getClass().getResource("/submit.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img));
		btnNewButton.addMouseListener(new MouseAdapter() {
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
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				Connection con = null;//new
				
				con = sqlite_connection.db_connector();//new
				
				String query = "select name,password from login";
				
				PreparedStatement stmt = null;
				try {
					stmt = con.prepareStatement(query);
				} catch (SQLException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				ResultSet rs = null;
				try {
					rs = stmt.executeQuery();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				try {
					username = rs.getString(1);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			
				
				if(passwordField.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null, "CURRENT PASSWORD can not be left Blank. PRESS OK");
					try {
						stmt.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						con.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else
					try {
						if(!passwordField.getText().equals(rs.getString(2)) )
								{
									System.out.println(""+passwordField.getText());
									JOptionPane.showMessageDialog(null, "CURRENT PASSWORD entered is WRONG. PRESS OK");
									try {
										stmt.close();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									try {
										con.close();
									} catch (SQLException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
						
						else if(passwordField_1.getText().isEmpty())
						{
							JOptionPane.showMessageDialog(null, "Please Enter NEW PASSWORD. PRESS OK");
							stmt.close();
							con.close();
						}	
							
						else if(!passwordField_1.getText().equals(passwordField_2.getText()))
						{
							JOptionPane.showMessageDialog(null, "NEW and CONFIRM PASSWORDS do not MATCH. PRESS OK");
							try {
								stmt.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								con.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
						else
						{
							stmt.close();
							con.close();
							Password_Change_Upload change_pass = new Password_Change_Upload();
							change_pass.new_password(username, passwordField_1.getText());
							dispose();
						}
					} catch (HeadlessException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnNewButton.setBounds(194, 306, 64, 63);
		contentPane.add(btnNewButton);
		
		JButton btnCancel = new JButton("");
		btnCancel.setHorizontalTextPosition(SwingConstants.LEADING);
		btnCancel.setHorizontalAlignment(SwingConstants.LEADING);
		btnCancel.setContentAreaFilled(false);
		btnCancel.setBorderPainted(false);
		Image img1 = new ImageIcon(this.getClass().getResource("/cancel1.png")).getImage();
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
		btnCancel.setIcon(new ImageIcon(img1));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		btnCancel.setBounds(395, 306, 64, 51);
		contentPane.add(btnCancel);
	}

}
