import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// just a small helper so I don't repeat the same styling code in every screen
public class UIHelper {

	public static Label heading(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
		label.setTextFill(Color.web("#00e0ff"));
		return label;
	}

	public static Label subText(String text) {
		Label label = new Label(text);
		label.setFont(Font.font("Segoe UI", 13));
		label.setTextFill(Color.web("#cfcfcf"));
		return label;
	}

	public static Label errorText() {
		Label label = new Label("");
		label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
		label.setTextFill(Color.web("#ff4d4d"));
		return label;
	}

	public static Button button(String text, String color) {
		Button btn = new Button(text);
		btn.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
		btn.setMaxWidth(Double.MAX_VALUE);
		btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
				+ "-fx-background-radius: 6; -fx-padding: 10 20 10 20; -fx-cursor: hand;");
		return btn;
	}

	public static TextField textField(String prompt) {
		TextField field = new TextField();
		field.setPromptText(prompt);
		field.setStyle("-fx-background-color: #2b2b3d; -fx-text-fill: white; "
				+ "-fx-prompt-text-fill: #888888; -fx-background-radius: 5; -fx-padding: 8;");
		return field;
	}

	public static PasswordField passwordField(String prompt) {
		PasswordField field = new PasswordField();
		field.setPromptText(prompt);
		field.setStyle("-fx-background-color: #2b2b3d; -fx-text-fill: white; "
				+ "-fx-prompt-text-fill: #888888; -fx-background-radius: 5; -fx-padding: 8;");
		return field;
	}

	public static VBox container() {
		VBox box = new VBox(15);
		box.setPadding(new Insets(35));
		box.setAlignment(Pos.TOP_CENTER);
		return box;
	}
}
