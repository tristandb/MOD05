package nl.utwente.mod05.breakout.model.items;

import javafx.scene.paint.Color;
import nl.utwente.mod05.breakout.Breakout;
import nl.utwente.mod05.breakout.ui.GUIController;

import java.util.List;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class representing a ball
 */
public class Ball extends Item {
	public static final double DEFAULT_RADIUS = 10;
	public static final double DEFAULT_HEADING = 90;
	public static final double DEFAULT_VELOCITY = 3; //Pixels per frame
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
		if (heading < 0) {
			heading = heading % -360;
			heading = 360 + heading;
		}
		this.heading = (heading % 360);
	}

	/**
	 * Determines whether the ball has hit an Item, returns the edge of collision.
	 * @param block The item to check.
	 * @return The edge of collision, either none, top, left, right or bottom.
	 */
	public synchronized Tuple<Edge, Point> intersects(double newX, double newY, Block block) {
		return this.getIntersection(newX, newY, block);
	}

	/**
	 * Determine whether the item has one or multiple intersections
	 * @param block The item to check.
	 * @return The edge of collision.
	 */
	private synchronized Tuple<Edge, Point> getIntersection(double newX, double newY,
															Block block) {
		double relativeX, relativeY, w, h, tx, ty, dx, dy, linelength;
		dx = this.posx - newX;
		dy = this.posy - newY;
		//dy = newY - this.posy;
		linelength = Math.sqrt(Math.abs(dx * dx) + Math.abs(dy * dy));

		if (!(this.ballGoesUp() && this.ballGoesLeft() && block.hasBottom() && block.hasRight())
				&& !(this.ballGoesUp() && this.ballGoesRight() && block.hasBottom() && block.hasLeft())
				&& !(this.ballGoesDown() && this.ballGoesLeft() && block.hasTop() && block.hasRight())
				&& !(this.ballGoesDown() && this.ballGoesRight() && block.hasTop() && block.hasLeft())
				) {
			for (int i = 0; i < (int) linelength; i += Math.max((int) (linelength /
					10), 1)) {
				HashMap<Edge, Point> intersections = new HashMap<>();
				tx = Math.max(0, this.posx + i * (dx / 10));
				ty = Math.max(0, this.posy + i * (dy / 10));
				//tx = Math.max(0, newX - i * (dx / 10));
				//ty = Math.max(0, newY - i * (dy / 10));
				relativeX = block.getX() - (tx + this.radius);
				relativeY = block.getY() - (ty + this.radius);
				w = block.getWidth();
				h = block.getHeight();

				Point a = new Point(relativeX, relativeY + h);
				Point b = new Point(relativeX + w, relativeY + h);
				Point c = new Point(relativeX + w, relativeY);
				Point d = new Point(relativeX, relativeY);
				Point invalid = new Point(Point.INVALID_X, Point.INVALID_Y);

				//Create a small bounding box since calculateDiscriminant uses infinite lines instead of
				// the finite we have.
				if (block.getX() < tx + (2 * this.radius) &&
						(block.getX() + block.getWidth()) > tx &&
						block.getY() < ty + (2 * this.radius) &&
						block.getY() + block.getHeight() > ty) {

					if (Breakout.DEBUG) {
						GUIController.context.setStroke(Color.RED);
						GUIController.context.strokeOval(tx - this.radius, ty - this.radius,
								this.radius * 2, this.radius * 2);
					}

					Point intersect = calculateIntersection(a, b, tx, ty);
					if (this.ballGoesUp() && !intersect.equals(invalid)) {
						//return new Tuple<>(Edge.BOTTOM, intersect);
						intersections.put(Edge.BOTTOM, intersect);
					}
					intersect = calculateIntersection(b, c, tx, ty);
					if (this.ballGoesLeft() && !intersect.equals(invalid)) {
						//return new Tuple<>(Edge.RIGHT, intersect);
						intersections.put(Edge.RIGHT, intersect);
					}
					intersect = calculateIntersection(c, d, tx, ty);
					if (this.ballGoesDown() && !intersect.equals(invalid)) {
						//return new Tuple<>(Edge.TOP, intersect);
						intersections.put(Edge.TOP, intersect);
					}
					intersect = calculateIntersection(d, a, tx, ty);
					if (this.ballGoesRight() && !intersect.equals(invalid)) {
						//return new Tuple<>(Edge.LEFT, intersect);
						intersections.put(Edge.LEFT, intersect);
					}

					if (intersections.size() > 1) {
						if (intersections.containsKey(Edge.BOTTOM)) {
							Point t1 = intersections.get(Edge.BOTTOM);
							if (intersections.containsKey(Edge.LEFT) && !block.hasRight()) {
								Point t2 = intersections.get(Edge.LEFT);
								if (t1.x - block.getX() < block.getY() + block.getHeight() - t2.y) {
									return new Tuple<>(Edge.LEFT, t2);
								}
							} else if (intersections.containsKey(Edge.RIGHT) && !block.hasLeft()) {
								Point t2 = intersections.get(Edge.RIGHT);
								if ((block.getX() + block.getWidth() - t1.x)
										< block.getY() + block.getHeight() - t2.y) {
									return new Tuple<>(Edge.RIGHT, t2);
								}
							}
							if (this.ballGoesDown()) {
								return  new Tuple<>(Edge.TOP, t1);
							}
							return new Tuple<>(Edge.BOTTOM, t1);
						} else if (intersections.containsKey(Edge.TOP)) {
							Point t1 = intersections.get(Edge.TOP);
							if (intersections.containsKey(Edge.LEFT) && !block.hasRight()) {
								Point t2 = intersections.get(Edge.LEFT);
								if (t1.x - block.getX() < t2.y - block.getHeight()) {
									return new Tuple<>(Edge.LEFT, t2);
								}
							} else if (intersections.containsKey(Edge.RIGHT) && !block.hasLeft()) {
								Point t2 = intersections.get(Edge.RIGHT);
								if ((block.getX() + block.getWidth() - t1.x)
										< t2.y - block.getHeight()) {
									return new Tuple<>(Edge.RIGHT, t2);
								}
							}
							if (this.ballGoesUp()) {
								return  new Tuple<>(Edge.BOTTOM, t1);
							}
							return new Tuple<>(Edge.TOP, t1);
						} else {
							if (this.ballGoesLeft()) {
								return new Tuple<>(Edge.RIGHT, intersections.get(Edge.RIGHT));
							} else {
								return new Tuple<>(Edge.LEFT, intersections.get(Edge.LEFT));
							}
						}
					} else if (intersections.size() == 1) {
						List<Edge> t = new ArrayList<>(intersections.keySet());
						Edge e = t.get(0);
						return new Tuple<>(e, intersections.get(e));
					}
				}
			}
		}

		return new Tuple<>(Edge.NONE, new Point(Point.INVALID_X, Point.INVALID_Y));
	}

	private Point calculateIntersection(Point p2, Point p1, double tx, double ty) {
		double dx, dy, dr, D, resX1, resX2, resY1, resY2, disc;

		dx = p2.x - p1.x;
		dy = p2.y - p1.y;
		dr = Math.sqrt(dx * dx + dy * dy);
		D = p1.x * p2.y - p2.x * p1.y;
		disc = (this.radius * this.radius) * (dr * dr) - (D * D);
		if (dr != 0 && compareDoubles(disc, 0, 0.1) >= 0) {
			resX1 = (D * dy + sign(dy) * dx * Math.sqrt(disc)) / (dr * dr);
			resX2 = (D * dy - sign(dy) * dx * Math.sqrt(disc)) / (dr * dr);
			resY1 = (-D * dx + Math.abs(dy) * Math.sqrt(disc)) / (dr * dr);
			resY2 = (-D * dx - Math.abs(dy) * Math.sqrt(disc)) / (dr * dr);
			return new Point(Math.max(0, (tx + this.radius) + ((resX1 + resX2) / 2)),
					Math.max(0, (ty + this.radius) + ((resY1 + resY2) / 2)));
		}
		return new Point(Point.INVALID_X, Point.INVALID_Y);
	}

	private double sign (double d) {
		return d < 0 ? -1 : 1;
	}

	public boolean ballGoesUp() {
		return this.heading > 180 && this.heading < 360;
	}

	public boolean ballGoesDown() {
		return this.heading < 180 && this.heading > 0;
	}

	public boolean ballGoesLeft() {
		return this.heading < 270 && this.heading > 90;
	}

	public boolean ballGoesRight() {
		return this.heading > 270 || this.heading < 90;
	}

	/**
	 * Compares 2 doubles by comparing with a certain delta.
	 * @param d1 first double
	 * @param d2 second double
	 * @param delta The delta to add.
	 * @return 0 if doubles are equal, -1 if d1 < d2, 1 if d1 > d2
	 */
	private static int compareDoubles(double d1, double d2, double delta) {
		if (d1 >= (d2 - delta) && d1 <= (d2 + delta)) {
			return 0;
		} else if (d1 < d2 - delta) {
			return -1;
		}
		return 1;
	}

	/**
	 * Determines whether the ball hit the paddle.
	 * @param paddle The paddle
	 * @return True if paddle is hit, false otherwise.
	 */
	public boolean determinePaddleHit(Paddle paddle) {
		return this.posy + (this.radius * 2) > paddle.getY()
				&& (this.posx + 2 * this.radius) > paddle.getX()
					&& (this.posx) < (paddle.getX() + paddle.getWidth());
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
	public class Point {
		public static final double INVALID_X = Double.POSITIVE_INFINITY;
		public static final double INVALID_Y = Double.POSITIVE_INFINITY;
		public final double x;
		public final double y;
		public Point(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public boolean equals(Point p) {
			return p != null
					&& (compareDoubles(this.x, p.x, 0.1) == 0
					&& (compareDoubles(this.y, p.y, 0.1) == 0));
		}
	}

	/**
	 * Represents an edge of an Item, used for determining what edge the ball hit an item on.
	 */
	public enum Edge {
		NONE, TOP, LEFT, RIGHT, BOTTOM
	}

	public class Tuple<T, U> {
		public T first;
		public U second;
		public Tuple(T first, U second) {
			this.first = first;
			this.second = second;
		}
	}
}
