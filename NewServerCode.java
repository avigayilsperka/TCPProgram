package program2;

//Racheli Reich Program
import java.io.*;
import java.net.*;
import java.util.*;

public class NewServerCode {
	public static void main(String[] args) throws IOException {
		//list to hold packets
		ArrayList<String> messageInPackets = new ArrayList<>();
		messageIntoPackets(messageInPackets);
		
		//list to hold copy of packets, shuffled
		ArrayList<String> copyMessageInPackets = new ArrayList<>();
		createCopyMessageInPacketsShuffled(messageInPackets, copyMessageInPackets);
		
		//create Random object
		Random randomNumber = new Random();
		
		//hard code port Number
		args = new String[] { "11158" };
		
		//if no port number passed in, end program
		if (args.length != 1) {
			System.err.println("Usage: java EchoServer <port number>");
			System.exit(1);
		}
		
		//port number
		int portNumber = Integer.parseInt(args[0]);
		
		try (ServerSocket serverSocket = new ServerSocket(portNumber);
				Socket clientSocket = serverSocket.accept();
				PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader requestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				){
			
			//display msg when client is connected
			System.out.println("Client connected");
			
			for(int y = 0; y < copyMessageInPackets.size(); y++) {
				
				//get packet to send from arraylist
				String packetToSend = copyMessageInPackets.get(y);

			
				double testValue = randomNumber.nextDouble();
				//only send to client with 80% probability
				if(testValue > .2) {	
					responseWriter.println(packetToSend);
					System.out.println("PACKET " + packetToSend + " ACTUALLY SENT.");   //May not be meant to keep track on server side of what sent
				}
				
				//display that packet sent (assuming all did)
				System.out.println("packet " + packetToSend + " sent.");

			}
			
			//get the final packet and send to client
			String lastPacket = messageInPackets.get(messageInPackets.size() - 1);
			responseWriter.println(lastPacket);
			System.out.println("Last packet sent: " + lastPacket);
			
			
			
			String clientRequest; 
			while((clientRequest=requestReader.readLine()) != null) {
				
				System.out.println("Client requested packet " + clientRequest);
			
		    }

			
			
		}
		
		catch (IOException e) {
			System.out.println(
					"Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			System.out.println(e.getMessage());
		}
		
		
	}



	private static void messageIntoPackets(ArrayList<String> messageInPackets) {
		//total number of packets message is broken into
		int numPackets =  24; 
		
		//message to be sent to client
		String message = "Hello client, you have successfully received all packets and arranged them in the right order!";
		
		//get the length of the message
		int msgLength =  message.length();
		
		///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
		System.out.println("msg length = " + msgLength);
		
		//create a packet size based on length of message and the total number of packets to be sent
		int packetSize = (msgLength / numPackets);
		
		//if there are remaining packets, add one char to each packet size
		if((msgLength % numPackets) > 0) {
			packetSize += 1;
			///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
			System.out.println("one added");
		}
		
		///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
		System.out.println("packetSize = " + packetSize);
		

		
		//id number to add to each packet
		
		int y = 10;
		
		for(int x = 0; x < (msgLength - 1); x += 4) {
			String packet;
			
			if((msgLength - x) < 4) {
				//get each packet
				packet = message.substring(x, msgLength);
///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
				
				//make id number into string and add to beginning packet
				String id = String.valueOf(y);
				packet = id.concat(packet);
				
				
				
///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
				//display the last packet
				System.out.println("last packet " + packet);
				
				
				//add the last packet to array
				messageInPackets.add(packet);
			}
			else {
				
				packet = message.substring(x, (x+ packetSize));
				
				//make id number into string and add to beginning of each packet
				String id = String.valueOf(y);
				packet = id.concat(packet);
				
				messageInPackets.add(packet);
				
			}
			
			//increment variable holding id
			y += 1;
		}
		
///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
		System.out.println("messageInPackets: " + messageInPackets.size());
		
		String totalNumPackets = String.valueOf(numPackets);
		String lastPacket = (messageInPackets.get(numPackets - 1)).concat(totalNumPackets);
		messageInPackets.set((numPackets - 1), lastPacket);
	
///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
		System.out.println(messageInPackets);
		
		
		
		
		
		
	}

	private static void createCopyMessageInPacketsShuffled(ArrayList<String> messageInPackets, ArrayList<String> copyMessageInPackets) {
		for(int x = 0; x < messageInPackets.size() - 1; x++) {
			String packet = messageInPackets.get(x);
			copyMessageInPackets.add(packet);
		}
		Collections.shuffle(copyMessageInPackets);
///////////////////ERASE WHEN DONE/////////////////////////////////////ERASE WHEN DONE/////////////////////////////////////
		System.out.println("shuffled: " + copyMessageInPackets);
		
	}
}

