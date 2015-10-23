package nl.utwente.mod05.breakout.ui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.model.items.Item;

import java.util.List;

/**
 * Controller class for the GUI. This class handles all user input and output.
 * TODO: documentation....
 */
public class AppGUIController {
	private static final Font DEBUGFONT = new Font(12);
	@FXML
	private BorderPane borderPane;

	private Stage stage;
	private Board board;

	@FXML
	private TableView<?> scores;

	@FXML
	private Canvas cameraCanvas;


	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void startGame() {
		final int width = this.board.getWidth();
		final int height = this.board.getHeight();

		Canvas cv = new Canvas(width, height);
		this.borderPane.setCenter(cv);
		GraphicsContext gc = cv.getGraphicsContext2D();
		final long startTime = System.nanoTime();

		new AnimationTimer() {
			long oldTime = System.nanoTime();
			public void handle(long time) {
				long frameTime = time  - oldTime;
				gc.clearRect(0, 0, width, height);
				if (Breakout.DEBUG) {
					printDebug(time, oldTime);
				}

				List<Item> items = board.getItems();
				for (Item item : items) {
					gc.setFill(Color.web(item.getColor()));
					if (item.getShape() == Item.Shape.CIRCLE) {
						gc.fillOval(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					} else {
						gc.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					}
				}

				//Draw blocks
				oldTime = time;
			}
		}.start();
	}

	private double calculateFPS(long n, long o) {
		return (1 / ((n - o) / (double) 1000000000));
	}

	private void printDebug(long newTime, long oldTime) {
		System.out.println((int) calculateFPS(newTime, oldTime));
		System.out.println((int) calculateFPS(newTime, oldTime));
	}
}