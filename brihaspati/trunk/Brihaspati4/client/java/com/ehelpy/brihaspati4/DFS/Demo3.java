package com.ehelpy.brihaspati4.DFS;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class Demo3 {
	public static void main(String[] args) throws IOException {
		LinkedHashMap<String, List<String>> nodeMap = new LinkedHashMap<String,List<String>>();
		
		List<String> Array=new ArrayList<>();
		Array.add("1233.897.878.89");
		Array.add("1234");
		Array.add("1233.897.878.89");
		Array.add(5,"1234");
		
		nodeMap.put("file1", Array);
		nodeMap.put("file2", Array);
		String abc=nodeMap.toString();
		FileWriter write = new FileWriter("DFSooooo.txt");
	
	
		PrintWriter wr = new PrintWriter(write);
		wr.write(abc);

		System.out.println("ip table written to file");
		wr.flush();

		
	}
	  
}
	

	/////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////
	