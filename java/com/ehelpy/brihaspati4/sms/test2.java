package com.ehelpy.brihaspati4.sms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class test2 {

	public static void main(String[] args) {
		
		
		 String filepath = null;
		 String own_msg_folder = null;
		 
		BufferedReader rec_folder = null;
		try {
			rec_folder = new BufferedReader(new FileReader("Msg_rec_Path.txt"));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			filepath = rec_folder.readLine();
											
			own_msg_folder = filepath + "\\manu.txt";
								
			Path loc_path1 = Paths.get(own_msg_folder);
			
			boolean check_dir1;
			check_dir1 = Files.exists(loc_path1);
			
			if (!check_dir1)
			{
				try {
					Files.createDirectory(loc_path1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		String s = "This is a demo of the getChars method.";
		int start = 10; 
		int end = 14; 
		char[] buf = new char[end - start]; 
		s.getChars(start, end, buf, 0); 
		System.out.println(buf);

		String new_file_name = new String(buf)+".txt";
		
		Path source = Paths.get(own_msg_folder);
		try {
			Files.move(source, source.resolveSibling(new_file_name));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
