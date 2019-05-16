package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;

import com.ehelpy.brihaspati4.sms.Send_SMS_Window;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.awt.Component;


public class Display_Window_After_Login extends JFrame{

	/**
	 * 
	 */
	private JPanel contentPane;
	static String Email_Id;
	public static JTable table;
	Connection con = null; //new
	private Popup popup;
//	public static ServerSocketChannel ss = null;
    
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Display_Window_After_Login frame = new Display_Window_After_Login();
					frame.setVisible(true);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create frame.
	 */
	
	public Display_Window_After_Login(){
		
		upload_csv.csv("information2.csv");
		B4services.display_window_open = true;
		
	       
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
				B4services.display_window_open = false;
				dispose();
			}
		});
		setForeground(Color.RED);
		setSize(new Dimension(5, 0));
		setTitle("ADDRESS BOOK");
		setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 656, 439);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.setBounds(191, 56, 205, 251);
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
		// creating new connection with db and uploading it to JTable
		
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
		
		////////////////////////////////////////////////////////////
		
		JButton CALL = new JButton("");
		CALL.addMouseListener(new MouseAdapter() {
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
		CALL.setBorderPainted(false);
		CALL.setContentAreaFilled(false);
		CALL.setHorizontalTextPosition(SwingConstants.LEADING);
		CALL.setHorizontalAlignment(SwingConstants.LEADING);
		CALL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int i = table.getSelectedRow();
				if(i==-1)
				{
					JOptionPane.showMessageDialog(null, "Please SELECT any ENTRY and then CLICK on this.");
				}
				
				else
				{ 
					B4services.display_window_open = false;
					
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
		Image img = new ImageIcon(this.getClass().getResource("/call.png")).getImage();
		CALL.setIcon(new ImageIcon(img));
		CALL.setBounds(406, 82, 43, 23);
		contentPane.add(CALL);
		
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
					
					Send_SMS_Window.get_email_id(Email_Id, "AddressBook");
					
					Send_SMS_Window obj = new Send_SMS_Window();
					obj.setVisible(true);
				}	
			}
		});
		Image img1 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		MESSAGE.setIcon(new ImageIcon(img1));
		MESSAGE.setBounds(406, 143, 43, 23);
		contentPane.add(MESSAGE);
		
		JButton DELETE = new JButton("");
		DELETE.addMouseListener(new MouseAdapter() {
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
		DELETE.setContentAreaFilled(false);
		DELETE.setBorderPainted(false);
		DELETE.setHorizontalAlignment(SwingConstants.LEADING);
		DELETE.addActionListener(new ActionListener() {
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
		Image img2 = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		DELETE.setIcon(new ImageIcon(img2));
		DELETE.setBounds(406, 200, 43, 23);
		contentPane.add(DELETE);
		
		JButton Show_Details = new JButton("");
		Show_Details.setHorizontalTextPosition(SwingConstants.LEADING);
		Show_Details.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("DETAILS AND UPDATE");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		Show_Details.setContentAreaFilled(false);
		Show_Details.setBorderPainted(false);
		Show_Details.setHorizontalAlignment(SwingConstants.LEADING);
		Show_Details.addActionListener(new ActionListener() {
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
					
					Show_Details_Window show_details_window = new Show_Details_Window();
					show_details_window.setVisible(true);
					
					dispose();
				
									
					Show_Details_Fetch obj=new Show_Details_Fetch();
					obj.Upload_Details_In_Window(Email_Id);
				}
			}
		});
		Image img3 = new ImageIcon(this.getClass().getResource("/details.png")).getImage();
		Show_Details.setIcon(new ImageIcon(img3));
		Show_Details.setBounds(406, 256, 43, 23);
		contentPane.add(Show_Details);
		
		JButton New_Entry = new JButton("");
		Image img4 = new ImageIcon(this.getClass().getResource("/new_entry.png")).getImage();
		New_Entry.setIcon(new ImageIcon(img4));
		New_Entry.setContentAreaFilled(false);
		New_Entry.setBorderPainted(false);
		New_Entry.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("MAKE A NEW ENTRY");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		New_Entry.setBounds(76, 316, 81, 60);
		New_Entry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				dispose();
				
				New_Entry_Window new_entry = new New_Entry_Window();
				new_entry.setVisible(true);
			}
		});
		contentPane.add(New_Entry);
		
		JButton Search = new JButton("");
		Search.setAlignmentY(Component.TOP_ALIGNMENT);
		Image img5 = new ImageIcon(this.getClass().getResource("/search.png")).getImage();
		Search.setIcon(new ImageIcon(img5));
		Search.setContentAreaFilled(false);
		Search.setBorderPainted(false);
		Search.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("SEARCH");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		Search.setBounds(431, 316, 55, 49);
		Search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				dispose();
				
				Search_Window search = new Search_Window();
				search.setVisible(true);
			}
		});
		contentPane.add(Search);
		
		JLabel lblNewLabel = new JLabel("PLEASE CLICK ON ANY ENTRY TO SELECT ");
		lblNewLabel.setLabelFor(table);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBackground(new Color(255, 215, 0));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		lblNewLabel.setBounds(155, 31, 296, 14);
		contentPane.add(lblNewLabel);
		
		JButton CANCEL = new JButton("");
		CANCEL.addMouseListener(new MouseAdapter() {
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
		CANCEL.setContentAreaFilled(false);
		CANCEL.setBorderPainted(false);
		CANCEL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				import_database.main(null);
				try 
				{
					B4services.ss.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				B4services.service();
				B4services.display_window_open = false;
				dispose();
				
			}
		});
		Image img7 = new ImageIcon(this.getClass().getResource("/cancel.png")).getImage();
		CANCEL.setIcon(new ImageIcon(img7));
		CANCEL.setBounds(273, 366, 33, 23);
		contentPane.add(CANCEL);
		
	}
	
}
