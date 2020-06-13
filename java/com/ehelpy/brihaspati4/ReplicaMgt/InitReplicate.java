package com.ehelpy.brihaspati4.ReplicaMgt;

import com.ehelpy.brihaspati4.comnmgr.Sender;
import com.ehelpy.brihaspati4.Encryption.GenerateKeys;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import static com.ehelpy.brihaspati4.Encryption.Encrypt.concat;
import static com.ehelpy.brihaspati4.Encryption.Hash.hashgenerator;
import static com.ehelpy.brihaspati4.XMLhandler2.XMLWriter.writer;

public class InitReplicate {
    /**
     * Starts the replication process
     * <p> This method computes the hash of encFile in Store.start()
     * signs it using the pvt key and sends this replica to successor.
     * This method runs at the root node</p>
     * @param encFile segment data including signed hash
     * @param hashedInode hashed segment inode (DFS://emailID//segmentpath)
     * used as the file name for storing into the local disk
     * @throws NoSuchAlgorithmException used in hashing if no algorithm found as specified
     * @throws IOException for input output exception
     * @throws SignatureException exception related to Signed Hash
     * @throws InvalidKeyException Key provided for hashing is not valid
     * @throws InvalidKeySpecException KeySpec provided is not valid
     */
    public static void initReplicate(byte[] encFile, String hashedInode)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, SignatureException {
        //compute the hash of segment to be replicated
        String hashofSegment = hashgenerator(encFile);
        //Sign the hash
        byte[] signedHash = GenerateKeys.signHash(hashofSegment.getBytes());
        //get the file ready to transmit to successor after adding signed hash into the segment
        byte[] replica = concat(signedHash,encFile);// combine the encfile and signed hash
        // package the replica into xml
        try {
            String xmlPath = writer(4,hashedInode,replica);
            //TODO - retrieve the ip of next node for replication
            Sender.start(xmlPath,"localhost");
            File f = new File(xmlPath);
            f.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean liveStatus = false;
        //.while(true){
            liveStatus = Monitor.statusCheck("localhost");
            if(!liveStatus)
             contactSuccessor(replica,hashedInode);
        //}
    }//end of initReplicate
    public static void contactSuccessor(byte[] replica,String hashedInode){
        //  create xml
        try {
            initReplicate(replica,hashedInode);
        } catch (IOException | NoSuchAlgorithmException
                | InvalidKeySpecException | InvalidKeyException
                | SignatureException e) {
            e.printStackTrace();
        }
        // TODO- enclose this logic in a thread
    }
}
