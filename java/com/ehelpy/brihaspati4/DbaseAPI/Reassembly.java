package com.ehelpy.brihaspati4.DbaseAPI;

import java.io.*;
import java.security.GeneralSecurityException;

import com.ehelpy.brihaspati4.DFS.Download;
import com.ehelpy.brihaspati4.Encryption.Encrypt;

import static com.ehelpy.brihaspati4.DbaseAPI.file.*;
import static com.ehelpy.brihaspati4.DbaseAPI.file.csvreader;

public class Reassembly {

    private static final String dir = System.getProperty("user.dir") +
            System.getProperty("file.separator");
    public static void start(byte[] inbound,String fileName) throws IOException {
        sequencing(inbound,fileName);
    }
    /**
     * Split a file into multiples files.
     *
     * @param fileName  Name of file to be split.
     * @param inbound number of kilo bytes per chunk.
     * @throws IOException
     */
    public static void sequencing(byte[] inbound,String fileName) throws IOException {

        int sequenceNo = 0;
        byte[] sequenced = TLVParser.startParsing(inbound, 4);
        if(sequenced[1]==0 && sequenced[2]==0 &&sequenced[3]==0)
            sequenceNo = sequenced[0];
        //byte[] data = TLVParser.startParsing(sequenced,4);
        byte[] data = deconcat(inbound,16);
        String splitFile = dir + "SplitIndex.csv";
        String[] segmentInode = csvreader(splitFile,fileName);
        writeData(data,segmentInode[sequenceNo-1]);
    }
    /**
     * Split a file into multiples files.
     *
     * @param fileName  Name of file to be split.
     * @param fileCount number of kilo bytes per chunk.
     * @throws IOException
     */
     public static void stichfiles (String fileName,int fileCount) throws IOException, GeneralSecurityException {

        String[] segmentInode = csvreader(dir + "SplitIndex.csv",fileName);
        //String mergedFilePath = fileName+"DFS";
        //File mergedFile = new File(mergedFilePath);
        byte[] completeFile = new byte[0];
        for(int i =0;i<segmentInode.length  && !(segmentInode[i]==null);i++){
            byte[] segmentData = readdata(segmentInode[i]);
            completeFile = Encrypt.concat(completeFile,segmentData);
            File f;
            f = new File(segmentInode[i]);
            f.delete();
        }
        Download.postDownload(completeFile);
    }
}
