package com.ehelpy.brihaspati4.DFS;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

//import org.apache.commons.io.FileUtils;

import com.ehelpy.brihaspati4.comnmgr.CommunicationManager;
import com.ehelpy.brihaspati4.comnmgr.CommunicationUtilityMethods;
import com.ehelpy.brihaspati4.comnmgr.ParseXmlFile;
import com.ehelpy.brihaspati4.comnmgr.XmlFileSegregation;
import com.ehelpy.brihaspati4.indexmanager.SHA1;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagement;
import com.ehelpy.brihaspati4.overlaymgmt.OverlayManagementUtilityMethods;
import com.ehelpy.brihaspati4.overlaymgmt.PredecessorSuccessor;
import com.ehelpy.brihaspati4.routingmgmt.PresentIP;
import com.ehelpy.brihaspati4.routingmgmt.SysOutCtrl;
import com.ehelpy.brihaspati4.voip.voip_gui;

//import sun.security.mscapi.KeyStore.MY;

public class DistFileSys extends Thread {
	static String myIp = PresentIP.MyPresentIP();
	static String myNodeId = OverlayManagement.myNodeId;

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// Create ReceivingBufferDFS for putting all the received xml files from Comm
	// Manager
	public static LinkedList<File> RxBufferDFS = new LinkedList<File>();// this need to be made in comn mgr
	public static Object lock_RecBuff_Main = new Object();

	public static Object lock_TransBuff_Main = new Object();
	public static boolean actionDetecter=true;

	// all file Chunks are here and each chunk of a file is associated with a root
	// node id,root ipAdd
	public static LinkedHashMap<String, String[]> nodeFileChunkMap = new LinkedHashMap<String, String[]>();
	// Creating map of files which are stored in DFS with no. of Chunks
	public static LinkedHashMap<String, Integer> nodefilemap = new LinkedHashMap<>();

	// keep ipAdd and Nodeid of the replication set
	public static LinkedHashMap<String, String[]> root_Fileinfo_Map = new LinkedHashMap<String, String[]>();
	// all the files for which this node is the root node with the details of
	// originator and replication nodes

	// ipAdd and Nodeid of originator node and root node
	public static LinkedHashMap<String, String[]> shared_Fileinfo_Map = new LinkedHashMap<String, String[]>();
	// all the shared files which are stored here

	// public static LinkedHashMap<String,String> Root_To_Originator_msg = new
	// LinkedHashMap<>();
	public static Lock lock = new ReentrantLock();// this is to protect simultaneously accessing writeIpTable method in
													// xmiFileSeggregation class.

	public void run() {
		/*try {
			Fetch_file.createAndShowGUI();
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}*/
		
	////////////////////////////////////////////////////////////////////////////////////////////////////////	
		/*Thread tDFSR1 = new Thread(new Runnable() {
			@Override
			public void run() {
				{
					try {FileReceiverDemox
						.fileReceiver_Store(DistFileSysUtilityMethods.choose_WhereToSave());

			} catch (IOException e) {
				e.printStackTrace();
			}
					
					System.out.println("DFS thread tDFSR1 is running");
				}
			}
		});

		tDFSR1.start();*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*	Thread tDFS2 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						DistFileSysUtilityMethods.fileUploading_DHT();// asking user to store a file in DFS
					} catch (ParserConfigurationException | TransformerException | IOException e) {
						e.printStackTrace();
					}
					System.out.println("DFS thread tDFS2 is running");
					try {
						Thread.sleep(120000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});

		tDFS2.start();*/
//////////////////////////////////////////////////////////////////////////////////////////////////////
		/*Thread tDFS3 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					Fetch_file.createAndShowGUI();// asking user which file to retrieve/Delete
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("DFS thread tDFS3 is running");
				}
			}
		});

		tDFS3.start();*/

//////////////////////////////////////////////////////////////////////////////////////////////////////

		Thread tDFS4 = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						DistFileSysUtilityMethods.Application_Alive_Response_ByRecieverSide();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("DFS thread tDFS4 is running");
				}
			}
		});

		tDFS4.start();

