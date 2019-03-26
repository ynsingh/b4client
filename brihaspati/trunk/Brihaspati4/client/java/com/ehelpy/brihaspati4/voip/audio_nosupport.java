package com.ehelpy.brihaspati4.voip;

	import java.awt.EventQueue;

	import javax.swing.JFrame;
	import java.awt.Font;
	import javax.swing.SwingConstants;


import javax.swing.JLabel;
	import javax.swing.JButton;
	import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

	public class audio_nosupport {

		private JFrame frame;

		/**
		 * Launch the application.
		 */
		public static void id_exist() {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						audio_nosupport window = new audio_nosupport();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		/**
		 * Create the application.
		 */
		public audio_nosupport() {
			initialize();
		}

		/**
		 * Initialize the contents of the frame.
		 */
		private void initialize() {
			frame = new JFrame();
			frame.setBounds(100, 100, 450, 300);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			JLabel lblNewLabel = new JLabel("Audio Not Supported");
			lblNewLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 18));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(38, 85, 329, 35);
			frame.getContentPane().add(lblNewLabel);
			
			JButton btnNewButton = new JButton("OK");
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					if(!B4services.display_window_open&&!B4services.address_book_delete_window&&!B4services.address_book_multiple_entries_window
							&&!B4services.address_book_new_entry_window&&!B4services.address_book_search_window&&!B4services.address_book_show_details_window
							&&!B4services.voip_gui_window)
					{	
						try {
							B4services.ss.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						B4services.service();
					}	
				}
			});
			btnNewButton.setBounds(156, 188, 97, 25);
			frame.getContentPane().add(btnNewButton);
		}
	}

