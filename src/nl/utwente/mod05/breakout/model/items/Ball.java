package nl.utwente.mod05.breakout.model.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
	public synchronized Tuple<Edge, Point> intersects(Item item) {
		return this.hasTangentOrIntersection2(item);
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
		if (item.getX() < this.posx + (this.radius) &&
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
		}
		return Edge.NONE;
	}
	/**
	 * Determine whether the item has one or multiple intersections
	 * @param item The item to check.
	 * @return The edge of collision.
	 */
	private synchronized Tuple<Edge, Point> hasTangentOrIntersection2(Item item) {
		double relativeX, relativeY, w, h;
		relativeX = (this.posx + this.radius) - item.getX();
		relativeY = (this.posy + this.radius) - item.getY();
		w = item.getWidth();
		h = item.getHeight();

		Point a = new Point(relativeX, relativeY + h);
		Point b = new Point(relativeX + w, relativeY + h);
		Point c = new Point(relativeX + w, relativeY);
		Point d = new Point(relativeX, relativeY);
		Point invalid = new Point(Point.INVALID_X, Point.INVALID_Y);

		//Create a small bounding box since calculateDiscriminant uses infinite lines instead of
		// the finite we have.
		if (item.getX() < this.posx + (this.radius) &&
				(item.getX() + item.getWidth()) > this.posx &&
				item.getY() < this.posy + (this.radius) &&
				item.getY() + item.getHeight() > this.posy) {
			Point intersect = calculateIntersection(a, b);
			if (!intersect.equals(invalid)) {
				return new Tuple<>(Edge.BOTTOM, intersect);
			}
			intersect = calculateIntersection(b, c);
			if (!intersect.equals(invalid)) {
				return new Tuple<>(Edge.BOTTOM, intersect);
			}
			intersect = calculateIntersection(c, d);
			if (!intersect.equals(invalid)) {
				return new Tuple<>(Edge.BOTTOM, intersect);
			}
			intersect = calculateIntersection(d, a);
			if (!intersect.equals(invalid)) {
				return new Tuple<>(Edge.BOTTOM, intersect);
			}
		}
		return new Tuple<>(Edge.NONE, invalid);
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

	private Point calculateIntersection(Point p1, Point p2) {
		double dx, dy, dr, D, resX, resY;

		dx = p2.x - p1.x;
		dy = p2.y - p1.y;
		dr = Math.sqrt(dx * dx + dy * dy);
		D = p1.x * p2.y - p2.x * p1.y;

		if (dr != 0) {
			resX = (D * dy + sign(dy) * dx *
					Math.sqrt((this.radius * this.radius) * (dr * dr) - (D * D))) / (dr * dr);
			resY = (-D * dx + Math.abs(dy) *
					Math.sqrt((this.radius * this.radius) * (dr * dr) - (D * D))) / (dr * dr);

			/*if (resX >= Math.min(p1.x, p2.x) && resX <= Math.max(p1.x, p2.x)
					&& resY >= Math.min(p1.y, p2.y) && resY <= Math.max(p1.y, p2.y)) {
				return new Point(resX, resY);
			}*/
			return new Point(resX, resY);
		}
		return new Point(Point.INVALID_X, Point.INVALID_Y);
	}

	private double sign(double x) {
		return x < 0 ? -1 : x;
	}

	public Tuple<Ball.Edge, Ball.Point> intersect(Item item, double nx, double ny) {
		double itemX, itemY, w, h;
		itemX = item.getX();
		itemY = item.getY();
		w = item.getWidth();
		h = item.getHeight();
		HashMap<Edge, Point> hits = new HashMap<>();
		Point ballOld = new Point(this.posx + this.radius, this.posy + this.radius);
		Point ballNew = new Point(nx + this.radius, this.posy + this.radius);
		Point itemA = new Point(itemX, itemY + h);
		Point itemB = new Point(itemX + w, itemY + h);
		Point itemC = new Point(itemX + w, itemY);
		Point itemD = new Point(itemX, itemY);
		Point invalid = new Point(Point.INVALID_X, Point.INVALID_Y);

		Point p = this.lineIntersect(ballOld, ballNew, itemA, itemB);
		if (!p.equals(invalid)) {
			hits.put(Edge.BOTTOM, p);
		}
		p = this.lineIntersect(ballOld, ballNew, itemB, itemC);
		if (!p.equals(invalid)) {
			System.out.printf("Hit on Right:\n" +
							"\tBall: (%f, %f) (%f, %f)\n" +
							"\tEdge: (%f, %f), (%f, %f)\n" +
							"\tHit:  (%f, %f)\n", ballOld.x, ballOld.y, ballNew.x, ballNew.y, itemB.x, itemB.y,
					itemC.x, itemC.y, p.x, p.y);
			hits.put(Edge.RIGHT, p);
		}
		p = this.lineIntersect(ballOld, ballNew, itemC, itemD);
		if (!p.equals(invalid)
				&& compareDoubles(p.y, itemC.y, 0.1) == 0
				&& compareDoubles(p.x, itemC.x, 0.1) < 0
				&& compareDoubles(p.x, itemD.x, 0.1) > 0) {
			hits.put(Edge.TOP, p);
		}
		p = this.lineIntersect(ballOld, ballNew, itemD, itemA);
		if (!p.equals(invalid)
				&& compareDoubles(p.x, itemB.x, 0.1) == 0
				&& compareDoubles(p.y, itemB.y, 0.1) < 0
				&& compareDoubles(p.y, itemC.y, 0.1) > 0) {
			hits.put(Edge.LEFT, p);
		}

		if (hits.size() > 1) {
			System.out.println("GOT TOO MANY HITS NOOOOOO");
			for (Map.Entry<Edge, Point> entry : hits.entrySet()) {
				return new Tuple<>(entry.getKey(), entry.getValue());
			}
		} else if (hits.size() == 1) {
			for (Map.Entry<Edge, Point> entry : hits.entrySet()) {
				return new Tuple<>(entry.getKey(), entry.getValue());
			}
		}
		return new Tuple<>(Edge.NONE, invalid);
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
	 * Checks whether the line between p1-p2 and p3-p4 intersect.
	 * @param p1 first point
	 * @param p2 second point
	 * @param p3 third point
	 * @param p4 fourth point.
	 * @return Point of intersection, or invalid if no intersection.
	 */
	private Point lineIntersect(Point p1, Point p2, Point p3, Point p4) {
		double resX, resY, D;
		/**/
		//(x1-x2)(y3-y4) - (y1-y2)(x3-x4)
		D = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);
		if (D != 0) {
			resX = ((p1.x*p2.y - p1.y*p2.x)*(p3.x - p4.x) - (p1.x - p2.x)*(p3.x*p4.y - p3.y*p4.x)) /
					D;
			resY = ((p1.x*p2.y - p1.y*p2.x)*(p3.y - p4.y) - (p1.y - p2.y)*(p3.x*p4.y - p3.y*p4.x)) /
					D;

			if (resX > Math.min(p1.x, p2.x) && resX < Math.max(p1.x, p2.x)
				&& resX > Math.min(p3.x, p4.x) && resX < Math.max(p3.x, p4.x)) {
				return new Point(resX, resY);
			}
		}
		return new Point(Point.INVALID_X, Point.INVALID_Y);
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
