package com.ehelpy.brihaspati4.mail.b4mail.sending;

import com.ehelpy.brihaspati4.mail.b4mail.SendImapMail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FragmentFile {
    /*
    private static final Logger bmaillog = LogManager.getLogger(FragmentFile.class);
    //Following function split a file by block size
    //https://www.admios.com/blog/how-to-split-a-file-using-java
    public static List<Path> b4mail.splitFile(final String fileName, final int kbPerSplit) throws IOException {

        if (kbPerSplit <= 0) {
            throw new IllegalArgumentException("KB per Split must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        File file2Split= new File(fileName);
        final long sourceSize =file2Split.length();
        //final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1L * 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;
        System.out.println("File size= "+sourceSize+" bytesPerSplit= "+bytesPerSplit+" number of split="+numSplits);
        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }
        }
        return partFiles;
    }private static final Logger bmaillog = LogManager.getLogger(SendImapMail.class);
    //Following function split a file by block size
    //https://www.admios.com/blog/how-to-split-a-file-using-java
    public static List<Path> b4mail.splitFile(final String fileName, final int kbPerSplit) throws IOException {

        if (kbPerSplit <= 0) {
            throw new IllegalArgumentException("KB per Split must be more than zero");
        }

        List<Path> partFiles = new ArrayList<>();
        File file2Split= new File(fileName);
        final long sourceSize =file2Split.length();
        //final long sourceSize = Files.size(Paths.get(fileName));
        final long bytesPerSplit = 1L * 1024L * kbPerSplit;
        final long numSplits = sourceSize / bytesPerSplit;
        final long remainingBytes = sourceSize % bytesPerSplit;
        int position = 0;
        System.out.println("File size= "+sourceSize+" bytesPerSplit= "+bytesPerSplit+" number of split="+numSplits);
        try (RandomAccessFile sourceFile = new RandomAccessFile(fileName, "r");
             FileChannel sourceChannel = sourceFile.getChannel()) {

            for (; position < numSplits; position++) {
                //write multipart files.
                writePartToFile(bytesPerSplit, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }

            if (remainingBytes > 0) {
                writePartToFile(remainingBytes, position * bytesPerSplit, sourceChannel, partFiles, position, fileName, numSplits);
            }
        }
        return partFiles;
    }

    // Following function is called from function b4mail.splitFile to write blocks of data to a file with extenstion .P1 .P2 etc.
    private static void writePartToFile(long byteSize, long position, FileChannel sourceChannel, List<Path> partFiles, int pos, String fn, long ns) throws IOException {
        //Path fileName = Paths.get(dir + UUID.randomUUID() + suffix);
        String suffix = ".P" + pos + "-" + ns;
        //System.out.println("fn is "+fn);
        Path nFileName = Paths.get(fn + suffix);
        try (RandomAccessFile toFile = new RandomAccessFile(nFileName.toFile(), "rw");
             FileChannel toChannel = toFile.getChannel()) {
            sourceChannel.position(position);
            toChannel.transferFrom(sourceChannel, 0, byteSize);
        }
        partFiles.add(nFileName);
    }

*/

}
