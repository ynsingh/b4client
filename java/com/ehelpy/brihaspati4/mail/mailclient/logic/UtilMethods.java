package com.ehelpy.brihaspati4.mail.mailclient.logic;

import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.io.*;

import java.util.*;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import java.util.Base64;

public class UtilMethods {

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();

    }


   // Though bcrypt is strongest it is not yet supported in openjdk, So commented
    //String storPass = BCrypt.hashpw(rawpass, BCrypt.gensalt(12));
    //System.out.println(storPass);
    //boolean matched = BCrypt.checkpw(rawpass, storPass);
    //System.out.println(matched);

    // SHA512 Encrryption of master password
    public static String passEncMethod(String rawpass) {
        String stropass = null;
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(rawpass.getBytes(StandardCharsets.UTF_8));
            byte[] messageDigest = md.digest(rawpass.getBytes());

            // Convert byte array into signum representation
            BigInteger num = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = num.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            stropass=Base64.getEncoder().encodeToString(hashtext.getBytes());
            return stropass;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // encrypt the email password string with same email password(symKey) for storing it
//    public static String fileEncByPass(String rawstr) {
//        String stropass = null;
//        try {
//
//            MPBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
//            SecretKeyFactory secretKeyFactory = SecretKeyFactory
//                    .getInstance("PBEWithMD5AndTripleDES");
//            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
//
//            byte[] salt = new byte[8];
//            Random random = new Random();
//            random.nextBytes(salt);
//
//            PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, 100);
//            Cipher cipher = Cipher.getInstance("PBEWithMD5AndTripleDES");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, pbeParameterSpec);
//            outFile.write(salt);
//
//            byte[] input = new byte[64];
//            int bytesRead;
//            while ((bytesRead = inFile.read(input)) != -1) {
//                byte[] output = cipher.update(input, 0, bytesRead);
//                if (output != null)
//                    outFile.write(output);
//            }
//
//            byte[] output = cipher.doFinal();
//            if (output != null)
//                outFile.write(output);
//
//            inFile.close();
//            outFile.flush();
//            outFile.close();
//
//            return stropass;
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

}



