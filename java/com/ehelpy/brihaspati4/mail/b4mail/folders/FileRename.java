package com.ehelpy.brihaspati4.mail.b4mail.folders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileRename extends Adaptor {
    private static final Logger bmaillog = LogManager.getLogger(FileRename.class);

    @Override
    public void renameFile(String Y, String Z) {
        Path filepath= Paths.get(Y);
        boolean exists = Files.exists(filepath);
        boolean notExists = Files.notExists(filepath);
        boolean isDir = Files.isDirectory(filepath);
        boolean isfile = Files.isRegularFile(filepath);
        File src = new File(Y);
        File dest = new File(Z);

        if (isDir) {
            if(src.renameTo(dest))    {
                bmaillog.debug("Dir renamed from " + Y + " to " + Z);
            } else{
                bmaillog.debug("Dir " + Y + " could not be renamed: failed");
            }
        } else if (exists) {
            if(src.renameTo(dest))    {
                bmaillog.debug("File renamed from " + Y + " to " + Z);
            } else{
                bmaillog.debug("File " + Y + " could not be renamed: failed");
            }
        } else if (notExists) {
            System.out.println("File doesn't exist!!");
        } else {
            System.out.println("Program doesn't have access to the file!!");
        }
    }

}
