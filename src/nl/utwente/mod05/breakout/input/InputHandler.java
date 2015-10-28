package nl.utwente.mod05.breakout.input;

/**
 * Interface for input handling. This interface will provide the required methods needed for an
 * input handler.
 */
public abstract class InputHandler {
	public static final int ERROR_STATE = -1;
	public static final int DEFAULT_MAX_INPUT = 600;
	protected int maxWidth;
	protected int position;

	/**
	 * Instantiates InputHandler. Input based on type of InputHandler.
	 * @param width The maximum width for the input.
	 */
	public InputHandler(int width) {
		this.maxWidth = width;
		this.position = ERROR_STATE;
	}
	/**
	 * Handles input, each input handler should implement it's own handle method, depending on
	 * it's ways of handling input. Should be declared synchronized.
	 */
	public abstract void handle();

	/**
	 * Returns the absolute position of the game paddle the inputhandler has registered.
	 * @return The absolute position of the game paddle.
	 */
	public synchronized int getInput() {
		return this.position;
	}
}
