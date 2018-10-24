package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.File;
import java.io.IOException;
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
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

//********************** class to extract the Routing Table entries(Node ID) from given XML file****************
public class NewXMLtoStringArray {
    String[] OutputStringArray = new String[30];

    public String[] XMLToStringArray(String ActionFile) {

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
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    OutputStringArray[i] = eElement.getAttribute("NodeID");
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
        System.out.println("Converting every incoming segregated (Tag = 0002 type) XML files into an array of NodeIDs");
        //System.out.println(OutputStringArray);
        System.out.println("");
        return OutputStringArray;
    }

}

