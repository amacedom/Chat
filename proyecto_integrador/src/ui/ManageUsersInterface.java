package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import db.User;
import webservice.WeatherService;

public class ManageUsersInterface {
	protected JFrame frame;
	protected JPanel left,combo;
	protected JLabel selectUsers; 
	protected JComboBox userList;
	protected JComboBox allUsers;
	protected JButton action,cancel;
	Dimension dim;
	User userDB;
	
	public ManageUsersInterface(User userDB,String option) {
		this.userDB = userDB;
		if(option.equals("block")) {
		//	if(!userDB.getDBConn().getUnblockedUsersString(userDB.getUsername()).equals(""))
				createBlockWindow();
			//else
				//JOptionPane.showMessageDialog(frame, "You do not have any blocked contacts");
			
		}
			
		else
			if(!userDB.getDBConn().getBlockedUsersString(userDB.getUsername()).equals(""))
				createUnBlockWindow();
			else
				JOptionPane.showMessageDialog(frame, "You do not have any blocked contacts");

		
	}

	
	public void createBlockWindow() {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame("Chat Window");
		this.frame.setTitle("Block user");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		//init the left part
		this.left = new JPanel();
		this.left.setPreferredSize(new Dimension(200,80));
		this.left.setLayout(new FlowLayout());
		this.selectUsers = new JLabel("Select user: ");
		this.left.add(Box.createRigidArea(new Dimension(0, 25)));
		this.left.add(selectUsers);
		this.combo = new JPanel();
		this.left.add(Box.createRigidArea(new Dimension(0, 5)));
		this.allUsers = new JComboBox(userDB.getDBConn().getUnblockedUsers(userDB.getUsername()));
		this.combo.add(allUsers);
		this.left.add(combo);
		
		this.action = new JButton("Block");
		this.left.add(action, BorderLayout.PAGE_END);
		this.left.setOpaque(true);
		this.frame.add(left,BorderLayout.CENTER);	
		this.frame.pack();
		this.frame.setVisible(true);
		
		action.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				userDB.getDBConn().blockUser(userDB.getUsername(),allUsers.getSelectedItem().toString());
				frame.dispose();
			}
			
		});
		
	}
	
	public void createUnBlockWindow() {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame("Chat Window");
		this.frame.setTitle("Unblock user");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		//init the left part
		this.left = new JPanel();
		this.left.setPreferredSize(new Dimension(200,80));
		this.left.setLayout(new FlowLayout());
		this.selectUsers = new JLabel("Select user: ");
		this.left.add(Box.createRigidArea(new Dimension(0, 25)));
		this.left.add(selectUsers);
		this.combo = new JPanel();
		this.left.add(Box.createRigidArea(new Dimension(0, 5)));
		this.allUsers = new JComboBox(userDB.getDBConn().getBlockedUsers(userDB.getUsername()));
		this.combo.add(allUsers);
		this.left.add(combo);
		
		this.action = new JButton("Unblock");
		this.left.add(action, BorderLayout.PAGE_END);
		this.left.setOpaque(true);
		this.frame.add(left,BorderLayout.CENTER);	
		this.frame.pack();
		this.frame.setVisible(true);
		
		action.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				userDB.getDBConn().unblockUser(userDB.getUsername(),allUsers.getSelectedItem().toString());
				frame.dispose();
			}
			
		});
	}

}
