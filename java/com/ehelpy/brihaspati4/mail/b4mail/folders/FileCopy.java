package com.ehelpy.brihaspati4.mail.b4mail.folders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileCopy extends Adaptor{
    private static final Logger bmaillog = LogManager.getLogger(FileCopy.class);
    @Override
    public void copyFile(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            copyDirectoryRecursively(src, dest);
            bmaillog.debug("Folder " + src + " Copied to folder " + dest);
        } else {
            Files.copy(src.toPath(), dest.toPath());
            bmaillog.debug("File " + src + " Copied to folder " + dest);
        }
    }

    // recursive method to copy directory and sub-diretory in Java
    private void copyDirectoryRecursively(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }
        for (String child : source.list()) {
            copyFile(new File(source, child), new File(target, child));
        }
    }

}
