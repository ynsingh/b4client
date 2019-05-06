package com.ehelpy.brihaspati4.sms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class test {

	public static void main(String[] args) 
	{
		send_msgs_of_node1("\\navleen@iitk.ac.in");
	}
	
	public static void send_msgs_of_node1(String folder_name)
	{
		String loc1_message_rec_folder = "null";
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			loc1_message_rec_folder = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String loc2_message_folder_name_of_rec= "null";
		
		String fileSeparator = System.getProperty("file.separator");
		
		loc2_message_folder_name_of_rec = loc1_message_rec_folder + fileSeparator + folder_name;
		Path loc_path1 = Paths.get(loc2_message_folder_name_of_rec);
		
		boolean check_dir1;
		check_dir1 = Files.exists(loc_path1);
		
		if (check_dir1)
		{
			File file = new File(loc2_message_folder_name_of_rec);
			String[] files = file.list();
			for(String sender_email : files)
			{
				String msg_file = loc2_message_folder_name_of_rec+fileSeparator + sender_email ;
				
				File file1 = new File(msg_file );
				String[] files1 = file1.list();
				for(String file_name_msg : files1)
				{	
					String msg_file_loc = msg_file + fileSeparator + file_name_msg;
					String text = sms_send_rec_management.readFileAsString(msg_file_loc);
					System.out.println(sender_email+" : "+file_name_msg);
					System.out.println(text);
				}	
			}
		}
	}
}
