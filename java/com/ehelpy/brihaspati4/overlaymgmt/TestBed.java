package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;

//import com.ehelpy.brihaspati4.overlaymgmt.Overlay;

public class TestBed {

    public static void main(String[] args)
    {
        System.out.println("Starting OverlayManagement thread");
        OverlayManagement ovlymgt= new OverlayManagement();
        ovlymgt.start();
        //indmgt.getState();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("Generating random nade id for newly joined Node");
        //Generate_newnode newNodeId = new Generate_newnode();
        //newNodeId.star
    }
}






