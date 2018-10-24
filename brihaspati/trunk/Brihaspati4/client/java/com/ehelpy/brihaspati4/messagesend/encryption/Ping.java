package com.ehelpy.brihaspati4.messagesend.encryption;

import java.io.*;
import java.util.*;

public class Ping
{
  public boolean  doCommand(List<String> command) 
  throws IOException
  {
    ProcessBuilder pb = new ProcessBuilder(command);
    Process process = pb.start();

    BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
    // read the output from the command
   // System.out.println("Here is the standard output of the command:\n");
    if ((stdInput.readLine()) != null)
    {
      return true ;
    }

    // read any errors from the attempted command
    
    return false;

}
}
