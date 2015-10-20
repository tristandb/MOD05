package nl.utwente.mod05.breakout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.ui.AppGUIController;

import java.io.IOException;

public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AppGUIController.class.getResource("views/App.fxml"));
		BorderPane layout;
		Board board = new Board();
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
