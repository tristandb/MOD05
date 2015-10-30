package nl.utwente.mod05.breakout.test;

import nl.utwente.mod05.breakout.model.items.Ball;
import nl.utwente.mod05.breakout.model.items.Block;
import nl.utwente.mod05.breakout.model.items.Paddle;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jelle Stege
 */
public class BallTest {

	private Ball createBall() {
		return createBall(50, 50);
	}

	private Ball createBall(int x, int y) {
		return new Ball(x, y, Ball.DEFAULT_COLOR, Ball.DEFAULT_RADIUS, Ball.DEFAULT_VELOCITY, 0);
	}

	@Test
	public void testHeading() throws Exception {
		double[] insertValues = {90, 85, 200, 100, 360, 400, -10};
		double[] outputValues = {90, 85, 200, 100, 0, 40, 350};
		Ball ball = createBall();

		for (int i = 0; i < insertValues.length; i++) {
			ball.setHeading(insertValues[i]);
			Assert.assertEquals(ball.getHeading(), outputValues[i], 0.1);
		}
	}

	@Test
	public void testIntersects() throws Exception {
	}

	@Test
	public void testBallGoesUp() throws Exception {
		Ball ball = createBall();

		ball.setHeading(90);
		Assert.assertEquals(ball.ballGoesUp(), false);
		ball.setHeading(270);
		Assert.assertEquals(ball.ballGoesUp(), true);
		ball.setHeading(180);
		Assert.assertEquals(ball.ballGoesUp(), false);
		ball.setHeading(179);
		Assert.assertEquals(ball.ballGoesUp(), false);
		ball.setHeading(181);
		Assert.assertEquals(ball.ballGoesUp(), true);
	}

	@Test
	public void testBallGoesDown() throws Exception {

		Ball ball = createBall();

		ball.setHeading(90);
		Assert.assertEquals(ball.ballGoesDown(), true);
		ball.setHeading(270);
		Assert.assertEquals(ball.ballGoesDown(), false);
		ball.setHeading(180);
		Assert.assertEquals(ball.ballGoesDown(), false);
		ball.setHeading(179);
		Assert.assertEquals(ball.ballGoesDown(), true);
		ball.setHeading(181);
		Assert.assertEquals(ball.ballGoesDown(), false);
	}

	@Test
	public void testBallGoesLeft() throws Exception {
		Ball ball = createBall();

		ball.setHeading(180);
		Assert.assertEquals(ball.ballGoesLeft(), true);
		ball.setHeading(200);
		Assert.assertEquals(ball.ballGoesLeft(), true);
		ball.setHeading(270);
		Assert.assertEquals(ball.ballGoesLeft(), false);
		ball.setHeading(0);
		Assert.assertEquals(ball.ballGoesLeft(), false);
		ball.setHeading(269);
		Assert.assertEquals(ball.ballGoesLeft(), true);
		ball.setHeading(271);
		Assert.assertEquals(ball.ballGoesLeft(), false);
	}

	@Test
	public void testBallGoesRight() throws Exception {
		Ball ball = createBall();

		ball.setHeading(0);
		Assert.assertEquals(ball.ballGoesRight(), true);
		ball.setHeading(300);
		Assert.assertEquals(ball.ballGoesRight(), true);
		ball.setHeading(270);
		Assert.assertEquals(ball.ballGoesRight(), false);
		ball.setHeading(180);
		Assert.assertEquals(ball.ballGoesRight(), false);
		ball.setHeading(271);
		Assert.assertEquals(ball.ballGoesRight(), true);
		ball.setHeading(269);
		Assert.assertEquals(ball.ballGoesRight(), false);

	}

	@Test
	public void testDeterminePaddleHit() throws Exception {
		Ball ball = createBall(200, 100);
		Paddle paddle = new Paddle(200, 100, 100, 15, Paddle.DEFAULT_COLOR);

		Assert.assertEquals(ball.determinePaddleHit(paddle), true);

		ball.setPosition(ball.getX(), 50);
		Assert.assertEquals(ball.determinePaddleHit(paddle), false);
	}
}