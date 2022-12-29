package program2;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;

//Racheli Reich Program
public class NewClientCode {

	public static void main(String[] args) throws IOException{
		
		//hard code the IP address and port number
		args = new String[] {"127.0.0.1", "11158"};
		
		//initialize arrayList for received Packets as objects
		ArrayList<Packet> packetsObj = new ArrayList<>();
		
		//an array to hold IDs of packets
		int[] IDs;
		
		//an array to hold the IDs of missing packets
		int[] missingPackets;
		
		
		//if IP and port not passed in, end program
		 if (args.length != 2) {
	            System.err.println(
	                "Usage: java EchoClient <host name> <port number>");
	            System.exit(1);
	        }
		 
		 String IPAddress = args[0];
	     int portNumber = Integer.parseInt(args[1]);
	     
	     try (
	    		 Socket clientSocket = new Socket(IPAddress, portNumber);
	    		 // stream to write text requests to server
	    		 PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
	    		 // stream to read text response from server
	    		 BufferedReader responseReader= new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
	    		 ){
	    	 
	    	 //read packets from server
	    	 String packetReceived  = responseReader.readLine();
	    	 while(!packetReceived.equals(null) ){
	    		 
	    		 //create packet into object with id and message
	    		 Packet packetObj = packetToObject(packetReceived);
	    		 
	    		 //add object to list of received packets
	    		 packetsObj.add(packetObj);
	    		 
	    		 
	    		 //read the next line from server
	    		 packetReceived  = responseReader.readLine();
	    		
	    		 
	    	 }
	    
	    	 //display the packets received
	    	 System.out.println("packets received " + packetsObj);
	    	 
	    	 //put the received packets in order by their id
	    	 sortPackets(packetsObj);
	    	 
	    	 
	    	 //get the total number of packets to receive, and initialize array to hold ID
	    	 int ttlPackets = getTotalNumPackets(packetsObj);
	    	 IDs = new int[ttlPackets];
	    	 
	    	 //all Ids of packets received to the array
	    	 addIDsToArray(IDs, packetsObj);
	    	 
	    	 //get the id of missing packets and put into array
	    	 missingPackets = addMissingIdsToarray(IDs);
	    	 
	    	 //request the missing packets from server
	    	for(int z = 0; z < missingPackets.length; z++) {
	    		 
	    		 //request the missing packet from server
	    		 requestWriter.println(String.valueOf(missingPackets[z]));
	    		 System.out.println("requesting missing packet " + missingPackets[0]);
	    		 
	    		 //read the server response
	    		String serverResponse = responseReader.readLine();
	    		 
	    		 
	    		 //if server response is not null, create Packet object and add to array
	    		 if (serverResponse !=  null) {
	    			 
	    			
	    			 System.out.println("server received packet " + serverResponse);
	    			 //create packet into object with id and message
		    		 Packet packetObj = packetToObject(serverResponse);
		    		//add object to list of received packets
		    		 packetsObj.add(packetObj);
	    		 }
	    		 //if serverResponse is null, keep looping until get a packet 
	    		 //and then create Packet object and add to array
	    		 else {
	    			 System.out.println("Still waiting to receive packet...");
	    			 while(serverResponse == null) {
	    				 serverResponse = responseReader.readLine();
	    			 }
	    			 
	    			 System.out.println("received packet " + serverResponse);
	    			 
	    			 //create packet into object with id and message
		    		 Packet packetObj = packetToObject(serverResponse);
		    		 
		    		 //add object to list of received packets
		    		 packetsObj.add(packetObj); 
	    		 }	 
	    	 }   	
	     }
	     
	     
	     catch (UnknownHostException e) {
	            System.err.println("Don't know about host " + IPAddress);
	            System.exit(1);
	        } catch (IOException e) {
	            System.err.println("Couldn't get I/O for the connection to " +
	                IPAddress);
	            System.exit(1);
	        }

	}
	/**
	 * the packetToObject method gets the Id and message 
	 * of packet and creates a Packet object
	 * @param packet the packet the client receives from server
	 * @return the Packet object
	 */
	private static Packet packetToObject(String packet) {
		//get the id of packet as a String
		//as per protocol, first two characters of packet is the id of packet
		String StringID = packet.substring(0, 2);
		
		//parse the String id to an int
		int id = Integer.parseInt(StringID);
		
		//display the substring(id) and then id 
		System.out.println("Substring " + StringID + " \tId " + id);
		
		//get the message from packet
		//as per protocol the third character till end of string is the message (besides for the last packet)
		String msg = packet.substring(2, packet.length());
		
		//create a Packet object with the id and msg
		Packet packetObj = new Packet(id, msg);
		
		return packetObj;
		
	}

