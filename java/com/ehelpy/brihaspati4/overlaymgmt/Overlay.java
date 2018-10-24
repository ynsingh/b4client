package com.ehelpy.brihaspati4.overlaymgmt;

import java.net.InetAddress;
	import java.util.ArrayList;
	import java.util.Random;
	import java.io.File;
	import java.lang.String;
	import java.lang.StringBuffer;
	import java.lang.Exception;
	import java.lang.Integer;

public class Overlay {
	

		/*
		 *  main()
		 *  {
		 *    current routing table = read the current node routing table form file.
		 *    neighbour's table = read the neighbours table from file.
		 *    current routing table = merge routing table ( current routing table, neighbour's table).
		 *  }
		 * 
		 * 
		 * 
		 */
//public void finalize(){System.out.println("Garbage collected");}  

		@SuppressWarnings("unused")
		public static void OverlayMgmt() {// "OverlayMgmt()" is the method to be called from object New7 class.
												// An object of the New7 class needs to be created 
												//in the actual main() method
												//and this method needs to be called 
												//  (1)  At every 30 mins or so using timer  
												// 				 OR
												//  (2)  In event of any of the below :
												//		(a) new Node Addition  OR
												//		(b) Node Departure (gracefully OR w/o info)  OR
												//		(c) Node dies(no ping response).
			CorrectHex CH = new CorrectHex();
			//TableBuffer TB = new TableBuffer();
			int numchars = 10;

			//**************************************	SELF NODE ID	**************************************
			
			XML_RTConversion XML_RT = new XML_RTConversion(); //Object of XML_RTConversion class


			String Self_NodeID = XML_RT.getSelfNodeID();// declaring and getting self Node ID(10 hex characters only)

			System.out.println("Self Node ID in Self Routing Table (NewSelfRouteTable.xml)  :  " + Self_NodeID); // checking Self NodeID
			System.out.println("");
			char[] Self_NodeIDarray = Self_NodeID.toCharArray(); // convert String to char array 
																 //to get independent Hex characters
			byte[] Self_NodeIDbytearray = CH.CorrectHexString(Self_NodeID);// convert String directly to corrected byte array.

			

//**************************************	SELF ROUTING TABLE	**************************************
			
			String[][] Self_RT = new String[3][10];   // declaring RT array of 3 rows(P/S/M) 
													  // and 10 columns (10 hex-character node IDs)
			//XML_RTConversion XML_RT = new XML_RTConversion()already declared above.
			Self_RT = XML_RT.XMLtoRT("NewSelfRouteTable.xml");// this is "null" if the node is newly registered.
			
			// Displaying self Routing Table
			
			/*System.out.println("Existing Self Routing Table in New7: ");
			System.out.println("      2                    4                     d                     f                     2                      1                    5                    b                     d                    c");
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
			int rows = 3;
			int columns = 10;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < columns; j++) {
					System.out.print(Self_RT[i][j] + "         ");
				}
				System.out.println();
			}
			System.out.println("");*/

//******** Selecting Node IDs after extracting NodeIDs from selected XMLfiles with Tag="0002"
			
			TableBuffer4 TB = new TableBuffer4();		// creating object of Table buffer4
			ArrayList<File> O_FileArray = TB.CheckTag("0002"); // segregating XML file with TAg = "0002"
															   // (O_FileArray = OutputFileArray in called method)
			int max_size = 30;
			for(int i = 0; i < (O_FileArray.size()); i++){		// For processing the segregated files one- by-one.
				NewXMLtoStringArray XSA = new NewXMLtoStringArray();// Creating a NodeID list from selected XML file
				String[] RTNodeList = new String[max_size];
				String S = (O_FileArray.get(i)).getPath();// or maybe getName()
				RTNodeList = XSA.XMLToStringArray(S);



				//**************************************	INCOMING  NODE ID	**************************************
				System.out.println("This is segregated XML file No.:  "+i);
				System.out.println("**********************************************************************************");
				for (int x=0; x < 30; x++){

					//String RTNodeListelement= TO BE ACQUIRED FROM NODE-ID entries of received Routing Tables. Read xml files with "RT  exchange" tag 
					//and extract NODE-ID entries.

					String RTNodeListelement= RTNodeList[x];
					String incoming_NodeID = RTNodeListelement;
					char[] incoming_NodeIDarray = incoming_NodeID.toCharArray(); // convert String to char array 
					 															//to get independent Hex characters
					byte[] incoming_NodeIDbytearray = CH.CorrectHexString(incoming_NodeID);// convert String directly to corrected byte array.
					//System.out.println("incoming_NodeID      : " + incoming_NodeID);
					System.out.println("");		

					System.out.println(" This is for Routing Table's Entry No. ----> " + x ); //  (0 <= x <= 29)
					System.out.println("");
					System.out.println("CANDIDATE is  :  "+incoming_NodeID); 
					System.out.println("");
					int j = 0;			
					for (j = 0; j < 10; j++) {								// for traversing 10 columns of self RT
						char[] Self_RTarrayP = Self_RT[0][j].toCharArray();  // char array of P
						char[] Self_RTarrayS = Self_RT[1][j].toCharArray();	 // char array of S	
						char[] Self_RTarrayM = Self_RT[2][j].toCharArray();  // char array of M

						byte[] Self_RTbytearrayP = CH.CorrectHexString(Self_RT[0][j]);// convert to array of bytes for P
						byte[] Self_RTbytearrayS = CH.CorrectHexString(Self_RT[1][j]);// convert to array of bytes for S
						byte[] Self_RTbytearrayM = CH.CorrectHexString(Self_RT[2][j]);// convert to array of bytes for M

						/*int inc_self = incoming_NodeID.compareToIgnoreCase(Self_NodeID);
						int inc_RT_P = incoming_NodeID.compareToIgnoreCase(Self_RT[0][j]);
						int inc_RT_S = incoming_NodeID.compareToIgnoreCase(Self_RT[1][j]);
						int inc_RT_M = incoming_NodeID.compareToIgnoreCase(Self_RT[2][j]);*/

						//String Exact_Middle_NodeID = (String)(((int)Self_NodeID + 2^15)%(2^16));

						
						// Calculating the exact middle (diagonally opposite) character on 0-15 ring 
						//for each character of self NodeID and converting them into byte datatype.
						
						byte[] Exact_Middlebytearray = new byte[10];// = CH.CorrectHexString(Exact_middle_NodeID);// convert to array of bytes for P
						
						for(int n=0; n<10;n++){

							Exact_Middlebytearray[n] = (byte)((Self_NodeIDbytearray[n]+8)%16);//finalizing the exact middle values for 
																							  // for every char of incoming nodeID
						}
						
						

						if (incoming_NodeIDbytearray[j] != Self_NodeIDbytearray[j]){
							
							// Allow entry into the column at possibly at P/S/M/ or discard if not found suitable.
							
							System.out.println(" I != S ..hence enter into the column NO. :" + (j));

							//System.out.println("  ");

							//*********************** PROBLEM CONDITION- 1 ( Problem for Predecessor ) ******************

							if ((0 <= Self_NodeIDbytearray[j]) && (Self_NodeIDbytearray[j]<= 3)){

								//FOR PREDECESSOR - "0-crossing" correction required.
								//FOR SUCCESSOR   - No change.
								//FOR MIDDLE      - No change.
								
								System.out.println(" PROBLEM CONDITION- 1 ");
								
								//"0-crossing" correction
								if(((Self_NodeIDbytearray[j]+12)%16 > (incoming_NodeIDbytearray[j]+12)%16)// self is ahead of incoming
										&&(((Self_NodeIDbytearray[j]+12)%16) - (incoming_NodeIDbytearray[j]+12)%16) <= 4){// I is in range of Predecessor
									System.out.println("  PROBLEM CONDITION- 1 - Predecessor selected");
									//System.out.println(" simple P has been selected");

									if (incoming_NodeIDbytearray[j] != Self_RTbytearrayP[j]){

										System.out.println(" I is a candidate for P  and I != P");

										int SminusI = ((Self_NodeIDbytearray[j]+12)%16) - ((incoming_NodeIDbytearray[j]+12)%16);
										int SminusP = ((Self_NodeIDbytearray[j]+12)%16) - ((Self_RTbytearrayP[j]+12)%16);
										
										//comparing with existing P
										if((incoming_NodeIDbytearray[j]+12)%16 > (Self_RTbytearrayP[j]+12)%16){	

											Self_RT[0][j] = incoming_NodeID;
											System.out.println("P is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if((incoming_NodeIDbytearray[j]+12)%16 < (Self_RTbytearrayP[j]+12)%16){				//compare with existing P

											//Self_RT[0][j] = incoming_NodeID;
											System.out.println("P is NOT replaced by I");
											//System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();

											}
										}
										else if(Self_RT[0][j] == null){	

											Self_RT[0][j] = incoming_NodeID;
											System.out.println("Existing P is null ....P is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayP[j]){

										System.out.println(" I is a candidate for P and I == P");
										
										System.out.println("P is NOT replaced by I");
										

										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										/*
										System.out.println(" ");
										int m = 0;
										m = j+1;
										
										while(m < 4){
											int m_SminusI = ((Self_NodeIDbytearray[m]+12)%16) - ((incoming_NodeIDbytearray[m]+12)%16);
											int m_SminusP = ((Self_NodeIDbytearray[m]+12)%16) - ((Self_RTbytearrayP[m]+12)%16);

											if(((incoming_NodeIDbytearray[m]+12)%16) > ((Self_RTbytearrayP[m]+12)%16)){//incoming[j+1/2/3] is ahead of existing P
												System.out.println("P is replaced by I");
												//System.out.println(" Debug P1");
												Self_RT[0][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
												break;
											}
											else if(((incoming_NodeIDbytearray[m]+12)%16) < ((Self_RTbytearrayP[m]+12)%16)){//incoming[j+1/2/3] is ahead of existing P
												System.out.println("P is NOT replaced by I");
												//System.out.println(" Debug P1");
												//Self_RT[0][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
												break;
											}
											else if(((incoming_NodeIDbytearray[m]+12)%16 == (Self_RTbytearrayP[m]+12)%16) && (m==3)){
												System.out.println("P is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											m++;
											//if (flag == true){
											//	break;
											//}
										}
									*/}//break;
									else if(Self_RT[0][j] == null){	

										Self_RT[0][j] = incoming_NodeID;
										System.out.println("Existing P is null ....P is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}

								// FOR SUCCESSOR - No change in code. Status Quo..
								else if(Self_NodeIDbytearray[j]%16 < incoming_NodeIDbytearray[j]%16// self is behind incoming
										&&(incoming_NodeIDbytearray[j] - Self_NodeIDbytearray[j]) <= 4){
									System.out.println("  PROBLEM CONDITION- 1 - Successor selected - NO Problem");
									if(incoming_NodeIDbytearray[j] != Self_RTbytearrayS[j]){						
										System.out.println(" I is a candidate for S  and I != S   ");

										if ((incoming_NodeIDbytearray[j] - Self_NodeIDbytearray[j])%16 < (Self_RTbytearrayS[j] -Self_NodeIDbytearray[j])%16){

											System.out.println("S is replaced by I");
											Self_RT[1][j] = incoming_NodeID;
											System.out.println(Self_RT[1][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if(Self_RT[1][j] == null){	

											Self_RT[1][j] = incoming_NodeID;
											System.out.println("Existing S is null ....S is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else{
											System.out.println("S is NOT replaced by I");

											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
											//break;
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayS[j]){

										System.out.println(" I is a candidate for S  and I == S");
										System.out.println("S is NOT replaced by I");
										

										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										/*int m = 0;
										m = j+1;							

										while(m < 10){
											
											if(incoming_NodeIDbytearray[m] < Self_RTbytearrayS[m]){
												System.out.println("S is replaced by I");
												//System.out.println(" Debug S1");
												Self_RT[1][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if(incoming_NodeIDbytearray[m] > Self_RTbytearrayS[m]){
												System.out.println("S is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[1][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if((incoming_NodeIDbytearray[m] == Self_RTbytearrayP[m]) && (m==9)){
												System.out.println("S and I are same NodeIDs hence S is being retained");
												//System.out.println("S and I are same NodeIDs");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											m++;

										}
									*/}
									else if(Self_RT[1][j] == null){	

										Self_RT[1][j] = incoming_NodeID;
										System.out.println("Existing S is null ....S is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}

								// FOR MIDDLE
								else {
									System.out.println("  PROBLEM CONDITION- 1 - Middle selected - NO Problem");
									if(incoming_NodeIDbytearray[j] != Self_RTbytearrayM[j]){						
										System.out.println(" I is a candidate for M  and I != M");

										if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){
											System.out.println("I ---- E ----- M ");
											if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");								
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
											else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												//Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is  NOT replaced by I");								
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
											

										}
										else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){
											System.out.println("M ---- E ----- I ");
											if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
											else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												System.out.println("M is  NOT replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
										}
										else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){
											System.out.println("E ---- M ----- I ");
											System.out.println("       OR        ");
											System.out.println("E ---- I ----- M ");
											if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												System.out.println("M is replaced by I");
												Self_RT[2][j] = incoming_NodeID;
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
											else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												System.out.println("M is NOT replaced by I");
												//Self_RT[2][j] = incoming_NodeID;
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
										}
										else if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){
											System.out.println("I ---- M ----- E ");
											System.out.println("       OR        ");
											System.out.println("M ---- I ----- E ");
											if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
											else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												//Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is NOT replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
													//flag = true;
												}
											}
										}
										else if((Exact_Middlebytearray[j] == incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] != Self_RTbytearrayM[j])){

											Self_RT[2][j] = incoming_NodeID;
											System.out.println("M is replaced by I");
											System.out.println(Self_RT[2][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
												//flag = true;
											}
										}
										else if((Exact_Middlebytearray[j] != incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] == Self_RTbytearrayM[j])){

											//Self_RT[2][j] = incoming_NodeID;
											System.out.println("M is NOT replaced by I");
											System.out.println(Self_RT[2][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
												//flag = true;
											}
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayM[j]){

										System.out.println(" I is a candidate for M  and I == M");
										System.out.println("M is NOT replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										/*System.out.println(" This is Possible only when I and M are on same side of E"
												+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
												+ "or        I ---- M ----- E   /  M ---- I ----- E "
												+ "Moving to next nibble.");
										int m = 0;
										m = j+1;							

										while(m < 4){
											if((Exact_Middlebytearray[m-1] > incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] > Self_RTbytearrayM[m-1])){
												System.out.println("E ---- M ----- I ");
												System.out.println("       OR        ");
												System.out.println("E ---- I ----- M ");

												if ((incoming_NodeIDbytearray[m])%16 > (Self_RTbytearrayM[m])%16){
													System.out.println("E ---- I ----- M ");
													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
														//flag = true;
													}
												}
												else if ((incoming_NodeIDbytearray[m])%16 < (Self_RTbytearrayM[m])%16){
													System.out.println("E ---- M ----- I ");
													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
														//flag = true;
													}
												}
												else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
													System.out.println("M is NOT replaced by I");
													//System.out.println(" Debug S1");
													//Self_RT[0][j] = incoming_NodeID;

													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}

												}
												break;
											}

											else if((Exact_Middlebytearray[m-1] < incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] < Self_RTbytearrayM[m-1])){
												System.out.println("I ---- M ----- E ");
												System.out.println("       OR        ");
												System.out.println("M ---- I ----- E ");

												if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

													Self_RT[2][j] = incoming_NodeID;
													System.out.println("M is replaced by I");
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
														//flag = true;
													}
												}
												else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

													//Self_RT[2][j] = incoming_NodeID;
													System.out.println("M is NOT replaced by I");
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
														//flag = true;
													}
												}
												else if((incoming_NodeIDbytearray[m] == Self_RTbytearrayP[m]) && (m==3)){
													System.out.println("M is NOT replaced by I");
													//System.out.println(" Debug S1");
													//Self_RT[0][j] = incoming_NodeID;

													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}

												}
												break;
											}
											else if((Exact_Middlebytearray[m-1] == incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] == Self_RTbytearrayM[m-1])){
												System.out.println(" catch the rabbit");

												while(m < 4){
													if((incoming_NodeIDbytearray[m]) != Self_RTbytearrayM[m]){						
														System.out.println(" I is a candidate for M  and I != M");

														if(((Exact_Middlebytearray[m]) < (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) > Self_RTbytearrayM[m])){
															System.out.println(" debug 1");
															if (((incoming_NodeIDbytearray[m]) - (Exact_Middlebytearray[m]))%16 < ((Exact_Middlebytearray[m]) -Self_RTbytearrayM[m])%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((incoming_NodeIDbytearray[m]) - (Exact_Middlebytearray[m]))%16 > ((Exact_Middlebytearray[m]) -Self_RTbytearrayM[m])%16){
																System.out.println(" debug 1");
																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m]) > (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) < Self_RTbytearrayM[m])){
															System.out.println(" debug 2");

															if (((Exact_Middlebytearray[m]) - (incoming_NodeIDbytearray[m]))%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m]))%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m]) - (incoming_NodeIDbytearray[m]))%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m]))%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m]) > (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) > Self_RTbytearrayM[m])){

															System.out.println(" debug 3");
															if (((Exact_Middlebytearray[m]) - (incoming_NodeIDbytearray[m]))%16 < ((Exact_Middlebytearray[m]) - Self_RTbytearrayM[m])%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m]) - (incoming_NodeIDbytearray[m]))%16 > ((Exact_Middlebytearray[m]) - Self_RTbytearrayM[m])%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m]) < (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) < Self_RTbytearrayM[m])){

															System.out.println(" debug 4");
															if (((incoming_NodeIDbytearray[m]) - (Exact_Middlebytearray[m]))%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m]))%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((incoming_NodeIDbytearray[m]) - (Exact_Middlebytearray[m]))%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m]))%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m]) == (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) != Self_RTbytearrayM[m])){

															Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														else if(((Exact_Middlebytearray[m]) != (incoming_NodeIDbytearray[m]))&&((Exact_Middlebytearray[m]) == Self_RTbytearrayM[m])){

															//Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is NOT replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														break;
													}

													else if((incoming_NodeIDbytearray[m]) == Self_RTbytearrayM[m]){
														System.out.println(" I is a candidate for M  and I == M");
														System.out.println(" This is Possible only when I and M are on same side of E"
																+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
																+ "or        I ---- M ----- E   /  M ---- I ----- E "
																+ "Moving to next nibble.");

														int m1 = 0;
														m1 = m+1;
														if((((Exact_Middlebytearray[m]))%16 > ((incoming_NodeIDbytearray[m]))%16)&&(((Exact_Middlebytearray[m]))%16 > (Self_RTbytearrayM[m])%16)){
															System.out.println("E ---- M ----- I ");
															System.out.println("       OR        ");
															System.out.println("E ---- I ----- M ");

															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16))){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																		//flag = true;
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																		//flag = true;
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}
														}

														else if(((Exact_Middlebytearray[m1])%16 < (incoming_NodeIDbytearray[m1])%16)&&((Exact_Middlebytearray[m-1]) < (Self_RTbytearrayM[m-1]))){
															System.out.println("I ---- M ----- E vaibbhav ");
															System.out.println("       OR        ");
															System.out.println("M ---- I ----- E ");

															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1]))%16)){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																		//flag = true;
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1]))%16 > ((Self_RTbytearrayM[m1]))%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																		//flag = true;
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1]))%16 == ((Self_RTbytearrayM[m1]))%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}

														}
														break;
													}
													m++;
												}
												m++;
											}

											m++;
										}

									*/}
									else if(Self_RT[2][j] == null){	

										Self_RT[2][j] = incoming_NodeID;
										System.out.println("Existing M is null ....M is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}
							}

							//*********************** PROBLEM CONDITION- 2 ( Problem for Successor )******************

							else if((Self_NodeIDbytearray[j] >= 12 ) && (Self_NodeIDbytearray[j] <= 15)){
								System.out.println("  PROBLEM CONDITION- 2 - Problem for Successor");
								
								// FOR PREDECESSOR - No change in code.
								if(Self_NodeIDbytearray[j]%16 > incoming_NodeIDbytearray[j]%16// self is ahead of incoming
										&&(Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j]) <= 4){
									System.out.println("  PROBLEM CONDITION- 1 - Predecessor selected - NO Problem");

									if(incoming_NodeIDbytearray[j] != Self_RTbytearrayP[j]){						
										System.out.println(" I is a candidate for P  and I != P###");

										if ((Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j])%16 < (Self_NodeIDbytearray[j] - Self_RTbytearrayP[j])%16){

											System.out.println("P is replaced by I ###");
											Self_RT[0][j] = incoming_NodeID;
											System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if ((Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j])%16 > (Self_NodeIDbytearray[j] - Self_RTbytearrayP[j])%16){
											
											System.out.println("P is NOT replaced by I ###");
											//Self_RT[0][j] = incoming_NodeID;
											System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayP[j]){

										System.out.println(" I is a candidate for P  and I == P");
										
										System.out.println("P is NOT replaced by I");
										

										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										/*
										int m = 0;
										if(j < 3){
											m = j+1;
											while(m < 4){

												if(incoming_NodeIDbytearray[m] > Self_RTbytearrayP[m]){
													System.out.println("P is replaced by I");
													Self_RT[0][j] = incoming_NodeID;

													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													break;
												}
												else if(incoming_NodeIDbytearray[m] < Self_RTbytearrayP[m]){
													System.out.println("P is NOT replaced by I");
													
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													break;
												}

												m++;
											}
										}
										else if((incoming_NodeIDbytearray[m] == Self_RTbytearrayP[m]) && (j==3)){
											System.out.println("P is NOT replaced by I");
											//System.out.println(" Debug S1");
											//Self_RT[0][j] = incoming_NodeID;

											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
											break;
										}

									*/}
									else if(Self_RT[0][j] == null){	

										Self_RT[0][j] = incoming_NodeID;
										System.out.println("Existing P is null ....P is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}

								// FOR SUCCESSOR - code needs to be changed.
								else if(((Self_NodeIDbytearray[j]-12)%16 < (incoming_NodeIDbytearray[j]-12)%16)// self is behind of incoming
										&&((incoming_NodeIDbytearray[j]-12)%16 - ((Self_NodeIDbytearray[j]-12)%16)) <= 4){
									System.out.println("  PROBLEM CONDITION- 2 - Successor  selected -  Problem");

									if (incoming_NodeIDbytearray[j] != Self_RTbytearrayS[j]){

										System.out.println(" I is a candidate for S  and I != S");

										int SminusI = ((incoming_NodeIDbytearray[j]-12)%16) - ((Self_NodeIDbytearray[j]-12)%16);
										int SminusS = ((Self_RTbytearrayS[j]-12)%16) - ((Self_NodeIDbytearray[j]-12)%16);
										//System.out.println("SminusI is  "+ SminusI);
										//System.out.println("SminusP is  "+ SminusP);
										//if((SminusI < SminusP)||(SminusI > SminusP)){
										if(SminusI < SminusS){				//compare with existing S

											System.out.println("S is replaced by I");
											Self_RT[1][j] = incoming_NodeID;
											//System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if(SminusI > SminusS){				//compare with existing S

											System.out.println("S is NOT replaced by I");
											//Self_RT[1][j] = incoming_NodeID;
											//System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}

									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayS[j]){

										System.out.println(" I is a candidate for S and I == S");
										
										System.out.println("");
										System.out.println("S is NOT replaced by I");
										

										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										/*int m = 0;
										m = j+1;

										while(m < 4){
											int m_SminusI = ((incoming_NodeIDbytearray[m]-12)%16) - ((Self_NodeIDbytearray[m]-12)%16);
											int m_SminusP = ((Self_RTbytearrayS[m]-12)%16) - ((Self_NodeIDbytearray[m]-12)%16);

											if ((incoming_NodeIDbytearray[m]-12)%16 < (Self_RTbytearrayS[m]-12)%16){//incoming[j+1/2/3] is ahead of existing P
												System.out.println("S is replaced by I");
												//System.out.println(" Debug P1");
												Self_RT[1][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if ((incoming_NodeIDbytearray[m]-12)%16 > (Self_RTbytearrayS[m]-12)%16){//incoming[j+1/2/3] is ahead of existing P
												System.out.println("S is NOT replaced by I");
												//System.out.println(" Debug P1");
												//Self_RT[1][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
												System.out.println("S is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											m++;
										}
									*/}
									else if(Self_RT[1][j] == null){	

										Self_RT[1][j] = incoming_NodeID;
										System.out.println("Existing S is null ....S is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}

								}

								// FOR MIDDLE
								else {
									System.out.println("  PROBLEM CONDITION- 2 -Middle  selected -  NO Problem");
									if(incoming_NodeIDbytearray[j] != Self_RTbytearrayM[j]){

										System.out.println(" I is a candidate for M  and I != M");

										if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){
											if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Exact_Middlebytearray[j] -Self_RTbytearrayM[j])%16){

												System.out.println("M is replaced by I");
												Self_RT[2][j] = incoming_NodeID;
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												//System.out.println(Self_RT[2][j]);
											}
											else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Exact_Middlebytearray[j] -Self_RTbytearrayM[j])%16){

												System.out.println("M is NOT replaced by I");
												//Self_RT[2][j] = incoming_NodeID;
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												//System.out.println(Self_RT[2][j]);
											}
										}
										else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){
											if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												System.out.println("M is replaced by I");
												Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												System.out.println("M is NOT replaced by I");
												//Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
										}
										else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){
											if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												System.out.println("M is replaced by I");
												Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

												System.out.println("M is replaced NOT by I");
												//Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
										}
										else if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){
											if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												System.out.println(" M is replaced by I");
												Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

												System.out.println(" M is NOT replaced by I");
												//Self_RT[2][j] = incoming_NodeID;
												//System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
										}
										else if((Exact_Middlebytearray[j] == incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] != Self_RTbytearrayM[j])){

											Self_RT[2][j] = incoming_NodeID;
											System.out.println("M is replaced by I");
											System.out.println(Self_RT[2][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}

										}
										else if((Exact_Middlebytearray[j] != incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] == Self_RTbytearrayM[j])){

											//Self_RT[2][j] = incoming_NodeID;
											System.out.println("M is NOT replaced by I");
											System.out.println(Self_RT[2][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}

									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayM[j]){

										System.out.println(" I is a candidate for M  and I == M");
										System.out.println("M is NOT replaced by I");
										
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										
										/*System.out.println(" This is Possible only when I and M are on same side of E"
												+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
												+ "or        I ---- M ----- E   /  M ---- I ----- E "
												+ "Moving to next nibble.");
										int m = 0;
										m = j+1;							

										while(m < 4){
											if((Exact_Middlebytearray[m-1] > incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] > Self_RTbytearrayM[m-1])){
												System.out.println("E ---- M ----- I ");
												System.out.println("       OR        ");
												System.out.println("E ---- I ----- M ");
												if ((incoming_NodeIDbytearray[m])%16 > (Self_RTbytearrayM[m])%16){
													System.out.println("E ---- I ----- M ");
													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((incoming_NodeIDbytearray[m])%16 < (Self_RTbytearrayM[m])%16){
													System.out.println("E ---- M ----- I ");
													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
													System.out.println("M is NOT replaced by I");
													//System.out.println(" Debug S1");
													//Self_RT[0][j] = incoming_NodeID;

													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}

												}
												break;
											}

											else if((Exact_Middlebytearray[m-1] < incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] < Self_RTbytearrayM[m-1])){
												System.out.println("I ---- M ----- E ");
												System.out.println("       OR        ");
												System.out.println("M ---- I ----- E ");
												if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

													Self_RT[2][j] = incoming_NodeID;
													System.out.println("M is replaced by I");
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

													//Self_RT[2][j] = incoming_NodeID;
													System.out.println("M is NOT replaced by I");
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
													System.out.println("M is NOT replaced by I");
													//System.out.println(" Debug S1");
													//Self_RT[0][j] = incoming_NodeID;

													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													break;
												}
												break;
											}
											else if((Exact_Middlebytearray[m-1] == incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] == Self_RTbytearrayM[m-1])){
												System.out.println(" catch the rabbit");

												while(m < 4){
													if(incoming_NodeIDbytearray[m] != Self_RTbytearrayM[m]){						
														System.out.println(" I is a candidate for M  and I != M");

														if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){
															System.out.println(" debug 1");
															if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){
																System.out.println(" debug 1");
																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){
															System.out.println(" debug 2");

															if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){

															System.out.println(" debug 3");
															if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){

															System.out.println(" debug 4");
															if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 == incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 != (Self_RTbytearrayM[m])%16)){

															Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														else if(((Exact_Middlebytearray[m])%16 != incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 == (Self_RTbytearrayM[m])%16)){

															//Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is NOT replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														break;
													}


													else if(incoming_NodeIDbytearray[m] == (Self_RTbytearrayM[m])%16){
														System.out.println(" I is a candidate for M  and I == M");
														System.out.println(" This is Possible only when I and M are on same side of E"
																+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
																+ "or        I ---- M ----- E   /  M ---- I ----- E "
																+ "Moving to next nibble.");

														int m1 = 0;
														m1 = m+1;
														if((((Exact_Middlebytearray[m])%16)%16 > (incoming_NodeIDbytearray[m])%16)&&(((Exact_Middlebytearray[m])%16)%16 > ((Self_RTbytearrayM[m])%16)%16)){
															System.out.println("E ---- M ----- I ");
															System.out.println("       OR        ");
															System.out.println("E ---- I ----- M ");


															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16))){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}
														}

														else if(((Exact_Middlebytearray[m1])%16 < (incoming_NodeIDbytearray[m1])%16)&&((Exact_Middlebytearray[m-1])%16 < (Self_RTbytearrayM[m-1])%16)){
															System.out.println("I ---- M ----- E vaibbhav ");
															System.out.println("       OR        ");
															System.out.println("M ---- I ----- E ");

															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1]))%16)){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1]))%16 > ((Self_RTbytearrayM[m1]))%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1]))%16 == ((Self_RTbytearrayM[m1]))%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}

														}
														break;
													}
													m++;
												}
												m++;
											}

											m++;
										}

									*/}
									else if(Self_RT[2][j] == null){	

										Self_RT[2][j] = incoming_NodeID;
										System.out.println("Existing M is null ....M is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}

								}
							}

							//*********************** CONDITION - 3  ( No Problem for P/S...but Problem for M )******************

							else if ((Self_NodeIDbytearray[j] > 3) && (Self_NodeIDbytearray[j] < 12)){// safe zone 4 to 11

								// FOR PREDECESSOR -
								if(Self_NodeIDbytearray[j]%16 > incoming_NodeIDbytearray[j]%16// self is ahead of incoming
										&&(Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j]) <= 4){
									System.out.println("  PROBLEM CONDITION- 3 - Predecessor selected -  NO Problem");
									if(incoming_NodeIDbytearray[j] != Self_RTbytearrayP[j]){						
										System.out.println(" I is a candidate for P  and I != P%%%");

										if ((Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j])%16 < (Self_NodeIDbytearray[j] - Self_RTbytearrayP[j])%16){

											System.out.println("P is replaced by I %%%");
											Self_RT[0][j] = incoming_NodeID;
											System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if ((Self_NodeIDbytearray[j] - incoming_NodeIDbytearray[j])%16 > (Self_NodeIDbytearray[j] - Self_RTbytearrayP[j])%16){

											System.out.println("P is NOT replaced by I %%%");
											//Self_RT[0][j] = incoming_NodeID;
											System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayP[j]){

										System.out.println(" I is a candidate for P  and I == P");
										System.out.println("P is NOT replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}

										/*int m = 0;
										m = j+1;							

										while(m < 4){

											if(incoming_NodeIDbytearray[m] > Self_RTbytearrayP[m]){
												System.out.println("P is replaced by I");
												//System.out.println(" Debug S1");
												Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if(incoming_NodeIDbytearray[m] < Self_RTbytearrayP[m]){
												System.out.println("P is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if((incoming_NodeIDbytearray[m] == Self_RTbytearrayP[m]) && (m==3)){
												System.out.println("P is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											m++;
										}
									*/}
									else if(Self_RT[0][j] == null){	

										Self_RT[0][j] = incoming_NodeID;
										System.out.println("Existing P is null ....P is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}

								// FOR SUCCESSOR - .
								else if(((Self_NodeIDbytearray[j])%16 < (incoming_NodeIDbytearray[j])%16)// self is ahead of incoming
										&&((incoming_NodeIDbytearray[j]%16) - (Self_NodeIDbytearray[j]%16)) <= 4){

									System.out.println("  PROBLEM CONDITION- 3 - Successor selected -  NO Problem");

									if (incoming_NodeIDbytearray[j] != Self_RTbytearrayS[j]){

										System.out.println(" I is a candidate for S  and I != S");

										int SminusI = ((incoming_NodeIDbytearray[j])%16)- ((Self_NodeIDbytearray[j])%16);
										int SminusS = ((Self_RTbytearrayS[j])%16) - ((Self_NodeIDbytearray[j])%16);
										//System.out.println("SminusI is  "+ SminusI);
										//System.out.println("SminusP is  "+ SminusP);
										//if((SminusI < SminusP)||(SminusI > SminusP)){
										if(SminusI < SminusS){				//compare with existing S

											System.out.println("S is replaced by I");
											Self_RT[1][j] = incoming_NodeID;
											//System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										else if(SminusI > SminusS){				//compare with existing S

											System.out.println("S is NOT replaced by I");
											//Self_RT[1][j] = incoming_NodeID;
											//System.out.println(Self_RT[0][j]);
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
									}
									else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayS[j]){

										System.out.println(" I is a candidate for S and I == S");
										System.out.println("S is NOT replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
										System.out.println(" ");
										/*int m = 0;
										m = j+1;

										while(m < 4){
											//int m_SminusI = (((incoming_NodeIDbytearray[m])%16 - (Self_NodeIDbytearray[m])%16));
											//int m_SminusS = (((Self_RTbytearrayS[m])%16 - (Self_NodeIDbytearray[m])%16));

											if(incoming_NodeIDbytearray[m] < Self_RTbytearrayS[m]){//incoming[j+1/2/3] is behind of existing S
												System.out.println("S is replaced by I");
												//System.out.println(" Debug P1");
												Self_RT[1][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if(incoming_NodeIDbytearray[m] > Self_RTbytearrayS[m]){//incoming[j+1/2/3] is behind of existing S
												System.out.println("S is NOT replaced by I");
												//System.out.println(" Debug P1");
												//Self_RT[1][j] = incoming_NodeID;
												//System.out.println(Self_RT[0][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											else if((incoming_NodeIDbytearray[m] == Self_RTbytearrayM[m]) && (m==3)){
												System.out.println("S is NOT replaced by I");
												//System.out.println(" Debug S1");
												//Self_RT[0][j] = incoming_NodeID;

												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
												break;
											}
											m++;
										}
									*/}
									else if(Self_RT[1][j] == null){	

										Self_RT[1][j] = incoming_NodeID;
										System.out.println("Existing S is null ....S is replaced by I");
										for (int r = 0; r < 3; r++) {
											for (int z = 0; z < 10; z++) {
												System.out.print(Self_RT[r][z] + "         ");
											}
											System.out.println();
										}
									}
								}

								// FOR MIDDLE
								else{
									System.out.println("  PROBLEM CONDITION- 3 - Middle selected -  Problem");

									if((Self_NodeIDbytearray[j] >= 5 ) && (Self_NodeIDbytearray[j] <= 7)){

										System.out.println("  Succesor - like problem .. case 1");
										//System.out.println("  PROBLEM CONDITION- 3 -Middle  selected -  NO Problem");
										if((incoming_NodeIDbytearray[j]-10)%16 != (Self_RTbytearrayM[j]-10)%16){

											System.out.println(" I is a candidate for M  and I != M");
											
											if(((Exact_Middlebytearray[j]-10)%16 < (incoming_NodeIDbytearray[j]-10)%16)&&((Exact_Middlebytearray[j]-10)%16 > (Self_RTbytearrayM[j]-10)%16)){
												if (((incoming_NodeIDbytearray[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16 < ((Exact_Middlebytearray[j]-10)%16 -(Self_RTbytearrayM[j]-10)%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													//System.out.println(Self_RT[2][j]);
												}
												else if (((incoming_NodeIDbytearray[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16 >= ((Exact_Middlebytearray[j]-10)%16 -(Self_RTbytearrayM[j]-10)%16)%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													//System.out.println(Self_RT[2][j]);
												}
											}
											else if(((Exact_Middlebytearray[j]-10)%16 > (incoming_NodeIDbytearray[j]-10)%16)&&((Exact_Middlebytearray[j]-10)%16 < (Self_RTbytearrayM[j]-10)%16)){
												if (((Exact_Middlebytearray[j]-10)%16 - (incoming_NodeIDbytearray[j]-10)%16)%16 < ((Self_RTbytearrayM[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((Exact_Middlebytearray[j]-10)%16 - (incoming_NodeIDbytearray[j]-10)%16)%16 >= ((Self_RTbytearrayM[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j]-10)%16 > (incoming_NodeIDbytearray[j]-10)%16)&&((Exact_Middlebytearray[j]-10)%16 > (Self_RTbytearrayM[j]-10)%16)){
												if (((Exact_Middlebytearray[j]-10)%16 - (incoming_NodeIDbytearray[j]-10)%16)%16 < ((Exact_Middlebytearray[j]-10)%16 - (Self_RTbytearrayM[j]-10)%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((Exact_Middlebytearray[j]-10)%16 - (incoming_NodeIDbytearray[j]-10)%16)%16 >= ((Exact_Middlebytearray[j]-10)%16 - (Self_RTbytearrayM[j]-10)%16)%16){

													System.out.println("M is replaced NOT by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j]-10)%16 < (incoming_NodeIDbytearray[j]-10)%16)&&((Exact_Middlebytearray[j]-10)%16 < (Self_RTbytearrayM[j]-10)%16)){
												if (((incoming_NodeIDbytearray[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16 < ((Self_RTbytearrayM[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16){

													System.out.println(" M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((incoming_NodeIDbytearray[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16 >= ((Self_RTbytearrayM[j]-10)%16 - (Exact_Middlebytearray[j]-10)%16)%16){

													System.out.println(" M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j]-10)%16 == (incoming_NodeIDbytearray[j]-10)%10)&&((Exact_Middlebytearray[j]-10)%16 != (Self_RTbytearrayM[j]-10)%16)){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if(((Exact_Middlebytearray[j]-10)%16 != (incoming_NodeIDbytearray[j]-10)%10)&&((Exact_Middlebytearray[j]-10)%16 == (Self_RTbytearrayM[j]-10)%16)){

												//Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is NOT replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
										}
										else if((incoming_NodeIDbytearray[j]-10)%16 == (Self_RTbytearrayM[j]-10)%16){

											System.out.println(" I is a candidate for M  and I == M");
											System.out.println("M is NOT replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
											
											/*System.out.println(" This is Possible only when I and M are on same side of E"
													+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
													+ "or        I ---- M ----- E   /  M ---- I ----- E "
													+ "Moving to next nibble.");
											int m = 0;
											m = j+1;							

											while(m < 4){
												if(((Exact_Middlebytearray[m-1]-10)%16 > (incoming_NodeIDbytearray[m-1]-10)%16)&&((Exact_Middlebytearray[m-1]-10)%16 > (Self_RTbytearrayM[m-1]-10)%16)){
													System.out.println("E ---- M ----- I ");
													System.out.println("       OR        ");
													System.out.println("E ---- I ----- M ");

													if (((incoming_NodeIDbytearray[m]-10)%16 > ((Self_RTbytearrayM[m]-10)%16)%16)){
														System.out.println("E ---- I ----- M ");
														System.out.println("M is replaced by I");
														Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													else if (((incoming_NodeIDbytearray[m]-10)%16)%16 < ((Self_RTbytearrayM[m]-10)%16)%16){
														System.out.println("E ---- M ----- I ");
														System.out.println("M is NOT replaced by I");
														//Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
														System.out.println("M is NOT replaced by I");
														//System.out.println(" Debug S1");
														//Self_RT[0][j] = incoming_NodeID;

														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													break;
												}

												else if(((Exact_Middlebytearray[m-1]-10)%16 < (incoming_NodeIDbytearray[m-1]-10)%16)&&((Exact_Middlebytearray[m-1]-10)%16 < (Self_RTbytearrayM[m-1]-10)%16)){
													System.out.println("I ---- M ----- E ");
													System.out.println("       OR        ");
													System.out.println("M ---- I ----- E ");

													if (((incoming_NodeIDbytearray[m]-10)%16 - ((Exact_Middlebytearray[m])%16-10)%16)%16 < ((Self_RTbytearrayM[m]-10)%16 - ((Exact_Middlebytearray[m])%16-10)%16)%16){

														Self_RT[2][j] = incoming_NodeID;
														System.out.println("M is replaced by I");
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													else if (((incoming_NodeIDbytearray[m]-10)%16 - ((Exact_Middlebytearray[m])%16-10)%16)%16 > ((Self_RTbytearrayM[m]-10)%16 - ((Exact_Middlebytearray[m])%16-10)%16)%16){

														//Self_RT[2][j] = incoming_NodeID;
														System.out.println("M is NOT replaced by I");
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													else if((incoming_NodeIDbytearray[m]%16 == Self_RTbytearrayP[m]%16) && (m==3)){
														System.out.println("M is NOT replaced by I");
														//System.out.println(" Debug S1");
														//Self_RT[0][j] = incoming_NodeID;

														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
													}
													break;
												}
												else if((Exact_Middlebytearray[m-1] == incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] == Self_RTbytearrayM[m-1])){
													System.out.println(" catch the rabbit");

													while(m < 4){
														if(incoming_NodeIDbytearray[m] != Self_RTbytearrayM[m]){						
															System.out.println(" I is a candidate for M  and I != M");

															if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){
																System.out.println(" debug 1");
																if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){

																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	//System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
																else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){
																	System.out.println(" debug 1");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	//System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
															}
															else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){
																System.out.println(" debug 2");

																if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
																else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
															}
															else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){

																System.out.println(" debug 3");
																if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
																else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
															}
															else if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){

																System.out.println(" debug 4");
																if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
																else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																}
															}
															else if(((Exact_Middlebytearray[m])%16 == incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 != (Self_RTbytearrayM[m])%16)){

																Self_RT[2][j] = incoming_NodeID;
																System.out.println("M is replaced by I");
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if(((Exact_Middlebytearray[m])%16 != incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 == (Self_RTbytearrayM[m])%16)){

																//Self_RT[2][j] = incoming_NodeID;
																System.out.println("M is NOT replaced by I");
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															break;
														}

														else if(incoming_NodeIDbytearray[m] == (Self_RTbytearrayM[m])%16){
															System.out.println(" I is a candidate for M  and I == M");
															System.out.println(" This is Possible only when I and M are on same side of E"
																	+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
																	+ "or        I ---- M ----- E   /  M ---- I ----- E "
																	+ "Moving to next nibble.");

															int m1 = 0;
															m1 = m+1;
															if((((Exact_Middlebytearray[m])%16)%16 > (incoming_NodeIDbytearray[m])%16)&&(((Exact_Middlebytearray[m])%16)%16 > ((Self_RTbytearrayM[m])%16)%16)){
																System.out.println("E ---- M ----- I ");
																System.out.println("       OR        ");
																System.out.println("E ---- I ----- M ");

																while(m1 < 4){
																	if (((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16))){
																		System.out.println("E ---- I ----- M ");
																		System.out.println("M is replaced by I");
																		Self_RT[2][j] = incoming_NodeID;
																		System.out.println(Self_RT[2][j]);
																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){
																		System.out.println("E ---- M ----- I ");
																		System.out.println("M is NOT replaced by I");
																		//Self_RT[2][j] = incoming_NodeID;
																		System.out.println(Self_RT[2][j]);
																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
																		System.out.println("M is NOT replaced by I");
																		//System.out.println(" Debug S1");
																		//Self_RT[0][j] = incoming_NodeID;

																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	m1++;
																}
															}

															else if(((Exact_Middlebytearray[m1])%16 < (incoming_NodeIDbytearray[m1])%16)&&((Exact_Middlebytearray[m-1])%16 < (Self_RTbytearrayM[m-1])%16)){
																System.out.println("I ---- M ----- E vaibbhav ");
																System.out.println("       OR        ");
																System.out.println("M ---- I ----- E ");



																while(m1 < 4){
																	if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1]))%16)){
																		System.out.println("E ---- I ----- M ");
																		System.out.println("M is replaced by I");
																		Self_RT[2][j] = incoming_NodeID;
																		System.out.println(Self_RT[2][j]);
																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	else if (((incoming_NodeIDbytearray[m1]))%16 > ((Self_RTbytearrayM[m1]))%16){
																		System.out.println("E ---- M ----- I ");
																		System.out.println("M is NOT replaced by I");
																		//Self_RT[2][j] = incoming_NodeID;
																		System.out.println(Self_RT[2][j]);
																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	else if((((incoming_NodeIDbytearray[m1]))%16 == ((Self_RTbytearrayM[m1]))%16) && (m1==3)){
																		System.out.println("M is NOT replaced by I");
																		//System.out.println(" Debug S1");
																		//Self_RT[0][j] = incoming_NodeID;

																		for (int r = 0; r < 3; r++) {
																			for (int z = 0; z < 10; z++) {
																				System.out.print(Self_RT[r][z] + "         ");
																			}
																			System.out.println();
																		}
																		break;
																	}
																	m1++;
																}

															}
															break;
														}
														m++;
													}
													m++;
												}
												m++;
											}

										*/}
										else if(Self_RT[2][j] == null){	

											Self_RT[2][j] = incoming_NodeID;
											System.out.println("Existing M is null ....M is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}

									}

									else if((Self_NodeIDbytearray[j] >= 8 ) && (Self_NodeIDbytearray[j] <= 10)){
										//(incoming_NodeIDbytearray[j]+12)%16
										System.out.println("  Predecessor - like problem...case 2 ");
										if((incoming_NodeIDbytearray[j])%16 != (Self_RTbytearrayM[j])%16){

											System.out.println(" I is a candidate for M  and I != M");
											//  (incoming_NodeIDbytearray[j]+12)%16
											//  (Exact_Middlebytearray[j]-10)%16
											//  (Self_RTbytearrayM[j])%16
											if(((Exact_Middlebytearray[j])%16 < (incoming_NodeIDbytearray[j])%16)&&((Exact_Middlebytearray[j])%16 > (Self_RTbytearrayM[j])%16)){
												if (((incoming_NodeIDbytearray[j])%16 - (Exact_Middlebytearray[j])%16)%16 < ((Exact_Middlebytearray[j])%16 -(Self_RTbytearrayM[j])%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													//System.out.println(Self_RT[2][j]);
												}
												else if (((incoming_NodeIDbytearray[j])%16 - (Exact_Middlebytearray[j])%16)%16 >= ((Exact_Middlebytearray[j])%16 -(Self_RTbytearrayM[j])%16)%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
													//System.out.println(Self_RT[2][j]);
												}
											}
											else if(((Exact_Middlebytearray[j])%16 > (incoming_NodeIDbytearray[j])%16)&&((Exact_Middlebytearray[j])%16 < (Self_RTbytearrayM[j])%16)){
												if (((Exact_Middlebytearray[j])%16 - (incoming_NodeIDbytearray[j])%16)%16 < ((Self_RTbytearrayM[j])%16 - (Exact_Middlebytearray[j])%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((Exact_Middlebytearray[j])%16 - (incoming_NodeIDbytearray[j])%16)%16 >= ((Self_RTbytearrayM[j])%16 - (Exact_Middlebytearray[j])%16)%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j])%16 > (incoming_NodeIDbytearray[j])%16)&&((Exact_Middlebytearray[j])%16 > (Self_RTbytearrayM[j])%16)){
												if (((Exact_Middlebytearray[j])%16 - (incoming_NodeIDbytearray[j])%16)%16 < ((Exact_Middlebytearray[j])%16 - (Self_RTbytearrayM[j])%16)%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((Exact_Middlebytearray[j])%16 - (incoming_NodeIDbytearray[j])%16)%16 >= ((Exact_Middlebytearray[j])%16 - (Self_RTbytearrayM[j])%16)%16){

													System.out.println("M is NOT replaced  by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j])%16 < (incoming_NodeIDbytearray[j])%16)&&((Exact_Middlebytearray[j])%16 < (Self_RTbytearrayM[j])%16)){
												if (((incoming_NodeIDbytearray[j])%16 - (Exact_Middlebytearray[j])%16)%16 < ((Self_RTbytearrayM[j])%16 - (Exact_Middlebytearray[j])%16)%16){

													System.out.println(" M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if (((incoming_NodeIDbytearray[j])%16 - (Exact_Middlebytearray[j])%16)%16 >= ((Self_RTbytearrayM[j])%16 - (Exact_Middlebytearray[j])%16)%16){

													System.out.println(" M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if(((Exact_Middlebytearray[j])%16 == (incoming_NodeIDbytearray[j])%10)&&((Exact_Middlebytearray[j])%16 != (Self_RTbytearrayM[j])%16)){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if(((Exact_Middlebytearray[j])%16 != (incoming_NodeIDbytearray[j])%10)&&((Exact_Middlebytearray[j])%16 == (Self_RTbytearrayM[j])%16)){

												//Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is NOT replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}

										}
										else if((incoming_NodeIDbytearray[j])%16 == (Self_RTbytearrayM[j])%16){

											System.out.println(" I is a candidate for M  and I == M");
											System.out.println("M is NOT replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
											/*System.out.println(" This is Possible only when I and M are on same side of E"
													+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
													+ "or        I ---- M ----- E   /  M ---- I ----- E "
													+ "Moving to next nibble.");

											int m = j+1;
											if(((Exact_Middlebytearray[m-1])%16 > (incoming_NodeIDbytearray[m-1])%16)&&((Exact_Middlebytearray[m-1])%16 > (Self_RTbytearrayM[m-1])%16)){
												System.out.println("E ---- M ----- I ");
												System.out.println("       OR        ");
												System.out.println("E ---- I ----- M ");
												int m1 = 0;
												m1 = j+1;							

												while(m1 < 4){
													if (((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16)%16)){
														System.out.println("E ---- I ----- M ");
														System.out.println("M is replaced by I");
														Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){
														System.out.println("E ---- M ----- I ");
														System.out.println("M is NOT replaced by I");
														//Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
														System.out.println("M is NOT replaced by I");
														//System.out.println(" Debug S1");
														//Self_RT[0][j] = incoming_NodeID;

														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													m1++;
												}
											}

											else if(((Exact_Middlebytearray[m-1])%16 < (incoming_NodeIDbytearray[m-1])%16)&&((Exact_Middlebytearray[m-1])%16 < (Self_RTbytearrayM[m-1])%16)){
												System.out.println("I ---- M ----- E vaibbhav ");
												System.out.println("       OR        ");
												System.out.println("M ---- I ----- E ");

												int m1 = 0;
												m1 = j+1;							

												while(m1 < 4){
													if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1])%16)%16)){
														System.out.println("E ---- I ----- M ");
														System.out.println("M is replaced by I");
														Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if (((incoming_NodeIDbytearray[m1])%16)%16 > ((Self_RTbytearrayM[m1])%16)%16){
														System.out.println("E ---- M ----- I ");
														System.out.println("M is NOT replaced by I");
														//Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
														System.out.println("M is NOT replaced by I");
														//System.out.println(" Debug S1");
														//Self_RT[0][j] = incoming_NodeID;

														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													m1++;
												}
											}
											else if(((Exact_Middlebytearray[m-1])%16 == (incoming_NodeIDbytearray[m-1])%16)&&((Exact_Middlebytearray[m-1])%16 == (Self_RTbytearrayM[m-1])%16)){
												System.out.println(" catch the rabbit");

												while(m < 4){
													if((incoming_NodeIDbytearray[m])%16 != Self_RTbytearrayM[m]){						
														System.out.println(" I is a candidate for M  and I != M");

														if(((Exact_Middlebytearray[m])%16 < (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 > Self_RTbytearrayM[m])){
															System.out.println(" debug 1");
															if (((incoming_NodeIDbytearray[m])%16 - (Exact_Middlebytearray[m])%16)%16 < ((Exact_Middlebytearray[m])%16 -Self_RTbytearrayM[m])%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((incoming_NodeIDbytearray[m])%16 - (Exact_Middlebytearray[m])%16)%16 > ((Exact_Middlebytearray[m])%16 -Self_RTbytearrayM[m])%16){
																System.out.println(" debug 1");
																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 < Self_RTbytearrayM[m])){
															System.out.println(" debug 2");

															if (((Exact_Middlebytearray[m])%16 - (incoming_NodeIDbytearray[m])%16)%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - (incoming_NodeIDbytearray[m])%16)%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 > Self_RTbytearrayM[m])){

															System.out.println(" debug 3");
															if (((Exact_Middlebytearray[m])%16 - (incoming_NodeIDbytearray[m])%16)%16 < ((Exact_Middlebytearray[m])%16 - Self_RTbytearrayM[m])%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - (incoming_NodeIDbytearray[m])%16)%16 > ((Exact_Middlebytearray[m])%16 - Self_RTbytearrayM[m])%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 < (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 < Self_RTbytearrayM[m])){

															System.out.println(" debug 4");
															if (((incoming_NodeIDbytearray[m])%16 - (Exact_Middlebytearray[m])%16)%16 < (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((incoming_NodeIDbytearray[m])%16 - (Exact_Middlebytearray[m])%16)%16 > (Self_RTbytearrayM[m] - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 == (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 != Self_RTbytearrayM[m])){

															Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														else if(((Exact_Middlebytearray[m])%16 != (incoming_NodeIDbytearray[m])%16)&&((Exact_Middlebytearray[m])%16 == Self_RTbytearrayM[m])){

															//Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is NOT replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														break;
													}

													else if((incoming_NodeIDbytearray[m])%16 == Self_RTbytearrayM[m]){
														System.out.println(" I is a candidate for M  and I == M");
														System.out.println(" This is Possible only when I and M are on same side of E"
																+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
																+ "or        I ---- M ----- E   /  M ---- I ----- E "
																+ "Moving to next nibble.");

														int m1 = 0;
														m1 = m+1;
														if((((Exact_Middlebytearray[m])%16)%16 > ((incoming_NodeIDbytearray[m])%16)%16)&&(((Exact_Middlebytearray[m])%16)%16 > (Self_RTbytearrayM[m])%16)){
															System.out.println("E ---- M ----- I ");
															System.out.println("       OR        ");
															System.out.println("E ---- I ----- M ");


															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16))){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}
														}

														else if(((Exact_Middlebytearray[m1])%16 < (incoming_NodeIDbytearray[m1])%16)&&((Exact_Middlebytearray[m-1])%16 < (Self_RTbytearrayM[m-1])%16)){
															System.out.println("I ---- M ----- E vaibbhav ");
															System.out.println("       OR        ");
															System.out.println("M ---- I ----- E ");



															while(m1 < 4){
																if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1]))%16)){
																	System.out.println("E ---- I ----- M ");
																	System.out.println("M is replaced by I");
																	Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if (((incoming_NodeIDbytearray[m1]))%16 > ((Self_RTbytearrayM[m1]))%16){
																	System.out.println("E ---- M ----- I ");
																	System.out.println("M is NOT replaced by I");
																	//Self_RT[2][j] = incoming_NodeID;
																	System.out.println(Self_RT[2][j]);
																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																else if((((incoming_NodeIDbytearray[m1]))%16 == ((Self_RTbytearrayM[m1]))%16) && (m1==3)){
																	System.out.println("M is NOT replaced by I");
																	//System.out.println(" Debug S1");
																	//Self_RT[0][j] = incoming_NodeID;

																	for (int r = 0; r < 3; r++) {
																		for (int z = 0; z < 10; z++) {
																			System.out.print(Self_RT[r][z] + "         ");
																		}
																		System.out.println();
																	}
																	break;
																}
																m1++;
															}

														}
														break;
													}
													m++;
												}
												m++;
											}

										*/}
										else if(Self_RT[2][j] == null){	

											Self_RT[2][j] = incoming_NodeID;
											System.out.println("Existing M is null ....M is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}
										
									}

									else if((Self_NodeIDbytearray[j] == 4 ) || (Self_NodeIDbytearray[j] == 11)){
										System.out.println("  PROBLEM CONDITION-3. MIDDLE SELECTED..No Problem Case..."
												+ " CASE 3...(BECAUSE Range of Middle is ONLY 3)  ");	

										if(incoming_NodeIDbytearray[j] != Self_RTbytearrayM[j]){						
											System.out.println(" I is a candidate for M  and I != M");

											if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){
												System.out.println(" debug 1");
												if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Exact_Middlebytearray[j] -Self_RTbytearrayM[j])%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Exact_Middlebytearray[j] -Self_RTbytearrayM[j])%16){
													System.out.println(" debug 1");
													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													//System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){
												System.out.println(" debug 2");

												if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if((Exact_Middlebytearray[j] > incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] > Self_RTbytearrayM[j])){

												System.out.println(" debug 3");
												if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 < (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((Exact_Middlebytearray[j] - incoming_NodeIDbytearray[j])%16 >= (Exact_Middlebytearray[j] - Self_RTbytearrayM[j])%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if((Exact_Middlebytearray[j] < incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] < Self_RTbytearrayM[j])){

												System.out.println(" debug 4");
												if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 < (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

													System.out.println("M is replaced by I");
													Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
												else if ((incoming_NodeIDbytearray[j] - Exact_Middlebytearray[j])%16 >= (Self_RTbytearrayM[j] - Exact_Middlebytearray[j])%16){

													System.out.println("M is NOT replaced by I");
													//Self_RT[2][j] = incoming_NodeID;
													System.out.println(Self_RT[2][j]);
													for (int r = 0; r < 3; r++) {
														for (int z = 0; z < 10; z++) {
															System.out.print(Self_RT[r][z] + "         ");
														}
														System.out.println();
													}
												}
											}
											else if((Exact_Middlebytearray[j] == incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] != Self_RTbytearrayM[j])){

												Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}
											else if((Exact_Middlebytearray[j] != incoming_NodeIDbytearray[j])&&(Exact_Middlebytearray[j] == Self_RTbytearrayM[j])){

												//Self_RT[2][j] = incoming_NodeID;
												System.out.println("M is NOT replaced by I");
												System.out.println(Self_RT[2][j]);
												for (int r = 0; r < 3; r++) {
													for (int z = 0; z < 10; z++) {
														System.out.print(Self_RT[r][z] + "         ");
													}
													System.out.println();
												}
											}

										}
										else if(incoming_NodeIDbytearray[j] == Self_RTbytearrayM[j]){
											int m =j+1;
											System.out.println(" I is a candidate for M  and I == M....same side ");
											System.out.println("M is NOT replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
											/*if((Exact_Middlebytearray[m-1] > incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] > Self_RTbytearrayM[m-1])){
												System.out.println(" debug 3");
												System.out.println("E ---- M ----- I ");
												System.out.println("       OR        ");
												System.out.println("E ---- I ----- M ");

												int m1 = 0;
												m1 = j+1;				
												while(m1 < 4){											
													if ((incoming_NodeIDbytearray[m1])%16 > ((Self_RTbytearrayM[m1])%16)%16){

														System.out.println("E ---- I ----- M ");
														System.out.println("M is replaced by I");
														Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if (((incoming_NodeIDbytearray[m1])%16)%16 < ((Self_RTbytearrayM[m1])%16)%16){

														System.out.println("E ---- M ----- I ");
														System.out.println("M is NOT replaced by I");
														//Self_RT[2][j] = incoming_NodeID;
														System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;	
													}

													else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m==3)){

														System.out.println("M is NOT replaced by I");
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													m++;
												}
											}

											else if((Exact_Middlebytearray[m-1] < incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] < Self_RTbytearrayM[m-1])){	
												System.out.println("I ---- M ----- E ");
												System.out.println("       OR        ");
												System.out.println("M ---- I ----- E ");

												int m1;
												m1=j+1;
												while(m1 < 4){

													if (((incoming_NodeIDbytearray[m1])%16 < ((Self_RTbytearrayM[m1])%16)%16)){
														System.out.println("E ---- I ----- M ");
														System.out.println("M is replaced by I");
														Self_RT[2][j] = incoming_NodeID;
														//System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if (((incoming_NodeIDbytearray[m1])%16)%16 > ((Self_RTbytearrayM[m1])%16)%16){
														System.out.println("E ---- M ----- I ");
														System.out.println("M is NOT replaced by I");
														//Self_RT[2][j] = incoming_NodeID;
														//System.out.println(Self_RT[2][j]);
														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													else if((((incoming_NodeIDbytearray[m1])%16)%16 == ((Self_RTbytearrayM[m1])%16)%16) && (m1==3)){
														System.out.println("M is NOT replaced by I");
														//System.out.println(" Debug S1");
														//Self_RT[0][j] = incoming_NodeID;

														for (int r = 0; r < 3; r++) {
															for (int z = 0; z < 10; z++) {
																System.out.print(Self_RT[r][z] + "         ");
															}
															System.out.println();
														}
														break;
													}
													m1++;
												}
											}
											else if((Exact_Middlebytearray[m-1] == incoming_NodeIDbytearray[m-1])&&(Exact_Middlebytearray[m-1] == Self_RTbytearrayM[m-1])){
												System.out.println(" catch the rabbit");

												while(m < 4){
													if(incoming_NodeIDbytearray[m] != Self_RTbytearrayM[m]){						
														System.out.println(" I is a candidate for M  and I != M");

														if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){
															System.out.println(" debug 1");
															if ((incoming_NodeIDbytearray[m])%16 - (Exact_Middlebytearray[m])%16 < ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Exact_Middlebytearray[m])%16 -(Self_RTbytearrayM[m])%16)%16){
																System.out.println(" debug 1");
																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																//System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){
															System.out.println(" debug 2");

															if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 > incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 > (Self_RTbytearrayM[m])%16)){

															System.out.println(" debug 3");
															if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 < ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if (((Exact_Middlebytearray[m])%16 - incoming_NodeIDbytearray[m])%16 > ((Exact_Middlebytearray[m])%16 - (Self_RTbytearrayM[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 < incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 < (Self_RTbytearrayM[m])%16)){

															System.out.println(" debug 4");
															if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 < ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is replaced by I");
																Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
															else if ((incoming_NodeIDbytearray[m] - (Exact_Middlebytearray[m])%16)%16 > ((Self_RTbytearrayM[m])%16 - (Exact_Middlebytearray[m])%16)%16){

																System.out.println("M is NOT replaced by I");
																//Self_RT[2][j] = incoming_NodeID;
																System.out.println(Self_RT[2][j]);
																for (int r = 0; r < 3; r++) {
																	for (int z = 0; z < 10; z++) {
																		System.out.print(Self_RT[r][z] + "         ");
																	}
																	System.out.println();
																}
															}
														}
														else if(((Exact_Middlebytearray[m])%16 == incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 != (Self_RTbytearrayM[m])%16)){

															Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														else if(((Exact_Middlebytearray[m])%16 != incoming_NodeIDbytearray[m])&&((Exact_Middlebytearray[m])%16 == (Self_RTbytearrayM[m])%16)){

															//Self_RT[2][j] = incoming_NodeID;
															System.out.println("M is NOT replaced by I");
															System.out.println(Self_RT[2][j]);
															for (int r = 0; r < 3; r++) {
																for (int z = 0; z < 10; z++) {
																	System.out.print(Self_RT[r][z] + "         ");
																}
																System.out.println();
															}
														}
														break;
													}
													else if(incoming_NodeIDbytearray[m] == (Self_RTbytearrayM[m])%16){
														System.out.println(" I is a candidate for M  and I == M");
														System.out.println(" This is Possible only when I and M are on same side of E"
																+ "i.e. when E ---- M ----- I   /  E ---- I ----- M"
																+ "or        I ---- M ----- E   /  M ---- I ----- E "
																+ "Moving to next nibble.");

														int m1 = 0;
														m1 = m+1;
														
													}
													m++;
												}
											}


										*/}
										else if(Self_RT[2][j] == null){	

											Self_RT[2][j] = incoming_NodeID;
											System.out.println("Existing M is null ....M is replaced by I");
											for (int r = 0; r < 3; r++) {
												for (int z = 0; z < 10; z++) {
													System.out.print(Self_RT[r][z] + "         ");
												}
												System.out.println();
											}
										}

									}

								}

								break;
							}
							break;
						}	
					}
				}
				System.out.println("");
				System.out.println(" No. of XML files worked upon = "+ (i+1));
				
			}
			System.out.println("");
			System.out.println(" The Final updated Self Routing Table is as under ");
			System.out.println("");
			System.out.println("                                                 Updated ROUTING TABLE after OVERLAY MANAGEMENT ");
			System.out.println("      2                    4                     d                     f                     2                      1                    5                    b                     d                    c");
			System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------");
			int rws = 3;
			int clmns = 10;
			for (int i = 0; i < rws; i++) {
				for (int j = 0; j < clmns; j++) {
					System.out.print(Self_RT[i][j] + "         ");
				}
				System.out.println();
			}
			System.out.println("");
			XML_RT.SelfRTArraytoXML(Self_RT);
			System.gc();
			//return Self_RT;
		}
	}
