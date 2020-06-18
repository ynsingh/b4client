package com.ehelpy.brihaspati4.DFS;

import com.ehelpy.brihaspati4.comnmgr.Sender;
import com.ehelpy.brihaspati4.DbaseAPI.Reassembly;
import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;
import com.ehelpy.brihaspati4.Encryption.Encrypt;
import com.ehelpy.brihaspati4.Encryption.Hash;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.ehelpy.brihaspati4.authenticate.emailid;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.GiveNextHop;

import static com.ehelpy.brihaspati4.DbaseAPI.Reassembly.stichfiles;
import static com.ehelpy.brihaspati4.DbaseAPI.file.*;
import static com.ehelpy.brihaspati4.Encryption.Hash.comparehash;
import static com.ehelpy.brihaspati4.XMLhandler2.XMLWriter.writer;

/**
 * Class responsible for performing Download.
 * This class downloads the file from the DFS.
 * <p><b>Functions:</b> At the user end of DFS.</p>
 * <b>Note:</b> Change this file to change functionality
 * related to Download.
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since   15th Feb 2020
 */
public class Download{
    static String fileName = null;
    static int segmentCount = 0;
    /**
     * Starts the download process.
     * This method starts the download of the file from DFS.
     * It interacts with the communication manager on DFS behalf for
     * download.
     * @param inode inode of the file that user selected
     * @throws IOException for input output exception
     * @throws GeneralSecurityException In case of general security violation occurs
     */
    public static void start(String inode) throws IOException, GeneralSecurityException {
        //get the hash of inode downloaded
        fileName = inode;
        // get path to splitindex.csv and retrieve the segment inodes
        String splitFile = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "SplitIndex.csv";
        String[] segmentInode = csvreader(splitFile,inode);
        //Querry the DFS with each segment inode for downloading them
        for(int i = 0;i< segmentInode.length  && !(segmentInode[i]==null);i++){
            // get own email
        	String email = emailid.getemaild();
			String hashedInode = Hash.hashpath("DFS://"+email+segmentInode[i]);
            // xml query  with inode.tag for download is 2
            //the data filed is blank hence "Nothing" to avoid null pointer exception
            String xmlPath = writer(2,hashedInode,"Nothing".getBytes());
            // retrieve the Ip of the node responsible
            String nodeId = GiveNextHop.NextHop(hashedInode);
			String IP = CommunicationUtilityMethods.getIpFromMyIpTable(nodeId);
            // hand over the xml query to xmlSender
            Sender.start(xmlPath, IP);
        }
    }//end of start
    /**
     * This method receives the downloaded segments by interacting with
     * the communication manager and processes them further.
     * This method executes all the functions related to  segment
     * downloading. It downloads the segments and sends them
     * for sequencing and stitching up to derive the complete filepluskey.
     * @param inbound byte array received from the communication manager
     * @throws IOException for input output exception
     * @throws GeneralSecurityException In case of general security violation occurs
     */
    public static void segmentDownload(byte[] inbound) throws GeneralSecurityException, IOException {

        // send segments for reassembly
        Reassembly.start(inbound,fileName);
        String splitFile = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "SplitIndex.csv";
        String[] segmentInode = csvreader(splitFile,fileName);
        // increment segment count on receiving each segment
        segmentCount++;
        System.out.println(+segmentCount+" files have been downloaded");
        // if all segments have been downloaded then start stitching them
        if(segmentCount == segmentInode.length)
             stichfiles(fileName,segmentCount);
        else
             System.out.println("Download in progress");
    }
    /**
     * receives the complete filepluskey after stitching
     * from reassembly. This method checks up the completefile for
     * integrity by comparing hash with the hash stored in uploaded.csv.
     * @param completefile this is the filepluskey for further processing
     * @throws IOException for input output exception
     * @throws GeneralSecurityException In case of general security violation occurs
     */

    public static void postDownload(byte[] completefile) throws GeneralSecurityException, IOException {

        // compare the hash of completefile with the original filepluskey
        boolean hashMatch = comparehash(fileName,completefile);
        // retrieve data, decrypt data after decrypting key
        // write the file on decrypting data
        if(hashMatch){
            // if the hash matches then retrieve encrypted key
            byte[] encKey = TLVParser.startParsing(completefile, 2);
            // retrieve framed data
            byte[] serialisedEncData = deconcat(completefile,encKey.length+8);
            // retrieve encrypted data after parsing the TLV
            byte[] encData = TLVParser.startParsing(serialisedEncData, 3);
            // get decrypted data
            byte[] data = Encrypt.startDec(encData,encKey);
            // write the data to original inode for the user
            writeData(data,fileName);
        }
        else
            System.out.println("hash mismatch");
        System.out.println("THe Download is complete");
    }
}
