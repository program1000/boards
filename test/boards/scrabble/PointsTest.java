package boards.scrabble;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import scrabble.board.Points;

public class PointsTest {

	@Test
	public void addPointsTest() {
		Points p = new Points();
		assertEquals(0,p.getPoints());
		p.addPoints(10);
		assertEquals(10,p.getPoints());
		p.addPoints(10);
		assertEquals(20,p.getPoints());
	}
	
	@Test
	public void multiplierTest() {
		Points p = new Points();
		assertEquals(0,p.getPoints());
		assertEquals(0,p.getMultiplier());
		p.addPoints(10);
		p.addMultiplier(2);
		assertEquals(10,p.getPoints());
		assertEquals(20,p.getTotal());
	
		p = new Points();
		p.addPoints(10);
		p.addMultiplier(2);
		p.addMultiplier(3);
		assertEquals(50,p.getTotal());
	}
}
