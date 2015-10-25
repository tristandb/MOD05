package nl.utwente.mod05.breakout.model.items;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Class representing a ball
 */
public class Ball extends Item {
	public static final double DEFAULT_RADIUS = 10;
	public static final double DEFAULT_HEADING = 90;
	public static final double DEFAULT_VELOCITY = 5; //Pixels per frame
	public static final double SPEED_MULTIPLIER = 1.3;
	public static final HashSet<Integer> MULTIPLY_ON_HIT = new HashSet<>(Arrays.asList(new
			Integer[] {4, 12}));
	public static final String DEFAULT_COLOR = "#A4A4A4";

	private double radius;
	private double velocity;
	private double heading;
	private int hits;

	/**
	 * Instantiates a ball.
	 * @param posx The x coordinate of the top left corner of the ball.
	 * @param posy The y coordinate of the top top left corner of the ball.
	 * @param color The color of the ball in web format.
	 * @param radius The radius of the ball.
	 * @param velocity The velocity of the ball.
	 * @param heading The heading of the ball.
	 */
	public Ball(double posx, double posy, String color, double radius, double velocity, double
			heading) {
		super(posx, posy, radius * 2, radius * 2, Shape.CIRCLE, color);
		this.radius = radius;
		this.velocity = velocity;
		this.heading = heading;
	}

	/**
	 * Returns the ball's radius
	 * @return The radius.
	 */
	public synchronized double getRadius() {
		return radius;
	}

	/**
	 * Returns the speed of the ball in pixels per frame.
	 * @return the speed of the ball.
	 */
	public synchronized double getVelocity() {
		return velocity;
	}

	/**
	 * Sets the speed of the ball in pixels per frame.
	 * @param velocity The speed of the ball.
	 */
	public synchronized void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns the heading of the ball in degrees.
	 * @return The heading of the ball.
	 */
	public synchronized double getHeading() {
		return heading;
	}

	/**
	 * Sets the heading of the ball in degrees.
	 * @param heading the heading of the ball.
	 */
	public synchronized void setHeading(double heading) {
		this.heading = heading;
	}

	/**
	 * Determines whether the ball has hit an Item, returns the edge of collision.
	 * TODO: new implementation using simple line intersections. Possible better results.
	 * @param item The item to check.
	 * @return The edge of collision, either none, top, left, right or bottom.
	 */
	public synchronized Edge intersects(Item item) {
		return this.hasTangentOrIntersection(item);

	}

	/**
	 * Determine whether the item has one or multiple intersections
	 * @param item The item to check.
	 * @return The edge of collision.
	 */
	private synchronized Edge hasTangentOrIntersection(Item item) {
		double relativeX, relativeY, w, h;
		relativeX = (this.posx + this.radius) - item.getX();
		relativeY = (this.posy + this.radius) - item.getY();
		w = item.getWidth();
		h = item.getHeight();

		Point a = new Point(relativeX, relativeY + h);
		Point b = new Point(relativeX + w, relativeY + h);
		Point c = new Point(relativeX + w, relativeY);
		Point d = new Point(relativeX, relativeY);

		//Create a small bounding box since calculateDiscriminant uses infinite lines instead of
		// the finite we have.
		/*if (item.getX() < this.posx + (this.radius) &&
				(item.getX() + item.getWidth()) > this.posx &&
				item.getY() < this.posy + (this.radius) &&
				item.getY() + item.getHeight() > this.posy) {
			if (calculateDiscriminant(a, b) >= 0) {
				return Edge.BOTTOM;
			} else if (calculateDiscriminant(b, c) > 0) {
				return Edge.RIGHT;
			} else if (calculateDiscriminant(c, d) > 0) {
				return Edge.TOP;
			} else if (calculateDiscriminant(d, a) > 0) {
				return Edge.LEFT;
			}
		}*/
		if (this.posx + this.radius > item.getX()
				&& this.posx < (item.getX() + item.getWidth()) - this.radius
				&& calculateDiscriminant(a, b) >= 0) {
			return Edge.BOTTOM;
		} else if (this.posx + this.radius > (item.getX() + item.getWidth())
				&& this.posx < item.getX() - this.radius
				&& calculateDiscriminant(c, d) >= 0) {
			return Edge.TOP;
		} else if (this.posy < (item.getY() + item.getHeight()) - this.radius
				&& this.posy > item.getY() - this.radius
				&& calculateDiscriminant(b, c) >= 0 ) {
			return Edge.RIGHT;
		} else if (this.posy < item.getY() - this.radius
				&& this.posy > (item.getX() + item.getWidth()) - this.radius
				&& calculateDiscriminant(d, a) >= 0) {
			return Edge.RIGHT;
		}

		return Edge.NONE;
	}

	/**
	 * Calculates the discriminant between the ball and a line. Returns < 0 for no intersection,
	 * = 0 for 1 intersection, > 0 for 2 intersections.
	 * @param p1 The coordinates of point 1
	 * @param p2 The coordinates of point 2
	 * @return The discriminant,
	 */
	private synchronized double calculateDiscriminant(Point p1, Point p2) {
		double dx, dy, dr, D;

		dx = p2.x - p1.x;
		dy = p2.y - p1.y;
		dr = Math.sqrt(dx * dx + dy * dy);
		D = p1.x * p2.y - p2.x * p1.y;
		return (this.radius * this.radius) * (dr * dr) - (D * D);
	}

	/**
	 * Determines whether the ball hit the paddle.
	 * @param paddle The paddle
	 * @return True if paddle is hit, false otherwise.
	 */
	public boolean determinePaddleHit(Paddle paddle) {
		return this.posy + (this.radius * 2) > paddle.getY()
				&& (this.posx + this.radius) > paddle.getX()
					&& (this.posx - this.radius) < (paddle.getX() + paddle.getWidth());
	}

	/**
	 * Executed when paddle is hit.
	 */
	public void paddleHit() {
		this.hits++;
		if (Ball.MULTIPLY_ON_HIT.contains(this.hits)) {
			this.velocity *= Ball.SPEED_MULTIPLIER;
		}
	}

	/**
	 * POJO object representing a coordinate
	 */
	private class Point {
		public double x;
		public double y;
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}
	}

	/**
	 * Represents an edge of an Item, used for determining what edge the ball hit an item on.
	 */
	public enum Edge {
		NONE, TOP, LEFT, RIGHT, BOTTOM
	}
}
