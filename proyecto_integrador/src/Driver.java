import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Driver extends JFrame{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		displayWindow();
		
	}
	
	private static void displayWindow() {
		JFrame frame = new JFrame("New Instance");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JLabel question = new JLabel("Please select one of the following options:");
		question.setPreferredSize(new Dimension(400,150));
		frame.getContentPane().add(question,BorderLayout.LINE_START);
		// show the window
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.pack();
		frame.setVisible(true);
	}

}
