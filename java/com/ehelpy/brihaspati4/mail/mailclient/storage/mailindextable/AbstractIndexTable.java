package com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable;

import javafx.beans.property.SimpleBooleanProperty;

public abstract class AbstractIndexTable {
    private final SimpleBooleanProperty read = new SimpleBooleanProperty(); //define whether mail is read or not

    public AbstractIndexTable (boolean isRead){
        this.setRead(isRead);
    }

    //Accessor for whole SimpleBooleanProperty
    public SimpleBooleanProperty getReadProperty(){
        return read;
    }

    //Accessor and Mutator methods
    public void setRead(boolean isRead){
        read.set(isRead);   //making beans using SimpleBooleanProperty
    }
    public boolean isRead(){
        return read.get();
    }
}
