
package com.ehelpy.brihaspati4.overlaymgmt;
import java.math.BigInteger;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.lang.StringBuffer;
import java.lang.Exception;
import java.lang.Integer;

public class XML_RTConversion {

    public void XML_RTConversion() {
        //constructor
    }

    //************************ Convert from XML file to 2-Dimensional RT

    public String[][] XMLtoRT(String ActionFile) {  // ActionFile is the XML file to
        // be converted to 2D RT.

        String [][] Output_RT = new String[3][10];
        //String S = ActionFile.getPath();

        try {
            File inputFile = new File(ActionFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/RoutingTable/column/row";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc,XPathConstants.NODESET);

            //System.out.println(nodeList);
            int c =0;
            while(c < 10) {							// c is var for coulmn
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element eElement = (Element) nodeList.item(i);
                    int r = Integer.parseInt(eElement.getAttribute("rowNo")); // r is var for row
                    Output_RT[r][c] = eElement.getAttribute("NodeID");
                    if (r == 2) {
                        c++;
                    }
                    //System.out.println("");
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        System.out.println("Existing Self Routing Table ");
        System.out.println("      2                    4                     d                     f                     2                      1                    5                    b                     d                    c");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (int r = 0; r < 3; r++) {
            for (int z = 0; z < 10; z++) {
                System.out.print(Output_RT[r][z] + "         ");
            }
            System.out.println("");
        }
        System.out.println("");
        System.out.println("");

        return Output_RT;
    }
    // This is an alternate implementation of the SelfRTArraytoXML where i tried to make some changes, read this and if so have any doubts contact me
    public File NewSelfRTArraytoXML(String [][] ActionArray) {
        // ActionArray is the XML file to be converted to XML file.

        //File Output_XML = null;
        File SelfRT_XMLTemplate = null;
        try {
            File inputFile = new File("SelfRT_XMLTemplate.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/RoutingTable/column/row";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc,XPathConstants.NODESET);
            String expression1 = "/RoutingTable/column/row/NodeID";
            NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(doc,XPathConstants.NODESET);
            String [] A_Array = new String [30];
            //System.out.println(nodeList);
            int c =0;
            while(c < 10) {							// c is var for column
                for (int i = 0; i < nodeList.getLength(); i++) { // i is actually 30 in this case(10 X 3 matrix)
                    Element eElement = (Element) nodeList.item(i);
                    Element eElement1 = (Element) nodeList1.item(i);
                    int r = Integer.parseInt(eElement.getAttribute("rowNo"));

                    // r is var for row
                    eElement.setAttribute("NodeID", ActionArray[r][c]);	//changing attribute value (NodeID)
                    eElement1.setNodeValue(ActionArray[r][c]);			//changing Node value (NodeID)
                    //if (r == 2){
                    while (r == 2) {
                        c++;
                    }
                    //System.out.println("");
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        /*System.out.println("Existing Self Routing Table XML_RTConversion class: ");
        System.out.println("      2                    4                     d                     f                     2                      1                    5                    b                     d                    c");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (int r = 0; r < 3; r++) {
        	for (int z = 0; z < 10; z++) {
        		System.out.print(Output_RT[r][z] + "         ");
        	}
        	System.out.println("");
        }
        System.out.println("");
        System.out.println("");*/

        //return Output_XML;
        return SelfRT_XMLTemplate;
    }
    //************************ Convert from 2-Dimensional RT to XML

    public void SelfRTArraytoXML(String [][] ActionArray) {
        // ActionArray is the XML file to be converted to XML file.

        //File Output_XML = null;

        try {
            File inputFile = new File("SelfRT_XMLTemplate.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            XPath xPath =  XPathFactory.newInstance().newXPath();
            String expression = "/RoutingTable/column/row";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc,XPathConstants.NODESET);
            String expression1 = "/RoutingTable/column/row/NodeID";
            NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(doc,XPathConstants.NODESET);

            //System.out.println(nodeList);
            int c =0;
            while(c < 10) {							// c is var for column
                for (int i = 0; i < nodeList.getLength(); i++) { // i is actually 30 in this case(10 X 3 matrix)
                    Element eElement = (Element) nodeList.item(i);
                    Element eElement1 = (Element) nodeList1.item(i);
                    int r = Integer.parseInt(eElement.getAttribute("rowNo")); // r is var for row
                    eElement.setAttribute("NodeID", ActionArray[r][c]);	//changing attribute value (NodeID)
                    eElement1.setNodeValue(ActionArray[r][c]);			//changing Node value (NodeID)
                    if (r == 2) {
                        c++;
                    }
                    //System.out.println("");
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        /*System.out.println("Existing Self Routing Table XML_RTConversion class: ");
        System.out.println("      2                    4                     d                     f                     2                      1                    5                    b                     d                    c");
        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (int r = 0; r < 3; r++) {
        	for (int z = 0; z < 10; z++) {
        		System.out.print(Output_RT[r][z] + "         ");
        	}
        	System.out.println("");
        }
        System.out.println("");
        System.out.println("");*/

        //return Output_XML;
    }


    //************************ Obtain (EXTRACT) Self NodeID from NewSelfRouteTable
    public String getSelfNodeID() {

        String SelfNodeID = null;

        try {

            String path = "NewSelfRouteTable.xml";
            //System.out.println(path);
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            SelfNodeID = doc.getDocumentElement().getAttribute("LocalNodeID");

            System.out.println("Fetching Self NodeID from NewSelfRouteTable.xml  : " + SelfNodeID);
            System.out.println("");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return SelfNodeID;
    }

}



