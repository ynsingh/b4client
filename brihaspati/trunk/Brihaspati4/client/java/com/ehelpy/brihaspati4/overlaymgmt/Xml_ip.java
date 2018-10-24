 package com.ehelpy.brihaspati4.overlaymgmt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
class Xml_ip{

public static String[][] convertXmlToipTable(File inFile) {
	//private static Map<String, String> convertXmlToIndexTable(File inFile) {
		
		// TODO Auto-generated method stub
	String[][] tempIpList = new String[2][2];
		// this method should convert the incoming xml file to myindex9hashmap)
		System.out.println("you are in convertXmlToipTable method");
		try{
			
			//File fxmlFile = filetoparse;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(inFile);
		//	Element rootElement = doc.getDocumentElement();					
		//	System.out.println("root element " + rootElement.getNodeName());
		//	System.out.println("---------------------  " + rootElement.getAttribute());
			
			NodeList nlist = doc.getElementsByTagName("indexentries");
	//		NodeList nlist = doc.getElementsByTagName("Request_for_Indexing");
	//		System.out.println("---------------------  " + nlist.getLength());
	  // int nlistlength= nlist.getLength();
	//System.out.println("nlist lenght"+nlistlength);
			for(int i=0; i<nlist.getLength(); i++)
				
			{
				//System.out.println("for loop i "+i);
				Node nNode = nlist.item(i);
				
				
		//	System.out.println("current element selected from xml: " + nNode.getNodeName());
			//System.out.println("nodetype " + nNode.getNodeType());
			//System.out.println("ELEMENT NODE " + Node.ELEMENT_NODE);
									
				if(nNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Element eElement = (Element) nNode;					//System.out.println(eElement.getAttribute("id"));
					
					String record_no = eElement.getAttribute("record_no");
				//	System.out.println();
					NodeList nodeList = eElement.getChildNodes();
					for(int x=0;x< 2; x++) 			// to get  tag value from each xml file.
					{						  
					//	System.out.println("for loop x "+x);
						Node n= nodeList.item(x);
						if(n.getNodeType()==Node.ELEMENT_NODE) {
							Element name=(Element) n;
						//	System.out.println("indexentries"+record_no+":"+name.getTagName()+"="+name.getTextContent());
						
						
//					System.out.println(nodeList.item(x).getAttributes().getNamedItem("record_no").getNodeValue());
					
//					details[0] = nodeList.item(x).getAttributes().getNamedItem("tag").getNodeValue();
//					details[1] = eElement.getElementsByTagName("to_hash_id").item(0).getTextContent();				//	System.out.println("Reading hash_id in readxmlfile : " + details);
//					details[2] = eElement.getElementsByTagName("to_node_id").item(0).getTextContent();				//	System.out.println("Reading node_id in readxmlfile : " + nodeid);	
//					details[3] = eElement.getElementsByTagName("self_node_id").item(0).getTextContent();
					String nodeid = eElement.getElementsByTagName("hash_id").item(0).getTextContent();
					String ipadd = eElement.getElementsByTagName("node_id").item(0).getTextContent();
//					details[4] = eElement.getElementsByTagName("self_ip_address").item(0).getTextContent();	
//					details[5] = eElement.getElementsByTagName("self_port_no").item(0).getTextContent();	
//					System.out.println("tag: "+details[0]);
//					System.out.println("to_hash_id"+details[1]);
//					System.out.println("to_node_id"+details[2]);
//					System.out.println("self_node_id"+details[3]);
//					System.out.println("ip_address"+details[4]);
//					System.out.println("port_no"+details[5]);
				
					
 					
					tempIpList[i][0] = nodeid;
					tempIpList[i][1] = ipadd;
						System.out.println(tempIpList);
					
					
					//System.out.println();
				}
				
			}}}
		}
		catch (Exception e)
			{
				e.printStackTrace();
			}
	//System.out.println("\n");
	//	System.out.println("tag value is " + details[0]);
	//	System.out.println("Parsing XML file " + filetoparse +"  and Returning hash_id from ReadXmlFile class to IndexManagement Class  : " + details[1]  + " : its corresponding nodeid is : "+ details[2] );
//		return details;
		return tempIpList;
	}

/*public class Xml_ip {
public static String[][] convert_to_ip(String Inputfile) {
	String [][] Output_RT = new String[2][2];
	try {
		File inputFile = new File(Inputfile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true); 
		DocumentBuilder dBuilder;			
		dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();			
		XPathFactory xPathFactory =  XPathFactory.newInstance();
		XPath xpath = xPathFactory.newXPath();
		List<String> nodes = getNodeid(doc, xpath, "Nodeid");
		System.out.println("list of nodeids:" + Arrays.toString(nodes.toArray()));
		List<String> ips = getIpadd(doc, xpath, "Nodeid");
		System.out.println("list of ips:" + Arrays.toString(ips.toArray()));
	} catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
    }


		//NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(doc,XPathConstants.NODESET);
	return Output_RT;
	
}
	private static List<String> getIpadd(Document XML, XPath xpath, String colmn2) {
	// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
        try {
            //create XPathExpression object
            XPathExpression expr =
                xpath.compile("/XML/Query/indexentries[hash_id='']/hash_id/text()");
            //evaluate expression result on XML document
            String list = Element.getElementsByTagName("hash_id").item(0).getTextContent();
            for (int i = 0; i < nodes.getLength(); i++)
                list.add(nodes.item(i).getNodeValue());
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
	return list;
}
	public static List<String> getNodeid(Document XML, XPath xpath, String colmn1) {
	// TODO Auto-generated method stub
	return null;
}*/
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//	File file = new File("XML.xml");
//		String[][] ipList=convertXmlToipTable(file);
//		for(int i=0; i<ipList.length;i++)
//			for(int j=0; j<2;j++)
//			
//			System.out.println(ipList[i][j]);
//
//	}

}
