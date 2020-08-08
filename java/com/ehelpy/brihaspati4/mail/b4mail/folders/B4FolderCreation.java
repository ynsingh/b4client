package com.ehelpy.brihaspati4.mail.b4mail.folders;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import java.io.*;



/*
01.01. This will create the maildir folder structure in the client to hold mails.
01.02. The number of upper level directory created depends on number of email accounts configured.
01.03. E.g. For Linux: directory will /home/<user>/B4/p2p, /home/<user>/B4/imap
            For Windows: directory will C:/users/<user>/B4/p2p, C:/users/<user>/B4/imap
01.04. Directory structure existence check is done every time the client comes alive.
01.05. If some how the directory get corrupted/deleted during operation,
        For IMAP, it should be recreated and synced with IMAP server.
        for P2P, Create folder structure, If backup is configured, then sync from backup, else inform user and exit.

 */

    public class B4FolderCreation extends Adaptor implements FolderConstants {
    private static final Logger b4maillog = LogManager.getLogger(B4FolderCreation.class);
    //public static void main(String[] args) throws IOException, MessagingException {
    public void createB4Folders() throws IOException,MessagingException {
        System.out.println("path: "+path);
        dirMake(path);
        dirMake(p2ppath);
        dirMake(imappath);
        dirMake(IMAPmaildirPath);

        maildirMake(P2PmaildirPath);
        maildirMake(sentpath);
        maildirMake(draftpath);
        maildirMake(trashpath);
        maildirMake(spampath);
        maildirMake(outboxpath);
        maildirMake(archivepath);
    }

    public static void dirMake(String pathname){
        if (new File(pathname).mkdir()) {
            b4maillog.debug("Directory"+pathname+" is created!");
        } else {
            b4maillog.debug("Directory  "+pathname+" already present, so will not be created");
            //call FolderSync
        }
    }
    // Following function will create p2p/Maildir/{cur,new,tmp,.Sent(cur,ew,tmp),.Trash(cur,new,tmp)...}
    public static void maildirMake(String pathname){
        String curpath=pathname+File.separator+"new";
        String newpath=pathname+File.separator+"cur";
        String tmppath=pathname+File.separator+"tmp";
        dirMake(pathname);
        dirMake(curpath);
        dirMake(newpath);
        dirMake(tmppath);
    }



}