package com.ehelpy.brihaspati4.DFS;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Set;

import com.ehelpy.brihaspati4.routingmgmt.CheckRTExists;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class Save_Retrive_data_Structures {

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	public static void Save_nodefilemap() throws IOException {
		Set<String> files_extracted = DistFileSys.nodefilemap.keySet(); /// code to extract hash_id from array by
		/// first
		/// converting it into collection then to an array
		FileWriter write = null;
		write = new FileWriter("DFS_nodefilemap.txt",true);
		PrintWriter wr = new PrintWriter(write);
		for (String file : files_extracted) {
			Integer no_Of_chunks = DistFileSys.nodefilemap.get(file);
			String line = file + "::" + no_Of_chunks.toString();

			wr.write(line);
			wr.println("");

		}
		wr.flush();
		System.out.println("fine");
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	public static void Retrive_nodefilemap() throws IOException {
		BufferedReader BR = new BufferedReader(new FileReader("DFS_nodefilemap.txt"));
		String Line;
		String file = null;

		try {
			do {
				Line = BR.readLine();
				String no_Of_chunk;
				System.out.println("Line  "+Line);
				file = Line.substring(0, Line.lastIndexOf("::"));
				System.out.println(Line.lastIndexOf("::"));
				System.out.println("file   "+file);
				no_Of_chunk = Line.substring(Line.lastIndexOf("::") + 2);
				DistFileSys.nodefilemap.put(file, Integer.parseInt(no_Of_chunk));

			} while (!Line.equals(null));

		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}
		System.out.println("DistFileSys.nodefilemap  " + DistFileSys.nodefilemap);
		BR.close();
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	public static void Save_nodeFileChunkMap() throws IOException {
		Set<String> files_extracted = DistFileSys.nodeFileChunkMap.keySet(); /// code to extract hash_id from array
																				/// by
/// first
/// converting it into collection then to an array
		FileWriter write = null;
		write = new FileWriter("DFS_nodeFileChunkMap.txt",true);
		PrintWriter wr = new PrintWriter(write);
		for (String file : files_extracted) {
			String[] IpNode = DistFileSys.nodeFileChunkMap.get(file);
			String line = file + "::" + IpNode[0] + "::" + IpNode[1] + "::" + IpNode[2] + "::" + IpNode[3];

			wr.write(line);
			wr.println("");

		}
		wr.flush();
		System.out.println("fine");
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	@SuppressWarnings("null")
	public static void Retrive_nodeFileChunkMap() throws IOException {
		BufferedReader BR = new BufferedReader(new FileReader("DFS_nodeFileChunkMap.txt"));
		String Line;
		String file = null;

		try {

			do {
				Line = BR.readLine();
				String IpNode[] = new String[20];
				System.out.println("Line   " + Line);
				file = Line.substring(0, Line.indexOf("::"));
				String Line1 = Line.substring(Line.indexOf("::") + 2);
				System.out.println("Line1  " + Line1);
				IpNode[0] = Line1.substring(0, Line1.indexOf("::"));
				String Line2 = Line1.substring(Line1.indexOf("::") + 2);
				System.out.println("Line2  " + Line2);
				IpNode[1] = Line2.substring(0, Line2.indexOf("::"));
				String Line3 = Line2.substring(Line2.indexOf("::") + 2);
				System.out.println("Line3  " + Line3);
				IpNode[2] = Line3.substring(0, Line3.indexOf("::"));
				String Line4 = Line3.substring(Line3.indexOf("::") + 2);
				System.out.println("Line4 " + Line4);
				IpNode[3] = Line4.substring(0);

				System.out.println("file   " + file);
				DistFileSys.nodeFileChunkMap.put(file, IpNode);

			} while (!Line.equals(null));
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}

		System.out.println("DistFileSys.nodeFileChunkMap  " + DistFileSys.nodeFileChunkMap);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	public static void Save_shared_Fileinfo_Map() throws IOException {
		Set<String> files_extracted = DistFileSys.shared_Fileinfo_Map.keySet(); /// code to extract hash_id from
																				/// array by
/// first
/// converting it into collection then to an array
		FileWriter write = null;
		write = new FileWriter("DFS_shared_Fileinfo_Map.txt",true);
		PrintWriter wr = new PrintWriter(write);
		for (String file : files_extracted) {
			String[] IpNode = DistFileSys.shared_Fileinfo_Map.get(file);
			String line = file + "::" + IpNode[0] + "::" + IpNode[1] + "::" + IpNode[2] + "::" + IpNode[3];

			wr.write(line);
			wr.println("");

		}
		wr.flush();
		System.out.println("fine");
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	@SuppressWarnings("null")
	public static void Retrive_shared_Fileinfo_Map() throws IOException {
		BufferedReader BR = new BufferedReader(new FileReader("DFS_shared_Fileinfo_Map.txt"));
		String Line;
		String file = null;

		try {

			do {
				Line = BR.readLine();
				String IpNode[] = new String[20];
				System.out.println("Line   " + Line);
				file = Line.substring(0, Line.indexOf("::"));
				String Line1 = Line.substring(Line.indexOf("::") + 2);
				System.out.println("Line1  " + Line1);
				IpNode[0] = Line1.substring(0, Line1.indexOf("::"));
				String Line2 = Line1.substring(Line1.indexOf("::") + 2);
				System.out.println("Line2  " + Line2);
				IpNode[1] = Line2.substring(0, Line2.indexOf("::"));
				String Line3 = Line2.substring(Line2.indexOf("::") + 2);
				System.out.println("Line3  " + Line3);
				IpNode[2] = Line3.substring(0, Line3.indexOf("::"));
				String Line4 = Line3.substring(Line3.indexOf("::") + 2);
				System.out.println("Line4 " + Line4);
				IpNode[3] = Line4.substring(0);

				System.out.println("file   " + file);
				DistFileSys.shared_Fileinfo_Map.put(file, IpNode);

			} while (!Line.equals(null));
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}

		System.out.println("DistFileSys.shared_Fileinfo_Map  " + DistFileSys.shared_Fileinfo_Map);
	}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	public static void Save_root_Fileinfo_Map() throws IOException {
		Set<String> files_extracted = DistFileSys.root_Fileinfo_Map.keySet(); /// code to extract hash_id from array
																				/// by
/// first
/// converting it into collection then to an array
		FileWriter write = null;
		write = new FileWriter("DFS_root_Fileinfo_Map.txt",true);
		PrintWriter wr = new PrintWriter(write);
		for (String file : files_extracted) {
			String[] IpNode = DistFileSys.root_Fileinfo_Map.get(file);
			String line = file + "::" + IpNode[0] + "::" + IpNode[1] + "::" + IpNode[2] + "::" + IpNode[3] + "::"
					+ IpNode[4] + "::" + IpNode[5] + "::" + IpNode[6];

			wr.write(line);
			wr.println("");

		}
		wr.flush();
		System.out.println("fine");
	}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This is old DFS function
	 * @deprecated
	 */
	 @Deprecated
	@SuppressWarnings("null")
	public static void Retrive_root_Fileinfo_Map() throws IOException {
		BufferedReader BR = new BufferedReader(new FileReader("DFS_root_Fileinfo_Map.txt"));
		String Line;
		String file = null;

		try {

			do {
				Line = BR.readLine();
				String IpNode[] = new String[20];
				System.out.println("Line   " + Line);
				file = Line.substring(0, Line.indexOf("::"));
				String Line1 = Line.substring(Line.indexOf("::") + 2);
				System.out.println("Line1  " + Line1);
				IpNode[0] = Line1.substring(0, Line1.indexOf("::"));
				String Line2 = Line1.substring(Line1.indexOf("::") + 2);
				System.out.println("Line2  " + Line2);
				IpNode[1] = Line2.substring(0, Line2.indexOf("::"));
				String Line3 = Line2.substring(Line2.indexOf("::") + 2);
				System.out.println("Line3  " + Line3);
				IpNode[2] = Line3.substring(0, Line3.indexOf("::"));
				String Line4 = Line3.substring(Line3.indexOf("::") + 2);
				System.out.println("Line4 " + Line4);
				IpNode[3] = Line4.substring(0, Line4.indexOf("::"));
				String Line5 = Line4.substring(Line4.indexOf("::") + 2);
				System.out.println("Line5 " + Line5);
				IpNode[4] = Line5.substring(0, Line5.indexOf("::"));
				String Line6 = Line5.substring(Line5.indexOf("::") + 2);
				System.out.println("Line6 " + Line6);
				IpNode[5] = Line6.substring(0, Line6.indexOf("::"));
				String Line7 = Line6.substring(Line6.indexOf("::") + 2);
				System.out.println("Line7 " + Line7);
				IpNode[6] = Line7.substring(0);

				System.out.println("file   " + file);
				DistFileSys.root_Fileinfo_Map.put(file, IpNode);

			} while (!Line.equals(null));
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}

		System.out.println("DistFileSys.root_Fileinfo_Map  " + DistFileSys.root_Fileinfo_Map);
	}

}
