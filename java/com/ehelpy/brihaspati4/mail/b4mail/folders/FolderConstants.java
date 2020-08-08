package com.ehelpy.brihaspati4.mail.b4mail.folders;

//import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStream;

public interface FolderConstants
{
    //static Logger log = Logger.getLogger(B4FolderMethods.class.getName());
    //InputStream log4jConfPath = B4FolderMethods.class.getResourceAsStream("log4j.properties");

    String myDirectory = "B4"; // user Folder Name
    String p2pDirectory = "P2P"; // user Folder Name
    String imapDirectory = "IMAP"; // user Folder Name
    String boxtype= "Maildir";
    String sentDir= ".Sent";
    String draftDir= ".Drafts";
    String trashDir= ".Trash";
    String spamDir= ".Junk";
    String outboxDir= ".Outbox";
    String archiveDir= ".Archive";
    String tmpbox= "tmp";
    String curbox= "cur";
    String newbox= "new";
    String path = getUsersHomeDir() + File.separator + myDirectory;
    String p2ppath = path+ File.separator+p2pDirectory;
    String imappath = path+ File.separator+imapDirectory;

    String IMAPmaildirPath = imappath+File.separator+boxtype;
    String P2PmaildirPath = p2ppath+File.separator+boxtype;


    String tmpInboxpath = P2PmaildirPath+File.separator+tmpbox+File.separator;
    String curInboxpath = P2PmaildirPath+File.separator+curbox+File.separator;
    String newInboxpath = P2PmaildirPath+File.separator+newbox+File.separator;

    String sentpath = P2PmaildirPath+File.separator+sentDir;
    String newsentpath = sentpath+File.separator+newbox+File.separator;
    String cursentpath = sentpath+File.separator+curbox+File.separator;
    String tmpsentpath = sentpath+File.separator+tmpbox+File.separator;

    String draftpath = P2PmaildirPath+File.separator+draftDir;
    String newdraftpath = draftpath+File.separator+newbox+File.separator;
    String curdraftpath = draftpath+File.separator+curbox+File.separator;
    String tmpdraftpath = draftpath+File.separator+tmpbox+File.separator;


    String trashpath = P2PmaildirPath+File.separator+trashDir;
    String newtrashpath = trashpath+File.separator+newbox+File.separator;
    String curtrashpath = trashpath+File.separator+curbox+File.separator;
    String tmptrashpath = trashpath+File.separator+tmpbox+File.separator;

    String spampath = P2PmaildirPath+File.separator+spamDir;

    String outboxpath = P2PmaildirPath+File.separator+outboxDir;
    String newoutboxpath = outboxpath+File.separator+newbox+File.separator;
    String curoutboxpath = outboxpath+File.separator+curbox+File.separator;
    String tmpoutboxpath = outboxpath+File.separator+tmpbox+File.separator;

    String archivepath = P2PmaildirPath+File.separator+archiveDir;

    public static String getUsersHomeDir() {
        String users_home = System.getProperty("user.home");
        //return users_home.replace("\\", "/"); // to support all platforms.
        return users_home;
    }

}
