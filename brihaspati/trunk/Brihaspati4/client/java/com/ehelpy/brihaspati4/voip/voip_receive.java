package com.ehelpy.brihaspati4.voip;

import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.crypto.SecretKey;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.ehelpy.brihaspati4.authenticate.b4server_services;
import com.ehelpy.brihaspati4.authenticate.debug_level;
import com.ehelpy.brihaspati4.authenticate.properties_access;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;

public class voip_receive extends Thread {

    private JFrame frame;
    private static int port1 =8880 ;
    public static Socket socket1 ;
    private static int port2=8880 ;
    public static Socket socket2 ;
    public static ServerSocket serverSocket1,serverSocket2;
    private static voip_key enc_key = null;
    private static SecretKey sec_key = null;
    private static InetAddress address = null;
    public static AudioFormat audio1,audio2 ;
    public static DataLine.Info info, info_out ;
    public static  TargetDataLine audio_in;
    public static SourceDataLine audio_out;
    public static boolean calling,flag = false;
    private static JButton button;
    public static AudioPlayer MGP = AudioPlayer.player;
    public static AudioStream BGM;
    AudioData MD;
    static ContinuousAudioDataStream loop = null;
    static String path = null;
    public static boolean music_on = true;


    /**
     * Launch the application.
     */
    public static void receive(String IPaddr, long sym_key) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    voip_receive window = new voip_receive(IPaddr, sym_key);
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
    public voip_receive(String IPaddr, long sym_key) {
        initialize(IPaddr, sym_key);
    }

    /**
     * Initialize the contents of the frame.
     */
    @SuppressWarnings("static-access")
    private void initialize(String IPaddr, long sym_key) {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        Image img = new ImageIcon(this.getClass().getResource("/phone_calling.png")).getImage();
        lblNewLabel.setIcon(new ImageIcon(img));
        lblNewLabel.setBounds(152, 30, 128, 124);
        frame.getContentPane().add(lblNewLabel);
        try {
            address = InetAddress.getByName(IPaddr);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        debug_level.debug(0, "The ip address of the far end client has reached the callmanager");
        debug_level.debug(1,"Socket created for txn of data in port number =   " +  port1 +port2 );
        enc_key = new voip_key(sym_key);

        Thread t = new Thread(enc_key);
        t.start();
        System.out.println("w1");
        try {
            sec_key = enc_key.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        audio1 = getAudioFormat();
        info = new DataLine.Info(TargetDataLine.class, audio1);
        audio2 = getAudioFormat();
        info_out = new DataLine.Info(SourceDataLine.class, audio2);
        if(!(AudioSystem.isLineSupported(info)||AudioSystem.isLineSupported(info_out))) {
            System.out.println("not supported audio format");
            System.exit(0);
        }

        try {
            audio_in = (TargetDataLine)AudioSystem.getLine(info);
            audio_in.open(audio1);
        } catch (LineUnavailableException e) {
            audio_in.drain();
            audio_in.close();
            audio_nosupport.id_exist();
        }

        audio_in.start();
        try {
            audio_out = (SourceDataLine)AudioSystem.getLine(info_out);
            audio_out.open(audio2);
        } catch (LineUnavailableException e) {
            audio_in.drain();
            audio_in.close();
            e.printStackTrace();
        }

        audio_out.start();
        player_rxthread p = new player_rxthread(sec_key);
        p.audio_out = audio_out;
        calling = true;
        flag = true;

        recorder_rxthread rec = new recorder_rxthread(sec_key);
        rec.audio_in = audio_in;
        try {
            rec.line = new DatagramSocket(0);
            rec.line.setSoTimeout(10000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        rec.far_endip = address;
        rec.comn_port=port2;
        System.out.println("the rec.line port is    "+   rec.line.getLocalPort());
        p.din = rec.line;

        button = new JButton("Stop");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                rec.line.close();
                calling=false;
                flag = false;
                music_on=true;

                voip_rxcall.flag=true;
                b4server_services.service();
                frame.dispose();
            }
        });
        button.setFont(new Font("Times New Roman", Font.BOLD, 20));
        button.setBounds(259, 167, 117, 57);
        frame.getContentPane().add(button);
        music();

        JButton start = new JButton("Start");
        start.setFont(new Font("Times New Roman", Font.BOLD, 20));
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                music_on =false;
                AudioPlayer.player.stop(BGM);
                lblNewLabel.removeAll();
                Image img = new ImageIcon(this.getClass().getResource("/phone_call.png")).getImage();
                lblNewLabel.setIcon(new ImageIcon(img));
                lblNewLabel.setBounds(152, 30, 128, 124);
                frame.getContentPane().add(lblNewLabel);
                start.setEnabled(false);
                System.out.println("port2" + port2);

                rec.comn_port = port2;
                rec.start();
                p.start();

                button.setEnabled(true);
                return;
            }
        });
        start.setBounds(46, 167, 117, 57);
        frame.getContentPane().add(start);

    }
    private static AudioFormat getAudioFormat() {
        float samplerate = 8000.0F;
        int samplesizebits = 16;
        int channel = 2;
        boolean signed = true;
        boolean bigEndian = false;

        return new AudioFormat(samplerate,samplesizebits,channel,signed,bigEndian );
    }
    public static void music() {

        path = properties_access.read_property("client.properties", "phone_ring");
        try {
            InputStream test = new FileInputStream(path);
            BGM = new AudioStream(test);
            if(music_on)
                AudioPlayer.player.start(BGM);
            //
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //AudioPlayer.player.start(BGM);
        //MGP.start(loop);


        //MGP.stop(loop);
    }
}