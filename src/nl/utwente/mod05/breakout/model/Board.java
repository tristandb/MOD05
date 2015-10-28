package nl.utwente.mod05.breakout.model;

import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.input.InputHandler;
import nl.utwente.mod05.breakout.model.items.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Model class representing a board.
 */
public class Board {
	public static final int DEFAULT_WIDTH = 800;
	public static final int DEFAULT_HEIGHT = 600;
	public static final int BLOCKS_PER_ROW = 15;
	public static final int BLOCK_ROWS = 4;
	private boolean running;
	private boolean paused;
	private boolean lastRowHit;
	private boolean secondToLastRowHit;
	private int width;
	private int height;
	private int score;
	private Paddle paddle;
	private Ball.Point lastHit;

	private List<Block> blocks;
	private Ball ball;

	/**
	 * Instantiates a board. Uses the reset() method.
	 * @param width The width of the board.
	 * @param height The height of the board.
	 */
	public Board(int width, int height) {
		this.reset(width, height);
	}

	/**
	 * Resets the board to default values.
	 * @param width Width of the board.
	 * @param height Height of the board.
	 */
	public synchronized void reset(int width, int height) {
		this.width = width;
		this.height = height;

		//Instantiate Ball object.
		this.ball = new Ball(
				width / 2 - Ball.DEFAULT_RADIUS,
				height / 2 - Ball.DEFAULT_RADIUS,
				Ball.DEFAULT_COLOR,
				Ball.DEFAULT_RADIUS,
				Ball.DEFAULT_VELOCITY,
				Ball.DEFAULT_HEADING);

		//Instantiate Paddle object.
		this.paddle = new Paddle(
				width / 2 - (Paddle.DEFAULT_WIDTH / 2),
				height - (Paddle.DEFAULT_HEIGHT + Paddle.HOVER_HEIGHT),
				Paddle.DEFAULT_WIDTH,
				Paddle.DEFAULT_HEIGHT,
				Paddle.DEFAULT_COLOR
		);

		//Instantiate Block objects.
		int blockWidth = width / BLOCKS_PER_ROW;
		int id = 0;
		this.blocks = new LinkedList<>();
		Block prev = null;
		for (int r = 0; r < BLOCK_ROWS; r++) {
			int colorRows = BLOCK_ROWS - 1;
			if (colorRows > Block.DEFAULT_COLORS.length) {
				colorRows = Block.DEFAULT_COLORS.length - 1;
			}
			String color = Block.DEFAULT_COLORS[colorRows - r];

			for (int c = 0; c < BLOCKS_PER_ROW; c++) {
				id++;
				Block block = new Block(
						c * blockWidth,
						r * Block.DEFAULT_HEIGHT,
						blockWidth,
						Block.DEFAULT_HEIGHT,
						color,
						BLOCK_ROWS - r,
						id
				);
				if (c != 0) {
					block.setLeft(prev);
					prev.setRight(block);
				}
				if (r != 0) {
					Block top = this.blocks.get(id - BLOCKS_PER_ROW);
					top.setBottom(block);
					block.setTop(top);
				}
				this.blocks.add(block);
				prev = block;
			}
		}
		//Reverse the blocks order, this will make it quicker to determine hits.
		//Collections.reverse(this.blocks);
		this.secondToLastRowHit = false;
		this.lastRowHit = false;
		this.running = false;
		this.paused = false;
		this.score = 0;
	}

	public Ball getBall() {
		return this.ball;
	}

	public Paddle getPaddle() {
		return this.paddle;
	}

	/**
	 * Starts the game.
	 */
	public synchronized void start() {
		this.running = true;
		if (this.paused) {
			this.paused = false;
		}
	}

	/**
	 * Unpauses the game.
	 */
	public synchronized void unpause() {
		this.paused = false;
	}
	/**
	 * Pauses the game.
	 */
	public synchronized void pause() {
		this.paused = true;
	}

	/**
	 * Returns whether the game is paused.
	 * @return true if the game is running, but paused. False if otherwise.
	 */
	public synchronized boolean isPaused() {
		return this.running && this.paused;
	}
	/**
	 * Determines whether the game is running or not.
	 * @return The game status.
	 */
	public synchronized boolean isRunning() {
		return this.running;
	}

	/**
	 * Returns all items on the board.
	 * @return a list of all items on the board.
	 */
	public synchronized List<Item> getItems() {
		List<Item> result = new LinkedList<>(this.blocks);
		result.add(this.ball);
		result.add(this.paddle);
		return result;
	}

	/**
	 * Returns the width of the board.
	 * @return The width of the board.
	 */
	public synchronized int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board.
	 * @return the height of the board.
	 */
	public synchronized int getHeight() {
		return height;
	}

	/**
	 * Returns the game score.
	 * @return The game score.
	 */
	public synchronized int getScore() {
		return this.score;
	}

