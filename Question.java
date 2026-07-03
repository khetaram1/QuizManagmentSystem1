public class Question {

	private int questionID;
	private String questionText;
	private String optionA, optionB, optionC, optionD;
	private char correctAnswer;
	private int marks;

	public Question(int questionID, String questionText, String optionA, String optionB,
			String optionC, String optionD, char correctAnswer, int marks) {
		this.questionID = questionID;
		this.questionText = questionText;
		this.optionA = optionA;
		this.optionB = optionB;
		this.optionC = optionC;
		this.optionD = optionD;
		this.correctAnswer = Character.toUpperCase(correctAnswer);
		this.marks = marks;
	}

	public Question(int questionID, String questionText, String optionA, String optionB,
			String optionC, String optionD, char correctAnswer) {
		this(questionID, questionText, optionA, optionB, optionC, optionD, correctAnswer, 1);
	}

	public int getQuestionID() {
		return questionID;
	}

	public String getQuestionText() {
		return questionText;
	}

	public String getOption(int index) {
		switch (index) {
		case 0: return optionA;
		case 1: return optionB;
		case 2: return optionC;
		case 3: return optionD;
		}
		return "";
	}

	public char getCorrectAnswer() {
		return correctAnswer;
	}

	public int getMarks() {
		return marks;
	}

	public boolean isCorrect(char givenAnswer) {
		return Character.toUpperCase(givenAnswer) == correctAnswer;
	}
}
