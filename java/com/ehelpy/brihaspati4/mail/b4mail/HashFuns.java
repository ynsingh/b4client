package com.ehelpy.brihaspati4.mail.b4mail;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class HashFuns {

    public static String getMD5(File eml) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (InputStream is = Files.newInputStream(Paths.get(String.valueOf(eml)));
             DigestInputStream dis = new DigestInputStream(is, md)) {
            /* Read decorated stream (dis) to EOF as normal... */
        }
        //byte[] digest = md.digest();
        //String MD5hash = new String(md.digest(), StandardCharsets.UTF_8);
        String MD5hash = Base64.getEncoder().encodeToString(md.digest());
        return MD5hash;
    }


    public static void MatchHashFromEmailHeader(Message message) throws MessagingException, IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        message.removeHeader("X-Mail-hash");
        try (OutputStream out = new FileOutputStream("Send1.eml")) {
            message.writeTo(out);
        }
        String fileHash1 = getMD5(new File("Send1.eml"));
        System.out.println(fileHash1);
        byte[] hashInBytes1 = checksum("Send1.eml", md);
        System.out.println(bytesToHex(hashInBytes1));
        // Send message
        //Transport.send(message);
        System.out.println("Sent message successfully....");
    }

    public static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    public static byte[] checksum(String filepath, MessageDigest md) throws IOException {

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        return md.digest();

    }
}