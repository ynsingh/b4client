package com.ehelpy.brihaspati4.authenticate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Created by Maj Dushyant Choudhary Dated 5th Feb 2020 ; 1500 Hrs
 *
 * This class is used to generate MD5 Hash of String and File
 * It is also used to get the MAC Address and NODE ID of Client Machine
 *
 */
public class Integrity
{
    /* This method is used to create a MD5 hash of a String
     *
     */

    public static String stringHash(String data) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = md.digest(data.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes)
        {
            sb.append(String.format("%02x", b));
        }
        //System.out.println(sb.toString());
        return sb.toString();
    }

    /* This method is used to create a MD5 hash of a File
     *
     */
    public static String fileHash(String file) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hashInBytes = checksum(file, md);
        //System.out.println(bytesToHex(hashInBytes));
        return bytesToHex(hashInBytes);

    }

    private static byte[] checksum(String filepath, MessageDigest md) throws IOException {

        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filepath), md)) {
            while (dis.read() != -1) ; //empty loop to clear the data
            md = dis.getMessageDigest();
        }

        return md.digest();

    }

    private static String bytesToHex(byte[] hashInBytes) {

        StringBuilder sb = new StringBuilder();
        for (byte b : hashInBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();

    }

    public static boolean checkKeyStoreExists()
    {
        boolean flag=false;

        File f = new File("SignedClientKeyStore.JKS");

        if(f.exists() && !f.isDirectory())
        {
            flag=true;
        }
        return flag;
    }

    /* This method is used to get Mac Address of Machine
     *
     */
    public static String getMACAddress()
    {
        String macaddr="";

        InetAddress ip;
        try {

            String ipa=com.ehelpy.brihaspati4.routingmgmt.SystemIPAddress.getSystemIP();
            System.out.println("Current IP address : " + ipa);
            NetworkInterface network = NetworkInterface.getByInetAddress(InetAddress.getByName(ipa));
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            System.out.println("Current MAC address : "+sb.toString());
            macaddr=sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return macaddr;
    }
    /* This method is used to get Node ID of Device
     *
     */
    public static String getNodeID()
    {
        String nodeid=null;

        File file = new File("NodeID.txt");

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                nodeid=st;
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return nodeid;
    }



}
