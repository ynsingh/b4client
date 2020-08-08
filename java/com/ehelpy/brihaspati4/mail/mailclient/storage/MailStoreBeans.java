/*
This class work as container to hold messages. When we read mails attributes are passed and stored in this bean.
1. In the message index we need to populate data
2. this java bean will hold data (one email sender, subject, size, date received) of an email per row.
3. we should have controls to change behaaviour of each bean (make mail read,unread, delete, mark etc.)
4. javaFX has good methods in property class to handle model classes
https://dzone.com/articles/the-bean-class-for-javafx-programming
 */

package com.ehelpy.brihaspati4.mail.mailclient.storage;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable.AbstractIndexTable;
import com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable.MailSizeFormat;


import javax.mail.Message;
import java.util.Date;


public class MailStoreBeans extends AbstractIndexTable {

    // In MVC,  Multiple controllers manipulate the model; multiple views display the data in the model, and change as the state of the model changes.
    // Java provides MVC architecture with two classes:
    // 1. Observer (any object that wishes to be notified when the state of another object changes), view is a subtype of observer.
    // 2. Observable (any object whose state may be of interest, and in whom another object may register an interest). Model is a subtype of Observable
    // SimpleStringProperty is used when a  variable is going to be observed by others.
    // StringProperty is the abstract base class for observable string properties.
    private SimpleStringProperty sub;
    private SimpleStringProperty from;
    private SimpleObjectProperty<MailSizeFormat> size;
    private SimpleObjectProperty<Date> date;
    private Message messageReference;   //serve to display message body

    // this map hold the mapping of integer value to string value
    // It is static as it need to be unique and need access from other class via SizeRedable method below.



  public MailStoreBeans(String subJ, String from, Date date, int size, String mailBody, boolean isRead, Message messageReference){
      super(isRead);
      this.sub = new SimpleStringProperty(subJ);
      this.from = new SimpleStringProperty(from);
      this.date = new SimpleObjectProperty<Date>(date);
      this.size = new SimpleObjectProperty<MailSizeFormat>(new MailSizeFormat(size));
      this.messageReference = messageReference;
    //   this.msgDate = new SimpleObjectProperty<Date>(msgDate);
    }


    // By @override we achieve run Time Polymorphism. It allows a subclass to provide a specific implementation of a method that is already provided by one of its super-classes.
    //
    @Override
    public String toString() {
        return "EmailMessageBean "
                + "sender=" + from.get() +
                ", subject=" + sub.get() +
                ", size=" + size.get();
    }


    // Use getter to get data from beans
    public String getSub(){
        return sub.get();
    }
    public String getFrom(){
        return from.get();
    }
    public Date getDate(){ return date.get();}
    public MailSizeFormat getSize(){
        return size.get();
    }
    public Message getMessageReference() { return messageReference;  }

   //     return msgDate.get();
    //}
}
