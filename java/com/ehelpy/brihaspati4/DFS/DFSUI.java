package com.ehelpy.brihaspati4.DFS;

import com.ehelpy.brihaspati4.comnmgr.Receiver;
import com.ehelpy.brihaspati4.ReplicaMgt.Monitor;
import com.ehelpy.brihaspati4.voip.B4services;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.ehelpy.brihaspati4.DFS.Upload.start;

public class DFSUI extends JFrame
{
    private final JMenuBar menu;
    private final JMenu m1;
    private JMenuItem View,Upload,Download,Delete,Exit,Modify;
    private JButton btnView,btnUpload,btnDelete,btnDownload,btnModify,btnExit;
    private JPanel pMain, pCenter;
    private JTextArea tac;
    private JLabel lbllogo;

    public DFSUI()
    {
        //menu bar and menu item initialization
        menu = new JMenuBar();
        m1 = new JMenu("Options");

        View = new JMenuItem("My Drive");
        Upload = new JMenuItem("Upload");
        Download = new JMenuItem("Download");
        Delete = new JMenuItem("Delete");
        Modify = new JMenuItem("Modify");
        Exit = new JMenuItem("Exit");

        //button intialization
        // The first line initialises the button with text as displayed against
        // each. the second line shows the tip related to the button

        btnView = new JButton("My Drive");
        btnView.setToolTipText("Show the contents in Drive");

        btnUpload = new JButton("Upload");
        btnUpload.setToolTipText("Upload to Drive");

        btnDelete = new JButton("DELETE");
        btnDelete.setToolTipText("Delete from Drive");

        btnDownload = new JButton("Download");
        btnDownload.setToolTipText("Download from Drive");

        btnModify = new JButton("Modify");
        btnModify.setToolTipText("Modify the contents in Drive");

        btnExit = new JButton("Exit");
        btnExit.setToolTipText("Exit from DFS");
        //initialization of panel
        // change these to add or remove any of the panel
        pMain = new JPanel();
        pCenter = new JPanel();
        // lbllogo change here to display a logo
        lbllogo = new JLabel(new ImageIcon("//G:/MFCfinish.gif"),JLabel.CENTER);
        //add menu items to Options menu
        m1.add(View);
        m1.add(Upload);
        m1.add(Download);
        m1.add(Delete);
        m1.add(Modify);
        m1.add(Exit);
        // add the menu items to menu bar
        menu.add(m1);
        // add buttons to the panel
        pMain.add(btnView);
        pMain.add(btnUpload);
        pMain.add(btnDownload);
        pMain.add(btnDelete);
        pMain.add(btnModify);
        pMain.add(btnExit);
        // set layout for the panel main change here for background, layout text displayed etc
        pMain.setLayout(new BoxLayout(pMain,BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createTitledBorder("OPTIONS"));
        pMain.setLayout(new GridLayout(6,1));
        pMain.setBackground(Color.white);
        // set layout for the panel center
        pCenter.setLayout(new BoxLayout(pMain,BoxLayout.Y_AXIS));
        pCenter.setLayout(new GridLayout(1,1));
        pCenter.add(lbllogo);
        // position of the panel is controlled from here
        this.getContentPane().add(pMain,"East");
        this.getContentPane().add(pCenter,"Center");
        // size of the window containing everything above is specified here
        this.setSize(500,400);
        this.setResizable(false);
        this.setLocation(450,200);
        this.setTitle("DFS Drive");
        this.show();
// The following sections list all the action listeners in order
// change here to modify what a button does
///////////////////////////////////////////////////////////////////////////////////////////////////////
        btnUpload.addActionListener(e -> {
            //call the Upload class once user clicks on Upload
            try {
                start();//reference to Upload.start()
            } catch (IOException | GeneralSecurityException ex) {
                ex.printStackTrace();
            }
    });
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnDownload.addActionListener(e -> {
            //call the Upload class once user clicks on Upload
            try {
                ListFiles.start();//reference to Upload.start()
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnDelete.addActionListener(e -> {
            //call the Upload class once user clicks on Upload
            try {
                ListFiles.start();//reference to Upload.start()
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        btnExit.addActionListener(e -> {
            dispose();
            try{
                B4services.ss.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
            B4services.service();
        });
/////////////////////////////////////////////////////////////////////////////////////////////////////////
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            dispose();
            try {
                B4services.ss.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            B4services.service();
            }
        });
    }
public static void essentials() {
	Thread rx = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Receiver.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });
    rx.start();
    Thread monitor = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Monitor.statusReport();
                System.out.println("Monitor started");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    });
    monitor.start();   
}
// Starts the DFS module
public static void main()
    {
	DFSUI UI = new DFSUI();
    }
}
