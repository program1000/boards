package scrabble.input.model;

public class InputLetter {

	private String chr;
	private int x;
	private int y;
	private boolean isBlanco=false;
	private boolean isExists=false;
	
	public InputLetter( int newX, int newY, String newChr) {
		x=newX;
		y=newY;
		chr=newChr;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String getChar() {
		return chr;
	}
	
	public void setBlanco() {
		isBlanco=true;
	}
	
	public boolean isBlanco() {
		return isBlanco;
	}

	public void setExists() {
		isExists=true;
	}
	
	public boolean isExists() {
		return isExists;
	}
}
