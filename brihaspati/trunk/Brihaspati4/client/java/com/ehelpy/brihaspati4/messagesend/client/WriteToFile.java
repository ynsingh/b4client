package com.ehelpy.brihaspati4.messagesend.client;

import com.ehelpy.brihaspati4.messagesend.client.StringGenerator;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteToFile {

    public static final StringGenerator msr = new StringGenerator();
    public static final String sess_client=msr.generateRandomString();
    public static final StringGenerator msr1 = new StringGenerator();
    BufferedWriter bufferedWriter = null ;
    BufferedWriter bufferedWriter1 = null ;
    public static final String off_sess = msr1.generateRandomString1();
    public File myFile2 = new File("Rand_client.txt");
    public File myFile3 = new File("off_sess_client.txt");
    public String myFileLocation2 = myFile3.getPath() ;
    public String myFileLocation1 = myFile2.getPath() ;
    {
        try {
            //File myFile2 = new File("/home/guest/NetBeansProjects/org.iitk.brihaspati4.messagesend/MyData2/Rand2.txt");
            // check if file exist, otherwise create the file before writing
            if (!myFile2.exists()) {
                myFile2.createNewFile();
            }
            if (!myFile3.exists()) {
                myFile3.createNewFile();
            }
            Writer writer = new FileWriter(myFile2);
            Writer writer1 = new FileWriter(myFile3);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter1 = new BufferedWriter(writer1);
            bufferedWriter.write(sess_client);
            bufferedWriter1.write(off_sess);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufferedWriter != null) bufferedWriter.close();
            } catch(IOException ex) {

            }
            try {
                if(bufferedWriter1 != null) bufferedWriter1.close();
            } catch(IOException ex) {

            }
        }
    }
}
