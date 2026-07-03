public abstract class User {

	protected int userID;
	protected String name;
	protected String username;
	protected String password;
	protected String role;

	public User(int userID, String name, String username, String password, String role) {
		this.userID = userID;
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public int getUserID() {
		return userID;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	// every type of user has a different dashboard title, so this stays abstract
	public abstract String getDashboardTitle();
}
