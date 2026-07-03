import java.util.ArrayList;
import java.util.List;

public class Student extends User {

	private List<Result> resultHistory;
	private List<Integer> attemptedQuizIDs;

	public Student(int userID, String name, String username, String password) {
		super(userID, name, username, password, "Student");
		resultHistory = new ArrayList<>();
		attemptedQuizIDs = new ArrayList<>();
	}

	public void addResult(Result r) {
		resultHistory.add(r);
		attemptedQuizIDs.add(r.getQuizID());
	}

	public boolean hasAttempted(int quizID) {
		return attemptedQuizIDs.contains(quizID);
	}

	public List<Result> getResultHistory() {
		return resultHistory;
	}

	@Override
	public String getDashboardTitle() {
		return "Student Dashboard";
	}
}
