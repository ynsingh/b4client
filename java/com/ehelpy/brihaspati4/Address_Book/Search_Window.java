package com.ehelpy.brihaspati4.Address_Book;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class Search_Window extends JFrame {

	private JPanel contentPane;
	private Popup popup;
	public static JTextField name_text;
	public static JTextField email_text;
	public static JTextField mobile_no_1_text;
	public static JTextField mobile_no_2_text;
	public static JTextField phone_number_text;
	public static JTextField address_text;
	public static JTextField gender_text;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Search_Window frame = new Search_Window();
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
	public Search_Window() {
		setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 12));
		setForeground(Color.RED);
		setTitle("ADDRESS BOOK");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 612, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFillAnyOne = new JLabel("FILL ANY ONE OF THE GIVEN DETAILS AND SEARCH.");
		lblFillAnyOne.setHorizontalAlignment(SwingConstants.CENTER);
		lblFillAnyOne.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblFillAnyOne.setBounds(0, 0, 536, 51);
		contentPane.add(lblFillAnyOne);
		
		JLabel name_label = new JLabel("NAME");
		name_label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		name_label.setBounds(102, 62, 92, 22);
		contentPane.add(name_label);
		
		JLabel email_id_label = new JLabel("EMAIL ID");
		email_id_label.setFont(new Font("Times New Roman", Font.BOLD, 14));
		email_id_label.setBounds(102, 108, 92, 14);
		contentPane.add(email_id_label);
		
		JLabel lblNewLabel = new JLabel("MOBILE NO 1");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setBounds(102, 156, 92, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("MOBILE NO 2");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel_1.setBounds(102, 198, 92, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("PHONE NUMBER");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel_2.setBounds(102, 242, 114, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("POSTAL ADDRESS");
		lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel_3.setBounds(102, 287, 140, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("GENDER");
		lblNewLabel_4.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel_4.setBounds(102, 333, 92, 14);
		contentPane.add(lblNewLabel_4);
		
		B4services.address_book_search_window = true;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
	
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);		
				
				B4services.address_book_search_window = false;
				
				dispose();
			}
		});
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setBorderPainted(false);
		btnNewButton.setContentAreaFilled(false);
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
			public void actionPerformed(ActionEvent arg0) {
				Search_Info obj=new Search_Info();
				if(!name_text.getText().isEmpty())
				{	
					obj.selection(name_text.getText(),"Name");
					dispose();
				}
				else if(!email_text.getText().isEmpty())
				{
					obj.selection(email_text.getText(),"Email_Id");
					dispose();
				}
				else if(!mobile_no_1_text.getText().isEmpty())
				{
					obj.selection(mobile_no_1_text.getText(),"Mobile_No_1");
					dispose();
				}
				else if(!mobile_no_2_text.getText().isEmpty())
				{
					obj.selection(mobile_no_2_text.getText(),"Mobile_No_2");
					dispose();
				}
				else if(!phone_number_text.getText().isEmpty())
				{
					obj.selection(phone_number_text.getText(),"Phone_Number");
					dispose();
				}
				else if(!address_text.getText().isEmpty())
				{
					obj.selection(address_text.getText(),"Postal_Address");
					dispose();
				}
				else if(!gender_text.getText().isEmpty())
				{
					obj.selection(gender_text.getText(),"Gender");
					dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Please fill any one of the details.");
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnNewButton.setBounds(175, 372, 41, 51);
		contentPane.add(btnNewButton);
		
		name_text = new JTextField();
		name_text.setBounds(274, 64, 155, 20);
		contentPane.add(name_text);
		name_text.setColumns(10);
		
		email_text = new JTextField();
		email_text.setBounds(274, 106, 155, 20);
		contentPane.add(email_text);
		email_text.setColumns(10);
		
		mobile_no_1_text = new JTextField();
		mobile_no_1_text.setBounds(274, 154, 155, 20);
		contentPane.add(mobile_no_1_text);
		mobile_no_1_text.setColumns(10);
		
		mobile_no_2_text = new JTextField();
		mobile_no_2_text.setBounds(274, 196, 155, 20);
		contentPane.add(mobile_no_2_text);
		mobile_no_2_text.setColumns(10);
		
		phone_number_text = new JTextField();
		phone_number_text.setBounds(274, 240, 155, 20);
		contentPane.add(phone_number_text);
		phone_number_text.setColumns(10);
		
		address_text = new JTextField();
		address_text.setBounds(274, 285, 155, 20);
		contentPane.add(address_text);
		address_text.setColumns(10);
		
		gender_text = new JTextField();
		gender_text.setBounds(274, 331, 155, 20);
		contentPane.add(gender_text);
		gender_text.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setHorizontalTextPosition(SwingConstants.LEFT);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.setContentAreaFilled(false);
		Image img2 = new ImageIcon(this.getClass().getResource("/call.png")).getImage();
		btnNewButton_1.setIcon(new ImageIcon(img2));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
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
		btnNewButton_1.setHorizontalAlignment(SwingConstants.LEADING);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(email_text.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Please perform search first.");
				
				else
				{
					voip_rxcall.flag = false;
					String email_id = email_text.getText();
					import_database.main(null);
					dispose();
					
					ProgressBar.get_email(email_id); 
					
					ProgressBar obj = new ProgressBar();
					obj.setVisible(true);
				}
			}
		});
		btnNewButton_1.setBounds(473, 62, 49, 32);
		contentPane.add(btnNewButton_1);
		
		JButton btnMessage = new JButton("");
		btnMessage.setContentAreaFilled(false);
		btnMessage.setBorderPainted(false);
		btnMessage.setHorizontalTextPosition(SwingConstants.LEADING);
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
		btnMessage.setHorizontalAlignment(SwingConstants.LEADING);
		btnMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String email_id = email_text.getText();
					
				Search_Info obj=new 	Search_Info();
				obj.message(email_id);;
			}
		});
		btnMessage.setBounds(478, 137, 44, 23);
		contentPane.add(btnMessage);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setHorizontalTextPosition(SwingConstants.LEADING);
		btnNewButton_2.setContentAreaFilled(false);
		btnNewButton_2.setBorderPainted(false);
		Image img4 = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		btnNewButton_2.setIcon(new ImageIcon(img4));
		btnNewButton_2.addMouseListener(new MouseAdapter() {
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
		btnNewButton_2.setHorizontalAlignment(SwingConstants.LEADING);
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(email_text.getText().isEmpty())
					JOptionPane.showMessageDialog(null, "Please perform search first.");
				
				else
				{
					String email_id = email_text.getText();
					dispose();
					
					Delete_Single_Entry.Receive(email_id);
					
					Delete_Single_Entry obj = new Delete_Single_Entry();
					obj.setVisible(true);
				}
			}
		});
		btnNewButton_2.setBounds(478, 195, 41, 23);
		contentPane.add(btnNewButton_2);
		
		JButton btnCancel = new JButton("");
		btnCancel.setBorderPainted(false);
		btnCancel.setContentAreaFilled(false);
		Image img1 = new ImageIcon(this.getClass().getResource("/cancel1.png")).getImage();
		btnCancel.setIcon(new ImageIcon(img1));
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
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				
				B4services.address_book_search_window = false;
				
				dispose();
			}
		});
		btnCancel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnCancel.setBounds(396, 372, 49, 44);
		contentPane.add(btnCancel);
	}
}
