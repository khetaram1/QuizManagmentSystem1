import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {

	private List<Integer> createdQuizIDs;

	public Teacher(int userID, String name, String username, String password) {
		super(userID, name, username, password, "Teacher");
		createdQuizIDs = new ArrayList<>();
	}

	public void addCreatedQuiz(int quizID) {
		createdQuizIDs.add(quizID);
	}

	public int getCreatedQuizCount() {
		return createdQuizIDs.size();
	}

	@Override
	public String getDashboardTitle() {
		return "Teacher Dashboard";
	}
}
