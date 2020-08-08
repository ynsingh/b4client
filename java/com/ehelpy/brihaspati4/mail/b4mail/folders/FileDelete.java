package com.ehelpy.brihaspati4.mail.b4mail.folders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class FileDelete extends Adaptor {
    private static final Logger bmaillog = LogManager.getLogger(FileDelete.class);
    @Override
    public void deleteFile(File Y) {
        if (Y.isDirectory()) {
            for (File sub : Y.listFiles()) {
                deleteFile(sub);
            }
            bmaillog.debug("Folder "+Y+" Deleted ");
        }
        if(Y.delete()){
            bmaillog.debug("File "+Y+" Deleted ");
        }else {
            bmaillog.debug("Failed to delete File " + Y + " : check");
        }

    }
}

