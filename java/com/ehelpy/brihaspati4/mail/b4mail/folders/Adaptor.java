package com.ehelpy.brihaspati4.mail.b4mail.folders;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
/* this adaptor class is used to avoid the declaratiom of each method in every implementation of the interface
* */

public class Adaptor implements FileFolderOperations{
    @Override
    public void deleteFile(File X) {

    }

    @Override
    public void renameFile(String Y, String Z) {

    }

    @Override
    public void copyFile(File Y, File Z) throws IOException {

    }
    @Override
    public void createB4Folders() throws IOException, MessagingException {

    }
}
