package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.jdbc.PreparedStatement;

import ui.AuthInterface;

public class MySQL {
	
	String url;
	String dbName;
	String driver;
	String userName;
	String password;
	Connection conn;
	
	PreparedStatement userExist,createUser,connect,disconn,passMatch;
	
	public MySQL() {
		this.url = "jdbc:mysql://localhost:3306/"; 
		this.dbName = "distribuidos";
		this.driver = "com.mysql.jdbc.Driver"; 
		this.userName = "root"; 
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
	
	public boolean passwordMatches(String username, String password) {
		String query = "select count(*) from " + dbName + ".chat_users where username = ? and password = ? ";
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
		String query = "update " + dbName + ".chat_users set status='online' where username = ?";
		try { 
			this.connect = (PreparedStatement) conn.prepareStatement(query);
			connect.setString(1, username);
			connect.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void logout(String username) {
		String query = "update " + dbName + ".chat_users set status='offline' where username = ?";
		try { 
			this.connect = (PreparedStatement) conn.prepareStatement(query);
			connect.setString(1, username);
			connect.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean userExists(String username) {
		String query = "select count(*) as count from " + dbName + ".chat_users where username = ?"; 
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
	
	public void closeConn() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
