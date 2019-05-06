package com.ehelpy.brihaspati4.sms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class sms_methods 
{
	public static void choose_loc()
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader("Mess_folder_Path.txt"));
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{
			JFrame frame1 = new JFrame("Message");
			JOptionPane.showMessageDialog(frame1, "SELECT THE LOCATION FOR STORING MESSAGES");
			
			String msg_file_loc  = "null";
			
			JFileChooser chooser = new JFileChooser(); 
		    
			chooser.setCurrentDirectory(new java.io.File("."));
		    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    
		    // disable the "All files" option.
		    chooser.setAcceptAllFileFilterUsed(false);
		    //    
		    int response = chooser.showOpenDialog(null);
		    
		    if (response == JFileChooser.APPROVE_OPTION)
		    { 
		      System.out.println("getCurrentDirectory(): "+chooser.getCurrentDirectory());
		      System.out.println("getSelectedFile() : "+chooser.getSelectedFile());
		      msg_file_loc = chooser.getSelectedFile().toString();
		    }
		    
		    else
		    {
		      System.out.println("No Selection ");
		    }
		    
		    create_file_path_txt(msg_file_loc, "msg");
		    creat_folders(msg_file_loc);
		}
	}
	
	public static void creat_folders(String file_loc)
	{
		String loc_message_folder = "null";
		String loc1_message_sent_folder = "null"; 
		String loc2_message_cache_folder = "null";
		String loc3_message_rec_folder = "null";
				
		loc_message_folder = file_loc+"\\B4_Messages";
		File file = new File(loc_message_folder);
		file.mkdir();
				    
		loc1_message_sent_folder = loc_message_folder+"\\Sent_Messages";
		File file1 = new File(loc1_message_sent_folder);
		file1.mkdir();
					
		create_file_path_txt(loc1_message_sent_folder, "sent");
				
		loc2_message_cache_folder = loc_message_folder+"\\Cached_Messages";
		File file2 = new File(loc2_message_cache_folder);
		file2.mkdir();
				
		create_file_path_txt(loc2_message_cache_folder, "cache");
		
		loc3_message_rec_folder = loc_message_folder+ "\\Received_Messages";
		File file4 = new File(loc3_message_rec_folder);
		file4.mkdir();
		String file_path_rec_folder = file4.getAbsolutePath();
		
		create_file_path_txt(file_path_rec_folder, "receive");
	}	
	
	public static void create_file_path_txt(String file_path, String from)
	{
		FileWriter write = null;
		
		if(from.equals("msg"))
		{	
			
			try {
				write = new FileWriter("Mess_folder_Path.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(from.equals("receive"))
		{	
			
			try {
				write = new FileWriter("Msg_rec_Path.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(from.equals("sent"))
		{	
			
			try {
				write = new FileWriter("Msg_sent_Path.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		if(from.equals("cache"))
		{	
			
			try {
				write = new FileWriter("Msg_cache_folder_path.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PrintWriter wr = new PrintWriter(write);
		wr.write(file_path);
		wr.println("");
	
		wr.flush();
	}

	public static String create_folder_sent(String folder_name)
	{
		
		String loc1_message_sent_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_sent_Path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_message_sent_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String loc2_message_folder_name_sent_to = "null";
				
		String fileSeparator = System.getProperty("file.separator");
		
		loc2_message_folder_name_sent_to = loc1_message_sent_folder + fileSeparator + folder_name;
		Path loc_path2 = Paths.get(loc2_message_folder_name_sent_to);
		
		boolean check_dir2;
		check_dir2 = Files.exists(loc_path2);
		
		if (!check_dir2)
		{
			try {
				Files.createDirectory(loc_path2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 
				 
		return loc2_message_folder_name_sent_to;
	}
	
	public static String create_textFIle_loc_foler(String file_loc)
	{
		DateFormat df = new SimpleDateFormat("ddMMyy_HHmmss");
		
		Calendar calobj = Calendar.getInstance();
	    System.out.println(df.format(calobj.getTime()));
	    
	    String fileSeparator = System.getProperty("file.separator");
	    
	    String file_name  = file_loc+fileSeparator+df.format(calobj.getTime())+"(new_message).txt";	
	    
	    Path loc_path = Paths.get(file_name);
	    
	    try {
			Files.createFile(loc_path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    System.out.println("file name : "+file_name);
	    
	    return file_name;
	}
	
	public static void creat_text_file(String msg, String file_name)
	{
	    FileWriter write = null;
	    try
		{
			write = new FileWriter(file_name, true);
		}
		catch (IOException e1) 
		{
	
			e1.printStackTrace();
		}
		
		PrintWriter wr = new PrintWriter(write);
	
		wr.write(msg);
		wr.println("");
	
		wr.flush();
	}
		
}
