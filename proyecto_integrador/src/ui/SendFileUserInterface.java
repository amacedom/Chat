package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import db.User;

public class SendFileUserInterface {
	
	protected JFrame frame;
	protected JPanel left,combo;
	protected JLabel selectUsers,selectFile; 
	protected JComboBox userList;
	protected JTextField filePath;
	protected JFileChooser fc;
	protected JButton action,cancel;
	protected String selectedUser;
	File file;
	Dimension dim;
	User userDB;
	DatagramSocket socket;
	DatagramPacket packet;
	
	public SendFileUserInterface(User userDB, String selectedUser, DatagramSocket socket) {
		this.userDB = userDB;
		this.selectedUser = selectedUser;
		this.socket = socket;
		if(userDB.getDBConn().isOnline(selectedUser) && !selectedUser.equals("All Users")) {
			JOptionPane.showMessageDialog(frame, "The file will be sent to: " + selectedUser);
			createWindow();
		}
		else {
			JOptionPane.showMessageDialog(frame, "The user you are trying to reach is not online at the moment");
		}
			
		
	}
    
	public void sendFile() throws IOException {
        int len = (int) file.length(); // figure out how big the file is
        byte[] message = new byte[len]; // create a buffer big enough
        FileInputStream in = new FileInputStream(file);
        int bytes_read = 0, n;
        do { // loop until we've read it all
          n = in.read(message, bytes_read, len - bytes_read);
          bytes_read += n;
        } 
        while ((bytes_read < len) && (n != -1));
        String fileText = new String(message);
        String messageFormat = "<file>" + fileText + "</file><path>" + file.getAbsolutePath() + "</path><user>" + selectedUser + "</user><from>" + userDB.getUsername() + "</from>";
        System.out.println(messageFormat);
        // Initialize a datagram packet with data and address
        len = (int) messageFormat.length();
        message = new byte[len];
        message = messageFormat.getBytes();
        this.packet = new DatagramPacket(message, message.length, InetAddress.getLocalHost(), 4405);

        // Create a datagram socket, send the packet through it, close it.
        
        socket.send(packet);

	}
	
	public void createWindow() {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame("Send File Window");
		this.frame.setTitle("Send File to user");
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
	
		this.fc = new JFileChooser();
		int result = fc.showDialog(frame,"Send");
		
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fc.getSelectedFile();
		    this.file = selectedFile;
		    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
		    try {
				sendFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (result == JFileChooser.CANCEL_OPTION) {
		    frame.dispose();
		}
		
		
	}
	
}
