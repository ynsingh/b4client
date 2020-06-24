package com.ehelpy.brihaspati4.authenticate ;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//Last modified by Maj Dushyant Choudhary May 2020
//This function includes all the GUI being called in the package

public class Gui 
{
	//private static String keystorealias = null;
	static String result=null;
	public static String getkeystorepass() throws Exception 
    {
    	String msg1 = "ALERT: Remember KEYSTORE PASSWORD.";
    	showMessageDialogBox(msg1);
       	JPanel panel = new JPanel();
    	JLabel jPassword = new JLabel("PASSWORD");
        JPasswordField password = new JPasswordField(20);
        panel.add(jPassword);
        panel.add(password);
        Object[] options = {"OK","Regeneration of Keystore","Certificate Revocation","Generate New Certificate"};
        int result = JOptionPane.showOptionDialog(null, panel, "KEYSTORE PASSWORD ",JOptionPane.OK_CANCEL_OPTION,
        			 JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String passwordValue = null;
        System.out.println("getkeystorepass RESULT :"+result);
        if (result == 0) {
            char[] passwordValues = password.getPassword();
            passwordValue=String.valueOf(passwordValues);
        }
        if (result == 4) {
        	ReadVerifyCert.loadKeyStore(null,null);
        }
        if (result == 1) {
        	passwordValue="1";
        }
        if (result == 2) {
        	passwordValue="2";
        }
        if (result == 3) {
        	passwordValue="3";
        }
        return passwordValue; 
    }
    
    public static String createCertificateGUI() throws Exception 
    {
    	
    	result=null;
    	Object lock = new Object();
    	JFrame jf=new JFrame();
    	jf.setTitle("New Certificate Create Form"); 
        jf.setBounds(300, 90, 900, 600); 
        jf.setDefaultCloseOperation(0); 
        jf.setResizable(false); 
  
       Container c = jf.getContentPane(); 
        c.setLayout(null); 
        
        JLabel title = new JLabel("Generate New Certificate"); 
        title.setFont(new Font("Arial", Font.PLAIN, 30)); 
        title.setSize(600, 30); 
        title.setLocation(200, 30); 
        c.add(title); 
        
        JLabel email = new JLabel("Email ID"); 
        email.setFont(new Font("Arial", Font.PLAIN, 20)); 
        email.setSize(300, 20); 
        email.setLocation(100, 100); 
        c.add(email); 
  
        JTextField temail = new JTextField(); 
        temail.setFont(new Font("Arial", Font.PLAIN, 15)); 
        temail.setSize(190, 20); 
        temail.setLocation(300, 100); 
        c.add(temail); 
  
        JLabel ou = new JLabel("Organization Unit"); 
        ou.setFont(new Font("Arial", Font.PLAIN, 20)); 
        ou.setSize(300, 20); 
        ou.setLocation(100, 150); 
        c.add(ou); 
  
        JTextField tou = new JTextField(); 
        tou.setFont(new Font("Arial", Font.PLAIN, 15)); 
        tou.setSize(150, 20); 
        tou.setLocation(300, 150); 
        c.add(tou);
        
        JLabel o = new JLabel("Organization"); 
        o.setFont(new Font("Arial", Font.PLAIN, 20)); 
        o.setSize(300, 20); 
        o.setLocation(100, 200); 
        c.add(o); 
  
        JTextField to = new JTextField(); 
        to.setFont(new Font("Arial", Font.PLAIN, 15)); 
        to.setSize(150, 20); 
        to.setLocation(300, 200); 
        c.add(to);
        
        JLabel city = new JLabel("City"); 
        city.setFont(new Font("Arial", Font.PLAIN, 20)); 
        city.setSize(300, 20); 
        city.setLocation(100, 250); 
        c.add(city); 
  
        JTextField tcity = new JTextField(); 
        tcity.setFont(new Font("Arial", Font.PLAIN, 15)); 
        tcity.setSize(150, 20); 
        tcity.setLocation(300, 250); 
        c.add(tcity);
        
        JLabel state = new JLabel("State"); 
        state.setFont(new Font("Arial", Font.PLAIN, 20)); 
        state.setSize(300, 20); 
        state.setLocation(100, 300); 
        c.add(state); 
  
        JTextField tstate = new JTextField(); 
        tstate.setFont(new Font("Arial", Font.PLAIN, 15)); 
        tstate.setSize(150, 20); 
        tstate.setLocation(300, 300); 
        c.add(tstate);
        
        JLabel country = new JLabel("Country"); 
        country.setFont(new Font("Arial", Font.PLAIN, 20)); 
        country.setSize(300, 20); 
        country.setLocation(100, 350); 
        c.add(country); 
  
        JTextField tcountry = new JTextField(); 
        tcountry.setFont(new Font("Arial", Font.PLAIN, 15)); 
        tcountry.setSize(150, 20); 
        tcountry.setLocation(300, 350); 
        c.add(tcountry);
        
        JButton sub = new JButton("Submit"); 
        sub.setFont(new Font("Arial", Font.PLAIN, 15)); 
        sub.setSize(150, 40); 
        sub.setLocation(150, 450); 
        sub.addActionListener(new ActionListener(){  
        	public void actionPerformed(ActionEvent e)
        	{  
        		result=temail.getText()+","+tou.getText()+","+to.getText()+","+tcity.getText()+","+tstate.getText()+","+tcountry.getText();
        	    jf.dispose();
        	}  
        }); 
        c.add(sub);
        
        JButton cancel = new JButton("Cancel"); 
        cancel.setFont(new Font("Arial", Font.PLAIN, 15)); 
        cancel.setSize(150, 40); 
        cancel.setLocation(350, 450); 
        cancel.addActionListener(new ActionListener(){  
        	public void actionPerformed(ActionEvent e)
        	{  
        	    jf.dispose();
        	}  
        }); 
        c.add(cancel);
        
        jf.setVisible(true);
        
        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (jf.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    //System.out.println("Working now");
                }
            }
        };
        t.start();

