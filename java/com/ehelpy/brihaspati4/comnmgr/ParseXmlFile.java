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


//
    public static String[] details = {"","","","","",""};
    static int k =0;
    public static String[] ParseXml(File filetoparse)
    {
        SysOutCtrl.SysoutSet("you are in ParseXml method",2);
        SysOutCtrl.SysoutSet("Xml Parsed, Details are ",2);
        try {

            //File fxmlFile = filetoparse;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filetoparse);
            //	SysOutCtrl.SysoutSett = doc.getDocumentElement();
            //	SysOutCtrl.SysoutSet("root element " + rootElement.getNodeName());
            //	SysOutCtrl.SysoutSet("---------------------  " + rootElement.getAttribute());

            NodeList nlist = doc.getElementsByTagName("Query_");
            //		NodeList nlist = doc.getElementsByTagName("Request_for_Indexing");
            //		System.out.println("---------------------  " + nlist.getLength());
//	int nlistlength= nlist.getLength();
//	System.out.println("nlist lenght"+nlistlength);
            for(int i=0; i<nlist.getLength(); i++)
            {
                Node nNode = nlist.item(i);


//			System.out.println("current element " + nNode.getNodeName());
//			System.out.println("nodetype " + nNode.getNodeType());
//			System.out.println("ELEMENT NODE " + Node.ELEMENT_NODE);

                if(nNode.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) nNode;					//System.out.println(eElement.getAttribute("id"));

                    NodeList nodeList = doc.getElementsByTagName("Query_");
                    for(int x=0,size= nodeList.getLength(); x<size; x++) 			// to get  tag value from each xml file.
                    {
                        //	System.out.println(nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue());
                        details[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();

                        details[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				//	System.out.println("Reading hash_id in readxmlfile : " + details);
                        details[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				//	System.out.println("Reading node_id in readxmlfile : " + nodeid);
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
        //System.out.println("\n");
        //	System.out.println("tag value is " + details[0]);
        //	System.out.println("Parsing XML file " + filetoparse +"  and Returning hash_id from ReadXmlFile class to IndexManagement Class  : " + details[1]  + " : its corresponding nodeid is : "+ details[2] );
        return details;
    }
}