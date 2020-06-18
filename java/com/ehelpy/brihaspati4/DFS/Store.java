package com.ehelpy.brihaspati4.DFS;

import com.ehelpy.brihaspati4.DbaseAPI.TLVParser;
import com.ehelpy.brihaspati4.Encryption.GenerateKeys;
import com.ehelpy.brihaspati4.Encryption.Hash;
import com.ehelpy.brihaspati4.ReplicaMgt.InitReplicate;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static com.ehelpy.brihaspati4.DbaseAPI.file.*;
import static com.ehelpy.brihaspati4.XMLhandler2.XMLWriter.writer;

/**
 * Class responsible for storing the uploaded data.
 * This class stores the segment of uploaded file
 * into the local disk through database package
 * <p><b>Functions:</b> At the cloud end of DFS</p>
 * <b>Note:</b> Change this file to change functionality
 * related to the storing function
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since   15th Feb 2020
 */
public class Store {
    /**
     * Starts the store process
     * <p> This method executes all the functions related to storing
     * of a segment after verifying signed hash within the segment
     * This method runs at the root node</p>
     * @param dataInbound segment data including signed hash
     * @param hashedInode hashed segment inode (DFS://emailID//segmentpath)
     * used as the file name for storing into the local disk
     * @throws NoSuchAlgorithmException used in hashing if no algorithm found as specified
     * @throws IOException for input output exception
     * @throws SignatureException exception related to Signed Hash
     * @throws InvalidKeyException Key provided for hashing is not valid
     * @throws InvalidKeySpecException KeySpec provided is not valid
     */
    public static void start(byte[] dataInbound, String hashedInode)
            throws IOException, NoSuchAlgorithmException, SignatureException,
            InvalidKeyException, InvalidKeySpecException {
        //TODO - receive the inode and hash from Xml handler with request to store
        //retrieve the signed hash and encrypted File
        byte[] signedHash = TLVParser.startParsing(dataInbound, 1);
        //retrieve the encrypted file by removing the length of signed hash + tag + length
        byte[] encFile = deconcat(dataInbound,signedHash.length+8);
        //compute the hash
        String hashComputed = Hash.hashgenerator(encFile);
        // verify the hash against signed hash
        boolean match = GenerateKeys.verifyHash(hashComputed.getBytes(),signedHash);
        // when the hash matches write the segment data with segment inode as filename
        if (match) {
            writeData(encFile, hashedInode);//
            index1(hashedInode,"root_index.csv");
            String xmlPath = writer(4,hashedInode,dataInbound);
            //TODO - retrieve the ip of next node for replication
            InitReplicate.initReplicate(encFile,hashedInode);
        } else
          System.out.println("hash mismatch cant store file");
    }//end of start
}
