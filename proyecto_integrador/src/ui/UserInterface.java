package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserInterface extends JFrame {
	// Elements of the user interface
	JFrame frame;
	JPanel top,mid,bottom;
	JTextArea incoming;
	JTextField outgoing,tweetField;
	JScrollPane scroll;
	JLabel onlineUsers,to,message,tweetit; 
	JComboBox userList;
	JButton tweet;
	Dimension dim;
	JMenuBar menuBar;
	JMenu menu, submenu;
	JMenuItem users,sendFile,quit;

	// The constructor to build the user interface of the client
	public void createWindow() {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame("Chat Window");
		this.frame.setTitle("Chat Client");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		// init the top part
		this.top = new JPanel();
		this.top.setPreferredSize(new Dimension(500,25));
		this.top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		this.top.add(Box.createRigidArea(new Dimension(10, 0)));
		this.onlineUsers = new JLabel("Connected Users:");
		this.onlineUsers.setPreferredSize(new Dimension(350,25));
		this.top.add(this.onlineUsers);
		this.to = new JLabel("To: ");
		this.top.add(to);
		this.userList = new JComboBox();
		this.top.add(userList);
		this.top.add(Box.createRigidArea(new Dimension(10, 0)));
	
		
		// init the middle part
		this.mid = new JPanel();
		this.mid.setPreferredSize(new Dimension(480,250));
		this.mid.setLayout(new BoxLayout(mid, BoxLayout.X_AXIS));
		this.incoming = new JTextArea();
		this.incoming.setText("testing this shit"); // remove this line
		this.scroll = new JScrollPane(incoming);
		this.mid.add(Box.createRigidArea(new Dimension(10, 0)));
		this.mid.add(this.scroll);
		this.mid.add(Box.createRigidArea(new Dimension(10, 0)));
		
		// init the bottom part
		this.bottom = new JPanel();
		this.bottom.setPreferredSize(new Dimension(500,100));
		this.bottom.setLayout(new FlowLayout());
		this.message = new JLabel("Message:");
		this.bottom.add(message);
		this.outgoing = new JTextField();
		this.outgoing.setPreferredSize(new Dimension(420,50));
		this.bottom.add(outgoing);
		this.bottom.add(Box.createRigidArea(new Dimension(5, 0)));
		this.tweetit = new JLabel("Tweet:");
		this.bottom.add(tweetit);
		this.tweetField = new JTextField();
		this.tweetField.setPreferredSize(new Dimension(360,25));
		this.bottom.add(tweetField);
		this.tweet = new JButton();
		this.tweet.setIcon(new ImageIcon(getImage()));
		this.bottom.add(tweet);
		
		// init menu options
		initMenu();
		
		this.frame.add(this.top,BorderLayout.NORTH);
		this.frame.add(this.mid,BorderLayout.CENTER);
		this.frame.add(this.bottom,BorderLayout.SOUTH);
		
		this.frame.pack();
		this.frame.setVisible(true);
		
	}
	
	private void initMenu() {
		menuBar = new JMenuBar();
	    
	    // build the File menu
	    this.submenu = new JMenu("Chat Options");
	    this.users = new JMenuItem("Manage users...");
	    this.sendFile = new JMenuItem("Send a file...");
	    this.quit = new JMenuItem("Quit");
	    this.submenu.add(this.users);
	    this.submenu.add(this.sendFile);
	    this.submenu.add(this.quit);
	 
	    // add menus to menubar
	    this.menuBar.add(this.submenu);
	 
	    // put the menubar on the frame
	    this.frame.setJMenuBar(this.menuBar);
	}
	
	private Image getImage() {
		Image img = null;
		try {
		    img = ImageIO.read(getClass().getResource("twitter.bmp"));
		} catch (IOException ex) {
			System.out.println("Error: Couldn't find image");
		}
		
		return img;
	}
	
}