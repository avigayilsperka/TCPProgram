package program3;
import java.net.*;
import java.io.*;
import java.util.*;


public class Server {
	
	public static void main(String args[]) throws IOException{
		
		try {
			ServerSocket server = new ServerSocket(30121);
			Socket clientSocket = server.accept();
			PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader responseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			//StringTokenizer breaks up message.
			//Pass message to StringTokenizer
			StringTokenizer str = new StringTokenizer("Congratulations. You have finally recieved every single packet and have succesfully put them in correct order! Job well done indeed!");
			
			//create array to hold packets and copy array for comparison
			ArrayList<Packet> packetArray= new ArrayList<Packet>();
			ArrayList<Packet> originalArray = new ArrayList<Packet>();
			
			int i = 0;
			while(str.hasMoreTokens()) {
				//save each word as unique packet
				Packet packet = new Packet(i++,str.nextToken());
				//add each packet to array of packets
				packetArray.add(packet);
				originalArray.add(packet);
			}
			
			//send each packet to client
			for (int x=0; x<packetArray.size();) {
				//get random value from array size
				int rnd = new Random().nextInt(packetArray.size());
				System.out.println("SERVER: Sending packet...");
				
				//only send 80% of time or last time 
				if(new Random().nextInt(100)>20 || packetArray.size()==1) {
					//send random message from packet to client
					responseWriter.println(packetArray.get(rnd).getString());
					//send Id to client
					responseWriter.println(Integer.toString(packetArray.get(rnd).getPacketId()));
				}
				
				responseWriter.println("packet sent.");
				//last packet being sent				
				if (packetArray.size()==1) {
					System.out.println("Sent all packets.");
					responseWriter.println("Done.");
				}
				//to continue loop
				else
					responseWriter.println("yes");
				//remove message which was sent from array
				packetArray.remove(rnd);
			}
			
			//send total number of packets
			responseWriter.println(originalArray.size()+" packets sent in total.");
			System.out.println("SERVER RESPONDING: type 'help' for missing packet:");
			responseWriter.println("Type 'help' for missing packet.");
			
			//collect response
			String userResponse= responseReader.readLine();
			System.out.println("User responded: "+ userResponse);
			
			//send missing packets
			while (userResponse.equals("help")) {
				System.out.println("SERVER Responding: Enter the id of the missing packet:");
				responseWriter.println("Enter the id of the missing packet:");
				String missingId = responseReader.readLine();
				//receive missing id and resend the message from original array
				System.out.println("User responded: "+ missingId);
				System.out.println("SERVER Responding: Sending packet with id "+missingId);
				responseWriter.println("Sending packet with id: "+missingId);
				int id = Integer.parseInt(missingId); 
				if(new Random().nextInt(100)>20 || packetArray.size()==1)
					responseWriter.println(originalArray.get(id).getString());
				System.out.println("SERVER Responding: packet Sent.");
				responseWriter.println("packet sent.");
				System.out.println("Responding: Type 'help' for missing packet. Otherwise type 'done'.");
				responseWriter.println("Type 'help' for missing packet. Otherwise type 'done'.");
				userResponse= responseReader.readLine();
				//check for further missing packets
				System.out.println("User responded: "+ userResponse);
			}
			//all packets sent
			System.out.println("Responding: Done.");
			responseWriter.println("Done.");
		}
		
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
