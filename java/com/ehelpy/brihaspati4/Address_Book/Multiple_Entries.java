package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableModel;

import com.ehelpy.brihaspati4.sms.Send_SMS_Window;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import net.proteanit.sql.DbUtils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.awt.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


public class Multiple_Entries extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private Popup popup;
	static String argument;
	static String flag;
	static String Email_Id;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Multiple_Entries frame = new Multiple_Entries();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void Receive(String get, String col_name)
	{
		argument = get;
		flag = col_name;
	}
	
	
	Connection con = null; //new
	Connection con1 = null; //new
	

	/**
	 * Create the frame.
	 */
	public Multiple_Entries() {
			
			
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 648, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(29, 55, 579, 216);
		contentPane.add(scrollPane);
		
		B4services.address_book_multiple_entries_window = true;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
	
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);	
				
				B4services.address_book_multiple_entries_window = false;
				dispose();
			}
		});
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		JLabel lblSearchFieldHas = new JLabel("SEARCHED FIELD HAS MULTIPLE ENTRIES. ");
		lblSearchFieldHas.setBounds(30, 8, 417, 28);
		contentPane.add(lblSearchFieldHas);
		
		JButton btnNewButton = new JButton("");
		Image img2 = new ImageIcon(this.getClass().getResource("/call.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img2));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("CALL");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int i = table.getSelectedRow();
				if(i==-1)
				{
					JOptionPane.showMessageDialog(null, "Please SELECT any ENTRY and then CLICK on this.");
				}
				
				else
				{
					voip_rxcall.flag = false;
					TableModel model = table.getModel();
					Email_Id = model.getValueAt(i, 1).toString();
					import_database.main(null);
					dispose();
					
					ProgressBar.get_email(Email_Id); 
					
					ProgressBar obj = new ProgressBar();
					obj.setVisible(true);
				}
			}
		});
		btnNewButton.setHorizontalAlignment(SwingConstants.LEADING);
		btnNewButton.setHorizontalTextPosition(SwingConstants.LEADING);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setRolloverEnabled(false);
		btnNewButton.setBounds(72, 282, 45, 23);
		contentPane.add(btnNewButton);
		
		JButton btnMessage= new JButton("");
		Image img3 = new ImageIcon(this.getClass().getResource("/message.png")).getImage();
		btnMessage.setIcon(new ImageIcon(img3));
		btnMessage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("MESSAGE");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		btnMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i==-1)
				{
					JOptionPane.showMessageDialog(null, "Please SELECT any ENTRY and then CLICK on this.");
				}
				
				else
				{
					Send_SMS_Window.get_email_id(Email_Id, "AddressBook");
					
					Send_SMS_Window obj = new Send_SMS_Window();
					obj.setVisible(true);
				}
			}
		});
		btnMessage.setContentAreaFilled(false);
		btnMessage.setBorderPainted(false);
		btnMessage.setBounds(237, 282, 38, 23);
		contentPane.add(btnMessage);
		
		JButton btnNewButton_1 = new JButton("");
		Image img4 = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		btnNewButton_1.setIcon(new ImageIcon(img4));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("DELETE");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if(i==-1)
				{
					JOptionPane.showMessageDialog(null, "Please SELECT any ENTRY and then CLICK on this.");
				}
				else
				{	
					TableModel model = table.getModel();
				
					Email_Id = model.getValueAt(i, 1).toString();
					
					dispose();
													
					Delete_Single_Entry.Receive(Email_Id);
				
					Delete_Single_Entry obj = new Delete_Single_Entry();
					obj.setVisible(true);
				}
			}
		});
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setBounds(386, 282, 38, 23);
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		Image img7 = new ImageIcon(this.getClass().getResource("/cancel.png")).getImage();
		btnNewButton_2.setIcon(new ImageIcon(img7));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
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
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				import_database.main(null);
				dispose();
				
				B4services.address_book_multiple_entries_window = false;
				
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				
			}
		});
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBorderPainted(false);
		btnNewButton_2.setBounds(519, 282, 89, 23);
		contentPane.add(btnNewButton_2);
		
	
		con = sqlite_connection.db_connector();//new
		
		if(flag.equals("Gender"))
		{
			if(argument.equals("Male")||argument.equals("male"))
			{
				String query = "select * from information2 where Gender ='Male' or  Gender = 'male'";
				try 
				{
					PreparedStatement stmt = con.prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
						
					table.setModel(DbUtils.resultSetToTableModel(rs));
					stmt.close();
					con.close();
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
			else if(argument.equals("Female")||argument.equals("female"))
			{
				String query = "select * from information2 where Gender ='Female' or  Gender = 'female'";
				try 
				{
					PreparedStatement stmt = con.prepareStatement(query);
					ResultSet rs = stmt.executeQuery();
						
					table.setModel(DbUtils.resultSetToTableModel(rs));
					stmt.close();
					con.close();
				} 
				catch (SQLException e1) 
				{
					e1.printStackTrace();
				}
			}
		}
		
		else
		{
			String query = "SELECT *  FROM information2 WHERE "+flag+"= ('"+argument+"')";
			try 
			{
				PreparedStatement stmt = con.prepareStatement(query);
				ResultSet rs = stmt.executeQuery();
					
				table.setModel(DbUtils.resultSetToTableModel(rs));
				stmt.close();
				con.close();
			} 
			catch (SQLException e1) 
			{
				e1.printStackTrace();
			}
		}
	}
}
