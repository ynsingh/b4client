package com.ehelpy.brihaspati4.Address_Book;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

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


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import java.awt.Dialog.ModalityType;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;

import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class Show_Details_Window extends JFrame {

	private JPanel contentPane;
	private JFrame frame;
	private Popup popup;
	public static JTextField name_text;
	public static JTextField email_id_text;
	public static JTextField mobile_no_1_text;
	public static JTextField mobile_no_2_text;
	public static JTextField phone_number_text;
	public static JTextField postal_address_text;
	public static JTextField gender_text;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Show_Details_Window frame = new Show_Details_Window();
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
	public Show_Details_Window() {
		
		setTitle("ADDRESS BOOK");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 610, 462);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("DETAILS");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(225, 11, 144, 28);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("NAME");
		lblNewLabel_1.setBounds(122, 55, 74, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("EMAIL ID");
		lblNewLabel_2.setBounds(122, 107, 74, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("MOBILE NO 1");
		lblNewLabel_3.setBounds(122, 145, 74, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("MOBILE NO 2");
		lblNewLabel_4.setBounds(122, 187, 74, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("PHONE NUMBER");
		lblNewLabel_5.setBounds(122, 219, 96, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("POSTAL ADDRESS");
		lblNewLabel_6.setBounds(122, 268, 110, 14);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("GENDER");
		lblNewLabel_7.setBounds(122, 322, 74, 14);
		contentPane.add(lblNewLabel_7);
		
		B4services.address_book_show_details_window = true;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
	
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);	
				
				B4services.address_book_show_details_window = false;
				
				dispose();
			}
		});
		
		name_text = new JTextField();
		name_text.setBounds(253, 50, 177, 20);
		contentPane.add(name_text);
		name_text.setColumns(10);
		
		email_id_text = new JTextField();
		email_id_text.setBounds(253, 104, 177, 20);
		contentPane.add(email_id_text);
		email_id_text.setColumns(10);
		
		mobile_no_1_text = new JTextField();
		mobile_no_1_text.setBounds(253, 142, 177, 20);
		contentPane.add(mobile_no_1_text);
		mobile_no_1_text.setColumns(10);
		
		mobile_no_2_text = new JTextField();
		mobile_no_2_text.setBounds(253, 184, 177, 20);
		contentPane.add(mobile_no_2_text);
		mobile_no_2_text.setColumns(10);
		
		phone_number_text = new JTextField();
		phone_number_text.setBounds(253, 216, 177, 20);
		contentPane.add(phone_number_text);
		phone_number_text.setColumns(10);
		
		postal_address_text = new JTextField();
		postal_address_text.setBounds(253, 255, 177, 40);
		contentPane.add(postal_address_text);
		postal_address_text.setColumns(10);
		
		gender_text = new JTextField();
		gender_text.setBounds(253, 319, 177, 20);
		contentPane.add(gender_text);
		gender_text.setColumns(10);
		
		JButton call_button = new JButton("");
		Image img2 = new ImageIcon(this.getClass().getResource("/call.png")).getImage();
		call_button.setIcon(new ImageIcon(img2));
		call_button.addMouseListener(new MouseAdapter() {
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
		call_button.setBorderPainted(false);
		call_button.setContentAreaFilled(false);
		call_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				voip_rxcall.flag = false;
				import_database.main(null);
				dispose();
				
				String email_id = email_id_text.getText();
						
				SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>()
			     {
			         @Override
			         protected Void doInBackground() throws Exception {

			            // mimic some long-running process here...
			        	voip_call.callstart_waiting(email_id); 
			            return null;
			         }
			     };

			     Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
			     final JDialog dialog = new JDialog(win, "Brihaspati 4", ModalityType.APPLICATION_MODAL);

			     mySwingWorker.addPropertyChangeListener
			     (
			    		 new PropertyChangeListener() 
			    		 {

			    			 @Override
			    			 public void propertyChange(PropertyChangeEvent evt)
			    			 {
			    				 if (evt.getPropertyName().equals("state"))
			    				 {
			    					 if (evt.getNewValue() == SwingWorker.StateValue.DONE)
			    					 {
			    						 dialog.dispose();
			    					 }
			    				 }
			    			 }
			    		 }
			      );
			      mySwingWorker.execute();

			      JProgressBar progressBar = new JProgressBar();
			      progressBar.setIndeterminate(true);
			      JPanel panel = new JPanel();
			      panel.add(new JLabel("Please wait while we connect you...."), BorderLayout.PAGE_START);
			      panel.add(progressBar, BorderLayout.PAGE_END);
			      dialog.add(panel);
			      dialog.setPreferredSize(new Dimension(300,100));
			      dialog.pack();
			      dialog.setLocationRelativeTo(win);
			      dialog.setVisible(true);
			}
		});
		call_button.setBounds(132, 356, 44, 23);
		contentPane.add(call_button);
		
		JButton message_button = new JButton("");
		Image img3 = new ImageIcon(this.getClass().getResource("/message.png")).getImage();
		message_button.setIcon(new ImageIcon(img3));
		message_button.addMouseListener(new MouseAdapter() {
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
		message_button.setContentAreaFilled(false);
		message_button.setBorderPainted(false);
		message_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Show_Details_Fetch obj=new Show_Details_Fetch();
				obj.message();;
			}
		});
		message_button.setBounds(234, 356, 49, 23);
		contentPane.add(message_button);
		
		JButton delete_button = new JButton("");
		Image img4 = new ImageIcon(this.getClass().getResource("/delete.png")).getImage();
		delete_button.setIcon(new ImageIcon(img4));
		delete_button.addMouseListener(new MouseAdapter() {
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
		delete_button.setContentAreaFilled(false);
		delete_button.setBorderPainted(false);
		delete_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				Show_Details_Fetch obj=new Show_Details_Fetch();
				obj.delete();
			}
		});
		delete_button.setBounds(357, 356, 35, 23);
		contentPane.add(delete_button);
		
		JButton btnNewButton = new JButton("");
		Image img5 = new ImageIcon(this.getClass().getResource("/update.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img5));
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("UPDATE");
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
				
				update_info  obj=new update_info();
				
				if((name_text.getText().isEmpty())||(email_id_text.getText().isEmpty()))
					JOptionPane.showMessageDialog(null, "Please Enter Name as well as Email Id.");
					
				else
					obj.Upload(name_text.getText(),email_id_text.getText(), mobile_no_1_text.getText(), mobile_no_2_text.getText(), phone_number_text.getText(), postal_address_text.getText(),gender_text.getText());
			}
				
			
		});
		btnNewButton.setBounds(468, 356, 44, 23);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("");
		Image img1 = new ImageIcon(this.getClass().getResource("/cancel1.png")).getImage();
		btnNewButton_1.setIcon(new ImageIcon(img1));
		btnNewButton_1.addMouseListener(new MouseAdapter() {
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
		btnNewButton_1.setContentAreaFilled(false);
		btnNewButton_1.setBorderPainted(false);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				
				B4services.address_book_show_details_window = false;
								
				dispose();
			}
		});
		btnNewButton_1.setBounds(280, 383, 65, 40);
		contentPane.add(btnNewButton_1);
				
	}

}
