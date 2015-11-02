package nl.utwente.mod05.breakout.input;

import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.model.Board;

import java.util.Random;

/**
 * CheatInputHandler will always set input to ball position (plus a random factor). This means
 * that it is impossible to lose using this input handler.
 */
public class CheatInputHandler extends InputHandler {
	private Board board;

	/**
	 * Instantiates a CheatInputHandler.
	 * @param width The maximum width the input
	 * @param board The board used for the game, the inputhandler will get the ball position from
	 *                 this board.
	 */
	public CheatInputHandler(int width, Board board) {
		super(width);
		this.board = board;
	}


	/**
	 * Handler is not needed by the cheatinputhandler
	 */
	@Override
	public synchronized void handle() {
		if (Breakout.DEBUG) {
			System.out.println("Using input: " + this.getClass().getSimpleName());
		}
	}

	@Override
	public synchronized int getInput() {
		double ballX = this.board.getBall().getX() + this.board.getBall().getRadius();
		this.position = (int) ballX
				+ ((new Random()).nextInt((int) this.board.getPaddle().getWidth())
				* (new Random()).nextInt(2) % 2 == 1 ? -1 : 1);
		return this.position;
	}
}
