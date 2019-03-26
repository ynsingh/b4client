package com.ehelpy.brihaspati4.overlaymgmt;
//Maj Piyush Tiwari 22 April 2018
//This code generates an XML file with tag for search for newly generated node id.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class Request_for_RT_xml {
	
	public static File createSearchXmlQuery(String self_node_id,String to_node_id,String self_ip_address,String self_port_number ) throws TransformerException, ParserConfigurationException, FileNotFoundException
{
DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
DocumentBuilder db = f.newDocumentBuilder();
Document doc = db.newDocument();

Element rootele = doc.createElement("Query");
Element codeele = doc.createElement("Query_");
Element hashidele = doc.createElement("to_hash_id");
Element tonodeidele = doc.createElement("to_node_id");
Element selfnodeidele = doc.createElement("self_node_id");
Element ipele = doc.createElement("self_ip_address");
Element portele = doc.createElement("self_port_no");

String tagvalue = "0021";
((Element)codeele).setAttribute("tag", tagvalue);
Text t1 = doc.createTextNode(self_node_id);
String nextHopNodeId="abcde";// next hop will be given by routing module
Text t2 = doc.createTextNode(nextHopNodeId);
String selfNodeId="abcd1";

Text t3 = doc.createTextNode(selfNodeId);
String selfIp= "abcd2";

Text t4 = doc.createTextNode(selfIp);
String selfPortNo= "2222";
Text t5 = doc.createTextNode(selfPortNo);
hashidele.appendChild(t1);
tonodeidele.appendChild(t2);
selfnodeidele.appendChild(t3);
ipele.appendChild(t4);
portele.appendChild(t5);

codeele.appendChild(hashidele);
codeele.appendChild(tonodeidele);
codeele.appendChild(selfnodeidele);
codeele.appendChild(ipele);
codeele.appendChild(portele);
 
rootele.appendChild(codeele);
doc.appendChild(rootele);

TransformerFactory transformerFactory = TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer();
DOMSource source = new DOMSource(doc);
StreamResult result = new StreamResult(new  FileOutputStream("RequestforRT.xml"));
transformer.transform(source, result);
StreamResult file = new StreamResult(new File("C:\\Users\\Lenovo\\Documents"));
System.out.println("XML file Generated.");

return (new File("Newnode.xml"));


}
}
