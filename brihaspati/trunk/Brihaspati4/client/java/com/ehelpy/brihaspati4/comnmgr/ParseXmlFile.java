package com.ehelpy.brihaspati4.comnmgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;

public class ParseXmlFile extends CommunicationManager {

    public static String[] details = {"","","","","",""};
    public static String[] details_0003 = {"","","","","","",""};
    static int k =0;
   
    public static String[] ParseXml(File filetoparse)
    {
        SysOutCtrl.SysoutSet("you are in ParseXml method",2);
        SysOutCtrl.SysoutSet("Xml Parsed, Details are ",2);
                
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filetoparse);
   
            NodeList nlist = doc.getElementsByTagName("Query_");

            for(int i=0; i<nlist.getLength(); i++)
            {
                Node nNode = nlist.item(i);

                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;					
                    
                    NodeList nodeList = doc.getElementsByTagName("Query_");
                    for(int x=0,size= nodeList.getLength(); x<size; x++) 			
                    {
                        details[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();

                        details[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				
                        details[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				
                        details[3] = eElement.getElementsByTagName("self_node_id").item(0).getTextContent();
                        details[4] = eElement.getElementsByTagName("self_ip_address").item(0).getTextContent();
                        details[5] = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();
                  
                        SysOutCtrl.SysoutSet("tag: "+details[0]);
                        SysOutCtrl.SysoutSet("to_hash_id"+details[1]);
                        SysOutCtrl.SysoutSet("to_node_id"+details[2]);
                        SysOutCtrl.SysoutSet("self_node_id"+details[3]);
                        SysOutCtrl.SysoutSet("ip_address"+details[4]);
                        SysOutCtrl.SysoutSet("port_no"+details[5]);

                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
     
        return details;
    }
    
    public static String[] ParseXml_0003(File filetoparse)
    {
        SysOutCtrl.SysoutSet("you are in ParseXml method",2);
        SysOutCtrl.SysoutSet("Xml Parsed, Details are ",2);
         
        try {

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filetoparse);
   
            NodeList nlist = doc.getElementsByTagName("Query_");

            for(int i=0; i<nlist.getLength(); i++)
            {
                Node nNode = nlist.item(i);

                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;					
                    
                    NodeList nodeList = doc.getElementsByTagName("Query_");
                    for(int x=0,size= nodeList.getLength(); x<size; x++) 			
                    {
                        details_0003[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();

                        details_0003[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				
                        details_0003[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				
                        details_0003[3] = eElement.getElementsByTagName("self_node_id").item(0).getTextContent();
                        details_0003[4] = eElement.getElementsByTagName("self_ip_address").item(0).getTextContent();
                        details_0003[5] = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();
                        details_0003[6] = eElement.getElementsByTagName("inter_ip").item(0).getTextContent();
             
                        SysOutCtrl.SysoutSet("tag: "+details_0003[0]);
                        SysOutCtrl.SysoutSet("to_hash_id"+details_0003[1]);
                        SysOutCtrl.SysoutSet("to_node_id"+details_0003[2]);
                        SysOutCtrl.SysoutSet("self_node_id"+details_0003[3]);
                        SysOutCtrl.SysoutSet("ip_address"+details_0003[4]);
                        SysOutCtrl.SysoutSet("port_no"+details_0003[5]);
                        SysOutCtrl.SysoutSet("inter_ip"+details_0003[6]);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
      
        return details_0003;
  
    }
}