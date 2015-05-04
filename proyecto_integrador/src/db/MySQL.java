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
	
	PreparedStatement userExist,createUser;
	
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
			new AuthInterface().createWindow();
			//conn.close(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 

		return conn;
	}
	
	public void login() {
		
	}
	
	public void logout() {
		
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
