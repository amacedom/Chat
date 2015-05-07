package bin;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import modules.ChatClient;
import db.MySQL;
import db.User;
import ui.AuthInterface;
import ui.ManageUsersInterface;
import ui.RegisterInterface;
import ui.UserInterface;

import java.sql.*;

public class Driver extends JFrame {

	static JFrame frame;
	static JPanel panel;
	static JPanel bottom;
	static JLabel question;
	static JRadioButton server;
	static JRadioButton client;
	static JButton ok;
	static JButton close;
	static ButtonGroup buttonGroup;
	static Dimension dim;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new UserInterface().createWindow();
		//MySQL mydb = new MySQL();
		MySQL mydb = new MySQL("45.55.251.74");
		AuthInterface au = new AuthInterface(mydb);
		//mydb.closeConn();
		//User userDB = mydb.getUserData("armandm");
		//new ManageUsersInterface(userDB,"block");
		//init(userDB);
	}
	
	public static void init(User userDB) {
		// create instances
		dim = Toolkit.getDefaultToolkit().getScreenSize();
		
		frame = new JFrame("New Instance");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setSize(100,200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(new Insets(20, 0, 20, 40)));
		
		question = new JLabel("Please select one of the following options:");
		server = new JRadioButton("Server instance");
		client = new JRadioButton("Client instance");
		
		initButtons(userDB); //initialize the buttons
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(server);
		buttonGroup.add(client);
		
		panel.add(question);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(server);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(client);
		panel.add(Box.createRigidArea(new Dimension(0, 15)));
		
		bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
		bottom.add(Box.createRigidArea(new Dimension(120, 0)));
		bottom.add(Box.createHorizontalGlue());
		bottom.add(ok);
		//bottom.add(Box.createRigidArea(new Dimension(5, 0)));
		//bottom.add(close);
		//bottom.add(Box.createRigidArea(new Dimension(15, 0)));
	
		panel.add(bottom);
		frame.getContentPane().add(panel); // add to a container
		server.setSelected(true); // set state
		displayWindow();
	}
	
	private static void runInstance(User userDB) {
		// depending on the selected option, do the following
		switch(getSelectedOption()) {
		case "server": System.out.println("Running server instance...");
					   frame.dispose();
					   
					   break;
			
		case "client": System.out.println("Running client instance...");
					   ChatClient client = new ChatClient(userDB);
					   frame.dispose();
					   break;
		
		case "error": System.out.println("Something went wrong!...");
					  System.exit(0); //closing app due to error with radiobuttons 
					  break;
		}
	}
	
	private static boolean canRun(String option) {
		boolean retval = true;
		//try to setup the networking of the selected socket
		switch(option) {
		case "server": System.out.println("Trying to setup server socket...");
		   			   break;

		case "client": System.out.println("Trying to setup client socket...");
		   			   break;
		}
		
		return retval;
	}
	
	private static String getSelectedOption() {
		String retval = "error";
		// check state
		if (server.isSelected()) {
		    // do something...
			retval = "server";
		} else {
		    // do something else...
			retval = "client";
		}

		return retval;
	}
	
	@SuppressWarnings("serial")
	private static void initButtons(User userDB) {
		ok = new JButton(new AbstractAction("OK"){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				runInstance(userDB);
			}
		});
		
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        JOptionPane.showMessageDialog(frame,"Please select an option","Attention",JOptionPane.INFORMATION_MESSAGE);
		        
		    }
		});
		
	}
	
	private static void displayWindow() { 
		// show the window
		frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		//frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.pack();
		frame.setVisible(true);
	}


}
