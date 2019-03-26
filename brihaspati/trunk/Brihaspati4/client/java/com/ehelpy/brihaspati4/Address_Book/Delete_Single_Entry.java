package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ehelpy.brihaspati4.voip.B4services;

import net.proteanit.sql.DbUtils;

import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JLabel;
import java.awt.Font;



public class Delete_Single_Entry extends JFrame {

	static String Email_Id;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Delete_Single_Entry frame = new Delete_Single_Entry();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void Receive(String get)
	{
		Email_Id = get;
	}
	/**
	 * Create the frame.
	 */
	public Delete_Single_Entry() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 378, 102);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		B4services.address_book_delete_window = true;
		
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
	
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);		
				
				B4services.address_book_delete_window = false;
				
				dispose();
			}
		});
		
		JButton btNDelete = new JButton("YES");
		btNDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Connection con = null;//new
				
				con = sqlite_connection.db_connector();//new
				
				Statement stmt = null;
				try {
					stmt = con.createStatement();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				String query = "delete from information2 where Email_Id =('"+Email_Id+"')";
				try {
					stmt.executeUpdate(query);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Record Deleted Sucessfully. PRESS OK");
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = sqlite_connection.db_connector();//new
				String query1 = "SELECT Name,Email_Id  FROM information2 order by Name";
				try 
				{
					PreparedStatement stmt1 = con.prepareStatement(query1);
					ResultSet rs = stmt1.executeQuery();
					
					Display_Window_After_Login.table.setModel(DbUtils.resultSetToTableModel(rs));
					stmt1.close();
					con.close();
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
				
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				dispose();
			}
		});
		btNDelete.setBounds(82, 40, 63, 23);
		contentPane.add(btNDelete);
		
		JButton btnNo = new JButton("NO");
		btnNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				
				B4services.address_book_delete_window = false;
				
				dispose();
				
			}
		});
		btnNo.setBounds(201, 40, 63, 23);
		contentPane.add(btnNo);
		
		JLabel lblNewLabel = new JLabel("ARE YOU SURE YOU WANT TO DELETE?");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel.setBounds(40, 11, 281, 14);
		contentPane.add(lblNewLabel);
	}
}
