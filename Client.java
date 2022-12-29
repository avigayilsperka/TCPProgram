package program3;
import java.net.*;
import java.io.*;
import java.util.*;

public class Client {

	public static void main(String args[]) throws IOException {
		
		args = new String[] {"127.0.0.1", "30121"};
		
		String hostName = args[0];
		int port = Integer.parseInt(args[1]);
		
		try {
			Socket clientSocket = new Socket(hostName, port);
			PrintWriter toServer = new PrintWriter(clientSocket.getOutputStream(),true);
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
			//create array to hold incoming messages
			ArrayList <Packet> packetArray = new ArrayList<Packet>();
			
			String serverResponse;
			do {
				  serverResponse = fromServer.readLine();
				  
				  //received 'packet' sent but no packet
	              if (serverResponse.equals("packet sent.")) {
	            	  System.out.println("SERVER RESPONDS: packet sent.");
	              }
	              
	              else {
	            	  //collect id
	            	  String id = fromServer.readLine();
	            	  //save new message and new id as new packet and add to array
	            	  packetArray.add(new Packet(Integer.parseInt(id), serverResponse));
	            	  serverResponse = fromServer.readLine();
	            	  System.out.println("SERVER RESPONDS: packet sent.");
	              }
	            //loop while packets being sent
	            serverResponse = fromServer.readLine();
	            } while(serverResponse.equals("Done.")==false);
		
			String userInput;
			//collect total number of packets sent
			serverResponse = fromServer.readLine();
			int totalSent= Integer.parseInt(serverResponse.substring(0,2));
			System.out.println("SERVER RESPONDS:"+serverResponse);
			serverResponse = fromServer.readLine();
			System.out.println("SERVER RESPONDS: "+serverResponse);
			
			//while less packets in array than sent
			while(totalSent!=packetArray.size()) {
				userInput = "help";
				System.out.println("Missing packets. responding 'help'.");
			    toServer.println(userInput); // send request to server
			    System.out.println("SERVER RESPONDS: \"" + fromServer.readLine() + "\"");
			    
			    //search for missing packets
			    int missing = missingId(packetArray,totalSent);
			    System.out.println("Missing packet with id: "+missing+". Responding to server...");
			    
			    //update server with missing id
			    toServer.println(Integer.toString(missing));
			    
			    System.out.println("SERVER RESPONDS: \""+ fromServer.readLine()+"\"");
			    serverResponse= fromServer.readLine();
				
			    //packet sent and not lost
			    if(!serverResponse.equals("packet sent.")) {
			    	//add to array
					packetArray.add(new Packet(missing, serverResponse));
					System.out.println("SERVER RESPONDS: \""+ fromServer.readLine()+"\"");
				}            
				System.out.println("SERVER RESPONDS: \""+ fromServer.readLine()+"\"");
				           	 
				
		}
		//done
		System.out.println("All packets received. Responding done.");
		toServer.println("done.");
		System.out.println("SERVER RESPONDS:"+fromServer.readLine());
		//blank line for spacing
		System.out.println();
		System.out.println("System Sorting Packets...");
		
		//sort array
		String message = sort(packetArray);
		System.out.println("MESSAGE RECEIVED FROM SERVER: "+ message);
		
		} catch(IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
                System.exit(1);
            } 
		
	}
	
	public static int missingId(ArrayList<Packet> array, int total) {
		//loop and find missing id by comparing each
		for (int x=0; x<total; x++) {
			boolean found=false;
			for(int y=0; y<array.size();y++) {
				if (x ==array.get(y).getPacketId())
					found=true;
			}
			//not found, return missing id
			if (found==false)
				return x;
		}
		return -1;
	}
	
	public static String sort(ArrayList<Packet> array) {
		//sort by id and concatenate to string when locate
		String message = "";
		for(int y=0; y<array.size(); y++) {
			for (int x=0; x<array.size();x++) {
				if (y==array.get(x).getPacketId()) {
					message= message+" "+array.get(x).getString();
				}	
			}
		}
		return message;
	}
}
