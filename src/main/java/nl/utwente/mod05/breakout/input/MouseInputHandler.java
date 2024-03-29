package nl.utwente.mod05.breakout.input;

import javafx.scene.Scene;
import nl.utwente.mod05.breakout.Breakout;

/**
 * MouseInputHandler, bases input on coordinates of the cursor.
 */
public class MouseInputHandler extends InputHandler {
	private Scene scene;

	/**
	 * Instantiates MouseInputHandler. Input based on coordinates of cursor.
	 * @param width The maximum width for the input. NOTE: Not needed for this InputHandler since
	 *                 coordinates dont overflow the window.
	 * @param scene The scene to bind the mouse event to.
	 */
	public MouseInputHandler(int width, Scene scene) {
		super(width);
		this.scene = scene;
	}

	/**
	 * Handles user input based on cursor coordinates.
	 */
	@Override
	public synchronized void handle() {
		if (Breakout.DEBUG) {
			System.out.println("Using input: " + this.getClass().getSimpleName());
		}
		this.scene.setOnMouseMoved(
				event -> position = (int) (event.getX())
		);

		this.scene.setOnKeyTyped(
				event -> position = InputHandler.ERROR_STATE
		);
	}
}
