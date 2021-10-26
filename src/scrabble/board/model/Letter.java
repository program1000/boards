package scrabble.board.model;

public class Letter {
	private String chr;
	private final int value;
	
	public Letter( String newChar, int newValue) {
		chr=newChar;
		value=newValue;
	}

	public int getValue() {
		return value;
	}

	public String getChar() {
		return chr;
	}
	
	public void setChar(String newChar) {
		chr=newChar;
	}

}
