import java.util.ArrayList;
import java.util.List;

public class Quiz {

	private int quizID;
	private String title;
	private int timeLimit; // total seconds for the whole quiz
	private int creatorID;
	private boolean active;
	private List<Question> questionList;

	public Quiz(int quizID, String title, int timeLimit, int creatorID) {
		this.quizID = quizID;
		this.title = title;
		this.timeLimit = timeLimit;
		this.creatorID = creatorID;
		this.active = true;
		questionList = new ArrayList<>();
	}

	public int getQuizID() {
		return quizID;
	}

	public String getTitle() {
		return title;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public int getCreatorID() {
		return creatorID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Question> getQuestionList() {
		return questionList;
	}

	public void addQuestion(Question q) {
		questionList.add(q);
	}

	public int getQuestionCount() {
		return questionList.size();
	}

	public int getTotalMarks() {
		int sum = 0;
		for (Question q : questionList) {
			sum = sum + q.getMarks();
		}
		return sum;
	}
}
