package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class AuthInterface extends JFrame {
	
	JFrame frame;
	JPanel form,buttons,contentPane;
	JTextField username;
	JPasswordField password;
	JLabel email,pass,newAccount; 
	JButton login,create;
	Dimension dim;
	
	public void createWindow () {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame();
		this.frame.setTitle("Authentication Window");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		setLoginFields();
		setButtons();
		
		this.contentPane = new JPanel(new BorderLayout());
		this.contentPane.setPreferredSize(new Dimension(350,120));
		this.contentPane.add(this.form, BorderLayout.NORTH);
		this.contentPane.add(this.buttons, BorderLayout.PAGE_END);
		
		
		this.contentPane.setOpaque(true);  //content panes must be opaque
		this.frame.add(this.contentPane);
		this.frame.pack();
		this.frame.setVisible(true);
		
	}
	
	private void setButtons() {
		this.buttons = new JPanel();
		this.buttons.setLayout(new BoxLayout(this.buttons, BoxLayout.X_AXIS));
		this.buttons.setPreferredSize(new Dimension(300,60));
		this.login = new JButton("Login");
		this.create = new JButton("New account");
		this.buttons.add(Box.createRigidArea(new Dimension(100, 0)));
		this.buttons.add(login);
		this.buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		this.buttons.add(create);
		
	}
	
	private void setLoginFields() {
		this.form = new JPanel(new SpringLayout());
		this.form.setPreferredSize(new Dimension(300,60));
		this.email = new JLabel("Username: ", JLabel.TRAILING);
        this.form.add(email);
        this.username= new JTextField(15);
        this.email.setLabelFor(this.username);
        this.form.add(this.username);
        
        this.pass = new JLabel("Password: ", JLabel.TRAILING);
        this.form.add(pass);
        this.password = new JPasswordField(16);
        this.pass.setLabelFor(this.password);
        this.form.add(this.password);
        
        SpringUtilities.makeCompactGrid(this.form,
                2, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
	}
	


}
