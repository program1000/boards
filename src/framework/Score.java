package framework;

public class Score {

	private int value=0;

	public void add(int points) {
		value+=points;
	}
	
	public int getValue() {
		return value;
	};
}
