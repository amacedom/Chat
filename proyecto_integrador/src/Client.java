import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class Client {
	public String nick; // Client's chosen nickname
    public DatagramPacket clientPacket; // Client contact info
    public DatagramSocket serverSocket; // Server Socket

    public void sendMessage(String message) {
        try {
            byte[] ba = message.getBytes();
            clientPacket.setData(ba);
            clientPacket.setLength(ba.length);
            serverSocket.send(clientPacket);
        }   catch(IOException e) { System.out.println(e); }
    }

    // Constructor
    public Client(DatagramPacket dp, DatagramSocket serverSocket, String nick, int packet_size) {            
        this.nick = nick;
        this.serverSocket = serverSocket;
        this.clientPacket = new DatagramPacket(new byte[packet_size], packet_size, dp.getAddress(), dp.getPort());
    }

}
