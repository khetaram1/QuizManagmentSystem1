public class Admin extends User {

	public Admin(int userID, String name, String username, String password) {
		super(userID, name, username, password, "Admin");
	}

	@Override
	public String getDashboardTitle() {
		return "Admin Dashboard";
	}
}
