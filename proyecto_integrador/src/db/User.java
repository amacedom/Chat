package db;

public class User {
	int id;
	String status;
	String username;
	String password;
	String email;
	String twitterUsername;
	MySQL dbConn;
	
	public User(int id, String username,String password,String email,String twitter,String status, MySQL dbConn) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.twitterUsername = twitter;
		this.status = status;
		this.dbConn = dbConn;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getTwitterUsername() {
		return this.twitterUsername;
	}
	
	public MySQL getDBConn() {
		return this.dbConn;
	}
	
}
