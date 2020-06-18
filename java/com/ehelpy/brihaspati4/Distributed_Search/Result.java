package com.ehelpy.brihaspati4.Distributed_Search;
// class to map keyword with hyperlink to be used with ResultWindow class
public class Result {
    
    String key;
    String link;
    
    Result(String key,String link)
    {
        this.key=key;
        this.link=link;
    }
    
    String getKey()
    {
        return key;
    }
    
    String getLink()
    {
        return link;
    }
}
