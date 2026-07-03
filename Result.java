public class Result {

	private int resultID;
	private int studentID;
	private String studentName;
	private int quizID;
	private String quizTitle;
	private int score;
	private int totalMarks;
	private int timeTaken;
	private String grade;

	public Result(int resultID, int studentID, String studentName, int quizID, String quizTitle,
			int score, int totalMarks, int timeTaken) {
		this.resultID = resultID;
		this.studentID = studentID;
		this.studentName = studentName;
		this.quizID = quizID;
		this.quizTitle = quizTitle;
		this.score = score;
		this.totalMarks = totalMarks;
		this.timeTaken = timeTaken;
		this.grade = calculateGrade();
	}

	private String calculateGrade() {
		double percentage = getPercentage();

		if (percentage >= 90)
			return "A+";
		else if (percentage >= 80)
			return "A";
		else if (percentage >= 70)
			return "B";
		else if (percentage >= 60)
			return "C";
		else if (percentage >= 50)
			return "D";
		else
			return "F";
	}

	public double getPercentage() {
		if (totalMarks == 0)
			return 0;
		return (score * 100.0) / totalMarks;
	}

	public int getStudentID() {
		return studentID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getScore() {
		return score;
	}

	public int getTotalMarks() {
		return totalMarks;
	}

	public String getGrade() {
		return grade;
	}

	public String getStudentName() {
		return studentName;
	}

	public String getQuizTitle() {
		return quizTitle;
	}

	public int getTimeTaken() {
		return timeTaken;
	}
}
