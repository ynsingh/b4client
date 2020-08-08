package com.ehelpy.brihaspati4.mail.b4mail.folders;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Enumeration;

import static com.ehelpy.brihaspati4.mail.b4mail.MergeFragments.mergeFiles;
import static com.ehelpy.brihaspati4.mail.b4mail.folders.FolderConstants.*;
import static com.ehelpy.brihaspati4.mail.b4mail.splitFile.splitFile;

interface FileNameGlobalConstants
{
    String tempFileName="Send.eml";
    boolean flag1=false;
    boolean flag2=false;
    boolean flag3=false;
    boolean flag4=false;

    //boolean[] arr = {false, false, false, false};
}

public class EmailFileName {
    static long start = System.nanoTime(); // used to calculate time in filename
    //Function which decides file naming before storing a file
    public static String getFileNameFormat(String oFileName) throws IOException {
        long ut1 = Instant.now().getEpochSecond();
        String epoch = Long.toString(ut1);
        String creationT = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //  System.out.println(epoch);
        File file = new File(oFileName);
        if (!file.exists() || !file.isFile()) return epoch;
        //System.out.println((double)file.length()/1024); // inKB, for fragment calculation,

        String localIP;
        System.out.println(getSysIP());
        if (getSysIP()==null){
             localIP="localhost";
        }else{
             localIP= getSysIP();
        }
        long end = System.nanoTime();
        long nanoseconds = end - start;
        //System.out.println(nanoseconds);
        String filename = epoch +"-"+file.length()+"-"+creationT+"-" + nanoseconds +"-" +localIP + "-" + booleanToInt(FileNameGlobalConstants.flag1) + booleanToInt(FileNameGlobalConstants.flag2) + booleanToInt(FileNameGlobalConstants.flag3) + booleanToInt(FileNameGlobalConstants.flag4);
        //System.out.println("File in class"+filename);

        return filename;
    }

    public static void getFileNameforSending(String oFileName) throws IOException {
        String filename=getFileNameFormat(oFileName); //passing the filename to note file size in naming
        //renameFile(oFileName,tmpoutboxpath+filename);
        copyFile(oFileName,newoutboxpath+filename);
        splitFile(oFileName, 512);
        File source= new File(oFileName);
        deleteFile(source);
    }
    //while receiving we receive with filename.P* into tmp,
    // we use filename to assemble, then move it to inbox new with new filename
    //we may return some thing for indexing
    public static void getFileNameWhileReceiving(String oFileName) throws IOException, NoSuchAlgorithmException {
        String filename=getFileNameFormat(oFileName);
        mergeFiles(tmpInboxpath, oFileName);
        renameFile(oFileName,newInboxpath+filename);
    }
    public static void renameFile(String ofname, String nfname){
        File ofile= new File(ofname);
        File nfile= new File(nfname);
        if(ofile.renameTo(nfile)){
            System.out.println("Rename successful");
        }else{
            System.out.println("Rename failed");
        }

    }

    private static void copyFile(String ofname, String nfname) throws IOException {
        File source= new File(ofname);
        File dest= new File(nfname);
        Files.copy(source.toPath(), dest.toPath());
    }

    //Following function converts a boolean to int i.e FALSE=0, TRUE=1
    public static int booleanToInt(boolean value) {
        // Convert true to 1 and false to 0.
        return value ? 1 : 0;
    }

    public static void deleteFile(File element) {
        if (element.isDirectory()) {
            for (File sub : element.listFiles()) {
                deleteFile(sub);
            }
        }
        element.delete();
    }
    public static String getSysIP(){
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                if (!ni.isUp() || ni.isLoopback() || ni.isVirtual()) continue;  //check if interface is up
                //else System.out.println("Internet not working");
                Enumeration<InetAddress> niv4 = ni.getInetAddresses();
                while(niv4.hasMoreElements()) {
                    InetAddress ia=  niv4.nextElement();

                    if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                        return ia.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("unable to get current IP " + e.getMessage());
            //LOG.error("unable to get current IP " + e.getMessage(), e);
        }
        return null;
    }
}
