package com.ehelpy.brihaspati4.DFS;

import com.ehelpy.brihaspati4.comnmgr.Sender;
import com.ehelpy.brihaspati4.DbaseAPI.Segmentation;
import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;
import com.ehelpy.brihaspati4.Encryption.Encrypt;
import com.ehelpy.brihaspati4.Encryption.GenerateKeys;
import com.ehelpy.brihaspati4.Encryption.Hash;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.ehelpy.brihaspati4.DbaseAPI.file.*;
import static com.ehelpy.brihaspati4.Encryption.Encrypt.concat;
import static com.ehelpy.brihaspati4.Encryption.Hash.hashgenerator;
import static com.ehelpy.brihaspati4.XMLhandler2.XMLWriter.writer;

import com.ehelpy.brihaspati4.routingmgmt.GiveNextHop;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.authenticate.emailid;

/**
 * Class responsible for uploading a File into DFS.
 * This class gets the file selected by user and uploads it
 * into the DFS using DHT
 * <p><b>Functions:</b> At the user end of DFS</p>
 * <b>Note:</b> Change this file to change functionality
 * related to the upload function
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since 13th Feb 2020
 */
public class Upload {
	/**
	 * Starts the upload process
	 * <p> This method executes all the functions related to uploading
	 * of a file like Encryption, hashing, segmentation and indexing</p>
	 * @throws NullPointerException Incase user invokes the method but doesn't upload anything
	 * @throws IOException for input output exception
	 * @throws GeneralSecurityException In case of general security violation occurs
	 */
	public static void start() throws NullPointerException, IOException,
			GeneralSecurityException {
		// get path of the selected file
		String path = readpath();
		//read the byte array of the selected file
		byte[] data = readdata(path);
		//Encrypt the file and key and combine both using TLV framing
		byte[] filePlusKey = Encrypt.startEnc(data);
		//send the file for segmentation
		Segmentation.start(filePlusKey,path);
		//Retrieve the segments and upload them one by one
		String splitFile = System.getProperty("user.dir") + System.getProperty("file.separator")
				+ "SplitIndex.csv";
		String[] segmentInode = csvreader(splitFile,path);

		for(int i = 0;i< segmentInode.length && !(segmentInode[i]==null);i++){

			byte[] segmentData = readdata(segmentInode[i]);
			//Delete the segments once the data is read into byte array
			File f;
			f = new File(segmentInode[i]);
			f.delete();
			//insert sequence number into the segment
			byte[] segmentData1 = TLVParser.startFraming(segmentData,i+1);
			//insert tag to identify the segment as already sequenced
			byte[] segmentData2 = TLVParser.startFraming(segmentData1,4);
			//Generate the inode of segment and compute the hash of the same
			// get own email
			String email = emailid.getemaild();
			String hashedInode = Hash.hashpath("DFS://"+email+segmentInode[i]);
			//compute the hash of segment
			String hashofSegment = hashgenerator(segmentData2);
			//Sign the hash
			byte[] signedHash = GenerateKeys.signHash(hashofSegment.getBytes());
			//get the file ready to transmit after adding signed hash into the segment
			byte[] fileTx = concat(signedHash,segmentData2);// combine the file,key and hash of Inode
			// Write the XML query. Tag for upload is 1
			String xmlPath = writer(1,hashedInode,fileTx);
			// handover the xml query to xmlSender (token for upload is 1)
			// query the dht and get the IP
			String nodeId = GiveNextHop.NextHop(hashedInode);
			String IP = CommunicationUtilityMethods.getIpFromMyIpTable(nodeId);
			Sender.start(xmlPath,IP);
		}
		// compute hash of the combination of encrypted Key and data of original file
		String hashofFile = hashgenerator(filePlusKey);
		// index the hash against the original inode for comparing after
		// downloading the file from cloud. DbaseAPI.index
		index(path, hashofFile);
	}//end of start
}//end of class
