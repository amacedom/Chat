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
import java.net.*;
import java.awt.*;
import java.util.concurrent.*;

public class ChatClient extends UserInterface implements SocketSetup {
    
    int PACKET_SIZE = 512;
    DatagramPacket receivedPacket;
    DatagramPacket sentPacket;
    DatagramSocket clientSocket;
    volatile boolean running = true;
    String consumerKey,consumerSecret;
    Twitter twitter;
    ExecutorService es = Executors.newFixedThreadPool(1);
    JFrame frame;
    JPanel top,mid,bottom;
    JTextField tweetField;
    JButton tweet;
    JComboBox userList;
    User userDB;
	
    public ChatClient(User userDB) {
    	super();
    	super.createWindow(userDB.getUsername());
    	new WeatherService();
    	this.frame = super.frame;
    	this.tweet = super.tweet;
    	this.userDB = userDB;
    	this.userList = super.userList;
    	this.tweetField = super.tweetField;
    	updateUserList();
  
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
		return false;
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