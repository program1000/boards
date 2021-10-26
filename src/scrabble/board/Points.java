package scrabble.board;

public class Points {
	private int points;
	private int multiplier;
	
	public void addPoints(int newPoints) {
		points+=newPoints;
	}
	
	public void addMultiplier(int newMultiplier) {
		multiplier+=newMultiplier;
	}
	
	public int getPoints() {
		return points;
	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
	public int getTotal() {
		if (multiplier>0) {
		    return points*multiplier;
		}
		return points;
	}

}
