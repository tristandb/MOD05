package nl.utwente.mod05.breakout.input;

import nl.utwente.mod05.breakout.model.Board;

import java.util.Random;

/**
 * Created by jelle on 28-10-15.
 */
public class CheatInputHandler extends InputHandler {
	private Board board;
	public CheatInputHandler(int width, Board board) {
		super(width);
		this.board = board;
	}

	@Override
	public synchronized void handle() {

	}

	@Override
	public synchronized int getInput() {
		double ballX = this.board.getBall().getX() + this.board.getBall().getRadius();
		this.position = (int) ballX
				+ ((new Random()).nextInt((int) this.board.getPaddle().getWidth() / 2)
				* (new Random()).nextInt(2) % 2 == 1 ? -1 : 1);
		return this.position;
	}
}
