import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

// this class is responsible for actually drawing each screen
// and switching the center of the root layout when buttons get clicked
public class ScreenManager {

	private QuizApp app;
	private BorderPane root;
	private Stage stage;

	// quiz-taking session variables
	private Quiz currentQuiz;
	private int currentQuestionIndex;
	private int currentScore;
	private int secondsPerQuestion;
	private long quizStartTimeMs;

	private QuizTimer timer;
	private Timeline ticker;
	private Label timerLabel;
	private ProgressBar timerBar;

	public ScreenManager(QuizApp app, BorderPane root, Stage stage) {
		this.app = app;
		this.root = root;
		this.stage = stage;
	}

	// ================= WELCOME =================
	public void showWelcome() {
		VBox box = UIHelper.container();
		box.setAlignment(Pos.CENTER);

		Label title = UIHelper.heading("QUIZ MANAGEMENT SYSTEM");
		Label sub = UIHelper.subText("Iqra University - OOP Lab Project (JavaFX)");

		Button loginBtn = UIHelper.button("Login", "#0078d7");
		Button registerBtn = UIHelper.button("Register as Student", "#2e7d32");
		Button exitBtn = UIHelper.button("Exit", "#b71c1c");

		loginBtn.setOnAction(e -> showLogin());
		registerBtn.setOnAction(e -> showRegister());
		exitBtn.setOnAction(e -> stage.close());

		VBox buttons = new VBox(10, loginBtn, registerBtn, exitBtn);
		buttons.setMaxWidth(260);
		buttons.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, sub, new Separator(), buttons);
		root.setCenter(box);
	}

	// ================= LOGIN =================
	public void showLogin() {
		VBox box = UIHelper.container();
		box.setAlignment(Pos.CENTER);

		Label title = UIHelper.heading("Login");
		TextField unameField = UIHelper.textField("Username");
		PasswordField pwdField = UIHelper.passwordField("Password");

		Label hint = UIHelper.subText("admin/admin123  teacher1/teach123  student1/stud123");
		hint.setFont(Font.font("Segoe UI", 10));

		Label error = UIHelper.errorText();

		Button loginBtn = UIHelper.button("Login", "#0078d7");
		Button backBtn = UIHelper.button("Back", "#555566");

		loginBtn.setOnAction(e -> {
			boolean ok = app.login(unameField.getText().trim(), pwdField.getText().trim());
			if (!ok) {
				error.setText("Invalid username or password.");
				return;
			}
			String role = app.getLoggedInRole();
			if (role.equals("admin"))
				showAdminDashboard();
			else if (role.equals("teacher"))
				showTeacherDashboard();
			else
				showStudentDashboard();
		});
		backBtn.setOnAction(e -> showWelcome());

		VBox form = new VBox(10, unameField, pwdField, error, loginBtn, backBtn, hint);
		form.setMaxWidth(280);
		form.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, form);
		root.setCenter(box);
	}

	// ================= REGISTER =================
	public void showRegister() {
		VBox box = UIHelper.container();
		box.setAlignment(Pos.CENTER);

		Label title = UIHelper.heading("Register New Student");
		TextField nameField = UIHelper.textField("Full Name");
		TextField unameField = UIHelper.textField("Username");
		PasswordField pwdField = UIHelper.passwordField("Password");
		Label error = UIHelper.errorText();

		Button registerBtn = UIHelper.button("Register", "#2e7d32");
		Button backBtn = UIHelper.button("Back", "#555566");

		registerBtn.setOnAction(e -> {
			String name = nameField.getText().trim();
			String uname = unameField.getText().trim();
			String pwd = pwdField.getText().trim();

			if (name.isEmpty() || uname.isEmpty() || pwd.isEmpty()) {
				error.setText("Please fill in every field.");
				return;
			}
			if (app.usernameTaken(uname)) {
				error.setText("That username is already taken.");
				return;
			}
			app.registerStudent(name, uname, pwd);
			error.setTextFill(Color.web("#33cc66"));
			error.setText("Done! You can log in now.");
		});
		backBtn.setOnAction(e -> showWelcome());

		VBox form = new VBox(10, nameField, unameField, pwdField, error, registerBtn, backBtn);
		form.setMaxWidth(280);
		form.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, form);
		root.setCenter(box);
	}

	// ================= STUDENT DASHBOARD =================
	public void showStudentDashboard() {
		Student s = app.getLoggedInStudent();

		VBox box = UIHelper.container();
		Label title = UIHelper.heading(s.getDashboardTitle());
		Label sub = UIHelper.subText("Welcome, " + s.getName() + "   |   Attempts so far: "
				+ s.getResultHistory().size());

		Button viewBtn = UIHelper.button("View Available Quizzes", "#0078d7");
		Button attemptBtn = UIHelper.button("Attempt a Quiz", "#2e7d32");
		Button resultsBtn = UIHelper.button("My Results", "#7b1fa2");
		Button logoutBtn = UIHelper.button("Logout", "#b71c1c");

		viewBtn.setOnAction(e -> showQuizList(false));
		attemptBtn.setOnAction(e -> showQuizList(true));
		resultsBtn.setOnAction(e -> showMyResults());
		logoutBtn.setOnAction(e -> {
			app.logout();
			showWelcome();
		});

		VBox buttons = new VBox(10, viewBtn, attemptBtn, resultsBtn, logoutBtn);
		buttons.setMaxWidth(300);
		buttons.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, sub, new Separator(), buttons);
		root.setCenter(box);
	}

	// ================= TEACHER DASHBOARD =================
	public void showTeacherDashboard() {
		Teacher t = app.getLoggedInTeacher();

		VBox box = UIHelper.container();
		Label title = UIHelper.heading(t.getDashboardTitle());
		Label sub = UIHelper.subText("Welcome, " + t.getName() + "   |   Quizzes created: "
				+ t.getCreatedQuizCount());

		Button createBtn = UIHelper.button("Create New Quiz", "#2e7d32");
		Button viewBtn = UIHelper.button("View All Quizzes", "#0078d7");
		Button logoutBtn = UIHelper.button("Logout", "#b71c1c");

		createBtn.setOnAction(e -> showCreateQuiz());
		viewBtn.setOnAction(e -> showQuizList(false));
		logoutBtn.setOnAction(e -> {
			app.logout();
			showWelcome();
		});

		VBox buttons = new VBox(10, createBtn, viewBtn, logoutBtn);
		buttons.setMaxWidth(300);
		buttons.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, sub, new Separator(), buttons);
		root.setCenter(box);
	}

	// ================= ADMIN DASHBOARD =================
	public void showAdminDashboard() {
		VBox box = UIHelper.container();
		Label title = UIHelper.heading(app.getAdmin().getDashboardTitle());
		Label sub = UIHelper.subText("Welcome, " + app.getAdmin().getName());

		Button usersBtn = UIHelper.button("Manage Users", "#0078d7");
		Button quizzesBtn = UIHelper.button("View All Quizzes", "#7b1fa2");
		Button resultsBtn = UIHelper.button("View All Results", "#ef6c00");
		Button logoutBtn = UIHelper.button("Logout", "#b71c1c");

		usersBtn.setOnAction(e -> showManageUsers());
		quizzesBtn.setOnAction(e -> showQuizList(false));
		resultsBtn.setOnAction(e -> showAllResults());
		logoutBtn.setOnAction(e -> {
			app.logout();
			showWelcome();
		});

		VBox buttons = new VBox(10, usersBtn, quizzesBtn, resultsBtn, logoutBtn);
		buttons.setMaxWidth(300);
		buttons.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, sub, new Separator(), buttons);
		root.setCenter(box);
	}

	// ================= MANAGE USERS =================
	private void showManageUsers() {
		VBox box = UIHelper.container();
		Label title = UIHelper.heading("User Management");

		VBox list = new VBox(6);
		list.setAlignment(Pos.CENTER_LEFT);

		Label teacherHead = new Label("TEACHERS");
		teacherHead.setTextFill(Color.web("#00e0ff"));
		teacherHead.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
		list.getChildren().add(teacherHead);

		for (Teacher t : app.getTeacherList()) {
			Label l = new Label("[" + t.getUserID() + "] " + t.getName() + "  @" + t.getUsername());
			l.setTextFill(Color.WHITE);
			list.getChildren().add(l);
		}

		Label studentHead = new Label("STUDENTS");
		studentHead.setTextFill(Color.web("#00e0ff"));
		studentHead.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
		list.getChildren().add(studentHead);

		for (Student s : app.getStudentList()) {
			Label l = new Label("[" + s.getUserID() + "] " + s.getName() + "  @" + s.getUsername());
			l.setTextFill(Color.WHITE);
			list.getChildren().add(l);
		}

		ScrollPane scroll = new ScrollPane(list);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(280);
		scroll.setStyle("-fx-background: #1e1e2f; -fx-background-color: #1e1e2f;");

		Button backBtn = UIHelper.button("Back", "#555566");
		backBtn.setMaxWidth(180);
		backBtn.setOnAction(e -> showAdminDashboard());

		box.getChildren().addAll(title, scroll, backBtn);
		root.setCenter(box);
	}

	// ================= ALL RESULTS =================
	private void showAllResults() {
		VBox box = UIHelper.container();
		Label title = UIHelper.heading("All Student Results");

		VBox list = new VBox(8);
		list.setAlignment(Pos.CENTER_LEFT);

		if (app.getResultList().isEmpty()) {
			Label none = new Label("No results yet.");
			none.setTextFill(Color.web("#ffcc00"));
			list.getChildren().add(none);
		} else {
			for (Result r : app.getResultList()) {
				Label l = new Label(r.getStudentName() + " -> " + r.getQuizTitle() + " -> "
						+ r.getScore() + "/" + r.getTotalMarks() + " (" + r.getGrade() + ")");
				l.setTextFill(r.getGrade().equals("F") ? Color.web("#ff4d4d") : Color.web("#33cc66"));
				list.getChildren().add(l);
			}
		}

		ScrollPane scroll = new ScrollPane(list);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(280);
		scroll.setStyle("-fx-background: #1e1e2f; -fx-background-color: #1e1e2f;");

		Button backBtn = UIHelper.button("Back", "#555566");
		backBtn.setMaxWidth(180);
		backBtn.setOnAction(e -> showAdminDashboard());

		box.getChildren().addAll(title, scroll, backBtn);
		root.setCenter(box);
	}

	// ================= MY RESULTS (student) =================
	private void showMyResults() {
		Student s = app.getLoggedInStudent();

		VBox box = UIHelper.container();
		Label title = UIHelper.heading("My Results");

		VBox list = new VBox(10);
		list.setAlignment(Pos.CENTER_LEFT);

		if (s.getResultHistory().isEmpty()) {
			Label none = new Label("You haven't attempted any quiz yet.");
			none.setTextFill(Color.web("#ffcc00"));
			list.getChildren().add(none);
		} else {
			for (Result r : s.getResultHistory()) {
				list.getChildren().add(buildResultCard(r));
			}
		}

		ScrollPane scroll = new ScrollPane(list);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(300);
		scroll.setStyle("-fx-background: #1e1e2f; -fx-background-color: #1e1e2f;");

		Button backBtn = UIHelper.button("Back", "#555566");
		backBtn.setMaxWidth(180);
		backBtn.setOnAction(e -> showStudentDashboard());

		box.getChildren().addAll(title, scroll, backBtn);
		root.setCenter(box);
	}

	private VBox buildResultCard(Result r) {
		VBox card = new VBox(4);
		card.setPadding(new Insets(12));
		card.setStyle("-fx-background-color: #2b2b3d; -fx-background-radius: 6;");

		Label name = new Label(r.getQuizTitle());
		name.setTextFill(Color.web("#00e0ff"));
		name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));

		Color c = r.getPercentage() >= 50 ? Color.web("#33cc66") : Color.web("#ff4d4d");
		Label score = new Label("Score: " + r.getScore() + "/" + r.getTotalMarks()
				+ "  (" + String.format("%.1f", r.getPercentage()) + "%)   Grade: " + r.getGrade());
		score.setTextFill(c);
		score.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));

		Label time = new Label("Time taken: " + r.getTimeTaken() + "s");
		time.setTextFill(Color.web("#999999"));
		time.setFont(Font.font("Segoe UI", 11));

		card.getChildren().addAll(name, score, time);
		return card;
	}

	// ================= QUIZ LIST =================
	private void showQuizList(boolean attemptMode) {
		VBox box = UIHelper.container();
		Label title = UIHelper.heading(attemptMode ? "Choose a Quiz" : "Available Quizzes");

		VBox list = new VBox(10);
		list.setAlignment(Pos.CENTER);

		if (app.getQuizList().isEmpty()) {
			Label none = new Label("No quizzes yet.");
			none.setTextFill(Color.web("#ffcc00"));
			list.getChildren().add(none);
		} else {
			for (Quiz q : app.getQuizList()) {
				HBox row = new HBox(12);
				row.setAlignment(Pos.CENTER_LEFT);
				row.setPadding(new Insets(10));
				row.setStyle("-fx-background-color: #2b2b3d; -fx-background-radius: 6;");

				Label info = new Label(q.getTitle() + "   |  " + q.getQuestionCount() + " Qs  |  "
						+ q.getTimeLimit() + "s  |  " + q.getTotalMarks() + " marks");
				info.setTextFill(Color.WHITE);
				HBox.setHgrow(info, Priority.ALWAYS);

				row.getChildren().add(info);

				if (attemptMode) {
					Student s = app.getLoggedInStudent();
					boolean done = s.hasAttempted(q.getQuizID());
					Button go = UIHelper.button(done ? "Done" : "Start", "#2e7d32");
					go.setMaxWidth(100);
					go.setDisable(done || q.getQuestionCount() == 0);
					go.setOnAction(e -> startQuiz(q));
					row.getChildren().add(go);
				}

				list.getChildren().add(row);
			}
		}

		ScrollPane scroll = new ScrollPane(list);
		scroll.setFitToWidth(true);
		scroll.setMaxHeight(300);
		scroll.setMaxWidth(540);
		scroll.setStyle("-fx-background: #1e1e2f; -fx-background-color: #1e1e2f;");

		Button backBtn = UIHelper.button("Back", "#555566");
		backBtn.setMaxWidth(180);
		backBtn.setOnAction(e -> {
			String role = app.getLoggedInRole();
			if (role.equals("admin"))
				showAdminDashboard();
			else if (role.equals("teacher"))
				showTeacherDashboard();
			else
				showStudentDashboard();
		});

		box.getChildren().addAll(title, scroll, backBtn);
		root.setCenter(box);
	}

	// ================= CREATE QUIZ =================
	private void showCreateQuiz() {
		VBox box = UIHelper.container();
		Label title = UIHelper.heading("Create New Quiz");

		TextField titleField = UIHelper.textField("Quiz Title");
		TextField timeField = UIHelper.textField("Time Limit (seconds)");
		TextField countField = UIHelper.textField("Number of Questions");
		Label error = UIHelper.errorText();

		Button nextBtn = UIHelper.button("Next: Add Questions", "#2e7d32");
		Button cancelBtn = UIHelper.button("Cancel", "#555566");

		nextBtn.setOnAction(e -> {
			try {
				String qTitle = titleField.getText().trim();
				int time = Integer.parseInt(timeField.getText().trim());
				int count = Integer.parseInt(countField.getText().trim());

				if (qTitle.isEmpty() || time <= 0 || count <= 0) {
					error.setText("Fill everything with valid values please.");
					return;
				}

				Quiz newQuiz = new Quiz(app.nextQuizID(), qTitle, time, app.getLoggedInID());
				showAddQuestion(newQuiz, count, 0);
			} catch (NumberFormatException ex) {
				error.setText("Time and question count need to be numbers.");
			}
		});
		cancelBtn.setOnAction(e -> showTeacherDashboard());

		VBox form = new VBox(10, titleField, timeField, countField, error, nextBtn, cancelBtn);
		form.setMaxWidth(300);
		form.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, form);
		root.setCenter(box);
	}

	private void showAddQuestion(Quiz quiz, int totalToAdd, int index) {
		if (index >= totalToAdd) {
			Teacher t = app.findTeacher(app.getLoggedInID());
			t.addCreatedQuiz(quiz.getQuizID());
			app.getQuizList().add(quiz);

			VBox done = UIHelper.container();
			done.setAlignment(Pos.CENTER);
			Label msg = UIHelper.heading("Quiz Created!");
			Label sub = UIHelper.subText("\"" + quiz.getTitle() + "\" now has " + quiz.getQuestionCount() + " questions.");
			Button okBtn = UIHelper.button("Back to Dashboard", "#0078d7");
			okBtn.setMaxWidth(220);
			okBtn.setOnAction(e -> showTeacherDashboard());
			done.getChildren().addAll(msg, sub, okBtn);
			root.setCenter(done);
			return;
		}

		VBox box = UIHelper.container();
		Label title = UIHelper.heading("Question " + (index + 1) + " of " + totalToAdd);

		TextField qField = UIHelper.textField("Question text");
		TextField aField = UIHelper.textField("Option A");
		TextField bField = UIHelper.textField("Option B");
		TextField cField = UIHelper.textField("Option C");
		TextField dField = UIHelper.textField("Option D");

		ComboBox<String> correctBox = new ComboBox<>();
		correctBox.getItems().addAll("A", "B", "C", "D");
		correctBox.setValue("A");

		Label error = UIHelper.errorText();

		Button nextBtn = UIHelper.button(index == totalToAdd - 1 ? "Finish" : "Add & Next", "#2e7d32");
		Button cancelBtn = UIHelper.button("Cancel", "#b71c1c");

		nextBtn.setOnAction(e -> {
			if (qField.getText().trim().isEmpty() || aField.getText().trim().isEmpty()
					|| bField.getText().trim().isEmpty() || cField.getText().trim().isEmpty()
					|| dField.getText().trim().isEmpty()) {
				error.setText("All fields are required.");
				return;
			}
			char correct = correctBox.getValue().charAt(0);
			quiz.addQuestion(new Question(app.nextQuestionID(), qField.getText().trim(),
					aField.getText().trim(), bField.getText().trim(),
					cField.getText().trim(), dField.getText().trim(), correct));
			showAddQuestion(quiz, totalToAdd, index + 1);
		});
		cancelBtn.setOnAction(e -> showTeacherDashboard());

		VBox form = new VBox(8, qField, aField, bField, cField, dField,
				UIHelper.subText("Correct Answer:"), correctBox, error, nextBtn, cancelBtn);
		form.setMaxWidth(320);
		form.setAlignment(Pos.CENTER);

		box.getChildren().addAll(title, form);
		root.setCenter(box);
	}

	// ================= TAKING THE QUIZ =================
	private void startQuiz(Quiz quiz) {
		currentQuiz = quiz;
		currentQuestionIndex = 0;
		currentScore = 0;
		secondsPerQuestion = Math.max(10, quiz.getTimeLimit() / Math.max(1, quiz.getQuestionCount()));
		quizStartTimeMs = System.currentTimeMillis();
		showQuizIntro();
	}

	private void showQuizIntro() {
		VBox box = UIHelper.container();
		box.setAlignment(Pos.CENTER);

		Label title = UIHelper.heading(currentQuiz.getTitle());
		Label info = UIHelper.subText(currentQuiz.getQuestionCount() + " Questions   |   "
				+ currentQuiz.getTimeLimit() + " seconds total");
		Label info2 = UIHelper.subText("About " + secondsPerQuestion + " seconds per question.");

		Button startBtn = UIHelper.button("Start Quiz", "#2e7d32");
		startBtn.setMaxWidth(200);
		startBtn.setOnAction(e -> showQuestionScreen());

		box.getChildren().addAll(title, info, info2, startBtn);
		root.setCenter(box);
	}

	private void showQuestionScreen() {
		if (ticker != null)
			ticker.stop();

		Question q = currentQuiz.getQuestionList().get(currentQuestionIndex);
		timer = new QuizTimer(secondsPerQuestion);

		VBox box = UIHelper.container();

		Label progress = UIHelper.subText("Question " + (currentQuestionIndex + 1) + " of "
				+ currentQuiz.getQuestionCount());

		timerLabel = new Label();
		timerLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

		timerBar = new ProgressBar(1.0);
		timerBar.setPrefWidth(380);

		refreshTimerDisplay();

		Label qText = new Label(q.getQuestionText());
		qText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 17));
		qText.setTextFill(Color.WHITE);
		qText.setWrapText(true);
		qText.setMaxWidth(540);

		VBox options = new VBox(8);
		options.setAlignment(Pos.CENTER);
		options.setMaxWidth(540);

		char[] letters = { 'A', 'B', 'C', 'D' };
		for (int i = 0; i < 4; i++) {
			char letter = letters[i];
			Button optBtn = new Button(letter + ")  " + q.getOption(i));
			optBtn.setMaxWidth(Double.MAX_VALUE);
			optBtn.setFont(Font.font("Segoe UI", 13));
			optBtn.setStyle(normalOptionStyle());
			optBtn.setOnMouseEntered(e -> optBtn.setStyle(hoverOptionStyle()));
			optBtn.setOnMouseExited(e -> optBtn.setStyle(normalOptionStyle()));
			optBtn.setOnAction(e -> submitAnswer(q, letter));
			options.getChildren().add(optBtn);
		}

		box.getChildren().addAll(progress, timerLabel, timerBar, new Separator(), qText, options);
		root.setCenter(box);

		ticker = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			timer.tick();
			refreshTimerDisplay();
			if (timer.isFinished()) {
				ticker.stop();
				submitAnswer(q, 'X'); // ran out of time, counts as wrong
			}
		}));
		ticker.setCycleCount(Timeline.INDEFINITE);
		ticker.play();
	}

	private String normalOptionStyle() {
		return "-fx-background-color: #2b2b3d; -fx-text-fill: white; -fx-background-radius: 6; "
				+ "-fx-padding: 12; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
	}

	private String hoverOptionStyle() {
		return "-fx-background-color: #3c3c55; -fx-text-fill: white; -fx-background-radius: 6; "
				+ "-fx-padding: 12; -fx-cursor: hand; -fx-alignment: CENTER_LEFT;";
	}

	// this is where the red line behavior actually happens
	private void refreshTimerDisplay() {
		int left = timer.getTimeLeft();
		String formatted = timer.getFormatted();

		if (timer.isDanger()) {
			// last 5 seconds - red line + warning text, just like a real quiz countdown
			timerLabel.setText("TIME LEFT: " + formatted + "   !! HURRY UP !!");
			timerLabel.setTextFill(Color.web("#ff2b2b"));
			timerBar.setStyle("-fx-accent: #ff2b2b;");
		} else if (timer.isWarning()) {
			timerLabel.setText("TIME LEFT: " + formatted);
			timerLabel.setTextFill(Color.web("#ffcc00"));
			timerBar.setStyle("-fx-accent: #ffcc00;");
		} else {
			timerLabel.setText("TIME LEFT: " + formatted);
			timerLabel.setTextFill(Color.web("#33cc66"));
			timerBar.setStyle("-fx-accent: #33cc66;");
		}

		timerBar.setProgress((double) left / timer.getTotalTime());
	}

	private void submitAnswer(Question q, char answer) {
		if (ticker != null)
			ticker.stop();

		if (q.isCorrect(answer)) {
			currentScore += q.getMarks();
		}

		currentQuestionIndex++;
		if (currentQuestionIndex < currentQuiz.getQuestionCount()) {
			showQuestionScreen();
		} else {
			finishQuiz();
		}
	}

	private void finishQuiz() {
		int timeTaken = (int) ((System.currentTimeMillis() - quizStartTimeMs) / 1000);

		Result result = new Result(app.nextResultID(), app.getLoggedInID(), app.getLoggedInName(),
				currentQuiz.getQuizID(), currentQuiz.getTitle(), currentScore,
				currentQuiz.getTotalMarks(), timeTaken);

		Student s = app.getLoggedInStudent();
		s.addResult(result);
		app.getResultList().add(result);

		showResultScreen(result);
	}

	private void showResultScreen(Result r) {
		VBox box = UIHelper.container();
		box.setAlignment(Pos.CENTER);

		Label title = UIHelper.heading("Quiz Finished!");

		Label grade = new Label(r.getGrade());
		grade.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
		grade.setTextFill(r.getGrade().equals("F") ? Color.web("#ff4d4d") : Color.web("#33cc66"));

		VBox card = buildResultCard(r);
		card.setMaxWidth(360);

		Button doneBtn = UIHelper.button("Back to Dashboard", "#0078d7");
		doneBtn.setMaxWidth(220);
		doneBtn.setOnAction(e -> showStudentDashboard());

		box.getChildren().addAll(title, grade, card, doneBtn);
		root.setCenter(box);
	}
}
