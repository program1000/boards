package scrabble.board.model;

public class BoardWord {
	private int headX;
	private int headY;
	private int mainX;
	private int mainY;
	private int len;
	private final String value;
	
	public BoardWord(int newHeadX, int newHeadY, int newMainX, int newMainY, int newLen, String newValue ) {
		headX = newHeadX;
		headY = newHeadY;
		mainX = newMainX;
		mainY = newMainY;
		len = newLen;
		value = newValue;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getHeadX() {
		return headX;
	}
	
	public int getHeadY() {
		return headY;
	}

	public int getMainX() {
		return mainX;
	}
	
	public int getMainY() {
		return mainY;
	}

	public int getLen() {
		return len;
	}
	
}
