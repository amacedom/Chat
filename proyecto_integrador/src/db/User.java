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
	
	private String getUsername() {
		return this.username;
	}
	
	private String getPassword() {
		return this.password;
	}
	
	private String getEmail() {
		return this.email;
	}
	
	private String getTwitterUsername() {
		return this.twitterUsername;
	}
	
	private MySQL getDBConn() {
		return this.dbConn;
	}
	
}