	/**
	 * the sortPackets method sorts the arraylist 
	 * holding the packet objects by packet id
	 * @param packetsObj the arraylist of Packet objects
	 */
	private static void sortPackets(ArrayList<Packet> packetsObj) {
	
		//sort the packets according to their ids, using selection sort algorithm
		for(int a = 0; a < packetsObj.size() - 1; a++) {
			//int index = a;
			for (int b = a + 1; b < packetsObj.size(); b++) {
				if(packetsObj.get(a).getPacketID() > packetsObj.get(b).getPacketID()) {
					Packet temp = packetsObj.get(a);
					packetsObj.set(a, packetsObj.get(b));
					packetsObj.set(b, temp);
				}
			}
			
		}
		
		//display the id and message for each packet
		for(int y=0; y < packetsObj.size(); y++) {
			System.out.println("obj id: " + (packetsObj.get(y)).getPacketID() + " obj msg: " + (packetsObj.get(y)).getPacketMsg());
		}
	
	}
	
	/**
	 * the getTotalNumPackets method gets the total
	 * number of packets server is trying to send
	 * @param packetsObj the arraylist of packetsObj the client received
	 * @return the total number of packets
	 */
	private static int getTotalNumPackets(ArrayList<Packet> packetsObj) {
		//get the last package msg 
		String lastPacketMsg = (packetsObj.get((packetsObj.size() - 1)).getPacketMsg());
	
		//get the total number of packets from the last packet msg, and parse it to an integer
		//As per protocol, last two digits of last packet is total number of packets
		int ttlPacket = Integer.parseInt(lastPacketMsg.substring(lastPacketMsg.length() - 2, lastPacketMsg.length()));
	
		//display the total number of packets
		System.out.println("total packets: " + ttlPacket);
	
		return ttlPacket;
	}
	
	/**
	 * the addIDsToArray method gets the ID of each 
	 * packet received and adds it to an array
	 * @param IDs the array to hold IDs
	 * @param packetsObj the arraylist of packetsObj the client received
	 */
	private static void addIDsToArray(int[] IDs, ArrayList<Packet> packetsObj) {
		 //initialize an array the size of total number of packets to be received, to hold the packet IDs
   	 
   	 
   	 //get the ID of each packet 
   	 for(int y = 0; y < packetsObj.size(); y++) {
   		 int packetID = (packetsObj.get(y)).getPacketID();
   		 //put the ID in correct index
   		 IDs[packetID - 10] = packetID;
   	 }
   	 
   	 //display the ID array
   	 System.out.print(" IDs array: ");
   	 for(int n= 0; n < IDs.length; n++) {
   		 System.out.print(" " + IDs[n]);
   	 }
		
	}
	
	/**
	 * the addMissingIdsToarray method finds the packets that it 
	 * didn't receive and adds the ids of those packets to an array
	 * @param IDs the array holing IDs of received packets
	 * @return the array of missing packet ids
	 */
	private static int[] addMissingIdsToarray(int[] IDs) {
		int numMissingPackets = getNumMissingPackets(IDs);
		 //get the total number of missing packets to initialize an array for missing packets
   	 
   	 
   	 //initialize the array to hold number of missing packets
   	 int[] missingPackets = new int[numMissingPackets];
   	 
   	 //get the ids for missing packets and put into array
   	 for (int a = 0, b = 0; a < IDs.length; a++) {
   		 //if the index in IDs array equals 0, 
   		 if(IDs[a] == 0) {
   			 //add the ID thats missing(as per protocol, the first index is 10) to the missingPackets array
   			 missingPackets[b] = a + 10;
   			 
   			 //display the index that equals 0
   			 System.out.println("index " + a + " equals zero.");
   			 
   			 //increment b, the placeholder for missingArrays index
   			 b++;
   		 }
   	 }
   	 
   	 //display the missingPackets array
   	 System.out.print(" missing packets array: ");
   	 for(int n= 0; n < missingPackets.length; n++) {
   			 System.out.print(" " + missingPackets[n]);
   	 } 
		return missingPackets;
	}
	
	/**
	 * the getNumMissingPackets method get the number of
	 * missing packets from the IDs array
	 * @param IDs the array holding IDs the client received
	 * @return the number of missing packets
	 */
	private static int getNumMissingPackets(int[] IDs) {
		int numMissingPackets = 0;
	   	 for(int u = 0; u < IDs.length; u++) {
	   		 if(IDs[u] == 0) {
	   			 //if the index is zero, increment count 
	   			 numMissingPackets++;
	   		 }
	   	 }
	   	 
	   	 //display array of missing packets
	   	 System.out.println("\nnumMissing packets " + numMissingPackets);
		return numMissingPackets;
	}

}

