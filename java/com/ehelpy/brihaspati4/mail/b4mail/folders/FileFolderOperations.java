package com.ehelpy.brihaspati4.mail.b4mail.folders;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

public interface FileFolderOperations {
    void deleteFile(File X);
    void renameFile(String Y, String Z);
    void copyFile(File Y, File Z) throws IOException;
    void createB4Folders() throws IOException, MessagingException;
}
