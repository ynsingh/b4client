package com.ehelpy.brihaspati4.ReplicaMgt;

import com.ehelpy.brihaspati4.comnmgr.Sender;
import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;
import com.ehelpy.brihaspati4.Encryption.GenerateKeys;
import com.ehelpy.brihaspati4.Encryption.Hash;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static com.ehelpy.brihaspati4.DbaseAPI.file.*;
import static com.ehelpy.brihaspati4.Encryption.Encrypt.concat;
import static com.ehelpy.brihaspati4.Encryption.Hash.hashgenerator;
import static com.ehelpy.brihaspati4.XMLhandler2.XMLWriter.writer;

public class Replicate {
    /**
     * Stores the replicated content
     * <p> This method executes all the functions related to storing
     * of a segment after verifying signed hash within the segment
     * This method runs at the root node</p>
     * @param replica replica of segment data including signed hash
     * @param hashedInode hashed segment inode (DFS://emailID//segmentpath)
     * used as the file name for storing into the local disk
     * @throws NoSuchAlgorithmException used in hashing if no algorithm found as specified
     * @throws IOException for input output exception
     * @throws SignatureException exception related to Signed Hash
     * @throws InvalidKeyException Key provided for hashing is not valid
     * @throws InvalidKeySpecException KeySpec provided is not valid
     */
    public static void start(byte[] replica, String hashedInode)
            throws IOException, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException, InvalidKeySpecException {
        // get data from XML Reader and parse with type 1 to get signed hash
        byte[] signedHash = TLVParser.startParsing(replica, 1);
        //retrieve the encrypted file by removing the length of signed hash + tag + length
        byte[] encFile = deconcat(replica,signedHash.length+8);
        //compute the hash
        String hashComputed = Hash.hashgenerator(encFile);
        // verify the hash against signed hash
        boolean match = GenerateKeys.verifyHash(hashComputed.getBytes(),signedHash);
        // when the hash matches write the segment data with segment inode as filename
        if (match) {
            writeData(encFile, hashedInode);//
            index1(hashedInode,"Replica_index.csv");
        } else
            System.out.println("hash mismatch during replication. Replication failed");
        // monitor the status of root node
        boolean liveStatus = false;
        //while(true){
            liveStatus = Monitor.statusCheck("localhost");
            // if root node not alive then republish the hash
            if(!liveStatus)
                republish(encFile, hashedInode);
        //}
    }//end of start
    public static void republish(byte[] encFile,String hashedInode)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, InvalidKeyException, SignatureException {
        // TODO - Republish
        //compute the hash of segment
        String hashofSegment = hashgenerator(encFile);
        //Sign the hash
        byte[] signedHash = GenerateKeys.signHash(hashofSegment.getBytes());
        //get the file ready to transmit after adding signed hash into the segment
        byte[] fileTx = concat(signedHash,encFile);// combine segment data and signed hash
        // Write the XML query. Tag for upload is 1
        String xmlPath = writer(1,hashedInode,fileTx);
        // handover the xml query to xmlSender (token for upload is 1)
        // TODO - query the dht and get the IP from B4
        File f;
        try{
            Sender.start(xmlPath,"localhost");
            f = new File(xmlPath);
            f.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
