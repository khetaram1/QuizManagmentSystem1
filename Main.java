import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		QuizApp app = new QuizApp();

		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #1e1e2f;");

		ScreenManager screens = new ScreenManager(app, root, primaryStage);
		screens.showWelcome();

		Scene scene = new Scene(root, 740, 560);
		primaryStage.setTitle("Quiz Management System - OOP Lab Project");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
