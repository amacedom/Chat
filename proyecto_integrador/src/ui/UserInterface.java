package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class UserInterface extends JFrame {
	// Elements of the user interface
	JFrame frame;
	JPanel top,mid,bottom;
	JTextArea incoming;
	JTextField outgoing;
	JScrollPane scroll;
	JLabel onlineUsers,to,message; 
	JButton exit;
	Dimension dim;
	

	// The constructor to build the user interface of the client
	public void createWindow() {
		this.frame = new JFrame("Chat Window");
		this.frame.setTitle("Chat Client");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		this.top = new JPanel();
		this.top.setPreferredSize(new Dimension(500,200));
		this.top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		
		this.mid = new JPanel();
		this.mid.setPreferredSize(new Dimension(500,200));
		this.mid.setLayout(new BoxLayout(mid, BoxLayout.PAGE_AXIS));
		
		this.bottom = new JPanel();
		this.bottom.setPreferredSize(new Dimension(500,200));
		this.bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		
		this.incoming = new JTextArea();
		this.scroll = new JScrollPane(incoming);
		
		this.incoming.setText("testing this shit");
		this.top.add(this.incoming);
		
		this.frame.add(this.top,BorderLayout.NORTH);
		this.frame.add(this.mid,BorderLayout.SOUTH);
		this.frame.getContentPane().add(top); // add to a container
		this.frame.pack();
		this.frame.setVisible(true);
		
	}
	
}
