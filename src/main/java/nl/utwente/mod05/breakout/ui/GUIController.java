package nl.utwente.mod05.breakout.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.helper.DatabaseConnector;
import nl.utwente.mod05.breakout.input.InputHandler;
import nl.utwente.mod05.breakout.model.Board;
import nl.utwente.mod05.breakout.model.Score;
import nl.utwente.mod05.breakout.model.ScoreList;
import nl.utwente.mod05.breakout.model.items.Item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Controller class for the GUI. This class handles all user input and output.
 */
public class GUIController {
	public static final int CAMERA_WIDTH = 200;
	public static final int CAMERA_HEIGHT = 150;

	@FXML
	private BorderPane borderPane;
	@FXML
	private TableView<Score> scores;
	@FXML
	private Canvas cameraCanvas;

	private String name = null;
	private boolean getHighscores = false;

	private Board board;
	private InputHandler inputHandler;
	private InputStream videoStream = null;
	public static GraphicsContext context;

	/**
	 * Creates the GUI, does not draw Items on the canvas.
	 */
	public void createGUI() {
		Canvas cv = new Canvas(this.board.getWidth(), this.board.getHeight());
		this.borderPane.setCenter(cv);

		if (this.inputHandler != null) {
			this.inputHandler.handle();
		}
		if (this.getHighscores) {
			this.updateScoreTable();
		}
		GUIController.context = cv.getGraphicsContext2D();
	}

	/**
	 * Updates the scoretable
	 */
	@FXML
	public void updateScoreTable() {
		// Remove all elements
		scores.getItems().removeAll(scores.getItems());
		// Add new elements
		scores.setItems((new ScoreList()).getScoreList());
	}

	/**
	 * Starts the game.
	 */
	public void startGame() {
		//Make a direct copy of the context object, for speed reasons.
		GraphicsContext gc = GUIController.context;
		//Reset the board to default values, this is useful for restarting after a stop.
		this.board.reset(this.board.getWidth(), this.board.getHeight());
		this.board.start();

		//Animation timer, should run.sh about 60 times a second.
		new AnimationTimer() {
			long oldTime = System.nanoTime();
			int callGc = 0;
			public synchronized void handle(long currentTime) {
				drawCamera();
				//Clear the canvas for a new frame.
				gc.clearRect(0, 0, board.getWidth(), board.getHeight());
				//Calculate new frame values.
				board.next();
				//Retrieve input from InputHandler and set the paddle position accordingly.

				int input = inputHandler.getInput();
				if (input == InputHandler.ERROR_STATE) {
					board.pause();
				} else if (board.isPaused()) {
					board.unpause();
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
						gc.setStroke(new Color(0, 0, 0, 0.5));
						gc.strokeRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					}
				}


				if (!board.isRunning() && !board.isPaused()) {
					createOverlay(gc, "Score: " + board.getScore());
					//Stop the AnimationTimer.
					this.stop();

					gameOver(board.getScore());

				}

				if (board.isPaused()) {
					createOverlay(gc, "Paused");
				}

				//On DEBUG, show FPS counter in the upper left corner.
				if (Breakout.DEBUG) {
					showDebug(gc, currentTime, oldTime);
				}

				//Time values used for FPS.
				oldTime = currentTime;

				//Call the garbage collector every 10 seconds.
				callGc++;
				if (callGc >= 600) {
					System.gc();
					callGc = 0;
				}
			}
		}.start();
	}

	private void createOverlay(GraphicsContext gc, String text) {
		gc.setFill(new Color(0, 0, 0, 0.75));
		gc.fillRect(0, 0, this.board.getWidth(), this.board.getHeight());
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.setFont(new Font(24));
		gc.setTextAlign(TextAlignment.CENTER);
		gc.fillText(text, this.board.getWidth() / 2, this.board.getHeight() / 2);
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

	public void gameOver(int score) {
		if (this.getHighscores && this.name != null) {
			DatabaseConnector dbc = DatabaseConnector.getInstance();
			if (dbc != null) {
				dbc.addScore(name, score);
				this.updateScoreTable();
			}
		}
	}

	public String getName() {
		return this.name;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public void setVideo(String location) {
		if (location != null) {
			try {
				this.videoStream = new FileInputStream(location);
			} catch (FileNotFoundException e) {
				if (Breakout.DEBUG) {
					System.err.println("Can not read camera stream for viewing.");
				}
			}
		}
	}

	public void drawCamera() {
		if (this.videoStream != null) {
			GraphicsContext gc = this.cameraCanvas.getGraphicsContext2D();
			gc.clearRect(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
			gc.drawImage(new Image(this.videoStream), 0, 0);
		}
	}

	public void setGetHighscores(boolean h) {
		this.getHighscores = h;
	}
}