//////////////////////////////////////////////////////////////////////////////////////////////////////

		Thread tDFS1 = new Thread(new Runnable() {
			@Override
			public void run() {

				while (true)

				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e3) {
						// TODO Auto-generated catch block
						e3.printStackTrace();
					}

					System.out.println("DFS thread tDFS1 is running");
					{

						try {
							DistFileSysUtilityMethods.choose_WhereToSave();

							// DistFileSysUtilityMethods.fileUploading_DHT();// asking user to store a file
							// in DFS

						//	Fetch_file.createAndShowGUI();// asking user which file to retrieve/Delete

							// DistFileSysUtilityMethods.Application_Alive_Response_ByRecieverSide();
						} catch (IOException e2) {
							e2.printStackTrace();
						}

						while (!RxBufferDFS.isEmpty())// if receive Buffer DFS is not empty this if block will be
														// executed
						{
							System.out.println(
									"just waitomgklppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppppp");
							File inFile = RxBufferDFS.removeFirst();

							String[] info_from_xml = ParseXmlFile.ParseXml(inFile); // calling parsexml method of
																					// readxmlfile and storing its
																					// return value in a string array

							if (info_from_xml[0].equals("0110")) // information received from Xml, it is a Space query
																	// message.
							{
								String[] xmlParsedFields_0003 = ParseXmlFile.ParseXml_0003(inFile);
								System.out.println(
										"Request with tag 0110 received : request for asking the availibilty of storage space");

								Boolean bool = new Boolean(false);
								try {
									bool = DistFileSysUtilityMethods.checkSpace(xmlParsedFields_0003[2],
											xmlParsedFields_0003[3], xmlParsedFields_0003[4], xmlParsedFields_0003[5],
											xmlParsedFields_0003[6]);
								} catch (ParserConfigurationException | TransformerException | IOException e1) {
									e1.printStackTrace();
								}

								if (bool.equals(true)) {

									int remNodes = Integer.parseInt(xmlParsedFields_0003[5]);// value is 3 for first msg
									// remNodes=remNodes-1;
									if (remNodes == 3) {
										// info of originator node being stored in root nodes
										String[] root_ReplNode_Array = new String[20];
										root_ReplNode_Array[0] = xmlParsedFields_0003[3];// originator node id is added
										root_ReplNode_Array[1] = xmlParsedFields_0003[4];// originator ip add is added
										root_ReplNode_Array[6] = xmlParsedFields_0003[6];// direction is being added to
																							// know this is which root
																							// node 1/2;
										DistFileSys.root_Fileinfo_Map.put(xmlParsedFields_0003[2], root_ReplNode_Array);
										try {
											Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
										} catch (IOException e) {
											e.printStackTrace();
										}

									} else {// when remNodes equal to 2,1
										// info being stored in Node1 and Node2
										String originatorNodeId = xmlParsedFields_0003[6].substring(0, 40);
										String originatorIp = xmlParsedFields_0003[6].substring(40);
										String rootNodeId = xmlParsedFields_0003[3];
										String rootIp = xmlParsedFields_0003[4];
										String[] shared_info_Array = new String[20];
										shared_info_Array[0] = originatorNodeId;
										shared_info_Array[1] = originatorIp;
										shared_info_Array[2] = rootNodeId;
										shared_info_Array[3] = rootIp;
										shared_Fileinfo_Map.put(xmlParsedFields_0003[2], shared_info_Array);
										// node1 Node2 have the information of the root node and originator node
										// only the root node dosen't have the information of Node1 and Node2
										// this will be gathered by 0111 packet by 1 and 2
										try {
											Save_Retrive_data_Structures.Save_shared_Fileinfo_Map();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}

									System.out.println("Reply SpaceQueryMsg send");
									
										try {
											FileReceiverDemox.fileReceiver_Store(DistFileSysUtilityMethods.choose_WhereToSave());
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

										
									System.out.println("Recevier is ready to receive the Data");

								} else {
								}

							} else if (info_from_xml[0].equals("0111")) // Reply from node which intend to store my data
																		// with path=messageid.
							{
								System.out.println("reply of space query received");
								String[] xmlParsedFields_0003 = ParseXmlFile.ParseXml_0003(inFile);
								int remNodes = Integer.parseInt(info_from_xml[5]);
								// remNodes can have only three options 3,2,1
								// if 3 then reply is for the originator node
								// if 2,1 then reply is for the root node
								if (remNodes == 3) {
									String[] IpNode = new String[20];// to keep ipAdd and Nodeid for a
																		// particular chunk
									IpNode = nodeFileChunkMap.get(info_from_xml[2]);
									String direction = xmlParsedFields_0003[6];

									// if Direction is 1 then the reply is first root node, info saved at index 0,1
									// if Direction is 2 then the reply is second root node,info saved at index 2,3
									switch (Integer.parseInt(direction)) {
									case 1:
										IpNode[0] = info_from_xml[3];
										IpNode[1] = info_from_xml[4];
										break;
									case 2:
										IpNode[2] = info_from_xml[3];
										IpNode[3] = info_from_xml[4];
										break;
									// since a file is stored at two places ,hence the information of Ist storage is
									// at
									// index 0,1 and the information of 2nd storage is at index 2,3

									}
									try {
										Save_Retrive_data_Structures.Save_nodeFileChunkMap();
									} catch (IOException e) {
										e.printStackTrace();
									}
									fileSending_start(info_from_xml[2], info_from_xml[4]);// FILE NAME, IP

									System.out.println("Thread tDFS1- file added to file fragment Map");
									// some coding to maintain the replication set
								} else {
									switch (remNodes) {
									case 2:// info of Node1 being saved in root node
										String[] root_ReplNode_Array0 = new String[20];
										root_ReplNode_Array0 = DistFileSys.root_Fileinfo_Map
												.get(xmlParsedFields_0003[2]);
										root_ReplNode_Array0[2] = xmlParsedFields_0003[3];
										root_ReplNode_Array0[3] = xmlParsedFields_0003[4];
										break;
									case 1:// info of Node2 being saved in root node
										String[] root_ReplNode_Array1 = new String[20];
										root_ReplNode_Array1 = DistFileSys.root_Fileinfo_Map
												.get(xmlParsedFields_0003[2]);
										root_ReplNode_Array1[4] = xmlParsedFields_0003[3];
										root_ReplNode_Array1[5] = xmlParsedFields_0003[4];
										break;
									}
									try {
										Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

							} else if (info_from_xml[0].equals("0112"))// fetchFile msg
							{
								String fileName = info_from_xml[2];
								System.out.println("0112 packet recieved");
								Boolean bool = null;
								try {
									bool = DistFileSysUtilityMethods.searchFile(fileName);
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								if (bool.equals(true)) {
									String value = "found";
									try {
										System.out.println("file found w.r.t 0112");
										
										String myfile = DistFileSysUtilityMethods.choose_WhereToSave() + "\\"
												+ info_from_xml[2];
										FileSenderDemox.fileSender_Fetch(info_from_xml[4], myfile);
										// file sender started(ipadd, fileName)to fetch the file back
										
										DistFileSysUtilityMethods.fetchFileReply(info_from_xml[3],info_from_xml[2], value);
												
										// 0113 packet is send back to the node demanding the file
										
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										e.printStackTrace();
									} catch (TransformerException e) {
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								} else {
									String value = "not found";
									try {
										System.out.println("file not found w.r.t 0112");
										DistFileSysUtilityMethods.fetchFileReply(info_from_xml[3],info_from_xml[2], 
												value);
										DistFileSysUtilityMethods.fetchFile(PredecessorSuccessor.mySuccessors[0],
												fileName, info_from_xml[3],	info_from_xml[4]);// the mynodeId and myIp is not of root Node which
										// does not have the file; but of the originator which demanded the file
										// and the request now goes to the successor of the root Node.
									} catch (FileNotFoundException e) {
										e.printStackTrace();
									} catch (ParserConfigurationException e) {
										e.printStackTrace();
									} catch (TransformerException e) {
										e.printStackTrace();
									}
								}

							} else if (info_from_xml[0].equals("0113")) {
								if (info_from_xml[5].equals("found")) {
								} else {
									// LinkedHashMap<String, ArrayList<String>> fileFragMap = new
									// LinkedHashMap<String, ArrayList<String>>();
									// extracting the value for this File(which is selected)
									// the value is a linked hash map
									// fileFragMap = fileNodeMap.get(info_from_xml[1].substring(0,
									// info_from_xml[1].lastIndexOf("Chunk", info_from_xml[1].length())));
									// extracting the map

									String[] IpNode = new String[20];// to keep ipAdd and Nodeid for a
																		// particular chunk

									IpNode = nodeFileChunkMap.get(info_from_xml[2]);
									String toNodeId = IpNode[2];
									try {
										DistFileSysUtilityMethods.fetchFile(info_from_xml[2], toNodeId,
												OverlayManagement.myNodeId, DistFileSysUtilityMethods.myIp);

										FileReceiverDemox.fileReceiver_Fetch();

									} catch (ParserConfigurationException | IOException | TransformerException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

								}
							} else if (info_from_xml[0].equals("R100"))// msg by root node to form replication set
							{
								// xmlParsedFields_0003[6]: has the nodeId which still remains in the
								// replication set"nodeId"+"node1/node2"
								// node1: the node1 is missing
								// node2: the node2 is missing
								// or
								// when it is just "node1" then both node1 and node2 are reqd in replication set
								// when it is just "node2" then both node2 is reqd in replication set
								String[] xmlParsedFields_0003 = ParseXmlFile.ParseXml_0003(inFile);
								File file1 = null;
								try {
									file1 = new File(DistFileSysUtilityMethods.choose_WhereToSave());
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								long spaceAvailable = file1.getUsableSpace();// how much space is available on this
																				// platform

								if (OverlayManagement.myNodeId.equals(xmlParsedFields_0003[3])
										|| OverlayManagement.myNodeId.equals(xmlParsedFields_0003[6].substring(0, 40))
										|| spaceAvailable < (512 * 1024)) {// the my nodeid should not be Root node or
																			// the Node1/2cof the replication
																			// set;storage space shpuld be there
									try {
										DistFileSysUtilityMethods.include_NewNode_ReplicationSet(
												xmlParsedFields_0003[2], PredecessorSuccessor.myPredecessors[0],
												xmlParsedFields_0003[3], xmlParsedFields_0003[4],
												xmlParsedFields_0003[5], xmlParsedFields_0003[6]);
									} catch (FileNotFoundException | ParserConfigurationException
											| TransformerException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								} else {

									switch (xmlParsedFields_0003[6].substring(40)) {
									case "node_all":
										// both "Node1 and Node2" are reqd ; but first sending the reply msg for node1

										try {
											DistFileSysUtilityMethods.reply_Replication_msg(xmlParsedFields_0003[2],
													xmlParsedFields_0003[3], OverlayManagement.myNodeId,
													PresentIP.MyPresentIP, "node1");
											// one more Node to be included in the replication set, hence replication
											// msg for "node2"

											DistFileSysUtilityMethods.include_NewNode_ReplicationSet(
													xmlParsedFields_0003[2], PredecessorSuccessor.myPredecessors[0],
													xmlParsedFields_0003[3], xmlParsedFields_0003[4],
													xmlParsedFields_0003[5], xmlParsedFields_0003[3] + "node2");
										} catch (FileNotFoundException | ParserConfigurationException
												| TransformerException e1) {
											e1.printStackTrace();
										}
										System.out.println("Reply of Replication msg send");
										try {

											FileReceiverDemox
													.fileReceiver_Store(DistFileSysUtilityMethods.choose_WhereToSave());

										} catch (IOException e) {
											e.printStackTrace();
										}

										System.out.println("Recevier is ready to receive the Data");

										break;
									case "node2":
										// both "Node1 and Node2" were reqd ;action for "node1" is already done ;now for
										// "node2"
										try {
											DistFileSysUtilityMethods.reply_Replication_msg(xmlParsedFields_0003[2],
													xmlParsedFields_0003[3], OverlayManagement.myNodeId,
													PresentIP.MyPresentIP, "node2");
										} catch (FileNotFoundException | ParserConfigurationException
												| TransformerException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										System.out.println("Reply of Replication msg send");
										try {

											FileReceiverDemox
													.fileReceiver_Store(DistFileSysUtilityMethods.choose_WhereToSave());

										} catch (IOException e) {
											e.printStackTrace();
										}

										System.out.println("Recevier is ready to receive the Data");

										break;
									case "node1":

										try {
											DistFileSysUtilityMethods.reply_Replication_msg(xmlParsedFields_0003[2],
													xmlParsedFields_0003[3], OverlayManagement.myNodeId,
													PresentIP.MyPresentIP, "node1");
										} catch (FileNotFoundException | ParserConfigurationException
												| TransformerException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										System.out.println("Reply of Replication msg send");
										try {

											FileReceiverDemox
													.fileReceiver_Store(DistFileSysUtilityMethods.choose_WhereToSave());

										} catch (IOException e) {
											e.printStackTrace();
										}

										System.out.println("Recevier is ready to receive the Data");
										break;

									}

									String[] shared_info_Array = new String[20];
									shared_info_Array[0] = xmlParsedFields_0003[5].substring(0, 40);// inserting
																									// originator nodeId
									shared_info_Array[1] = xmlParsedFields_0003[5].substring(40);// inserting originator
																									// Ip
									shared_info_Array[2] = xmlParsedFields_0003[3];// inserting rootNode nodeId
									shared_info_Array[3] = xmlParsedFields_0003[4];// inserting rootNode nodeId
									shared_Fileinfo_Map.put(xmlParsedFields_0003[2], shared_info_Array);
									try {
										Save_Retrive_data_Structures.Save_shared_Fileinfo_Map();
									} catch (IOException e) {
										e.printStackTrace();
									}
								}

							} else if (info_from_xml[0].equals("R101")) {
								String[] root_ReplNode_Array = new String[20];
								if (info_from_xml[5].equals("node1")) {
									root_ReplNode_Array[2] = info_from_xml[3];
									root_ReplNode_Array[3] = info_from_xml[4];
								} else {
									root_ReplNode_Array[4] = info_from_xml[3];
									root_ReplNode_Array[5] = info_from_xml[4];
								}
								root_Fileinfo_Map.put(info_from_xml[2], root_ReplNode_Array);
								fileSending_start(info_from_xml[2], info_from_xml[4]);// sending the files to node 1/2
								// from root node
								try {
									Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else if (info_from_xml[0].equals("D100")) {
								String fileName = info_from_xml[2];
								Boolean bool = null;
								try {
									bool = DistFileSysUtilityMethods.searchFile(fileName);
								} catch (IOException e1) {
									e1.printStackTrace();
								}
								if (bool.equals(true)) {

									try {
										String myfile = DistFileSysUtilityMethods.choose_WhereToSave() + "\\"
												+ info_from_xml[2];
										File myFile = new File(myfile);
										myFile.delete();
										if (info_from_xml[5].equals("root")) {
											String[] root_ReplNode_Array = new String[20];
											root_ReplNode_Array = root_Fileinfo_Map.get(info_from_xml[2]);
											String nodeId1 = root_ReplNode_Array[2];
											String nodeId2 = root_ReplNode_Array[4];
											DistFileSysUtilityMethods.file_Deletion_query(info_from_xml[2], nodeId1,
													myNodeId, myIp, "shared");
											DistFileSysUtilityMethods.file_Deletion_query(info_from_xml[2], nodeId2,
													myNodeId, myIp, "shared");
											root_Fileinfo_Map.remove(info_from_xml[2]);
											Save_Retrive_data_Structures.Save_root_Fileinfo_Map();
										} else // when action=="shared"
										{
											shared_Fileinfo_Map.remove(info_from_xml[2]);
										}

									} catch (ParserConfigurationException | TransformerException | IOException e) {
										e.printStackTrace();
									}
								}

							} else if (info_from_xml[0].equals("R500")) {
								String[] NodeIpArray = new String[20];
								NodeIpArray = nodeFileChunkMap.get(info_from_xml[2]);// extracting the Arraylist

								NodeIpArray[10] = info_from_xml[3] + info_from_xml[5] + info_from_xml[4];

							}

						}
					}
				}
			}
		});

		tDFS1.start();

///////////////////////////////////////////////////////////////////////////////////////////////////////

		Timer RootNode_To_Originator = new Timer();
		TimerTask message_RootNode = new TimerTask() {
			@Override
			public void run() {
				Set<String> allFiles = root_Fileinfo_Map.keySet();
				for (String key : allFiles) {
					String[] root_ReplNode_Array = new String[20];
					root_ReplNode_Array = root_Fileinfo_Map.get(key);
					String tohashid = root_ReplNode_Array[0];
					String area = root_ReplNode_Array[7];
					try {
						DistFileSysUtilityMethods.root_To_Originator(tohashid, key, myNodeId, myIp, area);
						// Periodic msg from root node to originator to ascertain that no duplication of
						// root nodes
						// it is important to know that there are two root nodes at any one point of
						// time
					} catch (FileNotFoundException | ParserConfigurationException | TransformerException e) {
						e.printStackTrace();
					}

				}
			}
		};
		RootNode_To_Originator.schedule(message_RootNode, 120000, 120000);
///////////////////////////////////////////////////////////////////////////////////////////////////////

		Timer Action_By_Originator = new Timer();
		TimerTask Action = new TimerTask() {
			@Override
			public void run() {
				Set<String> allFiles = nodeFileChunkMap.keySet();
				for (String key : allFiles) {
					String[] NodeIdArea = new String[20];
					NodeIdArea = nodeFileChunkMap.get(key);
					for (Integer i = 10; i < 20; i++) {
						String area = NodeIdArea[i].substring(40, 41);
						if (area.equals("1")) {
							ArrayList<String> Area1 = new ArrayList<>();
							Area1.add(area);
							if (Area1.size() > 1) {
								try {
									DistFileSysUtilityMethods.file_Deletion_query(key, NodeIdArea[i], myNodeId, myIp,
											"root");
								} catch (FileNotFoundException | ParserConfigurationException
										| TransformerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();

								}
							} else {
								NodeIdArea[0] = NodeIdArea[i].substring(0, 40);
								NodeIdArea[1] = NodeIdArea[i].substring(41);
							}
						} else// if area equal to "2"
						{
							ArrayList<String> Area = new ArrayList<>();
							Area.add(area);
							if (Area.size() > 1) {
								try {
									DistFileSysUtilityMethods.file_Deletion_query(key, NodeIdArea[i], myNodeId, myIp,
											"root");
								} catch (FileNotFoundException | ParserConfigurationException
										| TransformerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								NodeIdArea[2] = NodeIdArea[i].substring(0, 40);
								NodeIdArea[3] = NodeIdArea[i].substring(41);
							}
						}

					}

				}
			}
		};
		Action_By_Originator.schedule(Action, 120000, 120000);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		Timer checkAliveStatus = new Timer();
		TimerTask status = new TimerTask() {
			@Override
			public void run() {
				try {
					DistFileSysUtilityMethods.check_Alive_Status();
				} catch (FileNotFoundException | ParserConfigurationException | TransformerException e) {
					e.printStackTrace();
				}
			}
		};
		checkAliveStatus.schedule(status, 60000, 60000);
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////	

	public static void fileSending_start(String chunkName, String IpAdd) {
		System.out.println(" (File Transmitter) is running");

		synchronized (lock_TransBuff_Main) {

			try {
				FileSenderDemox.fileSender_Store(IpAdd, new File(chunkName));

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			System.out.println("IpAdd for saving file   " + IpAdd);
		}

	}

	
	

	
}

