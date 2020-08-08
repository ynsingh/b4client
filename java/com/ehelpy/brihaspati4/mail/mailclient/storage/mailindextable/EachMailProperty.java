package com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableRow;

//Every table view has a row factory, and we can control property of each row.
//generic method, using "Generics" in Java. T can be any type that is subclass of AbstractIndexTable.
public class EachMailProperty<T extends AbstractIndexTable> extends TableRow<T>{
    private final SimpleBooleanProperty bold = new SimpleBooleanProperty();
    private T currentItem = null;   // T helps in when we do not know the type

    public EachMailProperty() {
        super();

        bold.addListener((ObservableValue<? extends Boolean> observable, Boolean olValue, Boolean NewValue) ->{
            if(currentItem != null && currentItem==getItem()){
                updateItem(getItem(), isEmpty());
            }
        });
        itemProperty().addListener((ObservableValue<? extends T> observable, T olValue, T NewValue) ->{
            bold.unbind();
            if(NewValue != null){
                bold.bind(NewValue.getReadProperty()); //bind method is from SimpleBooleanProperty
                currentItem = NewValue;
            }
        });
    }

    @Override       //override the default method from Cell class
    final protected void updateItem(T item, boolean empty){     //T item means an item in  a row
        super.updateItem(item, empty);
        if(item !=null && !item.isRead()){
            setStyle("-fx-font-weight: bold");  //make it unread
        }else{
            setStyle("");   //toggle
        }

    }
}
