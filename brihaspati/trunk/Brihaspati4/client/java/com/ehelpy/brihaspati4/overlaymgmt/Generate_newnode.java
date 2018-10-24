package com.ehelpy.brihaspati4.overlaymgmt;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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

import com.ehelpy.brihaspati4.indexmanager.IndexManagementUtilityMethods;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.routingmgmt.UpdateIP;


public class Generate_newnode {

	public static BufferedReader br = null;
	public static String newNodeId="";
	public static boolean NodeIDExists = false;

//	 public static void main(String[] args) throws FileNotFoundException, TransformerException, ParserConfigurationException {
		// TODO Auto-generated method stub
		
//	String node = generateNewNodeId(40);
	
//	SysOutCtrl.SysoutSet("New node ID generated is "+node, 1);
//	
//	String myIp = IndexManagementUtilityMethods.getMyIp();
//	//String bootstrapIp = OverlayManagement.BootstrapIP;
//	//myIp="111111111111";		
//	File f=createSearchXmlQuery(node,node , "New node check", myIp, "2222");
//		System.out.println(f.toString());
//	}
//	private static String getbootstrapIp() {
//		// TODO Auto-generated method stub
//		return "localhost";
//	}
	
//	private static String getmyIp() {
//		// TODO Auto-generated method stub
//		return "localhost";
//	}
	public static File createNewNodeSearchQuery(String to_hash_id, String to_node_id,String self_node_id,String self_ip_address,String self_port_number ) throws TransformerException, ParserConfigurationException, FileNotFoundException
	{
	//	System.out.println("tohashid in xml"+to_hash_id);
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
	
	
	
	//System.out.println(to_hash_id);
	//System.out.println(to_node_id);
	String tagvalue = "0021";
	((Element)codeele).setAttribute("tag", tagvalue);
	Text t1 = doc.createTextNode(to_hash_id);
	//String nextHopNodeId=self_node_id;// next hop will be given by routing module
	Text t2 = doc.createTextNode(to_node_id);
	//String toNodeId = to_node_id; 
	Text t3 = doc.createTextNode(self_node_id);
	//String selfNodeId=self_node_id;
	
	Text t4 = doc.createTextNode(self_ip_address);
	//String selfIp= self_ip_address;
	
	Text t5 = doc.createTextNode(self_port_number);
	//String selfPortNo= self_port_number;
	//Text t6 = doc.createTextNode();
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
	StreamResult result = new StreamResult(new  FileOutputStream("GeneratenewNode.xml"));
	transformer.transform(source, result);
	//StreamResult file = new StreamResult(new File("Documents"));
	SysOutCtrl.SysoutSet("GeneratenewNode file generated", 2);
	
	return (new File("GeneratenewNode.xml"));
	
	
}
	
					
		 public static String generateNewNodeId(int len)
		{
			 
								
			 //Generating OTP using numeric values
			 
		    String numbers = "ABCDEF0123456789";

		    // Using random method
		    Random rndm_method = new Random();

		    char[] NewNode = new char[len];
		 
		    for (int i = 0; i < len; i++)
		    {
		        // Use of charAt() method : to get character value
		        // Use of nextInt() as it is scanning the value as int
		    	NewNode[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
		    }
		    
			String newNodeid = String.valueOf(NewNode);
			System.out.println(newNodeid);
			
			saveNodeID(newNodeid);
			OverlayManagement.iAmNewlyJoinedNode = true;
			
	    return newNodeid;
			
			}
		    //NodeGenr myNode = new NodeGenr();
					
		
		 public static void saveNodeID(String nodeID) {
			FileWriter write = null;
			String NodeID = nodeID;
			
			try {
				write = new FileWriter("NodeID.txt");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				write.write(NodeID);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				write.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
		 
		
		}
		




