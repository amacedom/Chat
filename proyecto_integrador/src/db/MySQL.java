package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import ui.AuthInterface;

public class MySQL {
	
	String url;
	String dbName;
	String driver;
	String userName;
	String password;
	Connection conn;
	
	PreparedStatement userExist,createUser,connect,disconn,passMatch,getUser,getAll;
	
	public MySQL() {
		this.url = "jdbc:mysql://localhost:3306/"; 
		this.dbName = "distribuidos";
		this.driver = "com.mysql.jdbc.Driver"; 
		this.userName = "armandm"; 
		this.password = "admin4us"; 	
		this.conn = connectToMySQL();
	}
	
	public MySQL(String hostname) {
		this.url = "jdbc:mysql://"+ hostname + ":3306/"; 
		this.dbName = "distribuidos";
		this.driver = "com.mysql.jdbc.Driver"; 
		this.userName = "armandm"; 
		this.password = "admin4us"; 	
		this.conn = connectToMySQL();
	}
	
	public Connection connectToMySQL() {
		try { 
			Class.forName(driver).newInstance(); 
			conn = DriverManager.getConnection(url+dbName,userName,password);
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 

		return conn;
	}
	
	public ArrayList<String> getAllUsers(String username) {
		String query = "select username from chat_users where username != ? order by username asc";
		//String[] users = new String[50];
		ArrayList<String> users = new ArrayList<String>();
		try {
			this.getAll = (PreparedStatement) conn.prepareStatement(query);
			getAll.setString(1, username);
			ResultSet rs = getAll.executeQuery();
			while(rs.next()) {
				users.add(rs.getString("username"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return users;
	}
	
	
	public boolean passwordMatches(String username, String password) {
		String query = "select count(*) from chat_users where username = ? and password = ? ";
		boolean retval = false;
		int count = 0;
		try {
			this.passMatch = (PreparedStatement) conn.prepareStatement(query);
			passMatch.setString(1, username);
			passMatch.setString(2, password);
			ResultSet rs = passMatch.executeQuery();
			if(rs.next()) 
				count = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(count >= 1)
			retval = true;
		
		return retval;
	}
	
	public void login(String username) {
		String query = "update chat_users set status='online' where username = ?";
		try { 
			this.connect = (PreparedStatement) conn.prepareStatement(query);
			connect.setString(1, username);
			connect.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void logout(String username) {
		String query = "update chat_users set status='offline' where username = ?";
		try { 
			this.connect = (PreparedStatement) conn.prepareStatement(query);
			connect.setString(1, username);
			connect.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean userExists(String username) {
		String query = "select count(*) as count from chat_users where username = ?"; 
		int count = -1;
		try {
			this.userExist = (PreparedStatement) conn.prepareStatement(query);
			userExist.setString(1, username);
			ResultSet rs = userExist.executeQuery();
			while (rs.next()) {
	            count = rs.getInt("count");
	            //System.out.println("the count is "+ count);
	        }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(count >= 1)
			return true;
		else
			return false;
		
	}
	
	public void createNewUser(String username, String password, String email, String twitter_username) {
		String query = "insert into distribuidos.chat_users (id,username,password,email,twitter_username,status, last_login) values (default,?,?,?,?,'offline', null)"; 
		if(username.length() >= 4) {
			try {
				this.createUser = (PreparedStatement) conn.prepareStatement(query);
				createUser.setString(1, username);
				createUser.setString(2, password);
				createUser.setString(3, email);
				createUser.setString(4, twitter_username);
				createUser.execute();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			JOptionPane.showMessageDialog(null, "Username must be 4 characters long","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public User getUserData(String username) {
		String query = "select id,username,password,email,twitter_username,status from chat_users where username = ?";
		User userDB = null;
		try {
			this.getUser = (PreparedStatement) conn.prepareStatement(query);
			getUser.setString(1, username);
			ResultSet rs = getUser.executeQuery();
			MySQL mysql = new MySQL();
			while(rs.next()) {
				userDB = new User(rs.getInt("id"),rs.getString("username"),rs.getString("password"),
						rs.getString("email"),rs.getString("twitter_username"),rs.getString("status"),mysql);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userDB;
	}
	
	public void closeConn() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}