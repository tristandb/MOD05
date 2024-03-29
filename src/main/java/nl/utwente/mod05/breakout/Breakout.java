package nl.utwente.mod05.breakout;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import nl.utwente.mod05.breakout.input.CameraInputHandler;
import nl.utwente.mod05.breakout.input.CheatInputHandler;
import nl.utwente.mod05.breakout.input.InputHandler;
import nl.utwente.mod05.breakout.input.MouseInputHandler;
import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.ui.GUIController;

import java.io.IOException;
import java.util.Map;

/**
 * The main class of Breakout. This will start the application.
 */
public class Breakout extends Application {
	/**
	 * Debug constant, this is used to check whether debug output should be printed.
	 */
	public static boolean DEBUG = false;

	/**
	 * Starts the application.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The method used to draw the GUI.
	 * @param primaryStage The stage to use. Determined by JavaFX
	 */
	@Override
	public void start(Stage primaryStage) {
		//Command line arguments as parsed by JavaFX
		Map<String, String> params = this.getParameters().getNamed();
		int width = getIntFromParam(params, "width", Board.DEFAULT_WIDTH);
		int height = getIntFromParam(params, "height", Board.DEFAULT_HEIGHT);
		Breakout.DEBUG = getBoolFromParam(params, "debug", Breakout.DEBUG);

		//Loads the correct FXML file.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUIController.class.getClassLoader().getResource("GUI.fxml"));
		BorderPane layout = null;

		try {
			layout = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (layout != null) {
			Scene scene = new Scene(layout);

			Board board = new Board(width, height);

			InputHandler input = null;
			if (params.containsKey("input")) {

				if (params.get("input").equals("camera")) {
					String pipeName = this.getStringFromParam(params, "pipename",
							CameraInputHandler.DEFAULT_INPUT_NAME);
					input = new CameraInputHandler(width, pipeName);
				} else if (params.get("input").equals("cheat")) {
					input = new CheatInputHandler(width, board);
				}
			}
			if (input == null) {
				input = new MouseInputHandler(width, scene);
			}

			GUIController controller = loader.getController();
			controller.setPlayerName(getStringFromParam(params, "name", null));
			controller.setGetHighscores(getBoolFromParam(params, "gethighscores", true));
			controller.setBoard(board);
			controller.setInputHandler(input);
			controller.setVideo(getStringFromParam(params, "cameraview", null));
			controller.createGUI();


			scene.setOnMouseClicked(
					event -> {
						if (!board.isRunning()) {
							controller.startGame();
						}
					}
			);

			if (getBoolFromParam(params, "fullscreen", false)) {
				primaryStage.setFullScreen(true);
			} else {
				primaryStage.setMinWidth(width+200);
				primaryStage.setMinHeight(height);
			}
			primaryStage.setTitle("Augmented Breakout");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * Gets an integer from a parameter string.
	 * @param params The parameter map to use.
	 * @param name The name of the parameter.
	 * @param def The default value.
	 * @return The int representation of the parameter value.
	 */
	private int getIntFromParam(Map<String, String> params, String name, int def) {
		int result = def;
		if (params.containsKey(name)) {
			try {
				result = Integer.parseInt(params.get(name));
			} catch (NumberFormatException e) {
				result = def;
			}
		}
		return result;
	}

	/**
	 * Gets a boolean from a parameter string.
	 * @param params The parameter map to use.
	 * @param name The name of the parameter.
	 * @param def The default value.
	 * @return The int representation of the parameter value.
	 */
	private boolean getBoolFromParam(Map<String, String> params, String name, boolean def) {
		boolean result = def;
		if (params.containsKey(name)) {
			result = Boolean.parseBoolean(params.get(name));
		}
		return result;
	}

	/**
	 * Gets a String from a parameter string.
	 * @param params The parameter map to use.
	 * @param name The name of the parameter.
	 * @param def The default value.
	 * @return The parameter value.
	 */private String getStringFromParam(Map<String, String> params, String name, String def) {
		String result = def;
		if (params.containsKey(name)) {
			result = params.get(name);
		}
		return result;
	}
}
