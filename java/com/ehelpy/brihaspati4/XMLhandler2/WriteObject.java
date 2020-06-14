package com.ehelpy.brihaspati4.XMLhandler2;

import java.util.Base64;

public class WriteObject {
    private int id;
    private String hashedInode;
    private String data;

    public WriteObject(int id, String hashedInode,byte[] data){
        this.id = id;
        this.hashedInode = hashedInode;
        // base 64 encoding
        byte[] encoded = Base64.getEncoder().encode(data);
        this.data = new String(encoded);
    }
    // method to getId
    public int getId() {
        return id;
    }
    // method to put Id
    public void setId(int id) {
        this.id = id;
    }
    // method to get Inode
    public String getInode() {
        return hashedInode;
    }
    // method to put Inode
    public void setInode(String hashedInode) {
        this.hashedInode = hashedInode;
    }
    // method to get Data
    public String getData() {
        return data;
    }
    // method to put Data
    public void setData(String data) {
        this.data = data;
    }
}
