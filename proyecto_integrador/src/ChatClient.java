import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.util.concurrent.*;

public class ChatClient extends JFrame {
    
    private static final int PACKET_SIZE = 512;
    private DatagramPacket receivedPacket;
    private DatagramPacket sentPacket;
    private DatagramSocket clientSocket;
    private volatile boolean running = true;
    private ExecutorService es = Executors.newFixedThreadPool(1);
    JLabel connectedUsers;
    final JTextArea output;
    final JTextField to;
    
    // The method to send a message to the server. 
    private void sendMessage(String message) {
        try {
            byte[] ba = message.getBytes();
            sentPacket.setData(ba);
            sentPacket.setLength(ba.length);
            clientSocket.send(sentPacket);
        } catch(IOException e) { System.out.println("ChatClient error: " + e); }        
    }
    
    public ChatClient(InetAddress addr, int port, final String nick) {
        // First, set up the desired Swing interface.
        JPanel topPanel = new JPanel();        
        JButton quit = new JButton("Salir");
        topPanel.add(quit);
        JButton users = new JButton("Ver usuarios conectados");
        //topPanel.add(users);
        connectedUsers = new JLabel("Usuarios conectados");
       // topPanel.add(connectedUsers);
        JLabel j = new JLabel("Para: ");
        topPanel.add(j);
        to = new JTextField(40);
        topPanel.add(to);
        
        JLabel j2 = new JLabel("Mensaje: ");
        topPanel.add(j2);
        final JTextField input = new JTextField(40);
        topPanel.add(input);
        
        this.add(topPanel, BorderLayout.NORTH);
        output = new JTextArea();
        JScrollPane sb = new JScrollPane(output);
        sb.setPreferredSize(new Dimension(500,300));
        this.add(sb, BorderLayout.CENTER);
        this.add(connectedUsers, BorderLayout.SOUTH);
        this.setTitle("Chat for " + nick);
        this.pack();
        this.setVisible(true);
        
        // Next, the client itself.
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(1000);
            receivedPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            sentPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE, addr, port);
        } catch(Exception e) { 
            System.out.println("ChatClient error: " + e);
            return;
        }
        
        sendMessage("<newUser>" +nick+"</newUser>");		//new user joined
        
        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	if(to.getText().equals("")){
            		sendMessage(nick+": "+input.getText());
            	}
            	else
            	{
            		String user = to.getText();
                	sendMessage("<privateMessage>"+nick+": "+input.getText()+"</privateMessage><user>"+user+"</user><from>"+nick+"</from>");
                	to.setText("");
            	}
            	input.setText("");
            }
        });
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage("<quit>");
                ChatClient.this.finalize();
            }
        });
        
        users.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage("<getConnectedUsers>"+nick+"<getConnectedUsers>");
            }
        });
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                sendMessage("<quit>");
                ChatClient.this.finalize();
            }
        });
        
      
        es.submit(new Runnable() {
            public void run() {                
                try {
                    while(running) {
                        try {
                            // Wait for the next incoming datagram.
                            clientSocket.receive(receivedPacket);
                            // Extract the message from the datagram.
                            byte[] ba = receivedPacket.getData();
                            ba = java.util.Arrays.copyOfRange(ba, 0, receivedPacket.getLength());
                            String message = new String(ba);
                            
                            readMessage(message);
                        }
                        catch(SocketTimeoutException e) { /* No message this time */ }
                    }
                }
                catch(IOException e) {
                    System.out.println("ChatClient error: " + e);
                }
                finally {
                    clientSocket.close();
                    System.out.println("ChatClient terminated");
                }
            }
        });        
    }
    
    public void finalize() {
        running = false;
        es.shutdownNow();
        this.dispose();
    }
    
    // For demonstration purposes.
    public static void launch(int port, String nick) throws Exception {
        new ChatClient(InetAddress.getLocalHost(), port, nick);   
    }
    public static void main(String[] args){
    	String nick = JOptionPane.showInputDialog("Seleccione un nick");
    	try{
    	launch(4405,nick);
    	}catch(Exception e){}
    }
    public void readMessage(String xml){
    	System.out.println(xml);
    	if(xml.startsWith("<users"))
    	{
	    	String[] parts = xml.split(">");
	    	String[] temp;
	    	if(parts[0].equals("<users")){
	    		temp = parts[1].split("<");
	    		String users = temp[0];
	    		users = users.substring(0, users.length() - 1);
	    		connectedUsers.setText("USUARIOS CONECTADOS: "+users);
	    	}
    	}
    	else
    		output.append(xml+"\n");
    }
}