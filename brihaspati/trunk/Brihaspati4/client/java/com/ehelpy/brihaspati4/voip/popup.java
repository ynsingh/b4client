package com.ehelpy.brihaspati4.voip;


	
	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	
	import javax.swing.JButton;
	import javax.swing.JDialog;
	import javax.swing.JFrame;
	import javax.swing.JOptionPane;
	import javax.swing.Timer;

	public class popup implements Runnable {

	    public void run() 
	    {
	    
	    JFrame frame1 = new JFrame();
	    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame1.setSize(100, 100);
	    frame1.setLocation(100, 100);

	   

	  //show a joptionpane dialog using showMessageDialog
	          JOptionPane.showMessageDialog(frame1,"CALL CONNECTING.PLEASE WAIT");
	}
	}
