package modules;
import interfaces.SocketSetup;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ChatServer implements SocketSetup {

    private static final int PACKET_SIZE = 10240;
    private List<Client> clients = new ArrayList<Client>();
    private volatile boolean running = false;
    private DatagramSocket serverSocket;
    private DatagramPacket receivedPacket;
    private ExecutorService es = Executors.newFixedThreadPool(1);
    
    public ChatServer(int port) {       
    	try {
        	if(!running)
        	{
	            serverSocket = new DatagramSocket(port);
	            serverSocket.setSoTimeout(60000);      
	            running = true;
        	}
        }
        catch(Exception e) {
            System.out.println("Error: Can't open ChatServer socket"); return;
        }        
        receivedPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
        es.submit(new LogicThread());
    }

    
    public void sendMessageToAll(String message) {
        for(Client c: clients) { c.sendMessage(message); }
    }
    
    public void sendImAliveMessageToAll() {
        for(Client c: clients) { c.sendMessage("alive"); }
    }
    
    public void sendPrivateMessage(String message,String nick)
    {
        for(Client c: clients) { // who wants to quit?
            if(c.nick.equals(nick)) {
            	c.sendMessage(message);
                break; // Can't continue iterating through collection once it has been modified
            }                        
        }
    }
    
    public void sendFile(String message,String path,String text, String nick) {
    	for(Client c: clients) { // who wants to quit?
            if(c.nick.equals(nick)) {
            	c.saveFile(message,path,text);
                break; // Can't continue iterating through collection once it has been modified
            }                        
        }
    }

    // The background thread to implement the chat server's mainloop.
    private class LogicThread implements Runnable {
        public void run() {
        	sendImAliveMessageToAll(); 	
            try {
                while(running) {
                    // Wait for the next incoming datagram.
                    serverSocket.receive(receivedPacket);
                    // Extract the message from the datagram.
                    byte[] ba = receivedPacket.getData();
                    ba = java.util.Arrays.copyOfRange(ba, 0, receivedPacket.getLength());
                    String message = new String(ba);
                    
                    // Do what the message tells us to do.
                  readXML(message);
                }
            }
            catch(SocketTimeoutException e) {
                System.out.println("Socket timeout, closing server");
            }
            catch(IOException e) {
                System.out.println("ChatServer error: " + e );
            }
            finally {
                serverSocket.close();
                System.out.println("ChatServer terminated");
            }
        }            
    }

    public void finalize() {
        running = false;
        es.shutdownNow();
    }
    public void readXML(String xml)
    {
    	String[] parts = xml.split(">");
    	String[] temp;
    	
    	if(parts[0].equals("<privateMessage")){
    		temp = parts[1].split("<");
    		String message = temp[0];
    		temp = parts[3].split("<");
    		String user = temp[0];
    		temp = parts[5].split("<");
    		String from = temp[0];
    		if(message.equals("*server*")){
    			sendPrivateMessage("I am the server",user);
    			sendMessageToAll(from + " has become the new server");
    		}
    		else {
    			sendPrivateMessage(message,user);
        		sendPrivateMessage(message,from);
    		}
    		
    	}
    	else if(parts[0].equals("<newUser")){
    		temp = parts[1].split("<");
    		String newUser = temp[0];
            Client c = new Client(receivedPacket, serverSocket, newUser,PACKET_SIZE);
            clients.add(c);
            sendMessageToAll(c.nick + " has joined the conversation");
            updateConnectedUsers();
    	}
    	else if(parts[0].equals("<quit")){
            for(Client c: clients) { // who wants to quit?
                if(c.clientPacket.getAddress().equals(receivedPacket.getAddress()) &&
                   c.clientPacket.getPort() == receivedPacket.getPort()) {
                     sendMessageToAll(c.nick + " has left the conversation");
                     running = false;
                     clients.remove(c);
                     
                     break; // Can't continue iterating through collection once it has been modified
                }                        
            }
            updateConnectedUsers();
    	}
    	else if(parts[0].equals("<getConnectedUsers")){
    		temp = parts[1].split("<");
    		String user = temp[0];			//usuario que lo pidio
    		
    		String response="<users>";
    		for(Client c: clients){
    			response+=c.nick+",";
    		}
    		response+="</users>";
    		sendPrivateMessage(response,user);
    	}
    	else if(parts[0].equals("<file")){
    		temp = parts[1].split("<");
    		String text = temp[0];
    		temp = parts[3].split("<");
    		String path = temp[0];
    		temp = parts[5].split("<");
    		String user = temp[0];
    		temp = parts[7].split("<");
    		String from = temp[0];
    		//System.out.println("texto: " + text);
    		//System.out.println("path: " + path);
    		//System.out.println("destinatario: " + user);
    		//System.out.println("enviado por :" + from);
    		sendPrivateMessage("You sent " + path + " to: " + user,from);
    		sendFile("You received the file '" + path +" from: " + from,path,text,user);
    		
    	}
    	else
    	{
    		sendMessageToAll(xml);
    	}
    }
    public void updateConnectedUsers()
    {
    	String response="<users>";
		for(Client c: clients){
			response+=c.nick+",";
		}
		response+="</users>";
		sendMessageToAll(response);
    }

	@Override
	public boolean setupSocket() {
		// TODO Auto-generated method stub
		return false;
	}
}