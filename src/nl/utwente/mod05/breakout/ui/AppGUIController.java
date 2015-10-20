package nl.utwente.mod05.breakout.ui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
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

	public void startGame() {
		GraphicsContext gc = this.boardCanvas.getGraphicsContext2D();
		Rectangle paddle = new Rectangle(15, 100);

		final long startTime = System.nanoTime();
		new AnimationTimer() {
			public void handle(long time) {
				long x = (time - startTime) % 600;
				gc.setFill(new Color(0, 0.39, 1, 1));
				gc.fillRect(x, 200, 100, 15);
				gc.setFill(Color.BLACK);
				gc.fillRect(x, 200, 100, 15);
			}
		}.start();
	}
}