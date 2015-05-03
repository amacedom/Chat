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
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class RegisterInterface extends JFrame{
	
	JFrame frame;
	JPanel form,buttons,contentPane;
	JTextField username,email,twitter;
	JPasswordField password;
	JLabel user,pass,mail,twitt; 
	JButton create,cancel;
	Dimension dim;
	
	public void createWindow () {
		this.dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame();
		this.frame.setTitle("Registrarion Window");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setLocation((dim.width/2)-250, (dim.height/2)-200);
		this.frame.setResizable(false);
		
		setLoginFields();
		setButtons();
		
		this.contentPane = new JPanel(new BorderLayout());
		this.contentPane.setPreferredSize(new Dimension(350,180));
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
		this.create = new JButton("Create");
		this.cancel = new JButton("Cancel");
		this.buttons.add(Box.createRigidArea(new Dimension(120, 0)));
		this.buttons.add(create);
		this.buttons.add(Box.createRigidArea(new Dimension(10, 0)));
		this.buttons.add(cancel);
		
	}
	
	private void setLoginFields() {
		this.form = new JPanel(new SpringLayout());
		this.form.setPreferredSize(new Dimension(300,120));
		this.user = new JLabel("Username: ", JLabel.TRAILING);
        this.form.add(user);
        this.username= new JTextField(15);
        this.user.setLabelFor(this.username);
        this.form.add(this.username);
        
        this.pass = new JLabel("Password: ", JLabel.TRAILING);
        this.form.add(pass);
        this.password = new JPasswordField(16);
        this.pass.setLabelFor(this.password);
        this.form.add(this.password);
        
        this.mail = new JLabel("Email Address: ", JLabel.TRAILING);
        this.form.add(mail);
        this.email= new JTextField(50);
        this.mail.setLabelFor(this.email);
        this.form.add(this.email);
        
        this.twitt = new JLabel("Twitter Username: ", JLabel.TRAILING);
        this.form.add(twitt);
        this.twitter= new JTextField(15);
        this.twitt.setLabelFor(this.twitter);
        this.form.add(this.twitter);
        
        SpringUtilities.makeCompactGrid(this.form,
                4, 2, //rows, cols
                6, 6,        //initX, initY
                6, 6);       //xPad, yPad
        
	}
}
