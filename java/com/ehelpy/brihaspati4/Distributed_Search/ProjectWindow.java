package com.ehelpy.brihaspati4.Distributed_Search;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
// This class is used to make the GUI of the search Engine
public class ProjectWindow extends JFrame 
{
        private JPanel contentPane;
	private JTextField textField;
        public static String peerID;
        public static JEditorPane result;    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
        {
            peerID=SearchClient_v4.ID;
            
            File dir1 = new File("dat/index");// to check whether a predefined folder exists or not
            if (!dir1.exists()) dir1.mkdirs();
            
            File dir2 = new File("dat/data");
            if (!dir2.exists()) dir2.mkdirs();
            
            File dir3 = new File("dat/result");
            if (!dir3.exists()) dir3.mkdirs();
            
            EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectWindow frame = new ProjectWindow();
					frame.setTitle(peerID);
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
	public ProjectWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 300, 1350, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
                JLabel title=new JLabel();
                title.setText("B4 DISTRIBUTED SEARCH ENGINE");
                title.setFont(new Font("Serif", Font.BOLD, 40));
                title.setForeground(Color.RED);
                title.setBounds(365, 10, 705, 85);
                contentPane.add(title);
                
		textField = new JTextField();
		textField.setBounds(395, 100, 605, 35);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnLocal = new JButton("P2P Search");
		btnLocal.setBounds(750, 150, 120, 50);
                btnLocal.addActionListener(new ActionListener() 
                {
			public void actionPerformed(ActionEvent e) // Action Listeners tasks
                        {
                            SearchClient_v4.QUERY = textField.getText().toString();
                            SearchClient_v4.message="";
                            LuceneTester.result="";
                            System.out.println("-------P2P SEARCH RESULT-------");
                            String result=LuceneTester.main(SearchClient_v4.QUERY);
                            
                            System.out.println(result);
                            System.out.println("-------GLOBAL SEARCH RESULT-------");
                            SearchClient_v4 sc=new SearchClient_v4();
                            sc.getBroadcastPeerThread(peerID,SearchClient_v4.hop,peerID);
                        }
                });
		contentPane.add(btnLocal);
		
		JButton btnGlobal = new JButton("Global");
		btnGlobal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = textField.getText().toString();
				String charset = "UTF-8";
				
				try {
					String search = textField.getText().toString();
					//String str = textField.getText().toString();
					Document google = Jsoup.connect("http://www.google.com/search?q=" + URLEncoder.encode(query, charset)).userAgent("Mozilla/5.0").get();
					System.out.println("Google : "+google);
					Elements links = google.getElementsByTag("a");
					
					if(links.isEmpty()) {
						System.out.println("No Result Found");
					}
					for(Element link: links) {
						System.out.println("Title : "+link.text());
						System.out.println("URL : "+link.absUrl("href"));
					}
					
					
				}catch(Exception e1) {
					System.out.println("Error : "+e1.getMessage());
				}
			}
		});
		btnGlobal.setBounds(525, 150, 120, 50);
		contentPane.add(btnGlobal);
                
                result=new JEditorPane();
                //365,,705,,
                result.setBounds(100, 210, 1105, 600);
                result.setContentType("text/html");
                result.setEditable(false);
                result.addHyperlinkListener(new HyperlinkListener() 
                        {
                            @Override
                            public void hyperlinkUpdate(HyperlinkEvent hle) 
                            {
                                if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) 
                                {
                                    String fileNameHint=hle.getDescription();
                                    String key=String.valueOf(fileNameHint.charAt(0));
                                    String num=String.valueOf(fileNameHint.charAt(2));
                                    String fileName=key+"_"+num+".txt";
                                    String d="dat\\result\\"+fileName;
                                    String content = null;
                                    try 
                                    {
                                        content = new String(Files.readAllBytes(Paths.get(d.trim())));
                                        ProcessBuilder pb = new ProcessBuilder("Notepad.exe", d);
                                        pb.start();
                                    } 
                                    catch (IOException ex) 
                                    {
                                        Logger.getLogger(SearchClient_v4.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                    System.out.println(fileName);
                                    
                                }
                            }
                        });
                contentPane.add(result);
	}

}
