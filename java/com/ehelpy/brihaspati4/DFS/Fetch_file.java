package com.ehelpy.brihaspati4.DFS;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.voip.B4services;

public class Fetch_file extends JFrame implements ActionListener {


	private JList list;
	private JButton retrive;
	private JButton delete;
	private JPanel panel;
	
	@SuppressWarnings("unchecked")
	public Fetch_file() throws IOException {
		
		// set flow layout for the frame
		//this.getContentPane().setLayout(new FlowLayout());
		panel= new JPanel();
		JLabel lab= new JLabel("SELECT THE FILE WHICH YOU WANT TO RETRIVE OR DELETE"); 
		Save_Retrive_data_Structures.Retrive_nodefilemap();
		Collection<String> fileName_Extracted=DistFileSys.nodefilemap.keySet();
		String file[]=fileName_Extracted.toArray(new String[DistFileSys.nodefilemap.size()]);
		//String file[]=DistFileSys.fileNodeLinkList.toArray(new String[DistFileSys.fileNodeLinkList.size()]);
		
		list = new JList(file);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		
		
		retrive = new JButton("RETRIVE");
		retrive.addActionListener(this);
		delete = new JButton("DELETE");
		delete.addActionListener(this);
		// add list to frame
		panel.add(list);
		panel.add(retrive);
		panel.add(delete);
		panel.add(lab);
		add(panel);
		setSize(300,300);
		System.out.println("fetch method is wkg");
		//Fetch_file.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

	}

	@Override
	public void actionPerformed(ActionEvent e){
		while (e.getActionCommand().equals(null))
				{
			     try {
					Thread.sleep(2000);
					System.out.println("fetch thread has no action");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
		 if (e.getActionCommand().equals("RETRIVE")) {
			int index = list.getSelectedIndex();
			System.out.println("Index Selected: " + index);
			String s = (String) list.getSelectedValue();
			System.out.println("Value Selected: " + s);
			System.out.println("why notttttt retrive");
						
			/*		try {
						B4services.ss.close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
				
					
				System.out.println("why notttttt11111111111111 retrive");		
				B4services.service();*/
			
			
			int no_of_Chunks=DistFileSys.nodefilemap.get(s);
			for(Integer i=0;i<no_of_Chunks;i++)
			{
				String[] IpNode=new String[20];//to keep ipAdd and Nodeid for a particular chunk
				String ChunkName=s+"Chunk"+i.toString();
				
				IpNode=DistFileSys.nodeFileChunkMap.get(ChunkName);
				String chunkname=ChunkName.replace(':', '&');
				String toNodeId=IpNode[0];
				String toNodeId1=IpNode[2];
				try {
					DistFileSysUtilityMethods.fetchFile(toNodeId,chunkname ,OverlayManagement.myNodeId,
							DistFileSysUtilityMethods.myIp);
					DistFileSysUtilityMethods.fetchFile(toNodeId1,chunkname ,OverlayManagement.myNodeId,
							DistFileSysUtilityMethods.myIp);
					
					FileReceiverDemox.fileReceiver_Fetch();
					
					}
				 catch (ParserConfigurationException| IOException | TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			/*try {
				createAndShowGUI();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
		}
		else if(e.getActionCommand().equals("DELETE")) {
			int index = list.getSelectedIndex();
			System.out.println("Index Selected: " + index);
			String s = (String) list.getSelectedValue();
			System.out.println("Value Selected: " + s);
			
			//LinkedHashMap<String, ArrayList<String>> fileFragMap=new  LinkedHashMap<String, ArrayList<String>>();
			//fileFragMap=DistFileSys.fileNodeMap.get(s);//extracting the value for this File(which is selected)
			// the value is a linked hash map
			int no_of_Chunks=DistFileSys.nodefilemap.get(s);
			for(Integer i=0;i<no_of_Chunks;i++)
			{
				String[] IpNode=new String[20];//to keep ipAdd and Nodeid for a particular chunk
				String ChunkName=s+"Chunk"+i.toString();
				
				IpNode=DistFileSys.nodeFileChunkMap.get(ChunkName);
				String chunkname=ChunkName.replace(':', '&');
				String toNodeId=IpNode[0];
				try {
					DistFileSysUtilityMethods.file_Deletion_query(toNodeId,chunkname , OverlayManagement.myNodeId, 
							DistFileSysUtilityMethods.myIp, "root");
					}
				 catch (ParserConfigurationException| IOException | TransformerException e1) {
					e1.printStackTrace();
				}
			}
			
		}
		 
			
	}

	public static void createAndShowGUI() throws IOException {

		// Create and set up the window.

		JFrame frame = new Fetch_file();

		// Display the window.

		frame.pack();

		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				
					System.out.println("why notttttt fetch");
					//frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					frame.dispose();
					try {
						B4services.ss.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				
				System.out.println("why notttttt11111111111111 fetch");		
				B4services.service();
			}
		});

		

	}

	public static void main(String[] args) {

		try {
			createAndShowGUI();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
