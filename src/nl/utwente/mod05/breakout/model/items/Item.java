package nl.utwente.mod05.breakout.model.items;


public abstract class Item {
	public enum Shape {
		ROUND, RECTANGLE
	}
	protected int posx;
	protected int posy;
	protected int dimx;
	protected int dimy;
	protected Shape shape;

	public int[] getPosition() {
		return new int[] {this.posx, this.posy};
	}

	public int[] getDimension() {
		return new int[] {this.dimx, this.dimy};
	}

	public Shape getShape() {
		return this.shape;
	}
}
