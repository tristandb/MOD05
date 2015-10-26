package nl.utwente.mod05.breakout.model.items;

/**
 * Class representing a block in a breakout game.
 */
public class Block extends Item {
	public static final int DEFAULT_HEIGHT = 35;
	public static final String[] DEFAULT_COLORS = {"#F0FF00", "#00FF32", "#FF7700", "#FF3200"};

	private int row;

	/**
	 * Instantiates a block.
	 * @param posx The x coordinate of the top left corner of the Block.
	 * @param posy The y coordinate of the top left corner of the Block.
	 * @param width The width of the Block.
	 * @param height The height of the Block.
	 * @param color The color of the block in web format.
	 * @param row The row identifier.
	 */
	public Block(double posx, double posy, double width, double height, String color, int
			row) {
		super(posx, posy, width, height, Shape.RECTANGLE, color);
		this.row = row;
	}

	/**
	 * Returns the row identifier of the Block.
	 * @return The row identifier as an int.
	 */
	public int getRow() {
		return this.row;
	}
}
