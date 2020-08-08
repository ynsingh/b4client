package com.ehelpy.brihaspati4.mail.mailclient.storage.mailindextable;

import java.util.Comparator;

public class MailSizeFormat implements Comparator<MailSizeFormat> {
    private int size;

    public MailSizeFormat(int size) {
        this.size = size;
    }

    @Override
    public String toString(){
        String returnValue;
        if(size<= 0){
            returnValue =  "0";}

        else if(size<1024){
            returnValue = size + " B";
        }
        else if(size < 1048576){
            returnValue = size/1024 + " kB";
        }else{
            returnValue = size/1048576 + " MB";
        }
        return returnValue;
    }

    @Override
    public int compare(MailSizeFormat o1, MailSizeFormat o2) {
        Integer int1 = o1.size;
        Integer int2 = o2.size;
        return int1.compareTo(int2);
    }
}
