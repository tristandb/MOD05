package nl.utwente.mod05.breakout.ui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import nl.utwente.mod05.breakout.model.Board;

/**
 * Controller class for the GUI. This class handles all user input and output.
 */
public class AppGUIController {

	private Stage stage;
	private Board board;

	@FXML
	private TableView<?> scores;

	@FXML
	private Canvas cameraCanvas;

	@FXML
	private Canvas boardCanvas;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}