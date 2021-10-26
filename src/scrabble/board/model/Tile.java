package scrabble.board.model;

import scrabble.board.Piece;

public class Tile {

	public enum Value{DL,TL,DW,TW,NONE}
	
	private boolean isEmpty=true;
	private Value value;
	private Piece piece;
	
	public Tile(Value newValue){
		value=newValue;
	}
	
	public void addPiece(Piece newPiece) {
		piece = newPiece;
		isEmpty=false;
	}
	
	public Piece getPiece() {
		return piece;
	}
	
	public int score( boolean isFirst ) {
		if(isEmpty) {
			return 0;
		}
		if( !isFirst ) {
			return piece.getLetter().getValue();
		}
		switch(value) {
		case DL:
			return 2 * piece.getLetter().getValue();
		case TL:
			return 3 * piece.getLetter().getValue();
		default:
			//DW,TW,NONE
			return piece.getLetter().getValue();
		}
	}
	
	public int multiplier( ) {
		if(isEmpty) {
			return 0;
		}
		switch(value) {
		case DW:
		    return 2;
		case TW:
		    return 3;
		default:
			//DL,TL,NONE
			return 0;
		}
	}

	public boolean isEmpty() {
		return isEmpty;
	}
	
	public String toString() {
		if(isEmpty) {
			if(value==Value.NONE) {
				return ". ";
			} else {
				return value.toString();
			}
		}
		String chr = piece.getLetter().getChar();
		if( chr.length()==1 ) {
			chr+=" ";
		}
		return chr;
	}
}
