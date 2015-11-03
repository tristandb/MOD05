package nl.utwente.mod05.breakout.model.items;

public class Paddle extends Item {
	public static final int DEFAULT_WIDTH = 150;
	public static final int DEFAULT_HEIGHT = 15;
	public static final int HOVER_HEIGHT = 10;
	public static final String DEFAULT_COLOR = "#0064FF"; //Blue paddle

	/**
	 * Instantiates an Item with a position and dimensions.
	 *
	 * @param posx  The x position of the top left corner of the Item.
	 * @param posy  The y position of the top left corner of the Item.
	 * @param dimx  The width of the Item.
	 * @param dimy  The height of the Item.
	 * @param color The color of the item, in web format (e.g. #FF0000, rgb(255,0,0) etc).
	 */
	public Paddle(double posx, double posy, double dimx, double dimy, String color) {
		super(posx, posy, dimx, dimy, Shape.RECTANGLE, color);
	}

	/**
	 * Returns the precise angle the ball should bounce into.
	 * @param hitx The position the paddle is hit on.
	 * @return The angle the ball should bounce to.
	 */
	public double getReflection(double hitx) {
		double rx = hitx - this.posx;
		rx = rx > this.dimx ? this.dimx : rx < 0 ? 0 : rx;
		return 190 + rx * (160 / this.dimx);
	}

	/**
	 * Sets the width of the paddle.
	 * @param width Width of the paddle.
	 */
	public void setWidth(double width) {
		this.dimx = width;
	}
}
