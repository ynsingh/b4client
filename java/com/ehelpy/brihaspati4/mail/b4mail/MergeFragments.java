package com.ehelpy.brihaspati4.mail.b4mail;

//import org.apache.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.ehelpy.brihaspati4.mail.b4mail.HashFuns.getMD5;
import static com.ehelpy.brihaspati4.mail.b4mail.folders.EmailFileName.deleteFile;
import static com.ehelpy.brihaspati4.mail.b4mail.HashFuns.getMD5;

public class MergeFragments {
    private static final Logger bmaillog = LogManager.getLogger(MergeFragments.class);

    // merge will be done at recipient end, it checks for all file fragments if received then merge
    // after merging the file wil be kept in new folder of INBOX e.g. /home/sm/B4/P2P/Maildir/new
    public static void mergeFiles(String srcDir, String destFile) throws IOException, NoSuchAlgorithmException {
        //This will merge files and save to INBOX
        bmaillog.trace("merging file from" + srcDir + " To file" + destFile);
        File directoryPath = new File(srcDir);
        File filesList[] = directoryPath.listFiles();
        int dirLen=filesList.length;
        String files=filesList[0].getName(); //get first file name
        int extractedLen=Integer.parseInt(files.substring(files.lastIndexOf("-") + 1)); // get the last number which indicates total files
        //System.out.println(extractedLen);
        if(extractedLen==dirLen-1) {
            bmaillog.trace("number of files in " + srcDir + " is equal to" +dirLen+" and file range is from 0-"+extractedLen);
            Scanner sc = null;
            FileWriter writer = new FileWriter(destFile);
            for (File file : filesList) {
                sc = new Scanner(file);
                String input;
                //      StringBuffer sb = new StringBuffer();
                while (sc.hasNextLine()) {
                    input = sc.nextLine();
                    writer.append(input + "\n");
                }
                writer.flush();
            }
            System.out.println("File merged");
            String fileHash1 = getMD5(new File(destFile));
            System.out.println(fileHash1);
        }
        else {
            bmaillog.trace("Failure while merging files from folder " + srcDir + ". Mismatch: files in dir is " +dirLen+" and expected is "+extractedLen);
            System.out.println("Failure while merging files from folder " + srcDir + ". Mismatch: files in dir is " +dirLen+" and expected is "+extractedLen);
        }
        deleteFile(new File(srcDir));   //delete the temp directory
    }



}
