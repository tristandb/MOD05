package nl.utwente.mod05.breakout.ui;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.input.InputHandler;
import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.model.items.Item;

import java.util.List;

/**
 * Controller class for the GUI. This class handles all user input and output.
 */
public class GUIController {
	@FXML
	private BorderPane borderPane;
	@FXML
	private TableView<?> scores;
	@FXML
	private Canvas cameraCanvas;

	private Board board;
	private InputHandler inputHandler;
	private GraphicsContext context;

	/**
	 * Creates the GUI, does not draw Items on the canvas.
	 */
	public void createGUI() {
		final int width = this.board.getWidth();
		final int height = this.board.getHeight();

		Canvas cv = new Canvas(width, height);
		this.borderPane.setCenter(cv);
		this.context = cv.getGraphicsContext2D();
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		//Make a direct copy of the context object, for speed reasons.
		GraphicsContext gc = this.context;
		//Reset the board to default values, this is useful for restarting after a stop.
		this.board.reset(this.board.getWidth(), this.board.getHeight());
		this.board.start();

		//Animation timer, should run about 60 times a second.
		new AnimationTimer() {
			long oldTime = System.nanoTime();
			public synchronized void handle(long currentTime) {
				//Clear the canvas for a new frame.
				gc.clearRect(0, 0, board.getWidth(), board.getHeight());
				//Calculate new frame values.
				board.next();
				//Retrieve input from InputHandler and set the paddle position accordingly.

				int input = inputHandler.getInput();
				if (input == InputHandler.ERROR_STATE) {
					board.pause();
				} else if (board.isPaused()){
					board.unpause();
				}

				if (!board.isRunning() && !board.isPaused()) {
					if (Breakout.DEBUG) {
						System.out.println("Score: " + board.getScore());
					}
					//Stop the AnimationTimer.
					this.stop();
				}

				board.setPaddlePosition(input);

				//Draw all items on the canvas. This will draw the whole game layout.
				List<Item> items = board.getItems();
				for (Item item : items) {
					gc.setFill(Color.web(item.getColor()));
					if (item.getShape() == Item.Shape.CIRCLE) {
						gc.fillOval(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					} else {
						gc.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					}
				}

				if (board.isPaused()) {
					gc.setFill(new Color(0, 0, 0, 0.5));
					gc.fillRect(0, 0, board.getWidth(), board.getHeight());
				}

				//On DEBUG, show FPS counter in the upper left corner.
				if (Breakout.DEBUG) {
					showDebug(gc, currentTime, oldTime);
				}

				//Time values used for FPS.
				oldTime = currentTime;
			}
		}.start();
	}

	/**
	 * Calculate the amount of frames per second
	 * @param n The new time used for FPS determination.
	 * @param o The old time used for FPS determination.
	 * @return The FPS
	 */
	private double calculateFPS(long n, long o) {
		return (1 / ((n - o) / (double) 1000000000));
	}

	/**
	 * Prints debug output on the screen
	 * @param gc The context to write to.
	 * @param newTime The new time used for FPS determination.
	 * @param oldTime The old time used for FPS determination.
	 */
	private void showDebug(GraphicsContext gc, long newTime, long oldTime) {
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.setFont(new Font(14));
		gc.fillText("FPS: " + (int) calculateFPS(newTime, oldTime), 20, 20);
		gc.strokeText("FPS: " + (int) calculateFPS(newTime, oldTime), 20, 20);

	}

	/**
	 * Sets the internal board representation.
	 * @param board The board.
	 */
	public void setBoard(Board board) {
		this.board = board;
	}

	/**
	 * Sets the InputHandler to be used.
	 * @param ih The InputHandler.
	 */
	public void setInputHandler(InputHandler ih) {
		this.inputHandler = ih;
	}
}