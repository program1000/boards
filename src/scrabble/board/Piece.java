package scrabble.board;

import scrabble.board.PieceWalker.Direction;
import scrabble.board.model.Letter;

public class Piece {

	private Letter letter;
	
	private Piece left;
	private Piece right;
	private Piece up;
	private Piece down;
	
	public Piece(Letter newLetter) {
		letter = newLetter;
	}

	public Piece getLeft() {
		return left;
	}

	public Piece getRight() {
		return right;
	}
	
	public Piece getUp() {
		return up;
	}

	public Piece getDown() {
		return down;
	}
	
	public String getChar() {
		return letter.getChar();
	}
	
	public void attach(Piece piece, Direction direction) {
		switch( direction ){
		case LEFT:
			left = piece;
			piece.right=this;
			break;
		case RIGHT:
			right = piece;
			piece.left=this;
			break;
		case DOWN:
			down = piece;
			piece.up=this;
			break;
		case UP:
			up = piece;
			piece.down=this;
			break;
		default:
			throw new RuntimeException("Missing direction");
		}
	}
	
	public void remove(Direction direction) {
		switch( direction ){
		case LEFT:
			left.right = null;
			left=null;
			break;
		case RIGHT:
			right.left = null;
			right = null;
			break;
		case DOWN:
			down.up = null;
			down = null;
			break;
		case UP:
			up.down = null;
			up = null;
			break;
		default:
			throw new RuntimeException("Missing direction");
		}
	}

	public Letter getLetter() {
		return letter;
	}
	
}
