package com.ehelpy.brihaspati4.Address_Book;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JRadioButton;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

public class New_Entry_Window extends JFrame {

	private JPanel contentPane;
	private Popup popup;
	private JTextField name_entry_text;
	private JTextField email_id_text;
	private JTextField mobile_no_1_text;
	private JTextField mobile_no_2_text;
	private JTextField phone_number_text;
	private JTextField postal_address_text;
	String c;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					New_Entry_Window frame = new New_Entry_Window();
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
	public New_Entry_Window() {
		setTitle("ADDRESS BOOK");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 613, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("NAME");
		lblNewLabel.setBounds(123, 68, 63, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("EMAIL ID");
		lblNewLabel_1.setBounds(123, 115, 76, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("MOBILE NO 1");
		lblNewLabel_2.setBounds(123, 160, 76, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("MOBILE NO 2");
		lblNewLabel_3.setBounds(123, 208, 91, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("PHONE NUMBER");
		lblNewLabel_4.setBounds(123, 250, 103, 14);
		contentPane.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("POSTAL ADDRESS");
		lblNewLabel_5.setBounds(123, 289, 128, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("GENDER");
		lblNewLabel_6.setBounds(128, 330, 46, 14);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblPleaseEnterThe = new JLabel("PLEASE ENTER THE DETAILS");
		lblPleaseEnterThe.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblPleaseEnterThe.setBounds(184, 11, 243, 14);
		contentPane.add(lblPleaseEnterThe);
		
		B4services.address_book_new_entry_window = true;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
	
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);	
				
				B4services.address_book_new_entry_window = false;
				
				dispose();
			}
		});
		
		name_entry_text = new JTextField();
		name_entry_text.setBounds(341, 65, 152, 20);
		contentPane.add(name_entry_text);
		name_entry_text.setColumns(10);
		
		email_id_text = new JTextField();
		email_id_text.setText("");
		email_id_text.setBounds(341, 112, 152, 20);
		contentPane.add(email_id_text);
		email_id_text.setColumns(10);
		
		mobile_no_1_text = new JTextField();
		mobile_no_1_text.setBounds(341, 157, 152, 20);
		contentPane.add(mobile_no_1_text);
		mobile_no_1_text.setColumns(10);
		
		mobile_no_2_text = new JTextField();
		mobile_no_2_text.setBounds(341, 205, 152, 20);
		contentPane.add(mobile_no_2_text);
		mobile_no_2_text.setColumns(10);
		
		phone_number_text = new JTextField();
		phone_number_text.setBounds(341, 247, 152, 20);
		contentPane.add(phone_number_text);
		phone_number_text.setColumns(10);
		
		postal_address_text = new JTextField();
		postal_address_text.setBounds(341, 286, 152, 20);
		contentPane.add(postal_address_text);
		postal_address_text.setColumns(10);
		
		JRadioButton male_button = new JRadioButton("MALE");
		male_button.setBounds(351, 326, 63, 23);
		contentPane.add(male_button);
		male_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c="Male";
			}
		});
		
		JRadioButton female_button = new JRadioButton("FEMALE");
		female_button.setBounds(430, 326, 109, 23);
		contentPane.add(female_button);
		female_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				c="Female";
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
		submit_button.setFont(new Font("Tahoma", Font.BOLD, 14));
		submit_button.setBounds(204, 363, 46, 52);
		contentPane.add(submit_button);
		submit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				New_Entry_Upload  obj=new New_Entry_Upload();
				
				if((name_entry_text.getText().isEmpty())||(email_id_text.getText().isEmpty()))
					JOptionPane.showMessageDialog(null, "Please Enter Name as well as Email Id.");
					
				else
				{
					obj.Upload(name_entry_text.getText(), email_id_text.getText(), mobile_no_1_text.getText(), mobile_no_2_text.getText(), phone_number_text.getText(), postal_address_text.getText(), c);
					import_database.main(null);
					Display_Window_After_Login obj1=new Display_Window_After_Login();
					obj1.setVisible(true);
					dispose();
				}
			}
		});
			
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
		call_button.setContentAreaFilled(false);
		call_button.setBorderPainted(false);
		call_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				if(email_id_text.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please Enter Email Id.");
				}
				else
				{
					voip_rxcall.flag = false;
					String email_id = email_id_text.getText();
					import_database.main(null);
					dispose();
				
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
			}
		});
		call_button.setBounds(534, 65, 31, 28);
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
			
				if(email_id_text.getText().isEmpty())
				{
					JOptionPane.showMessageDialog(null, "Please Enter Email Id.");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "NOT YET ACTIVE.");
				}
			}
		});
		message_button.setBounds(530, 115, 35, 28);
		contentPane.add(message_button);
		
		JButton btnNewButton = new JButton("");
		Image img1 = new ImageIcon(this.getClass().getResource("/cancel1.png")).getImage();
		btnNewButton.setIcon(new ImageIcon(img1));
		btnNewButton.addMouseListener(new MouseAdapter() {
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
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				import_database.main(null);
				Display_Window_After_Login obj=new Display_Window_After_Login();
				obj.setVisible(true);
				
				B4services.address_book_new_entry_window = false;
				
				dispose();
			}
		});
		btnNewButton.setBounds(417, 363, 63, 52);
		contentPane.add(btnNewButton);
	
	}
}
