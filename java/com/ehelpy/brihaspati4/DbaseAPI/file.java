package com.ehelpy.brihaspati4.DbaseAPI;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class file {
    /**
     * Use this function to get path of the file selected in java.
     * @return path of the file that is to be uploaded into the DHT.
     */
    public static String readpath() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);
        String path = null;
        // int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = selectedFile.getAbsolutePath();
        }
        return path;
    }
    /**
     * Use this function to read a file specified by user.
     * @param path of the file to be read.
     * @return the content in form of byte array after reading the file.
     */
    public static byte[] readdata(String path){
        Path file_path = Paths.get(path);
        byte[] data = null;
        try {
            data = Files.readAllBytes(file_path);
            //System.out.println(new String (data));
        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }
    /**
     * Use this function to create a .CSV file for indexing the file uploaded into DFS.
     * @param data inode in String format.
     * @param fileName the computed hash of file to check integrity.
     */
    //Replace with method in Dbase api
    public static void writeData(byte[] data, String fileName){
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Use this function to create a .CSV file for indexing the file uploaded into DFS.
     * @param inode inode in String format.
     * @param fileName the computed hash of file to check integrity.
     */
    public static String[] csvreader(String fileName,String inode) throws IOException {

        Stream<String> fileStream = Files.lines(Paths.get(fileName));
        int noOfLines = (int) fileStream.count();
        String line;
        String cvsSplitBy = ",";
        String[] localPath = new String[noOfLines];
        int j = 0;
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        for (;(line = br.readLine()) != null;){
            String[] record = line.split(cvsSplitBy);
            if(record[0].equals(inode)){
                localPath[j] = record[1];
                j++;
            }
        }
        int pos =0;
        for(String value : localPath){
            if(value != null)  pos++;
            else  break;
        }
        String[] temp = new String[pos];
        System.arraycopy(localPath,0,temp,0,pos);
        return temp;
    }
    /**
     * Use this function to concatenate two byte arrays.
     * @param dataPlusExtra byte array containing data and hash of inode as byte array.
     * @return deConcat concatenated byte array.
     */
    public static byte[] deconcat (byte[] dataPlusExtra, int removeLength){

        byte[] fileData = new byte[dataPlusExtra.length - removeLength];
        System.arraycopy(dataPlusExtra,removeLength,fileData,0,fileData.length);
        return fileData;
    }
    /**
     * <h1>index </h1>
     * creates the index at the root node
     * name of index file is "root_index.csv"
     * <p>
     * This method creates an index with entries of segment inode and
     * local path where the segment data is stored
     * </p>
     * @param indexInode segment inode acts as the primary key here
     */
    public static void index1(String indexInode, String indexName){
        HashMap<String, String> index = new HashMap<>();
        String indexPath = System.getProperty("user.dir") +
                System.getProperty("file.separator") + indexInode;
        // Put elements to the map
        index.put(indexInode, indexPath);// Put elements to the map
        /* Write CSV */
        try {
            // true is for appending and false is for over writing
            FileWriter writer = new FileWriter(indexName, true);
            Set set = index.entrySet();
            // Get an iterator
            Iterator i = set.iterator();
            while(i.hasNext()) {
                Map.Entry firstEntry = (Map.Entry)i.next();
                writer.write(firstEntry.getKey().toString());
                writer.write(",");
                writer.write(firstEntry.getValue().toString());
                writer.write("\n");
                //System.out.print(firstEntry.getKey() + " : ");
                //System.out.println(firstEntry.getValue());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * <h1>index </h1>
     * creates the index at the user node
     * name of index file is "uploaded.csv"
     * <p>
     * This method creates an index with entries of file inode and
     * hash of file. The hash will be used to verify integrity post
     * download the file
     * </p>
     * @param inode inode of file acts as the primary key here
     * @param hashOfFile will be used to store hash of file for comparision
     * when the file is downloaded
     */
    public static void index(String inode, String hashOfFile){
        HashMap<String,String> index = new HashMap<>();
        // Put elements to the map
        index.put(inode, hashOfFile);// Put elements to the map
        String fileName = "uploaded.csv";
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
    public static void DeleteRecord(String inode, String fileName) throws IOException {
        Scanner strInput =  new Scanner(inode);
        String ID, record;

        File temp = new File("temp.csv");
        File permt = new File(fileName);

        BufferedReader br = new BufferedReader( new FileReader( permt) );
        BufferedWriter bw = new BufferedWriter( new FileWriter( temp ) );

        ID =  strInput.nextLine();
        while( ( record = br.readLine() ) != null ) {
            if( record.contains(ID) )
                continue;

            bw.write(record);
            bw.flush();
            bw.newLine();
        }
        br.close();
        bw.close();
        permt.delete();
        temp.renameTo(permt);
    }

}
