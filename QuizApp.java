import java.util.ArrayList;
import java.util.List;

// holds all the data for the whole program (users, quizzes, results)
// and the current login session. screens read/write through this.
public class QuizApp {

	private List<Student> studentList = new ArrayList<>();
	private List<Teacher> teacherList = new ArrayList<>();
	private Admin admin;
	private List<Quiz> quizList = new ArrayList<>();
	private List<Result> resultList = new ArrayList<>();

	private int nextUserID = 100;
	private int nextQuizID = 1;
	private int nextQuestionID = 1;
	private int nextResultID = 1;

	// session info
	private String loggedInRole = "";
	private int loggedInID = -1;
	private String loggedInName = "";

	public QuizApp() {
		loadSampleData();
	}

	private void loadSampleData() {
		admin = new Admin(1, "System Admin", "admin", "admin123");

		Teacher t1 = new Teacher(nextUserID++, "Dr. Sarah Ali", "teacher1", "teach123");
		teacherList.add(t1);

		Student s1 = new Student(nextUserID++, "Ahmed Raza", "student1", "stud123");
		studentList.add(s1);

		Quiz q1 = new Quiz(nextQuizID++, "OOP Fundamentals", 60, t1.getUserID());
		q1.addQuestion(new Question(nextQuestionID++,
				"Which is a pillar of OOP?",
				"Looping", "Encapsulation", "Sorting", "Indexing", 'B'));
		q1.addQuestion(new Question(nextQuestionID++,
				"What does 'class' define in OOP?",
				"A function", "A variable", "A blueprint for objects", "A loop", 'C'));
		q1.addQuestion(new Question(nextQuestionID++,
				"Which keyword is used for inheritance in Java?",
				"implements", "extends", "inherits", "super", 'B'));
		q1.addQuestion(new Question(nextQuestionID++,
				"What is a constructor used for?",
				"Destroying an object", "Creating and initializing an object",
				"Looping through data", "Declaring a class", 'B'));
		q1.addQuestion(new Question(nextQuestionID++,
				"What does polymorphism mean?",
				"Multiple databases", "One name, many forms / behaviors",
				"Only single inheritance", "All methods are static", 'B'));

		t1.addCreatedQuiz(q1.getQuizID());
		quizList.add(q1);
	}

	// ---- getters for the data lists ----
	public List<Student> getStudentList() {
		return studentList;
	}

	public List<Teacher> getTeacherList() {
		return teacherList;
	}

	public Admin getAdmin() {
		return admin;
	}

	public List<Quiz> getQuizList() {
		return quizList;
	}

	public List<Result> getResultList() {
		return resultList;
	}

	// ---- ID generators ----
	public int nextUserID() {
		return nextUserID++;
	}

	public int nextQuizID() {
		return nextQuizID++;
	}

	public int nextQuestionID() {
		return nextQuestionID++;
	}

	public int nextResultID() {
		return nextResultID++;
	}

	// ---- login helpers ----
	public boolean login(String username, String password) {
		if (username.equals(admin.getUsername()) && password.equals(admin.getPassword())) {
			loggedInRole = "admin";
			loggedInID = admin.getUserID();
			loggedInName = admin.getName();
			return true;
		}

		for (Teacher t : teacherList) {
			if (username.equals(t.getUsername()) && password.equals(t.getPassword())) {
				loggedInRole = "teacher";
				loggedInID = t.getUserID();
				loggedInName = t.getName();
				return true;
			}
		}

		for (Student s : studentList) {
			if (username.equals(s.getUsername()) && password.equals(s.getPassword())) {
				loggedInRole = "student";
				loggedInID = s.getUserID();
				loggedInName = s.getName();
				return true;
			}
		}

		return false;
	}

	public void logout() {
		loggedInRole = "";
		loggedInID = -1;
		loggedInName = "";
	}

	public String getLoggedInRole() {
		return loggedInRole;
	}

	public int getLoggedInID() {
		return loggedInID;
	}

	public String getLoggedInName() {
		return loggedInName;
	}

	public Student findStudent(int id) {
		for (Student s : studentList) {
			if (s.getUserID() == id)
				return s;
		}
		return null;
	}

	public Teacher findTeacher(int id) {
		for (Teacher t : teacherList) {
			if (t.getUserID() == id)
				return t;
		}
		return null;
	}

	public Student getLoggedInStudent() {
		return findStudent(loggedInID);
	}

	public Teacher getLoggedInTeacher() {
		return findTeacher(loggedInID);
	}

	public boolean usernameTaken(String username) {
		for (Student s : studentList) {
			if (s.getUsername().equalsIgnoreCase(username))
				return true;
		}
		for (Teacher t : teacherList) {
			if (t.getUsername().equalsIgnoreCase(username))
				return true;
		}
		if (admin.getUsername().equalsIgnoreCase(username))
			return true;
		return false;
	}

	public void registerStudent(String name, String username, String password) {
		studentList.add(new Student(nextUserID(), name, username, password));
	}
}
