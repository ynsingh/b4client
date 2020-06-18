package com.ehelpy.brihaspati4.Encryption;


import com.ehelpy.brihaspati4.DFS.Locate;

import java.io.*;

import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <b>Hash - Computes hash using SHA-256 and returns hash in various formats</b><br>
 * hashCalc - accepts String/byte array and returns hash in String/byte array format respectively <br>
 * hashCalc1 - accepts String and returns the hash in byte array
 * <p>
 * NOTE - algorithms to implement different hashing functions
 * <p>
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since 13th Feb 2020
 */
public class Hash {
    /**
     * Use this function to generate hash of the string "DFS://email ID/filepath" in String form.
     * @param key String whose hash has to be computed.
     * @return the hash computed in String format hashValue.
     * @throws NoSuchAlgorithmException if no Provider supports implementation for the specified algorithm.
     */
    public static String hashpath (String key) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashInBytes = md.digest(key.getBytes(StandardCharsets.UTF_8));
        // bytes to hex
        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    /**
     * Use this function to generate MAC digest of a single block of message.
     * @param fileData file for which digest has to be created.
     * @return resulting hash value hashInBytes.
     * @throws NoSuchAlgorithmException if no Provider supports a MacSpi implementation for the specified algorithm.
     * @throws IOException if input/ouput exception is encountered.
     **/
     public static String hashgenerator (byte[] fileData)throws IOException, NoSuchAlgorithmException{

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        InputStream targetStream = new ByteArrayInputStream(fileData);
        byte[] bytesBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = targetStream.read(bytesBuffer)) != -1) {
            digest.update(bytesBuffer, 0, bytesRead);
        }
        byte[] hashedBytes = digest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < hashedBytes.length; i++) {
            stringBuffer.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
    /**
     * Use this class to do the post download processing.
     * @param fileName the fileName used to retrieve hash of file from index
     * @param fileData byte array with key and file data.
     */
    public static boolean comparehash(String fileName,byte[] fileData) {
        String hashOfFile = null;
        boolean result ;
        // compute hash and check integrity
        try{
            hashOfFile = Hash.hashgenerator(fileData);
        }catch (Exception e){
            e.printStackTrace();
        }
        String index = System.getProperty("user.dir") +
                System.getProperty("file.separator") + "uploaded.csv";
        String retrievedValue = Locate.csvreader(index,fileName);
        assert hashOfFile != null;
        return hashOfFile.equals(retrievedValue);
    }
}
