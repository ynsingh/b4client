package com.ehelpy.brihaspati4.DbaseAPI;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.ehelpy.brihaspati4.DbaseAPI.file.*;

public class Segmentation {

    private static final String dir = System.getProperty("user.dir") +
            System.getProperty("file.separator");
    private static final String suffix = ".splitPart";
    static String iNode = null;
    static String nameOfFile = null;

    public static void start(byte[] filePlusKey,String path) throws IOException {
        //new File(dir+"upload").mkdir();
        String writePath = path+ "DFS2";
        writeData(filePlusKey,writePath);
        splitFile(path,writePath,512);
    }
    /**
     * Split a file into multiples files.
     *
     * @param tempPath  Name of file to be split.
     * @param kbPerSplit number of kilo bytes per chunk.
     * @throws IOException
     */
    public static void splitFile(String inode,final String tempPath,final int kbPerSplit) throws IOException {
        File f;

        f = new File(inode);
        nameOfFile = f.getName();
        iNode = inode;

        if (kbPerSplit <= 0)
            throw new IllegalArgumentException("chunkSize must be more than zero");

        List<Path> partFiles = new ArrayList<>();
        final long sourceSize = Files.size(Paths.get(tempPath));
        final long bytesPerSplit = 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;
        try (RandomAccessFile sourceFile = new RandomAccessFile(tempPath, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++)
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, partFiles);

            if (remainingBytes > 0)
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, partFiles);
        }
        //Delete the temporary encrypted file
        f = new File(tempPath);
        f.delete();
        //return partFiles;
    }

    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel,
                                        List<Path> partFiles) throws IOException {
        Path segmentName = Paths.get(dir + nameOfFile+suffix + (int) ((position / (512 * 1024)) + 1));//TODO - replace the UUID with Integer.toString((position/512) - 1))
        try (RandomAccessFile toFile = new RandomAccessFile(segmentName.toFile(), "rw");
             FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
        partFiles.add(segmentName);
        splitIndex(iNode, segmentName.toString());
    }
    /**
     * Split a file into multiples files.
     *
     * @param inode Name of file to be split.
     * @param segmentName number of kilo bytes per chunk.
     * @throws IOException
     */
    public static void splitIndex(String inode, String segmentName){
        HashMap<String,String> index = new HashMap<>();
        // Put elements to the map
        index.put(inode, segmentName);// Put elements to the map
        String fileName = "SplitIndex.csv";
        /* Write CSV */
        try {
            String uploadPath = System.getProperty("user.dir") +
                    System.getProperty("file.separator") + fileName;
            // true is for appending and false is for over writing
            FileWriter writer = new FileWriter(uploadPath, true);
            Set set = index.entrySet();
            // Get an iterator
            for (Object o : set) {
                Map.Entry firstEntry = (Map.Entry) o;
                writer.write(firstEntry.getKey().toString());
                writer.write(",");//Explore how to write key and value in different fields
                writer.write(firstEntry.getValue().toString());
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