	/**
	 * Sets the position of the paddle. Assumes the given position is the left most coordinate of
	 * the paddle.
	 * @param paddlePosition The position to set the paddle to.
	 */
	public synchronized void setPaddlePosition(int paddlePosition) {
		if (this.running && paddlePosition != InputHandler.ERROR_STATE) {
			paddlePosition = paddlePosition - (int) (this.paddle.getWidth() / 2);

			if (paddlePosition < 0) {
				paddlePosition = 0;
			} else if (paddlePosition > (this.width - this.paddle.getWidth())) {
				paddlePosition = this.width - (int) this.paddle.getWidth();
			}
			this.paddle.setX(paddlePosition);
		}
	}

	/**
	 * Calculates the positions of all items for the next frame of the board.
	 */
	public synchronized void next() {
		if (this.running && !this.paused) {
			double speed = this.ball.getVelocity();
			double heading = this.ball.getHeading();
			double newX, newY;

			//Calculate new X and Y coordinates.
			newX = this.ball.getX() + Math.cos(heading * (Math.PI / 180)) * speed;
			newY = this.ball.getY() + Math.sin(heading * (Math.PI / 180)) * speed;

			//Reflect on hitting edges.
			if (newX < 0 || newX + (2 * this.ball.getRadius()) > this.width) {
				heading = 180 - heading;
			}
			if (newY < 0) {
				heading = 360 - heading;
			}

			//Check whether the ball hit the paddle, otherwise check if the ball hit the bottom egde
			if (this.ball.determinePaddleHit(this.paddle)) {
				heading = this.paddle.getReflection(this.ball.getX() + this.ball.getRadius());

				//Increase speed on several hit counts.
				this.ball.paddleHit();
			} else if (newY + (2 * this.ball.getRadius()) > this.height) {
				//Edge the bottom edge, stop the game.
				this.running = false;
				return;
			}

			//Make sure the new coordinates are actually on the game field.
			newX = newX < 0 ? 1 : newX > this.width ? this.width - 2 * this.ball.getRadius() : newX;
			newY = newY < 0 ? 1 : newY > this.height ? this.height - 2 * this.ball.getRadius() :
					newY;

			//Check if the ball hit any blocks. But only if it is possible a block might be hit.
			if (newY < BLOCK_ROWS * Block.DEFAULT_HEIGHT) {
				LinkedList<Block> tblocks = new LinkedList<>(this.blocks);
				for (Block b : tblocks) {
					if (b != null) {
						//hitOn variable representing the edge of the block where the ball hit the block
						Ball.Tuple<Ball.Edge, Ball.Point> t = this.ball.intersects(newX, newY, b);
						Ball.Edge hitOn = t.first;
						Ball.Point hitOnPoint =  t.second;
						if (hitOn != Ball.Edge.NONE) {
							if (Breakout.DEBUG) {
								System.out.println(hitOn + " width heading " + this.ball.getHeading());
							}

							//Increase score based on row identifier.
							this.score += b.getRow();

							//If blocks on the highest row are hit the first time, increase speed and
							// reduce the paddle size
							if (b.getRow() == BLOCK_ROWS && !this.lastRowHit) {
								this.lastRowHit = true;
								this.ball.setVelocity(speed * Ball.SPEED_MULTIPLIER);
								this.paddle.setWidth(this.paddle.getWidth() / 2);
							} else if (b.getRow() == BLOCK_ROWS - 1 && !this.secondToLastRowHit) {
								//If blocks on the second to highest row are hit the first time,
								// increase speed
								this.secondToLastRowHit = true;
								this.ball.setVelocity(speed * Ball.SPEED_MULTIPLIER);
							}


							//Calculate new ball heading
							if (hitOn.equals(Ball.Edge.TOP) || hitOn.equals(Ball.Edge.BOTTOM)) {
								heading = 360 - this.ball.getHeading();
							} else {
								heading = 180 - this.ball.getHeading();
							}

							//Remove block from the list.
							Block temp;
							if (b.hasBottom()) {
								b.getBottom().setTop(null);
							}
							if (b.hasLeft()) {
								b.getLeft().setRight(null);
							}
							if (b.hasRight()) {
								b.getRight().setLeft(null);
							}
							if (b.hasTop()) {
								b.getTop().setBottom(null);
							}
							this.blocks.remove(b);

							newX = this.ball.getX();
							newY = this.ball.getY();
							//newX = hitOnPoint.x - this.ball.getRadius();
							//newY = hitOnPoint.y - this.ball.getRadius();
							this.lastHit = hitOnPoint;
							//Only do 1 hit per frame.
							break;
						}
					}
				}
			}

			if (this.blocks.size() == 0) {
				this.running = false;
			}
			this.ball.setPosition(newX, newY);
			this.ball.setHeading(heading % 360);

		}
	}
	public Ball.Point lastHitPoint() {
		return this.lastHit;
	}
}
