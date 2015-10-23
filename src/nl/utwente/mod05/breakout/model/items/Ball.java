package nl.utwente.mod05.breakout.model.items;

/**
 * Class representing a ball
 */
public class Ball extends Item {
	public static final int DEFAULT_RADIUS = 10;
	public static final int DEFAULT_HEADING = 90;
	public static final int DEFAULT_VELOCITY = 10;
	public static final String DEFAULT_COLOR = "#A4A4A4";

	int radius;
	int velocity;
	int heading;

	/**
	 * Instantiates a ball.
	 * @param posx The x coordinate of the top left corner of the ball.
	 * @param posy The y coordinate of the top top left corner of the ball.
	 * @param color The color of the ball in web format.
	 * @param radius The radius of the ball.
	 * @param velocity The velocity of the ball.
	 * @param heading The heading of the ball.
	 */
	public Ball(int posx, int posy, String color, int radius, int velocity, int heading) {
		super(posx, posy, radius * 2, radius * 2, Shape.CIRCLE, color);
		this.radius = radius;
		this.velocity = velocity;
		this.heading = heading;
	}
}
