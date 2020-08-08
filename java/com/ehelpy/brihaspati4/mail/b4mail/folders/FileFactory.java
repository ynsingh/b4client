package com.ehelpy.brihaspati4.mail.b4mail.folders;


import java.io.File;
public class FileFactory {
    public FileFolderOperations getInstance(String str) {      // in the package itself get the OS string and send the required object depending on OS
        if (str.equals("delete"))
            return new FileDelete();
        else if (str.equals("rename"))
            return new FileRename();
        else if(str.equals("copy"))
            return new FileCopy();
        else if(str.equals("b4FolderChk"))
            return new B4FolderCreation();
        else
            return null;
    }
}
