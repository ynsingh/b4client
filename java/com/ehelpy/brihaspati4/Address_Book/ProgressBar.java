package com.ehelpy.brihaspati4.Address_Book;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingWorker;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class ProgressBar extends JFrame {

	private JPanel contentPane;
	private Popup popup;
	public static String Email_Id = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProgressBar frame = new ProgressBar();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void get_email(String email_id)
	{
		Email_Id = email_id;
	}
	/**
	 * Create the frame.
	 */
	public ProgressBar() {
		
	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try 
				{
					B4services.ss.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				B4services.service();
							
				dispose();
			}
				
		});
		
		setTitle("Brihaspati 4");
		setBounds(100, 100, 450, 145);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>()
	    {
			@Override
			protected Void doInBackground() throws Exception {

			//long-running process here...
			voip_call.callstart_waiting(Email_Id); 
			return null;
			}
	    };
		
	    mySwingWorker.addPropertyChangeListener(new PropertyChangeListener() {

	    	@Override
	    	public void propertyChange(PropertyChangeEvent evt) {
	    		if (evt.getPropertyName().equals("state")) {
	    			if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
	    				dispose();
	                }
	             }
	          }
	    });
	    mySwingWorker.execute();
	    
	    JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(44, 34, 347, 14);
		contentPane.add(progressBar);
		
		JButton CANCEL = new JButton("CANCEL");
		CANCEL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("WILL GET ACTIVATED AFTER 40 SEC OF CALL INITIATION");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		
		Timer time_set2 = new Timer();
		TimerTask task_timer2 = new TimerTask()
		{
			@Override
	        public void run()
	        {
				CANCEL.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				
						try 
						{
							B4services.ss.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						B4services.service();
									
						dispose();
					}
					
				});
			}
	    };
	    time_set2.schedule(task_timer2, 40000);	
	     
		CANCEL.setBounds(181, 72, 89, 23);
		contentPane.add(CANCEL);
		
		JLabel lblNewLabel = new JLabel("PLEASE WAIT WHILE CALL IS CONNECTED.....");
		lblNewLabel.setBounds(91, 11, 300, 14);
		contentPane.add(lblNewLabel);
	}
}
