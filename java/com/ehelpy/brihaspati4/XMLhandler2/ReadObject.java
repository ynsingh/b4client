package com.ehelpy.brihaspati4.XMLhandler2;

public class ReadObject {
    private int id;
    private String hashedInode;
    private String data;
    // method to getId
    public int getId() {return id;}
    // method to put id
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
       // byte[] decoded = Base64.getDecoder().decode(data.getBytes());
        this.data = data;

    }
}