        jf.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent arg0) {
                synchronized (lock) {
                    //jf.setVisible(false);
                    lock.notify();
                }
            }
                @Override
                public void windowClosed(WindowEvent arg0) 
                {
                    synchronized (lock) {
                        //jf.setVisible(false);
                        lock.notify();
                    }
            }

        });

        t.join();

    	if(result!=null) {
    		return result;	
    	}
		return result;
           	
    }
    
    
    public static String getkeystorerecovery() throws Exception 
    {
    	String msg1 = "ALERT: KEYSTORE CORRUPT.";
    	showMessageDialogBox(msg1);
       	JPanel panel = new JPanel();
    	JLabel jEmail = new JLabel("EMAIL");
        JTextField email = new JTextField(30);
        panel.add(jEmail);
        panel.add(email);
        Object[] options = {"OK", "CANCEL"};
        int result = JOptionPane.showOptionDialog(null, panel, "EMAIL ID ",JOptionPane.OK_CANCEL_OPTION,
        			 JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        String passwordValue = null;
        System.out.println("getkeystorepass RESULT :"+result);
        if (result == 0) 
        {
            passwordValue=email.getText();
        }
        
        return passwordValue; 
    }
    public static String getaliasname() {
    	JFrame frame = new JFrame("Alias Name");
        String keystorealias = JOptionPane.showInputDialog(frame, "Please enter new Alias Name");
        return keystorealias;
    }
    public static String getemailid() {        
        JLabel frame = new JLabel("EmailID");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter your Email-Id");
        if(commonname == null||(commonname != null && ("".equals(commonname))))
        {
        	  System.exit(0);
        }
        return commonname;
    }
    public static String getorganizationalunit() {
        JFrame frame = new JFrame("Enter Org Unit");
        String organizationalunit = JOptionPane.showInputDialog(frame, "Please enter your Organizational Unit");
        return organizationalunit;
    }
    public static String getorganization() {
        JFrame frame = new JFrame("Enter Organization");
        String organization = JOptionPane.showInputDialog(frame, "Please enter your Organization");
        return organization;
    }
    public static String getcity() {
        JFrame frame = new JFrame("Enter City");
        String city = JOptionPane.showInputDialog(frame, "Please enter your City");
        return city;
    }
    public static String getstate() {
    	JFrame frame = new JFrame("Enter State");
        String state = JOptionPane.showInputDialog(frame, "Please enter your State");
        return state;
    }
    public static String getcountry() {
        JFrame frame = new JFrame("Enter Country");
        String country = JOptionPane.showInputDialog(frame, "Please enter your Country");
        return country;
    }
    
    public static String getrevocationreason() {
        JFrame frame = new JFrame("Enter Certificate Revocation reason");
        String reason = JOptionPane.showInputDialog(frame, "Enter Certificate Revocation reason");
        return reason;
    }
    public static String getkeystorelocation() {
        JFrame frame = new JFrame("Enter keystore location");
        String keystorelocation = JOptionPane.showInputDialog(frame, "Please enter your keystore location");
        System.out.println(keystorelocation);
        return keystorelocation;
    }
    public static void showMessageDialogBox(String msg) {
        JFrame frame = new JFrame("Message");
        JOptionPane.showMessageDialog(frame,msg);
    }
    public static String getotp() {
        JLabel frame = new JLabel("OTP");
        String otp = JOptionPane.showInputDialog(frame, "Please Enter the OTP Recieved");
        return otp;
    }
    public static String getcalleremailid() {
        JLabel frame = new JLabel("EmailID");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter  Email-Id to be connected");
        return commonname;
    }
    public static String getip_address() {
        JLabel frame = new JLabel("IP Address");
        String commonname = JOptionPane.showInputDialog(frame, "Please enter  IP Address of the Destination");
        return commonname;
    }
    public static String connect() {
        String usr_name = javax.swing.JOptionPane.showInputDialog("Do u want to connect ");
        return usr_name;
    }
}


