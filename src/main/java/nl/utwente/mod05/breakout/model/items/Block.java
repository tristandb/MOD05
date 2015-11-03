package nl.utwente.mod05.breakout.model.items;

/**
 * Class representing a block in a breakout game.
 */
public class Block extends Item {
	public static final int DEFAULT_HEIGHT = 35;
	public static final String[] DEFAULT_COLORS = {"#F0FF00", "#00FF32", "#FF7700", "#FF3200"};

	public int id;
	private int row;
	private Block bottom;
	private Block right;
	private Block top;
	private Block left;

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
			row, int id) {
		super(posx, posy, width, height, Shape.RECTANGLE, color);
		this.id = id;
		this.row = row;
	}

	/**
	 * Returns the row identifier of the Block.
	 * @return The row identifier as an int.
	 */
	public int getRow() {
		return this.row;
	}


	public void setBottom(Block bottom) {
		this.bottom = bottom;
	}

	public void setRight(Block right) {
		this.right = right;
	}

	public void setTop(Block top) {
		this.top = top;
	}

	public void setLeft(Block left) {
		this.left = left;
	}

	public Block getBottom() {
		return bottom;
	}

	public Block getRight() {
		return right;
	}

	public Block getTop() {
		return top;
	}

	public Block getLeft() {
		return left;
	}

	public boolean hasBottom() {
		return this.bottom != null;
	}
	public boolean hasRight() {
		return this.right != null;
	}
	public boolean hasTop() {
		return this.top != null;
	}
	public boolean hasLeft() {
		return this.left != null;
	}
}
