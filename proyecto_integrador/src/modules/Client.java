package modules;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.swing.JOptionPane;


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
    
    public void saveFile(String message,String path,String text){
    	try{
    		byte[] ba = message.getBytes();
            clientPacket.setData(ba);
            clientPacket.setLength(ba.length);
            serverSocket.send(clientPacket);
            writeFile(text,path);
    	} catch(IOException e) { System.out.println(e); }
    }
    
    public void writeFile(String text, String path) throws IOException {
    	String newpath = JOptionPane.showInputDialog(null,"Change the name of the file to save the file",path);
    	File file = new File(newpath);
    	 System.out.println("writing file to " + path);
		// if file doesnt exists, then create it
		if (!file.exists()) {
			file.createNewFile();
			JOptionPane.showMessageDialog(null, "File created Succesfully");
		}
		else
			JOptionPane.showMessageDialog(null, "File exists :(");

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(text);
		bw.close();

    }

    // Constructor
    public Client(DatagramPacket dp, DatagramSocket serverSocket, String nick, int packet_size) {            
        this.nick = nick;
        this.serverSocket = serverSocket;
        this.clientPacket = new DatagramPacket(new byte[packet_size], packet_size, dp.getAddress(), dp.getPort());
    }

}
