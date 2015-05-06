package modules;
import interfaces.SocketSetup;

import javax.swing.*;

import db.User;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import ui.UserInterface;

import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.awt.*;
import java.util.concurrent.*;

public class ChatClient extends UserInterface implements SocketSetup {
    
    int PACKET_SIZE = 512;
    DatagramPacket receivedPacket;
    DatagramPacket sentPacket;
    DatagramSocket clientSocket;
    volatile boolean running = true;
    ExecutorService es;
    String consumerKey,consumerSecret;
    Twitter twitter;
    User userDB;
    
    JFrame frame;
    JPanel top,mid,bottom;
    JTextField tweetField,outgoing;
    JTextArea incoming;
    JLabel onlineUsers;
    JButton tweet;
    JComboBox userList;
    JMenuItem quit;
	
    public ChatClient(User userDB) {
    	super();
    	super.createWindow(userDB.getUsername());
    	this.frame = super.frame;
    	this.tweet = super.tweet;
    	this.userDB = userDB;
    	this.userList = super.userList;
    	this.onlineUsers = super.onlineUsers;
    	this.incoming = super.incoming;
    	this.outgoing = super.outgoing;
    	this.tweetField = super.tweetField;
    	this.quit = super.quit;
    	this.es = Executors.newFixedThreadPool(1);
    	updateUserList();
    	setupSocket();
    	
    	frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    String[] options = {"Yes","No"};
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        int option = JOptionPane.showOptionDialog(frame,"Are you sure you want to leave?", "Attention", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[0] );
		        if(option == JOptionPane.YES_OPTION) {
		        	for(ActionListener a: quit.getActionListeners()) {
		        		a.actionPerformed(new ActionEvent(this,ActionEvent.ACTION_PERFORMED,null));
		        	}
		        }
		    }
		});
  
    	tweet.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String tweetText = tweetField.getText();
				String [] options = {"Yes","Not now, thanks"};
				if(twitter == null) {
					int option = JOptionPane.showOptionDialog(frame,"Do you want to sign in to twitter?" , "No twitter account logged in", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[0] );
					if(option == JOptionPane.YES_OPTION){
						try {
							setTwitterConf();
						} catch (MalformedURLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (TwitterException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				else {
					try {
						Status stat = twitter.updateStatus(tweetText);
						System.out.println("Successfully updated the status to [" + stat.getText() + "].");
						tweetField.setText("");
					} catch (TwitterException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
    	});
    }
   
    @Override
	public boolean setupSocket() {
		// TODO Auto-generated method stub
    	// Next, the client itself.
        try {
            clientSocket = new DatagramSocket();
            clientSocket.setSoTimeout(1000);
            receivedPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            sentPacket = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE, InetAddress.getLocalHost(), 4405);
        } catch(Exception e) { 
            System.out.println("ChatClient error: " + e);
            return false;
        }
        
        sendMessage("<newUser>" +userDB.getUsername()+"</newUser>");		//new user joined
        
        outgoing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
            	if(userList.getSelectedItem().toString().equals("All Users")){
            		sendMessage(userDB.getUsername()+": "+outgoing.getText());
            	}
            	else
            	{
            		String user = userList.getSelectedItem().toString();
                	sendMessage("<privateMessage>"+userDB.getUsername()+": "+outgoing.getText()+"</privateMessage><user>"+user+"</user><from>"+userDB.getUsername()+"</from>");
                	to.setText("");
            	}
            	outgoing.setText("");
            }
        });
        
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                sendMessage("<quit>");
                ChatClient.this.finalize();
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
    	
		return true;
	}
    
    // The method to send a message to the server. 
    private void sendMessage(String message) {
        try {
            byte[] ba = message.getBytes();
            sentPacket.setData(ba);
            sentPacket.setLength(ba.length);
            clientSocket.send(sentPacket);
        } catch(IOException e) { 
        	System.out.println("ChatClient error: " + e); 
        }        
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
	    		onlineUsers.setText("Online Users: "+users);
	    	}
    	}
    	else
    		incoming.append(xml+"\n");
    }
    
    //shutdown client
    public void finalize() {
        running = false;
        es.shutdownNow();
        frame.dispose();
        userDB.getDBConn().closeConn();
    }
    
    public void setTwitterConf() throws MalformedURLException, TwitterException {
    	 this.consumerKey = "RGNk3E5WbotJHsF70mGDEIGL6";
         this.consumerSecret = "FZ6gaEs5U94R3I4NTdo8xBM2dVBLqqAOIxEOv0dxx1c8WDXCbI";
         //final String oAuthAccessToken = "256029375-q9w2hCaSLVy48izSfBtEHYU8Aif9RDDlaWkc8oSM";
         //final String oAuthAccessTokenSecret = "1bBGannyhHlhzjTIpzW6zOEcpvXVawAEQ1dk1Iv8ikSPf";
         ConfigurationBuilder cb = new ConfigurationBuilder();
         cb.setDebugEnabled(true)
             .setOAuthConsumerKey(consumerKey)
             .setOAuthConsumerSecret(consumerSecret);
             //.setOAuthAccessToken(oAuthAccessToken)
             //.setOAuthAccessTokenSecret(oAuthAccessTokenSecret);
         TwitterFactory tf = new TwitterFactory(cb.build());
         twitter = tf.getInstance();
         connectToTwitter();
    }
    
    public void connectToTwitter() throws TwitterException, MalformedURLException {
    	try {
            	RequestToken requestToken = twitter.getOAuthRequestToken();
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
                AccessToken accessToken = null;
                System.out.println("Open the following URL and grant access to your account:");
                String authURL = requestToken.getAuthorizationURL();
                System.out.println(authURL);
                URL url = new URL(authURL);
                openWebpage(url);
                String pin = JOptionPane.showInputDialog(frame,"Enter your Auth PIN:");
                
                try {
                	if (pin.length() > 0) {
                		accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                	} else {
                		accessToken = twitter.getOAuthAccessToken(requestToken);
                	}
                } catch (TwitterException te) {
                	if (401 == te.getStatusCode()) {
                		System.out.println("Unable to get the access token.");
                	} else {
                		te.printStackTrace();
                	}
                    
                }
                
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                }
            }
    	
    }
    
    public void openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    public void updateUserList() {
    	for(String str: this.userDB.getDBConn().getAllUsers(this.userDB.getUsername())) {
    		userList.addItem(str);
    	}
    		
    }
    
    
    
    
}