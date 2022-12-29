package program3;

public class Packet{
	
	private int packetId;
	private String str;
	
	public Packet(int id,String str) {
		this.str = str;
		packetId = id;
	}
	public Packet(String str) {
		this.str= str;
	}
	
	public String getString() {
		return str;
	}
	
	public int getPacketId() {
		return packetId;
	}
	
	public void setPacketId(int id) {
		packetId = id;
	}
	
}
