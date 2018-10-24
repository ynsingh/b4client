package com.ehelpy.brihaspati4.messagesend.client;

import com.ehelpy.brihaspati4.messagesend.client.SessionKey;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionKey {
    ArrayList<String> list = new ArrayList<String>();
    {
        try {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader("Rand_client.txt"));
                BufferedReader r = new BufferedReader(new FileReader( "Rand_server.txt"));
                String s1 =null;
                String s2 = null;
                
                while ((s1 = br.readLine()) != null)
                {
                    list.add(s1);
                }
                while((s2 = r.readLine()) != null)
                {
                    list.add(s2);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            
            BufferedWriter writer=null;
            writer = new BufferedWriter(new FileWriter("SessionKey.txt"));
            
            String listWord;
            for (int i = 0; i< list.size(); i++)
            {
                listWord = list.get(i);
                writer.write(listWord);
                
            }
            
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SessionKey.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
    

    

