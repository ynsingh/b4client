package com.ehelpy.brihaspati4.sms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;

import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.Address_Book.sqlite_connection;
import com.ehelpy.brihaspati4.Address_Book.upload_csv;
import com.ehelpy.brihaspati4.voip.B4services;

import net.proteanit.sql.DbUtils;

public class SMS_Window extends JFrame {

	private JPanel contentPane;
	static String Email_Id;
	Connection con = null;
	private JTable table;
	private Popup popup;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SMS_Window frame = new SMS_Window();
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
	public SMS_Window() {
		
		upload_csv.csv("information2.csv");
		B4services.sms_window = true;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
				import_database.main(null);
				try 
				{
					B4services.ss.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				B4services.service();
				B4services.sms_window = false;
				dispose();
			}
		});
		
		setForeground(Color.RED);
		setSize(new Dimension(5, 0));
		setTitle("SMS MANAGER");
		setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 560, 395);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(103, 72, 222, 228);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSize(new Dimension(2, 2));
		table.setRowHeight(22);
		table.setRowMargin(2);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setGridColor(Color.BLACK);
		table.setForeground(new Color(139, 0, 0));
		table.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(255, 255, 255)));
		table.setBackground(new Color(255, 228, 181));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				table.getSelectedRow();
			
			}
		});
		scrollPane.setViewportView(table);
		
		/////////////////////////////////////////////////////////////
		// 	creating new connection with db and uploading it to JTable

		con = sqlite_connection.db_connector();//new
		String query = "SELECT Name,Email_Id  FROM information2 ORDER BY Name";
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

		//	//////////////////////////////////////////////////////////
		
		JButton MESSAGE = new JButton("");
		MESSAGE.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("SEND MESSAGE");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		MESSAGE.setContentAreaFilled(false);
		MESSAGE.setBorderPainted(false);
		MESSAGE.setHorizontalAlignment(SwingConstants.LEADING);
		MESSAGE.addActionListener(new ActionListener() {
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
					
					Send_SMS_Window.get_email_id(Email_Id, "SMSWindow");
					
					Send_SMS_Window obj = new Send_SMS_Window();
					obj.setVisible(true);
				}	
			}
		});
		Image img1 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		MESSAGE.setIcon(new ImageIcon(img1));
		MESSAGE.setBounds(359, 155, 67, 23);
		contentPane.add(MESSAGE);
		
		JButton btnNewButton = new JButton("");
		Image img5 = new ImageIcon(this.getClass().getResource("/message.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img5));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("INBOX (Check Messages)");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SMS_Reader_Window obj = new SMS_Reader_Window();
				obj.setVisible(true);
				
				dispose();
			}
		});
		btnNewButton.setBounds(369, 100, 43, 23);
		contentPane.add(btnNewButton);
		
		JButton sentButton = new JButton("");
		Image img6 = new ImageIcon(this.getClass().getResource("/message.png")).getImage();
		sentButton.setIcon(new ImageIcon(img6));
		sentButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("SENT MESSAGES)");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		sentButton.setContentAreaFilled(false);
		sentButton.setBorderPainted(false);
		sentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				SMS_Sent_Messages_Window  obj = new SMS_Sent_Messages_Window ();
				obj.setVisible(true);
				
				dispose();
			}
		});
		sentButton.setBounds(369, 217, 43, 23);
		contentPane.add(sentButton);
		
		JLabel SMS = new JLabel("SMS MANAGER");
		SMS.setForeground(Color.RED);
		SMS.setFont(new Font("Times New Roman", Font.BOLD, 16));
		SMS.setBounds(233, 11, 142, 32);
		contentPane.add(SMS);
	
	}
}
