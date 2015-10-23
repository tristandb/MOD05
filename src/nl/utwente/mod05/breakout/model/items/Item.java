package nl.utwente.mod05.breakout.model.items;


public abstract class Item {
	public enum Shape {
		CIRCLE, RECTANGLE
	}

	protected int posx;
	protected int posy;
	protected int dimx;
	protected int dimy;
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
	public Item(int posx, int posy, int dimx, int dimy, Shape shape, String color) {
		this.posx = posx;
		this.posy = posy;
		this.dimx = dimx;
		this.dimy = dimy;
		this.shape = shape;
		this.color = color;
	}

	/**
	 * Returns a tuple with the x and y positions of the Item.
	 * @return a tuple as an int array containing the x and y position.
	 */
	public synchronized int[] getPosition() {
		return new int[] {this.posx, this.posy};
	}

	/**
	 * Sets the position of the Item.
	 * @param posx The x position of the top left corner of the Item.
	 * @param posy The y position of the top left corner of the Item.
	 */
	public synchronized void setPosition(int posx, int posy) {
		this.posx = posx;
		this.posy = posy;
	}

	/**
	 * Returns the x coordinate of the top left corner.
	 * @return x coordinate representing the top left corner of the item.
	 */
	public synchronized int getX() {
		return posx;
	}

	/**
	 * Returns the y coordinate of the top left corner.
	 * @return y coordinate representing the top left corner of the item.
	 */
	public synchronized int getY() {
		return posy;
	}

	/**
	 * Returns the height of the item.
	 * @return The height of the item.
	 */
	public synchronized int getHeight() {
		return dimy;
	}

	/**
	 * Returns the width of the item.
	 * @return the width of the item.
	 */
	public synchronized int getWidth() {
		return dimx;
	}

	/**
	 * Returns the width and height of the Item in a tuple
	 * @return a tuple as an int array containing the width and height.
	 */
	public synchronized int[] getDimension() {
		return new int[] {this.dimx, this.dimy};
	}

	/**
	 * Sets the width and height of the Item.
	 * @param dimx The width of the Item.
	 * @param dimy The height of the Item.
	 */
	public synchronized void setDimension(int dimx, int dimy) {
		this.dimx = dimx;
		this.dimy = dimy;
	}

	/**
	 * Returns the shape of the Item, either a rectangle or a circle.
	 * @return The shape of the Item.
	 */
	public Shape getShape() {
		return shape;
	}

	/**
	 * Sets the shape of the Item, either a rectangle or a circle.
	 * @param shape The shape of the Item.
	 */
	public void setShape(Shape shape) {
		this.shape = shape;
	}

	/**
	 * Returns the color of the Item in web format (e.g. #FF0000, rgb(255,0,0) etc) as a String.
	 * @return a string containing the color in web format.
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Sets the color of the Item in web format (e.g. #FF0000, rgb(255,0,0) etc) as a String.
	 * @param color a string containing the color in web format.
	 */
	public void setColor(String color) {
		this.color = color;
	}
}
