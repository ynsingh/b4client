package com.ehelpy.brihaspati4.mail.b4mail.folders;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;

public class FolderMain {

    public static void main(String args[]) throws IOException, MessagingException {
        FileFactory ff = new FileFactory();    // This  is a factory object, manufactured from factory class
        FileFolderOperations obj = ff.getInstance("delete");
        FileFolderOperations obj1 = ff.getInstance("copy");
        FileFolderOperations obj2 = ff.getInstance("rename");
        FileFolderOperations obj3 = ff.getInstance("b4FolderChk");
        File src = new File("test");
        File dest = new File("hi");
        //obj1.copyFile(src,dest);
        //obj2.renameFile("test","hello");
        //obj.deleteFile(new File("tt.txt"));
        obj3.createB4Folders();

    }
}
