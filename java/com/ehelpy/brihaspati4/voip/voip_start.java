package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;
import javax.swing.JFrame;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.authenticate.properties_access;
//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;
import javax.crypto.SecretKey;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JLabel;

public class voip_start extends Thread  {

	private JFrame frame;
	private static int port1=8880 ;
	public static Socket socket1 ;
	private static int port2=8880 ;
	public static Socket socket2 ;
	public static ServerSocket serverSocket = null;
	public static DatagramPacket packet = null;
	private static voip_key enc_key = null;
	private static SecretKey sec_key = null;
	public static AudioFormat audio1,audio2 ;
	public static DataLine.Info info, info_out ;
	public static  TargetDataLine audio_in;
	public static SourceDataLine audio_out; 
	public static boolean calling,flag = false;
	public  static DatagramSocket datagramSocket ;
	//public static AudioPlayer MGP = AudioPlayer.player;
	//public static AudioStream BGM;
	static String path = null;
	public static boolean music_on = true;
	

	/**
	 * Launch the application.
	 */
	public static void start(String IPaddr, long sym_key) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					voip_start window = new voip_start(IPaddr, sym_key);
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
	public voip_start(String IPaddr, long sym_key)
	{
		initialize(IPaddr, sym_key);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String IPaddr, long sym_key) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		B4services.BServices.setVisible(false);
		B4services.BServices.dispose();
		
		 try {
			InetAddress.getByName(IPaddr);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		 debug_level.debug(0, "The ip address of the far end client has reached the callmanager");
		 
		 debug_level.debug(1,"Socket created for txn of data in port number =   " +  port1   + port2);
		 
		 enc_key = new voip_key(sym_key);
		 Thread t = new Thread(enc_key);
		 t.start();
		 System.out.println("Fetching secret key at caller end in thread");
		   try {
			    sec_key = enc_key.get();
		       } 
		   catch (InterruptedException e) 
		      {
			    e.printStackTrace();
		      }
		 audio1 = getAudioFormat();
		 info = new DataLine.Info(TargetDataLine.class, audio1);   // to be read from mic
		 audio2 = getAudioFormat();
		 info_out = new DataLine.Info(SourceDataLine.class, audio2);   // to be written onto speaker
		 if(!(AudioSystem.isLineSupported(info)||AudioSystem.isLineSupported(info_out))) {
			 System.out.println("Audio format not supported");
			 audio_nosupport.id_exist();
		 }
		 
		 try {
			 audio_in = (TargetDataLine)AudioSystem.getLine(info);
			 audio_in.open(audio1);
		} catch (LineUnavailableException e) {
			audio_in.drain();
			audio_in.close();
		}
		
		 audio_in.start();   // start receiving audio from mic 
		 try {
			 
			audio_out = (SourceDataLine)AudioSystem.getLine(info_out);
			audio_out.open(audio2);
			
			
		} catch (LineUnavailableException e) {
			audio_in.drain();
			audio_in.close();
			e.printStackTrace();
			
		}
		 
		 audio_out.start();		// start sending the recieved audio out to speaker
		    player_thread p = new player_thread(sec_key);  // voice packet received and decrypted
		    player_thread.audio_out = audio_out;
		      try {
			        player_thread.din = new DatagramSocket(port1);
			        player_thread.din.setSoTimeout(10000);
		          } 
		      catch (SocketException e) {
			  e.printStackTrace();
		}
		 calling = true;
		 flag = true;
		 
		 recorder_thread rec = new recorder_thread(sec_key);
		 rec.audio_in = audio_in;
		 p.start();
	    
		 rec.comn_port = port2;
		 rec.start();
		 
		JButton btnNewButton = new JButton("Stop");
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				calling=false;
				flag = false;
				player_thread.din.close();
				
				frame.dispose();
				debug_level.debug(0,"The client main started");
				voip_rxcall.flag = true;
				
				if(!B4services.display_window_open&&!B4services.address_book_delete_window&&!B4services.address_book_multiple_entries_window
					&&!B4services.address_book_new_entry_window&&!B4services.address_book_search_window&&!B4services.address_book_show_details_window
					&&!B4services.voip_gui_window&&!B4services.sms_send_window&&!B4services.sms_window&&!B4services.sms_reader_window&&!B4services.sms_sent_messages_window )
				   {				
			 	    try {
					     B4services.ss.close();
				        } 
			 	    catch (IOException e1) 
			 	        {
					                       // TODO Auto-generated catch block
					                       e1.printStackTrace();
				        }
				    B4services.service();
				
				   }
			}
		});
		btnNewButton.setBounds(165, 183, 117, 57);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblLabel = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/phone_call.png")).getImage();
		lblLabel.setIcon(new ImageIcon(img));
		lblLabel.setBounds(152, 30, 128, 124);
		frame.getContentPane().add(lblLabel);
		
	}
	private static AudioFormat getAudioFormat() {
		float samplerate = 8000.0F;   //sampleRate      the number of samples per second
		int samplesizebits = 16;     //samplesizebits  the number of bits in each sample
		int channel = 2;             //channels  the number of channels (1 for mono, 2 for stereo, and so on)
		boolean signed = true;      //signed  indicates whether the data is signed or unsigned
		boolean bigEndian = false;  //bigEndian       indicates whether the data for a single sample
	                                                //  is stored in big-endian byte order 
	                                                //  or little-endian)
		
		return new AudioFormat(samplerate,samplesizebits,channel,signed,bigEndian );
	}
public static void music() {
		
		path = properties_access.read_property("client.properties", "phone_ring");
		try {
			 InputStream test = new FileInputStream(path);
	         //BGM = new AudioStream(test);
	        // if(music_on)
	         //AudioPlayer.player.start(BGM);
	         //
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
  }


}
