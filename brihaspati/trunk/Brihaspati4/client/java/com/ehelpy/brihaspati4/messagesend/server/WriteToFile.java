package com.ehelpy.brihaspati4.messagesend.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.ehelpy.brihaspati4.messagesend.server.StringGenerator;
 
public class WriteToFile {   

    
    	public static final StringGenerator msr = new StringGenerator();
    	public static final StringGenerator msr1 = new StringGenerator();
      public static final String sess_serv = msr.generateRandomString(); 
      public static final String off_sess = msr1.generateRandomString1(); 
        BufferedWriter bufferedWriter = null ;
        BufferedWriter bufferedWriter1 = null ;
   public File myFile1 = new File("Rand_server.txt");
   public static File myFile2 = new File("off_sess_server.txt");
   public String myFileLocation = myFile1.getPath() ;
   public String myFileLocation1 = myFile2.getPath() ;
   {
        try {            
           //File myFile1 = new File("/home/guest/NetBeansProjects/org.iitk.brihaspati4.messagesend/MyData1/Rand1.txt");
            // check if file exist, otherwise create the file before writing
            if (!myFile1.exists()) {
                myFile1.createNewFile();
            }
            if (!myFile2.exists()) {
                myFile2.createNewFile();
            }
            Writer writer = new FileWriter(myFile1);
            bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(sess_serv);
            Writer writer2 = new FileWriter(myFile2);
            bufferedWriter1 = new BufferedWriter(writer2);
            bufferedWriter1.write(off_sess);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(bufferedWriter != null) bufferedWriter.close();
            }catch (IOException ex){
            	
            }
            try{
                if(bufferedWriter1 != null) bufferedWriter1.close();
            } 
            catch(IOException ex){
                 
            }
        }
        }}     