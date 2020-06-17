/**
 * <b>Use this class to perform file handling operations<b><p>
 * @author Sarthak Bisht (https://t.me/bishtsarthak)
 * @since 25th Jan 2020
 */

package com.ehelpy.brihaspati4.screencast;


import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public final class File {
	public final static String CSV_DELIMIT = ",";
	
	/**
	 * Use this function to read a .CSV file into 2D ArrayList of String data
	 * Reference -
	 * <ul>
	 *  <li><a href="https://tools.ietf.org/html/rfc4180">RFC 4180</a> dated Oct 2005</li>
	 *  <li><a href="https://tools.ietf.org/html/rfc7111">RFC 7111</a> dated Jan 2014</li>
	 * </ul>
	 * @param fileName <name>.csv of source file (or its complete path from code_jar root directory)
	 * @return 2D ArrayList of String data
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static ArrayList<ArrayList<String>> csv2array(String fileName) 
			throws FileNotFoundException, IOException {
		ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
		String path = System.getProperty("user.dir") + System.getProperty("file.separator") + fileName;
		FileReader file = new FileReader(path);
		Scanner file2record = new Scanner(file);
		while(file2record.hasNextLine()) {
			Scanner record2value = new Scanner(file2record.nextLine());
			record2value.useDelimiter(CSV_DELIMIT);
			ArrayList<String> record = new ArrayList<String>();
			while(record2value.hasNext()) record.add(record2value.next());
			table.add(record);
			record2value.close();
		}
		file2record.close();
		file.close();
		return table;
	}
	
	/**
	 * Use this class to generate TableModel for JTable
	 * import auxx.File;
	 * String fileName = "userInfo.csv";
	 * JTable jTable = new JTable(File.array2table(File.csv2array(fileName)));
	 * @param array
	 * @return
	 */
	public static TableModel array2table(ArrayList<ArrayList<String>> array) {
		int maxColumn = 0;
		int rowSize = array.size();
		for(int i=0; i<rowSize; i++) {
			int columnSize = array.get(i).size();
			if (columnSize > maxColumn) maxColumn = columnSize;
		}
		TableModel tableModel = new DefaultTableModel(rowSize, maxColumn);
		for(int i=0; i<rowSize; i++)
			for(int j=0; j<array.get(i).size(); j++)
				tableModel.setValueAt(array.get(i).get(j), i, j);
		return tableModel;
	}
	
	public static void test1(String fileName) 
			throws CertificateException, IOException {
		FileInputStream fis = new FileInputStream(fileName);
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		while (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
		    System.out.println(cert.toString());
		}
	}
	
	public static void test2(String fileName) throws IOException, GSSException {
		FileInputStream fis = new FileInputStream(fileName);
		Base64.Decoder decoder = Base64.getMimeDecoder();
		InputStream dis = decoder.wrap(fis);
		Oid oid = new Oid(dis);
		byte[] der = oid.getDER();
		for (byte d : der) {
			System.out.println((char) d);
		}
		
		/*
		FileOutputStream fos = new FileOutputStream("dst_GlobalSign.crt");
		byte[] dst;
		String src;
		int i = 0;
		while (scan.hasNext()) {
			i++;
			src = scan.next();
			//System.out.println(i + ") " + src);
			if (i > 2 && i < 23) {
				dst = decoder.decode(src);
				//String dstStr = new String(dst);
				String[] ch = new String[dst.length];
				System.out.print(i + ", " + dst.length + ") ");
				for (int j=0; j<dst.length; j++) {
					ch[j] = String.format("%02x", dst[j]);
					System.out.print(ch[j] + " ");
				}
				System.out.println();
				
				fos.write(dst);
			}
		}
		scan.close();
		fos.flush();
		fos.close();
		byte[] src = new byte[fis.available()];
		fis.read(src);
		fis.close();
		Base64.Decoder decode = Base64.getDecoder();
		byte[] dst = decode.decode(src);
		FileOutputStream fos = new FileOutputStream("dst_"+fileName);
		fos.write(dst);
		fos.flush();
		fos.close();

		String srcStr = new String(src);
		String bgnStr = "-----BEGIN CERTIFICATE-----";
		String endStr = "-----END CERTIFICATE-----";
		int bgn = srcStr.indexOf(bgnStr) + bgnStr.length() + 2;
		int end = srcStr.indexOf(endStr) - 2;
		//System.out.println(bgnStr);
		//System.out.println(srcStr.substring(bgn, end));
		//System.out.println(endStr);
		src = (srcStr.substring(bgn, end)).getBytes();
		*/
	}
}
