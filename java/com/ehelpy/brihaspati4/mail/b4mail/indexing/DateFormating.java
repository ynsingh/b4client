package com.ehelpy.brihaspati4.mail.b4mail.indexing;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormating {
    public static void main(String args[]) throws ParseException {
        String S="Thu May 28 04:20:07 IST 2020";
        Date todaysDate = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy").parse(S);
        DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String str2 = df2.format(todaysDate);
        System.out.println("String in dd-MM-yyyy HH:mm:ss format is: " + str2);
    }

    }