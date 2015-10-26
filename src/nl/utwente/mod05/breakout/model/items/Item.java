package nl.utwente.mod05.breakout.model.items;

/**
 * Class representing an item on the playing field.
 */
public abstract class Item {
	public enum Shape {
		CIRCLE, RECTANGLE
	}

	protected double posx;
	protected double posy;
	protected double dimx;
	protected double dimy;
	protected Shape shape;
	protected String color;

	/**
	 * Instantiates an Item with a position and dimensions.
	 * @param posx The x position of the top left corner of the Item.
	 * @param posy The y position of the top left corner of the Item.
	 * @param dimx The width of the Item.
	 * @param dimy The height of the Item.
	 * @param shape The shape of the Item.
	 * @param color The color of the item, in web format (e.g. #FF0000, rgb(255,0,0) etc).
	 */
	public Item(double posx, double posy, double dimx, double dimy, Shape shape, String color) {
		this.posx = posx;
		this.posy = posy;
		this.dimx = dimx;
		this.dimy = dimy;
		this.shape = shape;
		this.color = color;
	}

	/**
	 * Sets the position of the Item.
	 * @param posx The x position of the top left corner of the Item.
	 * @param posy The y position of the top left corner of the Item.
	 */
	public synchronized void setPosition(double posx, double posy) {
		this.posx = posx;
		this.posy = posy;
	}

	/**
	 * Returns the x coordinate of the top left corner.
	 * @return x coordinate representing the top left corner of the item.
	 */
	public synchronized double getX() {
		return posx;
	}

	/**
	 * Returns the y coordinate of the top left corner.
	 * @return y coordinate representing the top left corner of the item.
	 */
	public synchronized double getY() {
		return posy;
	}

	/**
	 * Sets the x coordinate of the top left corner.
	 * @param x the x coordinate
	 */
	public synchronized void setX(double x) {
		this.posx = x;
	}

	/**
	 * Returns the height of the item.
	 * @return The height of the item.
	 */
	public synchronized double getHeight() {
		return dimy;
	}

	/**
	 * Returns the width of the item.
	 * @return the width of the item.
	 */
	public synchronized double getWidth() {
		return dimx;
	}

	/**
	 * Returns the shape of the Item, either a rectangle or a circle.
	 * @return The shape of the Item.
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Returns the color of the Item in web format (e.g. #FF0000, rgb(255,0,0) etc) as a String.
	 * @return a string containing the color in web format.
	 */
	public String getColor() {
		return color;
	}
}
