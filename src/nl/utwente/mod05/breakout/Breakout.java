package nl.utwente.mod05.breakout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.ui.AppGUIController;

import java.io.IOException;
import java.util.Map;

/**
 * TODO: Documentation...
 */
public class Breakout extends Application {
	public static final boolean DEBUG = true;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Map<String, String> params = this.getParameters().getNamed();
		int width = 600;
		int height = 480;
		if (params.containsKey("width") && params.containsKey("height")) {
			try {
				width = Integer.parseInt(params.get("width"));
				height = Integer.parseInt(params.get("height"));
			} catch (NumberFormatException e) {
				width = -1;
				height = -1;
			}
		}
		if (width < 600 || height < 480) {
			width = 600;
			height = 480;
		}

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AppGUIController.class.getResource("views/GUI.fxml"));
		BorderPane layout;
		Board board = new Board(width, height);
		try {
			layout = loader.load();
			AppGUIController controller = loader.getController();
			controller.setStage(primaryStage);
			controller.setBoard(board);

			controller.startGame();
			Scene scene = new Scene(layout);

			primaryStage.setMinWidth(800);
			primaryStage.setMinHeight(600);
			primaryStage.setTitle("Augmented Breakout");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
