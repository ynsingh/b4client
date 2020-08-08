package com.ehelpy.brihaspati4.mail.b4mail;

public class MailManager {
    //01. Check for folder structure in predefined location

    //02.if not present create it.

    //03. If present start email client

    //04. Sync emails from local storage to email client display

    //05. Sync emails root node to local storage


    //06. For sending compose a mail.  and get receivers public key, if public key not found use SMTP

    //07. If public key found, calculate composed files hash add it to header of email

    // 08. Keep the file in outbox with predefined filename.

    //09. Fragment the file, get a random AES sym Key from API.

    //10. Calculate hash of a fragment, append that to file fragment

    //11. AES encrypt each fragment of step 10

    //12. encrypt the AES key with receivers public key, append fragment name and from email-ID, Sign with own provate key.

    //13. Append the encrypted file fragment (step 10) and file metadata(step 12), and send to destination (TODO: separate CLASS)

    //14: TODO: CM should instantiate a buffer for MM, once instantiated it should also assign tags

    //15. Once email is Sent, wait for ack1 (all fragments received at receiver) and move file to Sent from OUTBOX. TODO: Best way get short packets? (XML/strings/RMI)

    //16. Keep counter(10 minute) to retry if ack not received in that time.

    //17.wait for ack2(hash match), ack3(read)

    //18. for root node case akc4 received, then also file goes to Sent folder



    //19. On receiver side, MM either poll or get signal from CM for new file in MM buffer.

    //20. get public key of Sender, decrypt meta data, file name defines number of fragments.

    //21. Wait for all fragments, once received decrypt and assemble. Send ack1 and update flag in file name.

    //22. check hash, if matches send ack2, move from INBOX/tmp to INBOX/new, and update flag in file name.

    //23. When user clicks on INBOX move from INBOX/new to INBOX/cur and send ack3, and update flag in file name.



    //24. When receiver offline, get root node and send to it.

    //25. MM in root node get file meta data, and once all fragments are received by root node ack4 is sent.


    //26. Mail indexing helps in displaying messages from a folder
    //27. for each mail:  filename-from-subject-date-size
    //28. msgcount,unseen msg count, last new check time, last-cur-checktime
    //29. when user gets a new mail, index gets updated, in INBOX
    //30. when user sends a mail, index gets updated, in Sent.
    //31. When mail moves a mail between filders, index get updated both in source and destination
    //32. When mail get deleted,index gets updated, in Trash
}
