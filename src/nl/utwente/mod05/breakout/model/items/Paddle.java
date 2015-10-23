package nl.utwente.mod05.breakout.model.items;

public class Paddle extends Item {
	public static final int DEFAULT_WIDTH = 100;
	public static final int DEFAULT_HEIGHT = 15;
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
	public Paddle(int posx, int posy, int dimx, int dimy, String color) {
		super(posx, posy, dimx, dimy, Shape.RECTANGLE, color);
	}

	/**
	 * Returns the precise angle the ball should bounce into.
	 * @param hitx The position the paddle is hit on.
	 * @return The angle the ball should bounce to.
	 */
	public int getRefraction(int hitx) {
		return 0;
	}
}
