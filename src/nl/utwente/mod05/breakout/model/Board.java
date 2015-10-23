package nl.utwente.mod05.breakout.model;

import nl.utwente.mod05.breakout.model.items.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Model class representing a board.
 */
public class Board {
	public static final int DEFAULT_WIDTH = 600;
	public static final int DEFAULT_HEIGHT = 480;
	public static final int BLOCKS_PER_ROW = 15;
	public static final int BLOCK_ROWS = 4;

	private int width;
	private int height;

	private Paddle paddle;
	private LinkedList<Block> blocks = new LinkedList<>();
	private Ball ball;

	/**
	 * Instantiates a board.
	 * @param width The width of the board.
	 * @param height The height of the board.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;

		this.ball = new Ball(
				width / 2 - Ball.DEFAULT_RADIUS,
				height / 2 - Ball.DEFAULT_RADIUS,
				Ball.DEFAULT_COLOR,
				Ball.DEFAULT_RADIUS,
				Ball.DEFAULT_VELOCITY,
				Ball.DEFAULT_HEADING);
		this.paddle = new Paddle(
				width / 2 - (Paddle.DEFAULT_WIDTH / 2),
				height - (Paddle.DEFAULT_HEIGHT),
				Paddle.DEFAULT_WIDTH,
				Paddle.DEFAULT_HEIGHT,
				Paddle.DEFAULT_COLOR
		);

		int blockWidth = width / BLOCKS_PER_ROW;
		for (int r = 0; r < BLOCK_ROWS; r++) {
			int colorRows = BLOCK_ROWS - 1;
			if (colorRows > Block.DEFAULT_COLORS.length) {
				colorRows = Block.DEFAULT_COLORS.length - 1;
			}
			String color = Block.DEFAULT_COLORS[colorRows - r];

			for (int c = 0; c < BLOCKS_PER_ROW; c++) {
				this.blocks.add(new Block(
						c * blockWidth,
						r * Block.DEFAULT_HEIGHT,
						blockWidth,
						Block.DEFAULT_HEIGHT,
						color,
						BLOCK_ROWS - r
				));
			}
		}
	}

	/**
	 * Returns the paddle.
	 * @return The paddle
	 */
	public Paddle getPaddle() {
		return paddle;
	}

	/**
	 * Returns all blocks
	 * @return The blocks as a list.
	 */
	public LinkedList<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Returns the ball.
	 * @return the ball.
	 */
	public Ball getBall() {
		return ball;
	}

	/**
	 * Returns all items on the board
	 * @return a list of all items on the board.
	 */
	public List<Item> getItems() {
		List<Item> result = (List<Item>) this.blocks.clone();
		result.add(this.ball);
		result.add(this.paddle);
		return result;
	}

	/**
	 * Returns the width of the board.
	 * @return The width of the board.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board.
	 * @return the height of the board.
	 */
	public int getHeight() {
		return height;
	}
}
