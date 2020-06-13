package com.ehelpy.brihaspati4.DFS;

import java.io.File;
import java.io.IOException;

import static com.ehelpy.brihaspati4.DbaseAPI.file.DeleteRecord;
/**
 * Class responsible for performing Delete from local disk
 * This class deletes the file from local disk on request from the user
 * <p><b>Functions:</b> At the cloud end of DFS.</p>
 * <b>Note:</b> Change this file to change functionality
 * related to Erasure from local disk
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since   15th Feb 2020
 */
public class Erase {
    /**
     * Starts the Erasure process.
     * This method starts the Erasing from local disk on receiving
     * XML query from user requesting a delete
     * @param inode inode of the file that user selected
     */
    public static void start(String inode){
        String path = System.getProperty("user.dir") + System.getProperty("file.separator")
                + "root_index.csv";
        //read the index and retrieve the local path
        String localPath = Locate.csvreader(path,inode);
        // delete the segment using the local path
        try {
            File f;//file to be delete
            f = new File(localPath);
            if(f.delete()) //returns Boolean value
                System.out.println(" deleted");//getting and printing the file name
            else
                System.out.println("failed");
        } catch(Exception e){
            e.printStackTrace();
        }
        String fileName = "root_index.csv";
        // update index
        try {
            DeleteRecord(inode,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
